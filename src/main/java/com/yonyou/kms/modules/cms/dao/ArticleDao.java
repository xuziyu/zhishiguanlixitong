/**
 * 
 */
package com.yonyou.kms.modules.cms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yonyou.kms.common.persistence.CrudDao;
import com.yonyou.kms.common.persistence.annotation.MyBatisDao;
import com.yonyou.kms.modules.cms.entity.Article;
import com.yonyou.kms.modules.cms.entity.Category;
import com.yonyou.kms.modules.cms.entity.FeaturePackage;
import com.yonyou.kms.modules.sys.entity.Audit;
import com.yonyou.kms.modules.sys.entity.User;

/**
 * 文章DAO接口
 * @author hotsum
 * @version 2013-8-23
 */
@MyBatisDao
public interface ArticleDao extends CrudDao<Article> {
	
	public List<Article> findByIdIn(String[] ids);
//	{
//		return find("from Article where id in (:p1)", new Parameter(new Object[]{ids}));
//	}
	
	public int updateHitsAddOne(String id);
//	{
//		return update("update Article set hits=hits+1 where id = :p1", new Parameter(id));
//	}
	
	public int updateCopyFrom(Map map);
	
	public int updateExpiredWeight(Article article);
	
	public List<Category> findStats(Category category);
//	{
//		return update("update Article set weight=0 where weight > 0 and weightDate < current_timestamp()");
//	}
	//add by zhengyu
	public void updateDelflag(Article article);
	//end
	
	//add by wuwq
	public void changeReason(Article article);
//	public void showReason(Article article);
	//end
	
	//add hefeng
	public String findArticleId(String userid);
	public Article findArticleListPage(String id);
	public List<Article> findListPage(Article article);
	public void deleteUserArticle(Article article);
	public void MergeArticle(String originalcategoryId,String categoryId,String articleId);
	//end
	
	//add by yinshh3 10月23 14:58
	/**
	 * 返回只有id和delflag两个属性的Article对象.被list装在
	 */
	public List<Article> getArticleID(User user);
	//end
	//add by luqibao
	public List<Article> findArticlesByCategoryId(String id,String userid);
	//根据SQL语句的拼接 返回文章的集合
	public List<Article> findArticlesBySql(String ids,String userid);
	
	public List<Article> findListByIds(Map	map);
	
	public List<Article> findListByTitle(String param,String userid);
	
	public List<Article> findListByLabelValue(String param,String userid);
	
	public List<Article> findListByContent(String param,String userid);
	
	//查询原创，转载文章
 	public List<Article> findListByOriginal(String state,String userid);
	
 	//add by yangshw6
	public List<Article> findListByUser(Map<String,Object> param);
	
	public int getCountArticleByUser(Map<String,Object> param);
	
	//add by 卢启宝
	public List<Article> findListByExamer(Map<String,Object> param);
	
	public int getCountArticleByExamer(Map<String,Object> param);
	
	
	public void updateDel(Article article);

	public void revertMsgFlag(String articleId,String MsgFlag);
	
	public List<Article> getNewestArticle(Map map);

	public void MergeCategory(String originalcategoryId, String categoryId);
	
	public List<Article> findAllByCategoryId(Category category);
	//end
 	public List<String> findAllowShareCategoryIds();
//	
//	public String getArticelUserOfficeCompanyName(User user);
 	//add by yangshw6
 	public List<Article> getHotestArticle(Map map);
 	//保存审批人
 	public void updateExaminerId(Article article);
 	
 	//add by lnj
 	public List<Article> findListByArticleTitle(Article title);
 	
 	//add by lnj
 	public List<Article> getArticlesFromFeaturePackage(FeaturePackage featurePackage);
 	
 	//add by lnj
 	public List<Article> getArticlesFromFeaturePackage(Article article);

	public List<Article> addArticleSearch(Map<String, Object> map);
 	
	//根据条件查询app知识列表,分页
	public List<Map<String,Object>> getAllListByCondition(Map<String,Object> map);
	/**
	 * 每一篇知识都有相应的审核员
	 * @param param
	 */
	public void insertArticleAudit(Map<String,Object> param);
	/**
	 * 查询需要指定用户审核的知识
	 */
	public List<String> findArticleNeedAuditByUserId(String userId);
	/**
	 * 查找指定审核人需要审核的知识
	 * @param articles
	 * @return
	 */
	public List<Article> findNeedAuditList(Article article);
	/**
	 * 插入批量的数据
	 * @param audits
	 */
	public void saveAudits(List<Audit> audits);
	/**
	 * 清除审核管理员的所有信息
	 */
	public void clearAudits();
	/**
	 * 查询所有的数据库数据  并且不加任何条件
	 * @return
	 */
	public List<Article> findListNoCondition();
	
	/**
	 * 查询知识的审核员
	 * @param articleId
	 * @return
	 */
	public List<String> findArticleAudit(String articleId);
	
	/**
	 * 通过知识分类获取具有权限的知识
	 * @param categoryidlist
	 * @return
	 */
	public List<Article> getArticlesByCategoryIds(@Param("categoryids")List<String> categoryidlist);
	
	/**
	 * 专家审核的知识数
	 * @param professorId 
	 * @return
	 */
	public int professorNeedAudit(@Param("professorId") String professorId,@Param("delFlag") String delFlag);
	/**
	 * aritcleId<->label
	 * @date 2016-10-17
	 * @return
	 */
	public List<Map<String,Object>> listAllArticleLables();
	
	//add by linj 通过标签值获取知识列表
	public List<Article> findListByLabel(String param,String userid);

	public List<Map<String, Object>> listAllArticleLables2();
	
	/**
	 * 未审核知识知识审核员
	 * @param articleId
	 * @return
	 */
	public List<User> articlePreExamers(@Param("articleId")String articleId,@Param("needExamer")boolean needExamer);
	
	
	/******************************2017-11-07新增*********************************/
	//根据条件查询app知识列表,分页
	public List<Map<String,Object>> getKnowledgeListByCondition(Map<String,Object> map);
	
	//根据条件查询app产品列表,分页
	public List<Map<String,Object>> getProductListByCondition(Map<String,Object> map);
	
	//根据条件查询app业务知识列表,分页
	public List<Map<String, Object>> getBusinessListByCondition(Map<String, Object> map);
}
