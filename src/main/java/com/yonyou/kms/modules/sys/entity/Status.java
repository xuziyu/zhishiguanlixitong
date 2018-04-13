package com.yonyou.kms.modules.sys.entity;

import java.util.Date;

import com.yonyou.kms.common.persistence.DataEntity;



/**
 * 
 * 记录每一更新的时间
 * @author luqibao
 *
 */
public class Status extends DataEntity<Status>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;//id 需要手动设置
	private String fromSys;//从哪个表更新的数据
	private String ts;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFromSys() {
		return fromSys;
	}
	public void setFromSys(String fromSys) {
		this.fromSys = fromSys;
	}
	
	public String getTs() {
		return ts;
	}
	public void setTs(String ts) {
		this.ts = ts;
	}
	@Override
	public String toString() {
		
		return "id="+id+" formSys="+fromSys+" tx="+ts;
	}

	
}
