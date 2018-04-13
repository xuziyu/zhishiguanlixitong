package com.yonyou.kms.modules.cms.entity;

import com.yonyou.kms.common.persistence.DataEntity;

/**
 * sys_article_featurepackage
 * 
 *
 */
public class ArticleFeaturePackage extends DataEntity<ArticleFeaturePackage>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Article article;
	private FeaturePackage featurePackage;
	public Article getArticle() {
		return article;
	}
	public void setArticle(Article article) {
		this.article = article;
	}
	public FeaturePackage getFeaturePackage() {
		return featurePackage;
	}
	public void setFeaturePackage(FeaturePackage featurePackage) {
		this.featurePackage = featurePackage;
	}
	
}
