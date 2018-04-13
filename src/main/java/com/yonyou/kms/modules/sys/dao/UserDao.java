/**
 * 
 */
package com.yonyou.kms.modules.sys.dao;

import java.util.List;
import java.util.Map;

import com.yonyou.kms.common.persistence.CrudDao;
import com.yonyou.kms.common.persistence.annotation.MyBatisDao;
import com.yonyou.kms.modules.cms.entity.Category;
import com.yonyou.kms.modules.sys.entity.User;

/**
 * 用户DAO接口
 * @author hotsum
 * @version 2014-05-16
 */
@MyBatisDao
public interface UserDao extends CrudDao<User> {
	
	/**
	 * 根据登录名称查询用户
	 * @param loginName
	 * @return
	 */
	public User getByLoginName(User user);

	/**
	 * 通过OfficeId获取用户列表，仅返回用户id和name（树查询用户时用）
	 * @param user
	 * @return
	 */
	public List<User> findUserByOfficeId(User user);
	
	/**
	 * 查询全部用户数目
	 * @return
	 */
	public long findAllCount(User user);
	
	/**
	 * 更新用户密码
	 * @param user
	 * @return
	 */
	public int updatePasswordById(User user);
	
	/**
	 * 更新登录信息，如：登录IP、登录时间
	 * @param user
	 * @return
	 */
	public int updateLoginInfo(User user);

	/**
	 * 删除用户角色关联数据
	 * @param user
	 * @return
	 */
	public int deleteUserRole(User user);
	
	/**
	 * 插入用户角色关联数据
	 * @param user
	 * @return
	 */
	public int insertUserRole(User user);
	
	/**
	 * 更新用户信息
	 * @param user
	 * @return
	 */
	public int updateUserInfo(User user);
	
	/**
	 * 更新：luqibao
	 *time:2015/9/17 
	 *
	 * 返回所有的用户
	 * @return
	 */
	public List<User> batchSelect();
	/**
	 * 根据登录名获取用户 不用验证是否有删除标记
	 * 2016-01-14
	 */
	public User getUserByLoginNameIgnoreDel(User user);
	/**
	 * 取消删除的状态  回复用户
	 */
	public void cancelDel(User user);
	/**
	 * 
	 * 批量插入用户
	 */
	public void batchInsert(List<User> user);
	/**
	 * 批量更新用户
	 * 
	 */
	public void batchUpdate(List<User> list);
	/**
	 * 用户返回id
	 * @param user
	 * @return
	 */
	public String getId(User user);
	
	/**
	 * 返回roleid
	 * @param user
	 * @return
	 */
	public List<User> getUserRole();
	//查询出对分类有管理权限的人员
	public List<User> findUsersByCategory(Category c);
	//add by yangshw6
	/*
	 * 获取当前用户的roleid
	 * 
	 * 
	 */
	public String findRoleId(String userid);
	//end by yangshw6
	
	public List<String> findOfficeNameByUserName(String username);
	//通过模糊查询名字拿到对应的用户
	public List<User> findUserBySearch(String username);
	
	/**
	 * 根据id 批量查询用户
	 * 
	 */
	public List<User> batchget(Map map);
	
	/**
	 * 批量上传者统计 原创,转载,知识数量
	 * 
	 */
	public List<Map<String,Object>> findStatsListByUser(Map<String,Object> list);
	/**
	 * 查询具有某权限的所有人员列表
	 * @yangshw6
	 */
	public List<User> findUserByRoleId(Map map);
	
	/**
	 *审核者报表
	 * @param list
	 * @return
	 */
	public List<Map<String,Object>> findExamerStatsList(Map<String,Object> list);
	
	
}

