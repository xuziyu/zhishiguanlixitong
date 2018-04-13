/**
 * 
 */
package com.yonyou.kms.modules.cms.entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.yonyou.kms.common.config.Global;
import com.yonyou.kms.common.persistence.DataEntity;

/**
 * 文章Entity
 * @author hotsum
 * @version 2013-01-15
 */
public class ArticleData extends DataEntity<ArticleData> {

	private static final long serialVersionUID = 1L;
	private String id;		// 编号
	private String content;	// 内容
	private String copyfrom;// 来源,暂用:是否同步过app 0-未同步；1-已同步；2-已修改 修改知识后需要改成不同步
 	private String relation;// 相关文章
	private String allowComment;// 是否允许评论
	private String allowshare;// 是否允许分享 huangmj 2015.10.22 1代表可以分享 0代表不可分享
	private Article article;
	private String isallowdownload;//允许下载 huangmj 2015.11.3
	private String hasAttFile;  //是否存在附件，0表示无，1表示有
	private String hasStore;		//登陆用户是否收藏，0表示无，1至多表示有
	public ArticleData() {
		super();
		this.isallowdownload="1";//默认允许下载
		this.allowshare = "0";//默认不允许分享
		this.allowComment = Global.YES;
		this.hasAttFile="0";
		this.hasStore="0";
	}
	
	public ArticleData(String id){
		this();
		this.id = id;
		this.hasAttFile="0";
	}

	
	

	public String getIsallowdownload() {
		return isallowdownload;
	}

	public void setIsallowdownload(String isallowdownload) {
		this.isallowdownload = isallowdownload;
	}

	public String getAllowshare() {
		return allowshare;
	}

	public void setAllowshare(String allowshare) {
		this.allowshare = allowshare;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@NotBlank
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Length(min=0, max=255)
	public String getCopyfrom() {
		return copyfrom;
	}

	public void setCopyfrom(String copyfrom) {
		this.copyfrom = copyfrom;
	}

	@Length(min=0, max=255)
	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	@Length(min=1, max=1)
	public String getAllowComment() {
		return allowComment;
	}

	public void setAllowComment(String allowComment) {
		this.allowComment = allowComment;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public String getHasAttFile() {
		return hasAttFile;
	}

	public void setHasAttFile(String hasAttFile) {
		this.hasAttFile = hasAttFile;
	}

	public String getHasStore() {
		return hasStore;
	}

	public void setHasStore(String hasStore) {
		this.hasStore = hasStore;
	}
	
}