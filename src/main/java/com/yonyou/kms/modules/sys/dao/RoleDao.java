/**
 * 
 */
package com.yonyou.kms.modules.sys.dao;

import java.util.List;

import com.yonyou.kms.common.persistence.CrudDao;
import com.yonyou.kms.common.persistence.annotation.MyBatisDao;
import com.yonyou.kms.modules.cms.entity.Category;
import com.yonyou.kms.modules.sys.entity.Role;
import com.yonyou.kms.modules.sys.entity.User;

/**
 * 角色DAO接口
 * @author hotsum
 * @version 2013-12-05
 */
@MyBatisDao
public interface RoleDao extends CrudDao<Role> {

	public Role getByName(Role role);
	
	public Role getByEnname(Role role);

	/**
	 * 维护角色与菜单权限关系
	 * @param role
	 * @return
	 */
	public int deleteRoleMenu(Role role);

	public int insertRoleMenu(Role role);
	
	/**
	 * 维护角色与公司部门关系
	 * @param role
	 * @return
	 */
	public int deleteRoleOffice(Role role);

	public int insertRoleOffice(Role role);
	/**
	 * 设置关联关系
	 * 
	 */
	public void insertUserRole(String userId);
	//add by luqibao
	//根据传入的用户查询知识分类的id集合
	public List<String> findCategoryIdByUser(User user);
	
	public List<User> hasUser(Role role);
	/**
	 * 根据特殊分类获取下面所有的角色
	 * @param c
	 * @return
	 */
	public List<Role> findRoleByCategory(Category c);
	//end
	/**
	 * 查询所有的具有管理员权限的人
	 * @return 用户ID的集合
	 */
	public List<String> findUserHasSystemRole();
}
