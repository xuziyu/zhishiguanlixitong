/**
 * 
 */
package com.yonyou.kms.modules.test.dao;

import com.yonyou.kms.common.persistence.CrudDao;
import com.yonyou.kms.common.persistence.annotation.MyBatisDao;
import com.yonyou.kms.modules.test.entity.Test;

/**
 * 测试DAO接口
 * @author hotsum
 * @version 2013-10-17
 */
@MyBatisDao
public interface TestDao extends CrudDao<Test> {
	
}
