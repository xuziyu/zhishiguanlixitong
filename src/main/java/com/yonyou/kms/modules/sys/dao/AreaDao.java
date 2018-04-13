/**
 * 
 */
package com.yonyou.kms.modules.sys.dao;

import com.yonyou.kms.common.persistence.TreeDao;
import com.yonyou.kms.common.persistence.annotation.MyBatisDao;
import com.yonyou.kms.modules.sys.entity.Area;

/**
 * 区域DAO接口
 * @author hotsum
 * @version 2014-05-16
 */
@MyBatisDao
public interface AreaDao extends TreeDao<Area> {
	
}
