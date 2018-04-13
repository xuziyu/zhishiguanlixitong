package com.yonyou.kms.modules.cms.dao;

import java.util.List;
import java.util.Map;

import com.yonyou.kms.common.persistence.CrudDao;
import com.yonyou.kms.common.persistence.annotation.MyBatisDao;
import com.yonyou.kms.modules.cms.entity.Article;
import com.yonyou.kms.modules.cms.entity.ArticleLabel;
import com.yonyou.kms.modules.cms.entity.Label;


@MyBatisDao
public interface ArticleLabelDao extends CrudDao<ArticleLabel>{
	//批量插入
	public void batchInsert(List<ArticleLabel> list);
	//批量删除
	public void batchdelete(Map map);
	//把标签列表更新到article表中
	public void updateInsertArticle(String labellist,String articleid);
	//取得关联文章的知识列表
	public List<String> getArticleLabel(String articleid);
	//取得关联文章的知识id
	public List<String> findLabelId(String articleid);
	//add by luqibao
	public List<Article> findArticlesByLabelId(String id,String userid);
	//end
	//查出目标文章下的所有标签 huangmj 2015.11.2
	public List<Label> findLabelByArticle(String articleId);
	
	
}
