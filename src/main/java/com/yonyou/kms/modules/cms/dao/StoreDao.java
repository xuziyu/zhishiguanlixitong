package com.yonyou.kms.modules.cms.dao;

import java.util.List;

import com.yonyou.kms.common.persistence.CrudDao;
import com.yonyou.kms.common.persistence.annotation.MyBatisDao;
import com.yonyou.kms.modules.cms.entity.Store;
/**
 * 收藏DAO接口
 * @author zy
 * 
 */
@MyBatisDao
public interface StoreDao extends CrudDao<Store>{
	public List<Store> getStore(String tid);
	
	public void deleteUserStore(Store entity);
	
	public Store get(Store store);
	public void MergeArticle(String originalcategoryId,String categoryId,String articleId);

	public void MergeCategory(String originalcategoryId, String categoryId);
}
