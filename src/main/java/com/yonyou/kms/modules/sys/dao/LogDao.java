/**
 * 
 */
package com.yonyou.kms.modules.sys.dao;

import com.yonyou.kms.common.persistence.CrudDao;
import com.yonyou.kms.common.persistence.annotation.MyBatisDao;
import com.yonyou.kms.modules.sys.entity.Log;

/**
 * 日志DAO接口
 * @author hotsum
 * @version 2014-05-16
 */
@MyBatisDao
public interface LogDao extends CrudDao<Log> {

}
