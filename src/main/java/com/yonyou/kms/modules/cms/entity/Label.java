package com.yonyou.kms.modules.cms.entity;

import com.yonyou.kms.common.persistence.DataEntity;

public class Label extends DataEntity<Label>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String labelvalue;		//标签的标题
	private String countuser;		//关联的用户数
	private String countarticle;	//关联的知识数
	private String userid;			//关联的用户id
	private String articleid;		//关联的知识id
	private String labellist; 		//标签列表
	private String categoryid;		//知识分类id
	private String labelcontent;	//标签的内容
	private int ischecked;			//0表示:没有 1表示:有
	private String pid;				//上级的id
	private String pname;			//上级标题
	private String pids;			//上级列表
	private String relationid;		//被关联的标签id
	private String relationname;	//被关联的标签标题
	private String mainid;			//关联标签的标签id
	private String issys;			//是否是系统标签，0为NC系统标签,1为自主增加,2为本系统标签
	public Label() {
		super();
		this.issys="2";
		this.ischecked=0;
		this.labelvalue="";
		// TODO Auto-generated constructor stub
	}
	public Label(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
	public String getIssys() {
		return issys;
	}
	public void setIssys(String issys) {
		this.issys = issys;
	}
	public String getMainid() {
		return mainid;
	}
	public void setMainid(String mainid) {
		this.mainid = mainid;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getPids() {
		return pids;
	}
	public void setPids(String pids) {
		this.pids = pids;
	}
	public String getRelationid() {
		return relationid;
	}
	public void setRelationid(String relationid) {
		this.relationid = relationid;
	}
	public String getRelationname() {
		return relationname;
	}
	public void setRelationname(String relationname) {
		this.relationname = relationname;
	}
	public Label(String labelvalue, String countuser, String countarticle,
			String userid, String articleid, String labellist,
			String categoryid, String labelcontent, int ischecked, String pid,
			String pname, String pids, String relationid, String relationname) {
		super();
		this.labelvalue = labelvalue;
		this.countuser = countuser;
		this.countarticle = countarticle;
		this.userid = userid;
		this.articleid = articleid;
		this.labellist = labellist;
		this.categoryid = categoryid;
		this.labelcontent = labelcontent;
		this.ischecked = ischecked;
		this.pid = pid;
		this.pname = pname;
		this.pids = pids;
		this.relationid = relationid;
		this.relationname = relationname;
	}
	public int getIschecked() {
		return ischecked;
	}
	public void setIschecked(int ischecked) {
		this.ischecked = ischecked;
	}
	public String getLabelvalue() {
		return labelvalue;
	}
	public void setLabelvalue(String labelvalue) {
		this.labelvalue = labelvalue;
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
	public String getLabellist() {
		return labellist;
	}
	public void setLabellist(String labellist) {
		this.labellist = labellist;
	}
	public String getCategoryid() {
		return categoryid;
	}
	public void setCategoryid(String categoryid) {
		this.categoryid = categoryid;
	}
	public String getLabelcontent() {
		return labelcontent;
	}
	public void setLabelcontent(String labelcontent) {
		this.labelcontent = labelcontent;
	}
	
	@Override
	public String toString() {
		return this.labelvalue;
	}
	
}
