package com.yonyou.kms.modules.cms.dao;

import java.util.List;

import com.yonyou.kms.common.persistence.CrudDao;
import com.yonyou.kms.common.persistence.annotation.MyBatisDao;
import com.yonyou.kms.modules.cms.entity.UserLabel;
/*
 * 用户关联标签的Dao
 * yangshiwei
 */
@MyBatisDao
public interface UserLabelDao extends CrudDao<UserLabel>{
	public int delete(String labelid);
	//批量插入标签
	public void batchInsert(List<UserLabel> list);
//	//批量更新标签
//	public void batchUpdate(List<UserLabel> list);
	//批量删除
	public void batchdelete(List<String> list);
	
}
