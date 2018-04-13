/**
 * 
 */
package com.yonyou.kms.modules.oa.dao;

import com.yonyou.kms.common.persistence.CrudDao;
import com.yonyou.kms.common.persistence.annotation.MyBatisDao;
import com.yonyou.kms.modules.oa.entity.OaNotify;

/**
 * 通知通告DAO接口
 * @author hotsum
 * @version 2014-05-16
 */
@MyBatisDao
public interface OaNotifyDao extends CrudDao<OaNotify> {
	
	/**
	 * 获取通知数目
	 * @param oaNotify
	 * @return
	 */
	public Long findCount(OaNotify oaNotify);
	
	public Long findUnReadMsg(String userId);
	
}