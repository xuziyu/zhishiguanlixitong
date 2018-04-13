/**
 * 
 */
package com.yonyou.kms.modules.cms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.yonyou.kms.common.mapper.JsonMapper;
import com.yonyou.kms.common.persistence.Page;
import com.yonyou.kms.common.service.CrudService;
import com.yonyou.kms.modules.cms.dao.ArticleDao;
import com.yonyou.kms.modules.cms.dao.CategoryDao;
import com.yonyou.kms.modules.cms.dao.CommentDao;
import com.yonyou.kms.modules.cms.entity.Article;
import com.yonyou.kms.modules.cms.entity.Category;
import com.yonyou.kms.modules.cms.entity.Comment;
import com.yonyou.kms.modules.cms.entity.Site;
import com.yonyou.kms.modules.sys.dao.UserDao;
import com.yonyou.kms.modules.sys.entity.Office;
import com.yonyou.kms.modules.sys.entity.User;
import com.yonyou.kms.modules.sys.utils.UserUtils;

/**
 * 评论Service
 * @author hotsum
 * @version 2013-01-15
 */
@Service
@Transactional(readOnly = true)
public class CommentService extends CrudService<CommentDao, Comment> {

	@Autowired
	private CommentDao commentDao;
	@Autowired
	private ArticleDao artilDao;
	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private UserDao userDao;
	
	@Override
	public Page<Comment> findPage(Page<Comment> page, Comment comment) {
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
		if (comment.getCategory()!=null && org.apache.commons.lang3.StringUtils.isNotBlank(comment.getCategory().getId()) && !Category.isRoot(comment.getCategory().getId())){
			Category category = categoryDao.get(comment.getCategory().getId());
			if (category==null){
				category = new Category();
			}
			category.setParentIds(category.getId());
			category.setSite(category.getSite());
			comment.setCategory(category);
		}
		else{
			comment.setCategory(new Category());
		}
		comment.getSqlMap().put("dsf", dataScopeFilter(comment.getCurrentUser(), "o", "u"));//获取当前user
		return super.findPage(page, comment);
	}
	public void delete(Comment entity, Boolean isRe) {
		super.delete(entity);
	}
	
	//add hefeng
	public Page<Comment> findUserCommentPage(Page<Comment> page, Comment comment) {
		if (comment.getCategory()!=null && org.apache.commons.lang3.StringUtils.isNotBlank(comment.getCategory().getId()) && !Category.isRoot(comment.getCategory().getId())){
			Category category = categoryDao.get(comment.getCategory().getId());
			if (category==null){
				category = new Category();
			}
			category.setParentIds(category.getId());
			category.setSite(category.getSite());
			comment.setCategory(category);
		}
		else{
			comment.setCategory(new Category());
		}
		comment.setPage(page);
		page.setList(commentDao.findUserCommentList(comment));
		return page;
	}
	public void change(Comment entity, Boolean isRe) {
		dao.change(entity);
	}
	public void deleteUserComment(Comment entity, Boolean isRe) {
		dao.deleteUserComment(entity);
	}
	public void MergeArticle(String originalcategoryId,String categoryId,String articleId){
		dao.MergeArticle(originalcategoryId, categoryId, articleId);
	}
	

	public void MergeCategory(String originalcategoryId, String categoryId) {
		dao.MergeCategory(originalcategoryId, categoryId);
	}


	public Page<Comment> findInitComment(Page<Comment> page, Comment comment) {
		List<String> categoryids=new ArrayList<String>();
		if (comment.getCategory()!=null && org.apache.commons.lang3.StringUtils.isNotBlank(comment.getCategory().getId()) && !Category.isRoot(comment.getCategory().getId())){
			Category category = getCategory(comment.getCategory().getId());
			if (category==null){
				category = new Category();
			}
			category.setParentIds(category.getId());
			category.setSite(category.getSite());
			categoryids.add(0, category.getId());
			comment.setCategory(category);
		}
		else{
			comment.setCategory(new Category());
			categoryids=commentList(UserUtils.getUser());
		}
		comment.getSqlMap().put("dsf", dataScopeFilter(comment.getCurrentUser(), "o", "u"));//获取当前user
		if(categoryids==null||categoryids.size()<=0){
			Page p=new Page<Comment>();
			p.setPageSize(30);
			return p;
		}
		comment.setCategoryids(categoryids);
		 page=super.findPage(page, comment);
		
		return page;
	}
	//end

	//add by luqibao
	@SuppressWarnings("unchecked")
	public List<String> commentList(User user){
		List<Category> list=null;
		if(user==null||userDao.get(user)==null){
			user=UserUtils.getUser();
		}
		Category category=new Category();
		category.setOffice(new Office());
		category.getSqlMap().put("dsf", dataScopeFilter(user, "o", "u"));
		category.setSite(new Site());
		category.setParent(new Category());
		list=categoryDao.findList(category);
		List<String> temp=Lists.newArrayList();
 		if(list==null){
			list=Lists.newArrayList();
		}
 		for(Category c:list){
 			if(!temp.contains(c)){
 				if(Category.CATEGORY_ARTICLE.equals(c.getModule())){
 					temp.add(c.getId());
 				}
 			}
 		}
		return temp;
	}
	//end
	public Category getCategory(String id){
		return categoryDao.get(id);
	}
	public List<Comment> findArticleComment(String id) {
		List<Comment> comments = new ArrayList<>();
		comments = commentDao.getCommentByTitle(id);
		return comments;
	}
}
