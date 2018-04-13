/**
 * 
 */
package com.yonyou.kms.modules.cms.dao;

import com.yonyou.kms.common.persistence.CrudDao;
import com.yonyou.kms.common.persistence.annotation.MyBatisDao;
import com.yonyou.kms.modules.cms.entity.Guestbook;

/**
 * 留言DAO接口
 * @author hotsum
 * @version 2013-8-23
 */
@MyBatisDao
public interface GuestbookDao extends CrudDao<Guestbook> {

}
