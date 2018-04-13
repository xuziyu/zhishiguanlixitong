package com.yonyou.kms.modules.cms.entity;

import com.yonyou.kms.common.persistence.DataEntity;

public class ArticleLabel extends DataEntity<ArticleLabel>{
	private String articleid;	//知识id
	private Category category;	//知识分类
	private String labelid;		//标签id
	private String labelvalue;	//标签的标题
	private String title;		//知识标题
	private String categorylist; //权限下的知识分类集合
	private String labellist;	//删除标签列表
	private String hits;	//文章点击数
	public ArticleLabel() {
		super();
		this.category=new Category();
		// TODO Auto-generated constructor stub
	}
	public ArticleLabel(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
	public ArticleLabel(String articleid, Category category, String labelid,
			String labelvalue, String title, String categorylist,
			String labellist, String hits) {
		super();
		this.articleid = articleid;
		this.category = category;
		this.labelid = labelid;
		this.labelvalue = labelvalue;
		this.title = title;
		this.categorylist = categorylist;
		this.labellist = labellist;
		this.hits = hits;
	}
	public String getArticleid() {
		return articleid;
	}
	public void setArticleid(String articleid) {
		this.articleid = articleid;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public String getLabelid() {
		return labelid;
	}
	public void setLabelid(String labelid) {
		this.labelid = labelid;
	}
	public String getLabelvalue() {
		return labelvalue;
	}
	public void setLabelvalue(String labelvalue) {
		this.labelvalue = labelvalue;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCategorylist() {
		return categorylist;
	}
	public void setCategorylist(String categorylist) {
		this.categorylist = categorylist;
	}
	public String getLabellist() {
		return labellist;
	}
	public void setLabellist(String labellist) {
		this.labellist = labellist;
	}
	public String getHits() {
		return hits;
	}
	public void setHits(String hits) {
		this.hits = hits;
	}
	
}
