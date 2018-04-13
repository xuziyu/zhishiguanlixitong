package com.yonyou.kms.modules.cms.entity;

import com.yonyou.kms.common.persistence.DataEntity;

public class DepartContribution extends DataEntity<DepartContribution>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String departid;//部门编号
	private String departname;//部门名称
	private int countarticle;//部门贡献数
	private String parentName; //上级部门
	public DepartContribution(String departid, String departname,
			int countarticle, String parentName) {
		super();
		this.departid = departid;
		this.departname = departname;
		this.countarticle = countarticle;
		this.parentName = parentName;
	}
	public DepartContribution() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DepartContribution(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	public String getDepartid() {
		return departid;
	}
	public void setDepartid(String departid) {
		this.departid = departid;
	}
	public String getDepartname() {
		return departname;
	}
	public void setDepartname(String departname) {
		this.departname = departname;
	}
	public int getCountarticle() {
		return countarticle;
	}
	public void setCountarticle(int countarticle) {
		this.countarticle = countarticle;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
}
