package com.yonyou.kms.modules.cms.dao;

import com.yonyou.kms.common.persistence.CrudDao;
import com.yonyou.kms.common.persistence.annotation.MyBatisDao;
import com.yonyou.kms.modules.cms.entity.Thumbs;
/**
 * 分享DAO接口
 * @author zy
 * 
 */
@MyBatisDao
public interface ThumbsDao extends CrudDao<Thumbs>{
	public int updateEntity(String isThumbs,String thumbsId,String userId);

	public Thumbs getEntity(String articleId, String userId);

	public int insertEntity(String thumbsId, String userId, String isThumbs);
}
