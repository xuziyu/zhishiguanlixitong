package com.yonyou.kms.modules.cms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yonyou.kms.common.mapper.JsonMapper;
import com.yonyou.kms.common.service.CrudService;
import com.yonyou.kms.modules.cms.dao.PersonContributionDao;
import com.yonyou.kms.modules.cms.entity.PersonContribution;
import com.yonyou.kms.modules.cms.utils.ContributionUtil;

/**
 * 贡献service，对个人贡献和部门贡献数据进行处理
 * 
 * @author yangshiwei
 *
 */
@Service
@Transactional(readOnly = false)
public class PersonContributionService extends CrudService<PersonContributionDao, PersonContribution>{
	@Autowired
	private PersonContributionDao personContriDao;
	
	/*
	 *将全部用户计算出的数据存入到个人贡献表中,批量插入或者批量更新,现在在frontcontroller.java中并没有定时调用这个任务
	 * 
	 */ 
	public boolean saveData(){
		boolean flag=false;
		List<PersonContribution> insertlist=new ArrayList<PersonContribution>();
		List<PersonContribution> updatelist=new ArrayList<PersonContribution>();
		//从数据库中将数据取出在判断。
		List<PersonContribution> list=personContriDao.getUserToActi();
		List<String>	idFromCon=new ArrayList<String>();
		idFromCon=personContriDao.getAllid();
		List<String>	idFromList=new ArrayList<String>();
		for(int i=0;i<list.size();i++){
			idFromList.add(list.get(i).getUserid());
//			String judge=idFromCon.get(i);			
//			//judge字段是用来判断贡献表中是否有这个用户记录,有则更新,否则插入
			int count=0;
			String id=list.get(i).getUserid();
			for(int j=0;j<idFromCon.size();j++){
				String did=idFromCon.get(j);
				if(id.equals(did)){
					count++;
					break;
				}
			}
			if(count==0){
				PersonContribution pc=ContributionUtil.copy(list.get(i));
				insertlist.add(pc);
				//System.out.println("personContribution中insertlistpc的值为"+pc.toString());
			}
			else if(count==1){
				PersonContribution pc=ContributionUtil.copy(list.get(i));
				updatelist.add(pc);
				//System.out.println("personContribution中updatelistpc的值为"+pc.toString());
			}
		}
		//System.out.println("计算personContribution出的插入数据为:"+insertlist.toString()+"-"+insertlist.size());
		//System.out.println("计算personContribution出的更新数据为:"+updatelist.toString()+"-"+updatelist.size());
		List<String> diff=ContributionUtil.getDiffrent(idFromCon, idFromList);
		if(diff.size()>0 && diff !=null){
			personContriDao.deleteData(diff);
		}
		if(insertlist.size()>0){
			
			personContriDao.insertContributionData(insertlist);
		}
		if(updatelist.size()>0){
		personContriDao.updateContributionData(updatelist);
		}
		flag=true;
		return flag;
	}
	/*
	 * 全部读取个人贡献表中的数据
	 * 
	 * 
	 */
	public List<PersonContribution> getPersonContributionData(){
		List<PersonContribution>  list=new ArrayList<PersonContribution>();
		list=personContriDao.getContributionData();
		//System.out.println("从cms_person_contribution表中读取的数据为:"+JsonMapper.toJsonString(list));
		return list;
	}
	/*
	 * 取得用户所属部门名
	 * 
	 */
	public String getOfficeName(String userid){
		return personContriDao.getOfficeName(userid);
	}
}
