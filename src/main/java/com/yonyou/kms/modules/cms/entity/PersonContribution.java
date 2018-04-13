package com.yonyou.kms.modules.cms.entity;

import com.yonyou.kms.common.persistence.DataEntity;

public class PersonContribution extends DataEntity<PersonContribution>{
	
	private String userid;
	private String username;
	private int countarticle;
	
	public PersonContribution() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PersonContribution(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	public PersonContribution(String userid, String username, int countarticle) {
		super();
		this.userid = userid;
		this.username = username;
		this.countarticle = countarticle;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getCountarticle() {
		return countarticle;
	}
	public void setCountarticle(int countarticle) {
		this.countarticle = countarticle;
	}
	
	
}
