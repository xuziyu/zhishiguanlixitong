package com.yonyou.kms.modules.cms.entity;

import java.util.Date;

import com.yonyou.kms.common.persistence.DataEntity;
/**
 * 
 * 收藏entity
 * @author zy
 *
 */
public class Store extends DataEntity<Store>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Category category;// 分类编号
	private int storeCount;//收藏次数
	private Date storeDate;//收藏时间
	private Date articleupdateDate;//知识更新时间
	private String articletitle;//知识标题（新方法）add hefeng
	private String creater;//收藏人
	private String title;//文章标题（知识标题改动后，这里不改动）
	private String titleId;//收藏文章的ID
	private String upLoadUserId;//上传用户name
	//private String contentId;	// 归属分类内容的编号（Article.id、Photo.id、Download.id）
	private String delFlag;	// 删除标记删除标记（1：删除；2：收藏）
	
	public Store(Category category){
		this();
		this.category = category;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public Store(){
		super();
	}
	public Store(String titleId){
		this();
		this.titleId = titleId;
	}
	public Store(String id,String creater){
		super();
		this.id=id;
		this.creater=creater;
	}
	
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public int getStoreCount() {
		return storeCount;
	}
	public void setStoreCount(int storeCount) {
		this.storeCount = storeCount;
	}
	
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getStoreDate() {
		return storeDate;
	}
	public void setStoreDate(Date storeDate) {
		this.storeDate = storeDate;
	}
	public String getTitleId() {
		return titleId;
	}
	public void setTitleId(String titleId) {
		this.titleId = titleId;
	}
	public String getUpLoadUserId() {
		return upLoadUserId;
	}
	public void setUpLoadUserId(String upLoadUserId) {
		this.upLoadUserId = upLoadUserId;
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
	
	
	
//	public String getContentId() {
//		return contentId;
//	}
//	public void setContentId(String contentId) {
//		this.contentId = contentId;
//	}
	
	
	
}
