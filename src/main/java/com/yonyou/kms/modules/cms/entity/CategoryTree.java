package com.yonyou.kms.modules.cms.entity;

import java.util.List;

import com.yonyou.kms.common.persistence.DataEntity;
import com.yonyou.kms.common.persistence.TreeEntity;

public class CategoryTree  extends DataEntity<CategoryTree>{
	private String id;
	private String name;
	private String image; //存入该知识库(分类)的样式,颜色加图片
	private CategoryTree categoryChild;
	private String sort;
	private String hasArticle;//排列顺序
	public CategoryTree() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public CategoryTree(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	public CategoryTree(String id, String name, String image,
			CategoryTree categoryChild) {
		super();
		this.id = id;
		this.name = name;
		this.image = image;
		this.categoryChild = categoryChild;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public CategoryTree getCategoryChild() {
		return categoryChild;
	}
	public void setCategoryChild(CategoryTree categoryChild) {
		this.categoryChild = categoryChild;
	}

	public String getHasArticle() {
		return hasArticle;
	}

	public void setHasArticle(String hasArticle) {
		this.hasArticle = hasArticle;
	}
	
	
	
	
}
