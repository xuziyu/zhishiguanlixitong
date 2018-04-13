package com.yonyou.kms.modules.cms.entity;

import java.util.Date;

import com.yonyou.kms.common.persistence.DataEntity;
/**
 * 
 * 分享entity
 * @author zy
 *
 */
public class Share extends DataEntity<Share>{

	private static final long serialVersionUID = 1L;
	private Category category;// 分类编号
	private String title;//文章name
	private String articletitle;//知识标题（新方法）add hefeng
	private String titleId;//文章id
	//private String authorId;//分享人id
	private String ownlib;//文章所属分类
	private Date shareDate;//分享时间
	private Date articleupdateDate;//知识更新时间
	private String way;//分享方式
	private String allowShare;//是否允许分享
	private String shareCount;//分享次数
	
	public Share(){
		super();
	}
	public Share(String id){
		this();
		this.id = id;
	}
	public Share(String id,String titleId){
		super();
		this.id=id;
		this.titleId=titleId;
	}
	
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitleId() {
		return titleId;
	}
	public void setTitleId(String titleId) {
		this.titleId = titleId;
	}
//	public String getAuthorId() {
//		return authorId;
//	}
//	public void setAuthorId(String authorId) {
//		this.authorId = authorId;
//	}
	public String getOwnlib() {
		return ownlib;
	}
	public void setOwnlib(String ownlib) {
		this.ownlib = ownlib;
	}
	public Date getShareDate() {
		return shareDate;
	}
	public void setShareDate(Date shareDate) {
		this.shareDate = shareDate;
	}
	public String getWay() {
		return way;
	}
	public void setWay(String way) {
		this.way = way;
	}
	public String getAllowShare() {
		return allowShare;
	}
	public void setAllowShare(String allowShare) {
		this.allowShare = allowShare;
	}
	public String getShareCount() {
		return shareCount;
	}
	public void setShareCount(String shareCount) {
		this.shareCount = shareCount;
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
