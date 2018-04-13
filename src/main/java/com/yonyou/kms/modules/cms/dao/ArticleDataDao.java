/**
 * 
 */
package com.yonyou.kms.modules.cms.dao;

import java.util.List;

import com.yonyou.kms.common.persistence.CrudDao;
import com.yonyou.kms.common.persistence.annotation.MyBatisDao;
import com.yonyou.kms.modules.cms.entity.ArticleData;

/**
 * 文章DAO接口
 * @author hotsum
 * @version 2013-8-23
 */
@MyBatisDao
public interface ArticleDataDao extends CrudDao<ArticleData> {
	
	public List<ArticleData> findAllByIds(List<String> list);
}
