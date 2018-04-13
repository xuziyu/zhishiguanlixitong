package com.yonyou.kms.modules.cms.dao;

import java.util.List;

import com.yonyou.kms.common.persistence.CrudDao;
import com.yonyou.kms.common.persistence.annotation.MyBatisDao;
/*
 *首页显示动态知识库树dao
 * 
 * 
 */
import com.yonyou.kms.modules.cms.entity.CategoryTree;
@MyBatisDao
public interface CategoryTreeDao extends CrudDao<CategoryTree>{
	//去数据,去出categoryid和他对应的parentid及name
	public List<CategoryTree> getcategoryData(List<String> categorylist);
	//取得知识分类下的总父级编号及名称
	public  List<CategoryTree>  getAllFather(List<String> categorylist);
	//批量获取
	public List<CategoryTree>	batchget(List<String>	id);
}
