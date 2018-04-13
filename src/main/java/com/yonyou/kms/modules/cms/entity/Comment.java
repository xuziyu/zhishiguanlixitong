/**
 * 
 */
package com.yonyou.kms.modules.cms.entity;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yonyou.kms.common.persistence.DataEntity;
import com.yonyou.kms.modules.sys.entity.User;

/**
 * 评论Entity
 * @author hotsum
 * @version 2013-05-15
 */
public class Comment extends DataEntity<Comment> {

	private static final long serialVersionUID = 1L;
	private Category category;// 分类编号
	private String articleCreater;//文章创建人
	private String contentId;	// 归属分类内容的编号（Article.id、Photo.id、Download.id）
	private String title;	// 归属分类内容的标题（Article.title、Photo.title、Download.title）
	private String articletitle;//知识标题（新方法）add hefeng
	private String content; // 评论内容
	private String name; 	// 评论人姓名
	private String ip; 		// 评论IP
	private Date createDate;// 评论时间
	private User auditUser; // 审核人
	private Date auditDate;	// 审核时间
	private String delFlag="0";	// 删除标记删除标记（0：正常；1：删除；2：审核）
	private String articleCreaterId;//文章创建人ID
	private String nameId;//评论人ID
	private List<String> categoryids;

	public Comment() {
		super();
//		this.delFlag = DEL_FLAG_AUDIT;
	}
	
	public Comment(String id){
		this();
		this.id = id;
	}
	
	public Comment(Category category){
		this();
		this.category = category;
	}
	


	@NotNull
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getArticleCreater() {
		return articleCreater;
	}

	public void setArticleCreater(String articleCreater) {
		this.articleCreater = articleCreater;
	}

	@NotNull
	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	@Length(min=1, max=255)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=1, max=255)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=1, max=100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(User auditUser) {
		this.auditUser = auditUser;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Override
	@NotNull
	public Date getCreateDate() {
		return createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	@Length(min=1, max=1)
	public String getDelFlag() {
		return delFlag;
	}

	@Override
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getArticleCreaterId() {
		return articleCreaterId;
	}

	public void setArticleCreaterId(String articleCreaterId) {
		this.articleCreaterId = articleCreaterId;
	}

	public String getNameId() {
		return nameId;
	}

	public void setNameId(String nameId) {
		this.nameId = nameId;
	}

	public String getArticletitle() {
		return articletitle;
	}

	public void setArticletitle(String articletitle) {
		this.articletitle = articletitle;
	}

	public List<String> getCategoryids() {
		return categoryids;
	}

	public void setCategoryids(List<String> categoryids) {
		this.categoryids = categoryids;
	}


	
}