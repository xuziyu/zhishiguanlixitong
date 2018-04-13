package com.yonyou.kms.modules.cms.entity;

import com.yonyou.kms.common.persistence.DataEntity;
/**
 * 
 * 点赞entity
 * @author zy
 *
 */
public class Thumbs extends DataEntity<Thumbs>{

	private static final long serialVersionUID = 1L;
	
	private String thumbsId;//文章id
	
	private String userId;
	
	private String isThumbs;
	
	public Thumbs() {
		
	}
	
	public Thumbs(String thumbsId) {
		this.thumbsId = thumbsId;
	}

	public Thumbs(String thumbsId, String userId, String isThumbs) {
		this.thumbsId = thumbsId;
		this.userId = userId;
		this.isThumbs = isThumbs;
	}

	public String getThumbsId() {
		return thumbsId;
	}

	public void setThumbsId(String thumbsId) {
		this.thumbsId = thumbsId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIsThumbs() {
		return isThumbs;
	}

	public void setIsThumbs(String isThumbs) {
		this.isThumbs = isThumbs;
	}
	
}
