package com.yonyou.kms.modules.sys.entity;

/**
 * 中间映射表 Office <---> User
 *
 */
public class Audit {
	
	private String userId;
	private String officeId;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
}
