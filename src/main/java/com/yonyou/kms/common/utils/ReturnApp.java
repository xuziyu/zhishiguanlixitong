package com.yonyou.kms.common.utils;

import java.io.Serializable;

/**
 * @author zhangjian
 * 返回给app端的数据实体类
 *
 */
public class ReturnApp implements Serializable {

	private static final long serialVersionUID = 1L;
	private String errorCode;		//返回的错误码	0：成功  1：失败
	private String errorMsg;		//返回的错误信息
	private Object data;			//返回的数据
	
	public ReturnApp(){		//默认构造函数
		this.errorCode = "0";
		this.errorMsg = "";	
		this.data = "";
	}
	
	public ReturnApp(String errorCode, String errorMsg){
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;			
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
}