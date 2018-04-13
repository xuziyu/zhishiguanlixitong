/**
 * 
 */
package com.yonyou.kms.modules.oa.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yonyou.kms.common.persistence.CrudDao;
import com.yonyou.kms.common.persistence.annotation.MyBatisDao;
import com.yonyou.kms.modules.oa.entity.OaNotify;
import com.yonyou.kms.modules.oa.entity.OaNotifyRecord;

/**
 * 通知通告记录DAO接口
 * @author hotsum
 * @version 2014-05-16
 */
@MyBatisDao
public interface OaNotifyRecordDao extends CrudDao<OaNotifyRecord> {

	/**
	 * 插入通知记录
	 * @param oaNotifyRecordList
	 * @return
	 */
	public int insertAll(List<OaNotifyRecord> oaNotifyRecordList);
	
	/**
	 * 根据通知ID删除通知记录
	 * @param oaNotifyId 通知ID
	 * @return
	 */
	public int deleteByOaNotifyId(String oaNotifyId);
	
	//add hefeng
	
	/**
	 * 获取当前通知阅读时间
	 */
	public Date getReadDateByOaNotifyId(OaNotifyRecord oaNotifyRecord);
	/**
	 * 批量更新阅读状态
	 */
	public int Batchupdate(OaNotifyRecord oaNotifyRecord);
	/**
	 * 物理删除
	 */
	public int Physicsdelete(OaNotify oaNotify);
	
	/**
	 * 批量物理删除
	 */
	public int BatchPhysicsdelete(String userId);
	//end hefeng
	//add by luqibao
	public void delList(Map<String,Object> map);
	//end
}