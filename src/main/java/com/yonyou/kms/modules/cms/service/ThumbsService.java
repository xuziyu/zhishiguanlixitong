package com.yonyou.kms.modules.cms.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.restlet.resource.Get;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysql.fabric.xmlrpc.base.Data;
import com.sun.accessibility.internal.resources.accessibility;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.yonyou.kms.common.persistence.Page;
import com.yonyou.kms.common.service.CrudService;
import com.yonyou.kms.modules.cms.dao.ArticleCountDao;
import com.yonyou.kms.modules.cms.dao.CategoryDao;
import com.yonyou.kms.modules.cms.dao.ShareDao;
import com.yonyou.kms.modules.cms.dao.ThumbsDao;
import com.yonyou.kms.modules.cms.entity.Category;
import com.yonyou.kms.modules.cms.entity.Share;
import com.yonyou.kms.modules.cms.entity.Thumbs;

/**
 * 分享Service
 * @author zy
 * 
 */
@Service
public class ThumbsService  extends CrudService<ThumbsDao, Thumbs>{
	@Autowired 
	ThumbsDao thumbsDao;
	
	@Autowired
	ArticleCountService articlecountService;
	
	public Thumbs getEntity(String articleId,String userId){
		Thumbs thumbs = thumbsDao.getEntity(articleId, userId);
		return thumbs;
	}
	
	@Override
	public String save(Thumbs entity) {
		Thumbs thumbs= thumbsDao.getEntity(entity.getThumbsId(), entity.getUserId());
		if(thumbs==null){
			entity.setIsNewRecord(false);
			entity.preInsert();
			int k = thumbsDao.insertEntity(entity.getThumbsId(), entity.getUserId(),entity.getIsThumbs());
		}else{
			entity.preUpdate();
		}
		articlecountService.updateSingleData(6,1,entity.getThumbsId());
		return "";
	}
	
	public int updateEntity(String isThumbs,String thumbsId,String userId){
		int k = 0;
		k = thumbsDao.updateEntity(isThumbs, thumbsId, userId);
		if (null != isThumbs && !"".equals(isThumbs)) {
			// 0为点赞  1为取消点赞
			if ("0".equals(isThumbs)) {
				articlecountService.updateSingleData(6,1,thumbsId);
			}else if ("1".equals(isThumbs)) {
				articlecountService.updateSingleData(6,0,thumbsId);
			}
		}
		return k;
	}

}
