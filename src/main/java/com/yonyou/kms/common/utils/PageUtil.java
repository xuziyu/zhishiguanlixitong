package com.yonyou.kms.common.utils;

public class PageUtil {
	
	private int pageNo;
	private int pageSize;
	private int row;
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public PageUtil() {}
	
	public static int getTotalPage(int pageSise){
		int totalRow =  getTotalRow();
		int totalPage = totalRow/pageSise;
		if (totalRow%pageSise != 0) {
			totalPage += 1;
		}
		return totalPage;
	}
	public static int getTotalRow(){
		int row = 0;
		row = OracleUtil.getTotalRow();
		return row;
	}
}
