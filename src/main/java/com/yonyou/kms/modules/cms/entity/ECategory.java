package com.yonyou.kms.modules.cms.entity;
/**
 * 
 * @author Hotusm
 *
 */
public class ECategory {
	
	private int num;//行号
	private String thirdCategroy;	//三级知识分类名称
	private String secondCategory;	//二级知识分类名称
	private String firstCategory; 	//一级知识分类名称
	private String office;	//归属机构(通过code去office表查到部门)
	private String keywords;		//关键字
	private String allowComment;	//是否允许评论
	private String sort;//排序
	private int index;//分类层级数
	
	
	
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getThirdCategroy() {
		return thirdCategroy;
	}
	public void setThirdCategroy(String thirdCategroy) {
		this.thirdCategroy = thirdCategroy;
	}
	public String getSecondCategory() {
		return secondCategory;
	}
	public void setSecondCategory(String secondCategory) {
		this.secondCategory = secondCategory;
	}
	public String getFirstCategory() {
		return firstCategory;
	}
	public void setFirstCategory(String firstCategory) {
		this.firstCategory = firstCategory;
	}
	public String getOffice() {
		return office;
	}
	public void setOffice(String office) {
		this.office = office;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getAllowComment() {
		return allowComment;
	}
	public void setAllowComment(String allowComment) {
		this.allowComment = allowComment;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	
	
}
