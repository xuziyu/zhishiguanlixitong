package com.yonyou.kms.modules.cms.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.kms.common.utils.SpringContextHolder;
import com.yonyou.kms.modules.cms.entity.DepartContribution;
import com.yonyou.kms.modules.cms.entity.PersonContribution;
import com.yonyou.kms.modules.cms.service.PersonContributionService;
import com.yonyou.kms.modules.sys.utils.UserUtils;

/**
 * 
 * 个人贡献的工具类和部门贡献的工具类
 * 
 * @author yangshiwei
 *
 */

public class ContributionUtil {
	
	private static PersonContributionService   personContributionSercive=SpringContextHolder.getBean(PersonContributionService.class);
	/*
	 * 复制
	 * parameter 
	 */
	public static PersonContribution copy(PersonContribution pc){
		PersonContribution pc2=new PersonContribution();
		pc2.setUserid(pc.getUserid());
		pc2.setUsername(pc.getUsername());
		pc2.setCountarticle(pc.getCountarticle());
		return pc2;
	}
	/*
	 * 取出两个list中差异的部分,并且其差异的部分为list1中有的数据
	 * 
	 */
	public static List<String>  getDiffrent(List<String> list1,List<String> list2){
		List<String> diff=new ArrayList<String>();
		List<String> maxList=list1;
		List<String> minList=list2;
		if(list2.size()>list1.size()){
			maxList=list2;
			maxList=list1;
		}
		Map<String,Integer> map=new HashMap<String,Integer>();
		for(String string:maxList)
			map.put(string,1);
		for(String string:minList){
			if(map.get(string)!=null){
				map.put(string,2);
				continue;
			}
			map.put(string,1);
		}
		for(Map.Entry<String,Integer> entry:map.entrySet()){
			if(entry.getValue()==1){
				diff.add(entry.getKey());
			}
			
		}
		List<String> newdiff=new ArrayList<String>();
		for(int i=0;i<list1.size();i++){
			String id=list1.get(i);
			for(int j=0;j<diff.size();j++){
				String cid=diff.get(j);
				if(id.equals(cid)){
					newdiff.add(cid);
					break;
				}
			}
		}
		//System.out.println("getDiffrent........"+newdiff.size());
		return newdiff;
	}
	public static DepartContribution copyDepart(DepartContribution dc){
		DepartContribution dc2=new DepartContribution();
		dc2.setDepartid(dc.getDepartid());
		dc2.setDepartname(dc.getDepartname());
		dc2.setCountarticle(dc.getCountarticle());
		return dc2;
		
	}
	/*
	 * 设置数据到个人贡献表中
	 * @param 
	 * 
	 */
	public static boolean setPersonContributionData(){
		boolean flag=false;
		flag=personContributionSercive.saveData();
		return flag;
	}
	/*
	 * 
	 * 从个人贡献表中读取数据
	 * 
	 */
	public static List<PersonContribution> getPersonContributionData(){
		List<PersonContribution> list=new ArrayList<PersonContribution>();
		list=personContributionSercive.getPersonContributionData();
		return list;
	}
	
	//add by yinshh3 
	/**
	 * 从个人贡献表返回的list里取数据,并非直接访问数据库.
	 * @return 返回个人贡献数
	 */
	public static String getNumPersonContribution(){
		String s=null;	 
		for(PersonContribution pc:getPersonContributionData()){
//			System.out.println("==========pc.getUserid()1====================="+pc.getUserid());
//			System.out.println("==========UserUtils.getUser().getId())1====================="+UserUtils.getUser().getId());
			if(pc.getUserid().equals(UserUtils.getUser().getId())){
//				System.out.println("==========pc.getUserid()2====================="+pc.getUserid());
//				System.out.println("==========UserUtils.getUser().getId())2====================="+UserUtils.getUser().getId());
			 s=String.valueOf(pc.getCountarticle());
			}
		}
		return s;
	}
}
