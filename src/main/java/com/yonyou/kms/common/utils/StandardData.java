package com.yonyou.kms.common.utils;

import java.io.Serializable;

/**
 * 标准数据类
 * @author zhangjian
 *
 */
public class StandardData implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String version;				//版本号
	private String platform;			//平台
	private String ipAddress;			//ip地址
	private String timestamp;			//时间戳
	private Object data;				//数据
	private String sign;				//签名
	
	public StandardData(){}
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
		
}