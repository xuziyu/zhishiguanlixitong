package com.yonyou.kms.modules.cms.entity;

import java.util.Date;

import com.yonyou.kms.common.persistence.DataEntity;
/**
 * 
 * 推荐entity
 * @author zy
 *
 */
public class Recommend extends DataEntity<Recommend>{
	
	private static final long serialVersionUID = 1L;
	private Category category;// 分类编号
	private Date recomDate;//推荐时间
	private Date articleupdateDate;//知识更新时间
	private String articletitle;//知识标题（新方法）add hefeng
	private int recomCount;//推荐次数
	private String delFlag;	// 删除标记删除标记（0：正常；1：删除；2：审核）
	private String titleId;//文章ID
	private String title;//文章标题
	
	public Recommend(){
		super();
	}
	
	public Recommend(String id){
		this();
		this.id = id;
	}
	
	public Recommend(Date recomDate){
		super();
		this.recomDate=recomDate;
		
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Date getRecomDate() {
		return recomDate;
	}

	public void setRecomDate(Date recomDate) {
		this.recomDate = recomDate;
	}

	

	public int getRecomCount() {
		return recomCount;
	}

	public void setRecomCount(int recomCount) {
		this.recomCount = recomCount;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getTitleId() {
		return titleId;
	}

	public void setTitleId(String titleId) {
		this.titleId = titleId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getArticleupdateDate() {
		return articleupdateDate;
	}

	public void setArticleupdateDate(Date articleupdateDate) {
		this.articleupdateDate = articleupdateDate;
	}

	public String getArticletitle() {
		return articletitle;
	}

	public void setArticletitle(String articletitle) {
		this.articletitle = articletitle;
	}
	
	
}
