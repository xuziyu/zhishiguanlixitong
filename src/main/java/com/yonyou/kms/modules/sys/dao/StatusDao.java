package com.yonyou.kms.modules.sys.dao;

import java.util.List;

import com.yonyou.kms.common.persistence.CrudDao;
import com.yonyou.kms.common.persistence.annotation.MyBatisDao;
import com.yonyou.kms.modules.sys.entity.Status;

/**
 * 
 * @author luqibao
 *插入和取出相应的最新时间
 */
@MyBatisDao
public interface StatusDao extends CrudDao<Status>{
	
	/**
	 * 每次更新的时候插入记录到数据库中
	 */
	public void insertStatus(Status status);
	/**
	 * 
	 *根据给定系统名字，取出最新的
	 * @param fromSys
	 * @return@Param("fromSys")
	 */
	public String selectStatus(String fromSys);
	
	/**
	 * 批量插入数据 
	 * 
	 * @param list
	 */
	public void batchInsert(List<Status> list);
	/**
	 * 
	 * 批量更新
	 * @param list
	 */
	public void batchUpdate(List<Status> list);
	
}
