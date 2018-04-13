package com.yonyou.kms.common.persistence;

import java.util.List;

import com.google.common.collect.Lists;
import com.yonyou.kms.modules.cms.entity.Article;

/**
 * @author zy 首页搜索分页 将搜索得来的list进行页 list 搜索得来的ArticleList pageSize 每页文章数量
 */
public class Split {
	private int currentPage = 1; // 当前页码
	private List<Article> articleList;// 所有的文章
	private List<Article> pageList;// 每页的文章
	private int pageSize = 8; // 页面大小，设置为“-1”表示不进行分页（分页无效）
	// private List<List<Article>> list;
	// List<List<Article>> listArray = new ArrayList<List<Article>>();
	private int pageNum = 1;// 分页数默认为1
	private int listSize = 1;// ArticleList中的文章数量

	// private StringBuffer sb=new StringBuffer();

	// private int lastPage=-1;

	public Split() {

	}

	public Split(List<Article> articleList, int currentPage, int pageSize) {
		this.articleList = articleList;
		this.pageSize = pageSize;
		// this.currentPage=currentPage;
		this.listSize = articleList.size(); // ArticleList中的文章数量
		this.pageNum = (listSize + (pageSize - 1)) / pageSize; // 需要的分页数
		setCurrentPage(currentPage);
		// getsplitList( pageSize, articleList);
		// this.list=getsplitList( pageSize, articleList);
		// if(articleList.size()%pageSize==0){
		// pageNum=articleList.size()/pageSize;
		// }else{
		// pageNum=articleList.size()/pageSize+1;
		// }
	}

	// public List<List<Article>> getList() {
	// return list;
	// }
	//
	//
	//
	// public void setList(List<List<Article>> list) {
	// this.list = list;
	// }

	public List<Article> getPageList() {
		return pageList;
	}

	public void setPageList(List<Article> pageList) {
		this.pageList = pageList;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		pageList = Lists.newArrayList();
		if (articleList.size() > 0) {
			if (currentPage <= 1) {
				this.currentPage = 1;
			} else if (currentPage >= pageNum) {
				this.currentPage = pageNum;
			} else {
				this.currentPage = currentPage;
			}
			if (this.currentPage == pageNum) {
				for (int i = (this.currentPage - 1) * pageSize; i < articleList
						.size(); i++) {
					pageList.add(articleList.get(i));
				}
			} else {
				for (int i = (this.currentPage - 1) * pageSize; i < this.currentPage
						* pageSize; i++) {
					pageList.add(articleList.get(i));
				}
			}
		}
	
	}

	public List<Article> getArticleList() {
		return articleList;
	}

	public void setArticleList(List<Article> articleList) {
		this.articleList = articleList;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getListSize() {
		return listSize;
	}

	public void setListSize(int listSize) {
		this.listSize = listSize;
	}

	// public StringBuffer getSb() {
	// sb.append("<div class='fenye'> <a href='#' class='prePage'
	// 'onclick=nextPage()'>上一页</a><c:forEach var='i' begin='1'
	// end='5'></c:forEach><a href='#' class='nextPage'
	// 'onclick=prePage()'>下一页</a>");
	// return sb;
	// }
	//
	// public void setSb(StringBuffer sb) {
	// this.sb = sb;
	// }

	// public <Article> List<List<Article>> getsplitList(int
	// pageSize,List<Article> articleList )
	// {
	//	  
	// this.listSize = articleList.size(); //ArticleList中的文章数量
	// this.pageNum = (listSize + (pageSize - 1)) / pageSize; //需要的分页数
	//	  
	// //进行分页操作将分好的存入allPage
	// List<List<Article>> listArray = new ArrayList<List<Article>>();
	// for (int i = 0; i < pageNum; i++)
	// {
	// //每一页要显示的ArticleList
	// List<Article> subList = new ArrayList<Article>();
	// for (int j = 0; j < listSize; j++)
	// {
	// int pageIndex = ((j + 1) + (pageSize - 1)) / pageSize;
	// if (pageIndex == (i + 1))
	// {
	// subList.add(articleList.get(j));
	// }
	// if ((j + 1) == ((j + 1) * pageSize))
	// {
	// break;
	// }
	// }
	// listArray.add(subList);
	// }
	// return listArray;
	// }
}
