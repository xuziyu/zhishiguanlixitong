package com.yonyou.kms.modules.sys.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;


/**
 * System Utils
 * 
 */
public class SystemUtil {

	// 系统相关路径
	private static String rootPath = null;
	private static String classesPath = null;
	private static String projectName = null;

	/**
	 * 获取系统编译文件的路径
	 * 
	 * @return
	 */
	public static String getProjectClassesPath() {
		System.out.println(classesPath);
		if (classesPath == null) {
			classesPath = SystemUtil.class.getClassLoader().getResource("")
					.getPath().trim();
			
			try {
				classesPath=java.net.URLDecoder.decode(classesPath,"utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			} 
			if (!isLinux()) {
				classesPath = classesPath.replaceFirst("/", "");
			}
		}
		return classesPath;
	}

	// 系统类型
	private static String osName = null;

	/**
	 * GET Project Root Path
	 * 
	 * @return /var/opt/tomcat/../project_name/
	 */
	public static String getProjectRootPath() {
		if (rootPath == null) {
			String classesPath = getProjectClassesPath();
			// java
			if (classesPath.endsWith("build/classes/")) {
				rootPath = classesPath.replace("build/classes/", "");
			} else if (classesPath.endsWith("WEB-INF/classes/")) {
				// java web
				rootPath = classesPath.replace("WEB-INF/classes/", "");
			}
		}
		return rootPath;
	}

	/**
	 * 获取项目名称
	 * 
	 * @return project_name
	 */
	public static String getProjectName() {
		if (projectName == null) {
			String classesPath = getProjectClassesPath();
			// java
			String rootPath = "";
			if (classesPath.endsWith("build/classes/")) {
				rootPath = classesPath.replace("build/classes/", "");
			} else if (classesPath.endsWith("WEB-INF/classes/")) {
				// java web
				rootPath = classesPath.replace("WEB-INF/classes/", "");
			}
			rootPath += "__";
			rootPath = rootPath.replace("/__", "");
			rootPath = rootPath.replaceAll("/", "/__");
			int index = rootPath.lastIndexOf("/__");
			if (index == -1) {
				return "";
			}
			projectName = rootPath.substring(index + 3);
		}
		return projectName;
	}

	/**
	 * 获取系统的类型
	 * 
	 * @return
	 */
	public static String getOsName() {
		if (osName == null) {
			Properties prop = System.getProperties();
			osName = prop.getProperty("os.name");
		}
		return osName;
	}

	/**
	 * 判断系统是否为Linux
	 * 
	 * @return true：linux false: win
	 */
	public static boolean isLinux() {
		if (getOsName().startsWith("win") || getOsName().startsWith("Win")) {
			return false;
		}
		return true;
	}

	// public static void main(String[] args) {
	// System.out.println(getProjectName());
	// }
	
	//格式化日期时间(调用方法如下)
	/*SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
	String s=simpleDateFormat.format(value);
	System.out.println(s);
	FormatNoteDateTime formatNoteDateTime=new FormatNoteDateTime();
	json.put("TIME",formatNoteDateTime.FormatDateTime(s));*/
	
	/**格式化日期时间（类似今天10：10，昨天10：00）
	 * @author hefeng
	 * @param String time
	 */
	public String FormatDateTime(String time){
		SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm"); 
		if(time==null ||"".equals(time)){
			return "";
		}
		Date date = null;
		try {
			date = format.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Calendar current = Calendar.getInstance();
		
		Calendar today = Calendar.getInstance();	//今天
		
		today.set(Calendar.YEAR, current.get(Calendar.YEAR));
		today.set(Calendar.MONTH, current.get(Calendar.MONTH));
		today.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH));
		//  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
		today.set( Calendar.HOUR_OF_DAY, 0);
		today.set( Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		
		Calendar yesterday = Calendar.getInstance();	//昨天
		
		yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
		yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
		yesterday.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH)-1);
		yesterday.set( Calendar.HOUR_OF_DAY, 0);
		yesterday.set( Calendar.MINUTE, 0);
		yesterday.set(Calendar.SECOND, 0);
		
		Calendar beforeyesterday = Calendar.getInstance();	//前天
		
		beforeyesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
		beforeyesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
		beforeyesterday.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH)-2);
		beforeyesterday.set( Calendar.HOUR_OF_DAY, 0);
		beforeyesterday.set( Calendar.MINUTE, 0);
		beforeyesterday.set(Calendar.SECOND, 0);
		
		current.setTime(date);
		
		if(current.after(today)){
			return "今天 "+time.split(" ")[1];
		}else if(current.before(today) && current.after(yesterday)){
			
			return "昨天 "+time.split(" ")[1];
		}else if(current.before(yesterday)&&current.after(beforeyesterday)){
			return "前天 "+time.split(" ")[1];
		}else{
			int index = time.indexOf("-")+1;
			return time.substring(index, time.length());
		}
	}
}