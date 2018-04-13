package com.yonyou.rmiservice.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.yonyou.kms.modules.cms.entity.Article;
import com.yonyou.kms.modules.cms.entity.ArticleData;
import com.yonyou.kms.modules.cms.entity.Category;
import com.yonyou.kms.modules.cms.service.ArticleAttFileService;
import com.yonyou.kms.modules.cms.service.ArticleLabelService;
import com.yonyou.kms.modules.cms.service.ArticleService;
import com.yonyou.kms.modules.cms.service.CategoryService;
import com.yonyou.kms.modules.sys.entity.User;
import com.yonyou.rmiservice.IRmiservice;
import com.yonyou.rmiservice.entity.RmiEntity;

import net.sf.json.JSONObject;

public class Rmiservice implements IRmiservice {
	
	@Autowired
	private ArticleService articleService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ArticleAttFileService articleAttFileService;
	@Autowired
	private ArticleLabelService articleLabelService;
	
	@Override
	public void test() {
		System.out.println("成功调用");
		
	}
	
	/**
	 * 获取知识库中的所有三级分类
	 */
	@Override
	public List<RmiEntity> getCategory() {
		List<RmiEntity> list = categoryService.queryAll();
		if (list.size() > 0) {
			System.out.println(list.toString());
		}
		return list;
	}
	
	/**
	 * 提交一键转知识
	 */
	@Override
	public String submitKnowledge(String data) {
		JSONObject j = JSONObject.fromObject(data);
		String content = j.optString("content");
		String title = j.optString("title");
		String keyword = j.optString("keyword");
		String fromname = j.optString("fromname");
		System.out.println(keyword);
		System.out.println(fromname);
		String categoryId = j.optString("categoryId");
		Article article = new Article();
		String temp_article_id = "";
		article.setTitle(title);
		article.setIsOriginal("0");
		article.setOriginalreason("转自你问我答评论区");
		article.setRemarks("00");
		article.setCategory(new Category(categoryId));
		ArticleData articleData = new ArticleData();
		articleData.setContent(content);
		article.setArticleData(articleData);
		articleService.save_exchange(article, temp_article_id );
		List<String> labellist = new ArrayList<>();
		labellist.add(categoryId);
		String name = "nihao";
		return name;
	}

}
