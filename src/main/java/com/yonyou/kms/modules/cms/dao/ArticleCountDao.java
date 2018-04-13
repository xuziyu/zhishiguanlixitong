package com.yonyou.kms.modules.cms.dao;

import java.util.List;

import com.yonyou.kms.common.persistence.CrudDao;
import com.yonyou.kms.common.persistence.annotation.MyBatisDao;
import com.yonyou.kms.modules.cms.entity.ArticleCount;
/**
 * 
 * 知识统计Dao
 * @author yangshiwei
 *
 */
@MyBatisDao
public interface ArticleCountDao extends CrudDao<ArticleCount>{
	//通过id查询是否有着条记录
	public String getData(String articleid);
	
	//取出由各个表中计算出的数据
	public List<ArticleCount> getDataFromTable();
	
	//将数据存入到知识统计表中(批量插入)
	public int insertArticleCount(List<ArticleCount> list);
	//将数据存入到知识统计表中(批量更新)
	public int updateArticleCount(List<ArticleCount> list);
	
	//将知识全部数据从知识统计表中取出,按权重取前几列，通过知识分类查找
	public List<ArticleCount> getArticleCountData(List<String> categoryid);
	
	//将分享数据从知识统计表中取出,通过知识分类id查找
	public List<ArticleCount>  getArticleShareData(List<String> categoryid);
	//取出最新知识从知识统计表中，通过知识分类id查找
	public List<ArticleCount>  getNewArticleCountData(List<String> categoryid);
	//查出所有的id
	public List<String> getAllid();
	//批量删除
	public void deleteData(List<String> list);
	//计算并取出单条数据
	public ArticleCount getSingleData(String articleid);
	//点击收藏，推荐，分享等更新数据+1
	public void updateSingleAdd(ArticleCount ac);
	//点击收藏，推荐，分享等更新数据-1
	public void updateSingleReduce(ArticleCount ac);
	//删除记录
	public void deleteSingleData(String articleid);
	
}