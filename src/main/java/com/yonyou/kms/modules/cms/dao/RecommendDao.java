package com.yonyou.kms.modules.cms.dao;

import java.util.List;

import com.yonyou.kms.common.persistence.CrudDao;
import com.yonyou.kms.common.persistence.annotation.MyBatisDao;
import com.yonyou.kms.modules.cms.entity.Recommend;
import com.yonyou.kms.modules.cms.entity.Store;
/**
 * 评论DAO
 * @author zy
 * 
 */
@MyBatisDao
public interface RecommendDao extends CrudDao<Recommend>{
	public Recommend getRecommend(String tid);
	public Recommend get(Recommend recommend);
	
	public List<Recommend> getRecommendList(String titleid);

	public void deleteUserRecommend(Recommend recommend);
	public void MergeArticle(String originalcategoryId,String categoryId,String articleId);
	public void MergeCategory(String originalcategoryId, String categoryId);
}
