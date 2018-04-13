package com.yonyou.kms.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * oracle数据库工具类
 * 
 * @author Candy
 *
 */
public class OracleUtil {

	// oracle数据库配置文件
	private static Properties sqlConfig = new Properties();

	static {
		// 如果oracle配置文件为空则去加载配置文件
		initOracleConfig();

		// 加载驱动
		try {
			String oracleDriver = sqlConfig.getProperty("jdbc.driver");
			Class.forName(oracleDriver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取oracle数据库连接
	 * 
	 * @return
	 */
	public static Connection getOracleConnection() {

		Connection connection = null;
		// 获取数据库连接登录配置信息
		String oracleUrl = sqlConfig.getProperty("jdbc.url");
		String oracleUser = sqlConfig.getProperty("jdbc.username");
		String oraclePassword = sqlConfig.getProperty("jdbc.password");

		try {
			// 获取连接
			connection = DriverManager.getConnection(oracleUrl, oracleUser, oraclePassword);
			System.out.println("获取oracle连接成功!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	/**
	 * 关闭连接
	 * 
	 * @param resultSet
	 * @param parameterMetaData
	 * @param connection
	 */
	public static void closeConnection(ResultSet resultSet, PreparedStatement preparedStatement,
			Connection connection) {
		try {
			if (null != resultSet) {
				resultSet.close();
			}
			if (null != preparedStatement) {
				preparedStatement.close();
			}
			if (null != connection) {
				connection.close();
			}
			System.out.println("关闭连接成功!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化加载oracle数据库连接配置信息
	 */
	private static void initOracleConfig() {
		InputStream inputStream = null;
		try {
			// 将jdbc.properties文件读到字节输入流
			inputStream = OracleUtil.class.getClassLoader().getResourceAsStream("kms.properties");
			sqlConfig.load(inputStream);
			System.out.println("初始化加载数据库连接配置信息成功!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭输入流
			try {
				if (null != inputStream) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	public static int getTotalRow(){
		int row = 0;
		String sql = "select count(id) from cms_share";
		Connection connection = null; 
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try {
			connection = getOracleConnection();
			pstmt = connection.prepareStatement(sql);
			resultSet = pstmt.executeQuery();
			while(resultSet.next()){
				row = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeConnection(resultSet, pstmt, connection);
		return row;
	}
	
	public static List findMoreResult(String sql, Object...params) throws SQLException {
		List list = new ArrayList();
		int index = 1;
		Connection connection = getOracleConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql);
		if (null != params && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				pstmt.setObject(index++, params[i]);
			}
		}
		ResultSet resultSet = pstmt.executeQuery();
		ResultSetMetaData metaData = resultSet.getMetaData();
		int cols_len = metaData.getColumnCount();
		while (resultSet.next()) {
			Map map = new HashMap();
			for (int i = 0; i < cols_len; i++) {
				String cols_name = metaData.getColumnName(i + 1);
				Object cols_value = resultSet.getObject(cols_name);
				if (cols_value == null) {
					cols_value = "";
				}
				map.put(cols_name, cols_value);
			}
			list.add(map);
		}
		closeConnection(resultSet, pstmt, connection);
		return list;
	}
	
}
