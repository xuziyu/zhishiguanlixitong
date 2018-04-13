package com.yonyou.kms.modules.cms.entity;

import com.yonyou.kms.common.persistence.DataEntity;
/*
 * 用作用户关联标签数据的分页显示
 * 
 */

public class UserLabel extends DataEntity<UserLabel>{
	private String labelvalue;		//标签的标题
	private String labelid;			//标签的id
	private String userid;			//用户的id
	private String articleid;			//标签关联的知识id
	private String flag;			//两种数据格式分页的标记:flag=1 取用户标签，flag=0 取不是用户的标签
	private String countuser;		//关联用户数
	private String countarticle;	//关联知识数
	private int ischecked;			//0表示:没有 1表示:有
	private String selectedTagString;
	public UserLabel() {
		super();
		this.ischecked=0;
		// TODO Auto-generated constructor stub
	}
	public UserLabel(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	public UserLabel(String labelvalue, String labelid, String userid,
			String articleid, String flag, String countuser,
			String countarticle, int ischecked) {
		super();
		this.labelvalue = labelvalue;
		this.labelid = labelid;
		this.userid = userid;
		this.articleid = articleid;
		this.flag = flag;
		this.countuser = countuser;
		this.countarticle = countarticle;
		this.ischecked = ischecked;
	}
	public String getLabelvalue() {
		return labelvalue;
	}
	public void setLabelvalue(String labelvalue) {
		this.labelvalue = labelvalue;
	}
	public String getLabelid() {
		return labelid;
	}
	public void setLabelid(String labelid) {
		this.labelid = labelid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getArticleid() {
		return articleid;
	}
	public void setArticleid(String articleid) {
		this.articleid = articleid;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getCountuser() {
		return countuser;
	}
	public void setCountuser(String countuser) {
		this.countuser = countuser;
	}
	public String getCountarticle() {
		return countarticle;
	}
	public void setCountarticle(String countarticle) {
		this.countarticle = countarticle;
	}
	public int getIschecked() {
		return ischecked;
	}
	public void setIschecked(int ischecked) {
		this.ischecked = ischecked;
	}
	public String getSelectedTagString() {
		return selectedTagString;
	}
	public void setSelectedTagString(String selectedTagString) {
		this.selectedTagString = selectedTagString;
	}
	
}
