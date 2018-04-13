/**
 * 
 */
package com.yonyou.kms.modules.cms.dao;

import java.util.List;
import java.util.Map;

import com.yonyou.kms.common.persistence.CrudDao;
import com.yonyou.kms.common.persistence.annotation.MyBatisDao;
import com.yonyou.kms.modules.cms.entity.Article;
import com.yonyou.kms.modules.cms.entity.Comment;

/**
 * 评论DAO接口
 * @author hotsum
 * @version 2013-8-23
 */
@MyBatisDao
public interface CommentDao extends CrudDao<Comment> {
	public List<Comment> findUserCommentList(Comment comment);

	public void deleteUserComment(Comment entity);
	public void change(Comment entity);
	public void MergeArticle(String originalcategoryId,String categoryId,String articleId);

	public void MergeCategory(String originalcategoryId, String categoryId);

	public List<Comment> InitComment(Map<String,Object> map);

	public List<Comment> getCommentByTitle(String id);
}
