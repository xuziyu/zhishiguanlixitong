/**
 * 
 */
package com.yonyou.kms.modules.gen.dao;

import com.yonyou.kms.common.persistence.CrudDao;
import com.yonyou.kms.common.persistence.annotation.MyBatisDao;
import com.yonyou.kms.modules.gen.entity.GenTable;

/**
 * 业务表DAO接口
 * @author hotsum
 * @version 2013-10-15
 */
@MyBatisDao
public interface GenTableDao extends CrudDao<GenTable> {
	
}
