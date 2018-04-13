package com.yonyou.kms.modules.cms.utils;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.kms.common.utils.SpringContextHolder;
import com.yonyou.kms.modules.cms.entity.ArticleCount;
import com.yonyou.kms.modules.cms.service.ArticleCountService;

public class ArticleCountUtil {
	private static ArticleCountService articlecountservice=SpringContextHolder.getBean(ArticleCountService.class);
	
	public static ArticleCount copy(ArticleCount ac){
		ArticleCount ac2=new ArticleCount();
		ac2.setArticleid(ac.getArticleid());
		ac2.setArticletitle(ac.getArticletitle());
		ac2.setCountclick(ac.getCountclick());
		ac2.setCountcollect(ac.getCountcollect());
		ac2.setCountcomm(ac.getCountcomm());
		ac2.setCountreco(ac.getCountreco());
		ac2.setCountshare(ac.getCountshare());
		ac2.setCountthumbs(ac.getCountthumbs());
		return ac2;
	}
	
	/*
	 * 设置数据到知识统计表
	 * 
	 * 
	 */	
	public static boolean setArticleCountData(){
		boolean flag=false;
		flag=articlecountservice.saveData();
		return flag;
	}
	/*
	 * 取数据，按权重排行，取前几条数据
	 * @param categorylist 知识分类id（权限控制）
	 * 
	 */
	public static List<ArticleCount> getArticleCountData(List<String> categorylist){
		List<ArticleCount> list=new ArrayList<ArticleCount>();
		list=articlecountservice.getArticleCountData(categorylist);
		return list;
	}
	/*
	 * 取出知识统计表中最新分享数据
	 * @param categoryid:知识分类id集合(权限控制)
	 * 
	 * 
	 */
	public static List<ArticleCount> getArticleCountShareData(List<String> categoryid){
		return articlecountservice.getArticleCountShareData(categoryid);
	}
	/*
	 * 
	 * 取出最新知识,
	 * @param categoryid 知识分类id(权限控制)
	 * 
	 */
	public static List<ArticleCount> getNewArticleCountData(List<String> categoryid){
		return articlecountservice.getNewArticleCountData(categoryid);
	}
}
