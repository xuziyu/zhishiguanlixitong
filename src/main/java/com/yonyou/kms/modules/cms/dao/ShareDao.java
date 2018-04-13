package com.yonyou.kms.modules.cms.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yonyou.kms.common.persistence.CrudDao;
import com.yonyou.kms.common.persistence.annotation.MyBatisDao;
import com.yonyou.kms.modules.cms.entity.Share;
/**
 * 分享DAO接口
 * @author zy
 * 
 */
@MyBatisDao
public interface ShareDao extends CrudDao<Share>{
	public Share getShare(String tid);
	public void MergeArticle(String originalcategoryId,String categoryId,String articleId);
	public void MergeCategory(String originalcategoryId, String categoryId);
	public List<Share> getShareList(int beginNum,int endNum);
	public List<Map<String, Object>> getAllListByCondition(Map<String, Object> map);
	public int updateEntity(String shareCount,String date,String id);
}
