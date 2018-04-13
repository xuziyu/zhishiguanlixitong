package com.yonyou.kms.modules.cms.entity;

import com.yonyou.kms.common.persistence.DataEntity;
import com.yonyou.kms.modules.sys.entity.User;

/**
 * sys_user_category
 * @author Hotusm
 *
 */
public class UserCategory extends DataEntity<UserCategory>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Category category;
	private User user;
	
	public UserCategory() {
		super();
		category = new Category();
		user = new User();
		// TODO Auto-generated constructor stub
	}
	public UserCategory(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
