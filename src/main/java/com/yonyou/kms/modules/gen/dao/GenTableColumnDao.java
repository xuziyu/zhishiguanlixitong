/**
 * 
 */
package com.yonyou.kms.modules.gen.dao;

import com.yonyou.kms.common.persistence.CrudDao;
import com.yonyou.kms.common.persistence.annotation.MyBatisDao;
import com.yonyou.kms.modules.gen.entity.GenTableColumn;

/**
 * 业务表字段DAO接口
 * @author hotsum
 * @version 2013-10-15
 */
@MyBatisDao
public interface GenTableColumnDao extends CrudDao<GenTableColumn> {
	
	public void deleteByGenTableId(String genTableId);
}
