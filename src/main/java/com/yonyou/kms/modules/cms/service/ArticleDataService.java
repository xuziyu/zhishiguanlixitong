/**
 * 
 */
package com.yonyou.kms.modules.cms.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.yonyou.kms.common.service.CrudService;
import com.yonyou.kms.modules.cms.dao.ArticleDataDao;
import com.yonyou.kms.modules.cms.entity.ArticleData;

/**
 * 站点Service
 * @author hotsum
 * @version 2013-01-15
 */
@Service
@Transactional(readOnly = true)
public class ArticleDataService extends CrudService<ArticleDataDao, ArticleData> {
	
	public List<ArticleData> findAllByIds(List<String> ids){
		if(ids==null){
			ids=Lists.newArrayList();
		}
		List<ArticleData> list=dao.findAllByIds(ids);
		if(list==null){
			list=Lists.newArrayList();
		}
		return list;
	}
}
