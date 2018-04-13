/**
 * 
 */
package com.yonyou.kms.modules.gen.dao;

import com.yonyou.kms.common.persistence.CrudDao;
import com.yonyou.kms.common.persistence.annotation.MyBatisDao;
import com.yonyou.kms.modules.gen.entity.GenScheme;

/**
 * 生成方案DAO接口
 * @author hotsum
 * @version 2013-10-15
 */
@MyBatisDao
public interface GenSchemeDao extends CrudDao<GenScheme> {
	
}
