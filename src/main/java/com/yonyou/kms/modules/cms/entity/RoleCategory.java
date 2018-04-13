package com.yonyou.kms.modules.cms.entity;

import com.yonyou.kms.common.persistence.DataEntity;
import com.yonyou.kms.modules.sys.entity.Role;
/**
 * 
 * @author Hotusm
 *  映射sys_role_category表的关系
 */

public class RoleCategory extends DataEntity<UserCategory>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Role role;
	private Category category;
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	
}
