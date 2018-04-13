package com.yonyou.kms.modules.cms.entity;

import com.yonyou.kms.common.persistence.DataEntity;



public class ArticleCount extends DataEntity<ArticleCount> {
	private String articleid;       //知识id
	private String articletitle;	//知识标题
	private int countclick;			//点击数
	private int countreco;			//推荐数
	private int countcomm;			//评论数
	private int countshare;			//分享数
	private int countcollect;		//收藏数
	private int countthumbs;           //点赞数
	public ArticleCount(String articleid, String articletitle, int countclick, int countreco, int countcomm,
			int countshare, int countcollect, int countthumbs) {
		super();
		this.articleid = articleid;
		this.articletitle = articletitle;
		this.countclick = countclick;
		this.countreco = countreco;
		this.countcomm = countcomm;
		this.countshare = countshare;
		this.countcollect = countcollect;
		this.countthumbs = countthumbs;
	}
	public ArticleCount() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ArticleCount(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	public String getArticleid() {
		return articleid;
	}
	public void setArticleid(String articleid) {
		this.articleid = articleid;
	}
	public String getArticletitle() {
		return articletitle;
	}
	public void setArticletitle(String articletitle) {
		this.articletitle = articletitle;
	}
	public int getCountclick() {
		return countclick;
	}
	public void setCountclick(int countclick) {
		this.countclick = countclick;
	}
	public int getCountreco() {
		return countreco;
	}
	public void setCountreco(int countreco) {
		this.countreco = countreco;
	}
	public int getCountcomm() {
		return countcomm;
	}
	public void setCountcomm(int countcomm) {
		this.countcomm = countcomm;
	}
	public int getCountshare() {
		return countshare;
	}
	public void setCountshare(int countshare) {
		this.countshare = countshare;
	}
	public int getCountcollect() {
		return countcollect;
	}
	public void setCountcollect(int countcollect) {
		this.countcollect = countcollect;
	}
	public int getCountthumbs() {
		return countthumbs;
	}
	public void setCountthumbs(int countthumbs) {
		this.countthumbs = countthumbs;
	}
	
}
