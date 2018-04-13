/**
 * 
 */
package com.yonyou.kms.modules.cms.entity;

import java.util.Date;

import com.yonyou.kms.common.persistence.DataEntity;

/**
 * 文章Entity
 * 
 * @author hotsum
 * @version 2013-05-15
 */
public class Switch extends DataEntity<Switch> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String articleUrl;//知识链接
	private String imageUrl;//图片地址
	private String topicWord;//主题概要
	private String detailExplanation;//详细描述

	
	public Switch() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Switch(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	public String getArticleUrl() {
		return articleUrl;
	}
	public void setArticleUrl(String articleUrl) {
		this.articleUrl = articleUrl;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getTopicWord() {
		return topicWord;
	}
	public void setTopicWord(String topicWord) {
		this.topicWord = topicWord;
	}
	public String getDetailExplanation() {
		return detailExplanation;
	}
	public void setDetailExplanation(String detailExplanation) {
		this.detailExplanation = detailExplanation;
	}
	
	
	
}
