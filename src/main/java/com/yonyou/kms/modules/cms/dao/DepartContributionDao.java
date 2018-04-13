package com.yonyou.kms.modules.cms.dao;

import java.util.List;

import com.yonyou.kms.common.persistence.CrudDao;
import com.yonyou.kms.common.persistence.annotation.MyBatisDao;
import com.yonyou.kms.modules.cms.entity.DepartContribution;

/**
 * 
 * @author victor
 *
 */
@MyBatisDao
public interface DepartContributionDao extends CrudDao<DepartContribution>{
	//取得用户在数据库中ID对应的数据,用于之后判断该用户是否在部门贡献表中已经存在.来区分更新还是插入的操作.
	public String getData(String departid);
	
	//将全部用户关联的知识数计算出，并取出
	public List<DepartContribution>  getDepartToActi();
	
	//批量插入数据
	public void insertContributionData(List<DepartContribution> list);
	
	//批量更新数据
	public void updateContributionData(List<DepartContribution> list);
	
	//批量删除数据
	public void deleteAll(List<String> deletelist);
	//读取部门贡献表中的数据(查询得到的实体集合)
	public List<DepartContribution> getContributionData();
	//读取所有的id
	public List<String> getAllid();
	//取得父级名称
	public String getParentName(String officeid);
}
