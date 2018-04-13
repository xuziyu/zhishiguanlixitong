package com.yonyou.kms.modules.cms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yonyou.kms.common.mapper.JsonMapper;
import com.yonyou.kms.common.service.CrudService;
import com.yonyou.kms.modules.cms.dao.DepartContributionDao;
import com.yonyou.kms.modules.cms.entity.DepartContribution;
import com.yonyou.kms.modules.cms.utils.ContributionUtil;
@Service
@Transactional(readOnly = false)
public class DepartContributionService extends CrudService<DepartContributionDao, DepartContribution>{

	@Autowired
	private DepartContributionDao departContributionDao;
	
	/*
	 *将全部用户计算出的数据存入到部门贡献表中,批量插入或者批量更新
	 *定时任务()暂时没写
	 * 
	 */ 
	public boolean saveData(){
		boolean flag=false;
		List<DepartContribution> insertlist=new ArrayList<DepartContribution>();
		List<DepartContribution> updatelist=new ArrayList<DepartContribution>();
		
		//获取数据的数据存放到实体为DepartContribution的集合中.
		List<DepartContribution> list=departContributionDao.getDepartToActi();
		//System.out.println("-------------------进入savedata-------------");
		//System.out.println("-------------------list.tostring-------------"+list.toString());
		List<String> officeidfromuser=new ArrayList<String>();
		List<String> officeidfromdepart=new ArrayList<String>();
		officeidfromdepart=departContributionDao.getAllid();	
		for(int i=0;i<list.size();i++){
			officeidfromuser.add(list.get(i).getDepartid());
			String id=list.get(i).getDepartid();
//			String judge=new String();
//			System.out.println("----------judge--------="+judge+"---------------");
//			judge=officeidfromdepart.get(i);
//			System.out.println("-------------取部门list.get(i).getDepartid()------"+list.get(i).getDepartid()+"----------------------");
			int  count=0;
			for(int j=0;j<officeidfromdepart.size();j++){
				String did=officeidfromdepart.get(j);
				if(id.equals(did)){
					count++;
					break;
				}
			}
			if(count==0)
			{
				//System.out.println("------------进入-------------");
				DepartContribution dc=ContributionUtil.copyDepart(list.get(i));
				insertlist.add(dc);
				//System.out.println("DepartContribution中insertlistdc的值为"+dc.toString());
			}
			else if(count==1){
				DepartContribution dc=ContributionUtil.copyDepart(list.get(i));
				updatelist.add(dc);
				//System.out.println("DepartContribution中updatelistpc的值为"+dc.toString());
			}
		}
		//删除在贡献表中与其在其他表中计算出的数据差异,贡献表中已有的数据
		List<String> diffid=ContributionUtil.getDiffrent(officeidfromdepart, officeidfromuser);
		if(diffid.size()>0){
			departContributionDao.deleteAll(diffid);
			//System.out.print("删除成功");
		}
		//System.out.println("计算DepartContribution出的插入数据为:"+insertlist.toString()+"-"+insertlist.size());
		//System.out.println("计算DepartContribution出的更新数据为:"+updatelist.toString()+"-"+updatelist.size());
		if(insertlist.size()>0){
			departContributionDao.insertContributionData(insertlist);
		}
		if(updatelist.size()>0){
			departContributionDao.updateContributionData(updatelist);
		}
		flag=true;
		return flag;
	}
	
	public List<DepartContribution> getContributionData(){
		List<DepartContribution> list=new ArrayList<DepartContribution>();
		list=departContributionDao.getContributionData();
		//System.out.println("从cms_depart_contribution表中读取的数据为:"+JsonMapper.toJsonString(list));
		return list;
		
	}
	/*
	 * 获取部门的父级节点名称
	 * 
	 */
	public String getParentName(String officeid){
		return departContributionDao.getParentName(officeid);
	}
}
