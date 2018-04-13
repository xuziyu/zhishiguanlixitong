/**
 * 
 */
package com.yonyou.kms.test.dao;

import com.yonyou.kms.common.persistence.TreeDao;
import com.yonyou.kms.common.persistence.annotation.MyBatisDao;
import com.yonyou.kms.test.entity.TestTree;

/**
 * 树结构生成DAO接口
 * @author hotsum
 * @version 2015-04-06
 */
@MyBatisDao
public interface TestTreeDao extends TreeDao<TestTree> {
	
}