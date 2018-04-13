package com.yonyou.kms.modules.cms.entity;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.kms.common.persistence.DataEntity;
import com.yonyou.kms.modules.sys.entity.Office;
import com.yonyou.kms.modules.sys.entity.User;

public class FeaturePackage extends DataEntity<FeaturePackage> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name; 	// 专题名称
	private String canShare;//是否可以同步(1:可同步；1：不可同步)
	private String title;
	private Office office;	// 归属部门
	private String image; 	// 专题图片
	private String description; 	// 描述，填写有助于搜索引擎优化
	private String keywords; 	// 关键字，填写有助于搜索引擎优化
 	private Integer sort; 		// 排序（升序）
 	private Integer type; 		// 专题包类型
 	private String count;	//知识数
 	
 	private String isChecked; //是否被选中 1:选中 0:未被选中
 	private List<String> ids;//用户关联的专题包id
 	
 	private List<Article> articleList = new ArrayList<Article>();
 	
 	private User user;
 	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCanShare() {
		return canShare;
	}
	public void setCanShare(String canShare) {
		this.canShare = canShare;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Office getOffice() {
		return office;
	}
	public void setOffice(Office office) {
		this.office = office;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public List<Article> getArticleList() {
		return articleList;
	}
	public void setArticleList(List<Article> articleList) {
		this.articleList = articleList;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public List<String> getIds() {
		return ids;
	}
	public void setIds(List<String> ids) {
		this.ids = ids;
	}
 	
	public String getIsChecked() {
		return isChecked;
	}
	public void setIsChecked(String isChecked) {
		this.isChecked = isChecked;
	}
	public FeaturePackage() {
		super();
		this.isChecked="0";
		// TODO Auto-generated constructor stub
	}
	public FeaturePackage(String id) {
		super(id);
		this.isChecked="0";
		// TODO Auto-generated constructor stub
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
}
