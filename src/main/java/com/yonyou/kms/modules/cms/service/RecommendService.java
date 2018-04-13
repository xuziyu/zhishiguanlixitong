package com.yonyou.kms.modules.cms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yonyou.kms.common.persistence.Page;
import com.yonyou.kms.common.service.CrudService;
import com.yonyou.kms.modules.cms.dao.ArticleDao;
import com.yonyou.kms.modules.cms.dao.CategoryDao;
import com.yonyou.kms.modules.cms.dao.RecommendDao;
import com.yonyou.kms.modules.cms.entity.ArticleAttFile;
import com.yonyou.kms.modules.cms.entity.Category;
import com.yonyou.kms.modules.cms.entity.Recommend;
import com.yonyou.kms.modules.cms.entity.Store;
import com.yonyou.kms.modules.oa.entity.OaNotify;
import com.yonyou.kms.modules.oa.service.OaNotifyService;
import com.yonyou.kms.modules.sys.utils.UserUtils;
/**
 * 评论Service
 * @author zy
 * 
 */
@Service
public class RecommendService extends CrudService<RecommendDao, Recommend> {
	@Autowired 
	RecommendDao recommendDao;
	@Autowired 
	ArticleDao articleDao;
	@Autowired 
	OaNotifyService oaNotifyService;
	@Autowired
	private CategoryDao categoryDao;

	public Page<Recommend> findPage(Page<Recommend> page, Recommend recommend) {//分页显示
//		DetachedCriteria dc = commentDao.createDetachedCriteria();
//		if (StringUtils.isNotBlank(comment.getContentId())){
//			dc.add(Restrictions.eq("contentId", comment.getContentId()));
//		}
//		if (StringUtils.isNotEmpty(comment.getTitle())){
//			dc.add(Restrictions.like("title", "%"+comment.getTitle()+"%"));
//		}
//		dc.add(Restrictions.eq(Comment.FIELD_DEL_FLAG, comment.getDelFlag()));
//		dc.addOrder(Order.desc("id"));
//		return commentDao.find(page, dc);
		recommend.getSqlMap().put("dsf", dataScopeFilter(recommend.getCurrentUser(), "o", "u"));//获取当前user
		if (recommend.getCategory()!=null && org.apache.commons.lang3.StringUtils.isNotBlank(recommend.getCategory().getId()) && !Category.isRoot(recommend.getCategory().getId())){
			Category category = categoryDao.get(recommend.getCategory().getId());
			if (category==null){
				category = new Category();
			}
			category.setParentIds(category.getId());
			category.setSite(category.getSite());
			recommend.setCategory(category);
		}
		else{
			recommend.setCategory(new Category());
		}
		return super.findPage(page, recommend);
	}
	
	@Override
	public String save(Recommend entity) {
		Recommend recommend= recommendDao.get(entity);
		if(recommend==null){
			entity.setIsNewRecord(false);
			entity.preInsert();
			dao.insert(entity);
		}else{
			entity.preUpdate();
			dao.update(entity);
		}
		return "";
	}
	public Recommend getRecommend( String tid ){
		return recommendDao.getRecommend(tid);
	}
	public Recommend get(Recommend recommend){
		return recommendDao.get(recommend);
	}
	public void delete(Recommend entity) {
		super.delete(entity);
	}
	//add hefeng
	public void deleteUserRecommend(Recommend recommend) {
		if(recommend==null){
			recommend=new Recommend();
		}
		recommendDao.deleteUserRecommend(recommend);
	}
	public void MergeArticle(String originalcategoryId,String categoryId,String articleId){
		dao.MergeArticle(originalcategoryId, categoryId, articleId);
	}
	//end hefeng

	public void MergeCategory(String originalcategoryId, String categoryId) {
		dao.MergeCategory(originalcategoryId, categoryId);
	}
}
