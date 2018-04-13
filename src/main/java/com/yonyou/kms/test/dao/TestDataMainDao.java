/**
 * 
 */
package com.yonyou.kms.test.dao;

import com.yonyou.kms.common.persistence.CrudDao;
import com.yonyou.kms.common.persistence.annotation.MyBatisDao;
import com.yonyou.kms.test.entity.TestDataMain;

/**
 * 主子表生成DAO接口
 * @author hotsum
 * @version 2015-04-06
 */
@MyBatisDao
public interface TestDataMainDao extends CrudDao<TestDataMain> {
	
}