package com.yonyou.kms.modules.cms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;


import com.yonyou.kms.common.persistence.CrudDao;
import com.yonyou.kms.common.persistence.annotation.MyBatisDao;
import com.yonyou.kms.modules.cms.entity.Article;
import com.yonyou.kms.modules.cms.entity.ArticleFeaturePackage;
import com.yonyou.kms.modules.cms.entity.FeaturePackage;
import com.yonyou.kms.modules.sys.entity.User;

/**
 * 专题管理DAO接口
 * 
 * @version 2013-8-23
 */
@MyBatisDao
public interface FeaturePackageDao extends CrudDao<FeaturePackage> {
	
	public FeaturePackage getByName(FeaturePackage featurePackage);

	/**
	 * 插入专题包知识关联数据
	 * @param article
	 * @return
	 */
	public int insertArticleFeature(ArticleFeaturePackage articleFeaturePackage);

	/*查询知识所属的专题包*/
	public List<String> getFeaturePackageIdsList(Article article);

	/*移除专题包下的一条知识*/
	public void deleteArtileFeaturePackage(String featuerPackageId, String aritcleId);

	/*批量删除专题包下的知识*/
	public void delBatch(Map<String,Object> map);
	
	//查询知识关联的专题数据 add by yangshw6
	public List<FeaturePackage> getListByArticle(Article article);


	//批量添加知识关联的专题
	public void batchinsert(List<ArticleFeaturePackage>  list);
	
	
	

	public List<User> findAllUserFromFeaturePackageID(String id);

	public void assignusertofeaturepackage(String userid, String fid);

	public void unassignuser(String userId, String featurePackageId);

	//删除专题包下所有知识的关联关系
	public void deleteArticleFeaturePackage(FeaturePackage featurePackage);
	
	//删除专题包下所有用户的关联关系
	public void deleteUserFeaturePackage(FeaturePackage featurePackage);
	//删除知识下所有专题的关联关系
	public void deleteArticle(String articleId);
	
	//获取专题包中sort字段的最大值
	public String getMaxSort();

	//查询当前用户下 排序好的专题包 的前五条数据
	public List<FeaturePackage> getFeaturePackageListData(String userId);

	//获取专题包下的知识数
	public String getCount(FeaturePackage f);
	
	//获取当前用户被授权的专题包
	public List<String> getFeaturePackageByUser(String userId);

	public List<FeaturePackage> findFeaturePackageByUserId(FeaturePackage featurePackage);
	
	//取出所有专题下关联的知识(分页) 按点击量降序排列
	public List<String> findAllListByFeature(Map<String,Object> map);
	
	/**
	 * 判断用户对专题包是否具有权限
	 * @param userId
	 * @param featurepackageId
	 * @return
	 */
	public int hasFeaturePackage(@Param("userId")String userId,@Param("featurepackageId")String featurePackageId);

	public List<FeaturePackage> findKnowledgeList(FeaturePackage fp);
}
