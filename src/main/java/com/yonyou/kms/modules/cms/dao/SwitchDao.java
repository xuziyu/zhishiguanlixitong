package com.yonyou.kms.modules.cms.dao;

import java.util.List;

import com.yonyou.kms.common.persistence.CrudDao;
import com.yonyou.kms.common.persistence.annotation.MyBatisDao;
import com.yonyou.kms.modules.cms.entity.Switch;


@MyBatisDao
public interface SwitchDao extends CrudDao<Switch>  {
	//取得全部数据
	public List<Switch> getAll();
	//取得delFlag为0的全部数据(启用状态)
	public List<Switch> getAllBydelFlag();
	//更新单条数据
	public int update(Switch switchOnly);
	
}
