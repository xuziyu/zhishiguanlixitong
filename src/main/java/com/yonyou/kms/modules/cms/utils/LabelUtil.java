package com.yonyou.kms.modules.cms.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.kms.common.utils.SpringContextHolder;
import com.yonyou.kms.modules.cms.entity.Label;
import com.yonyou.kms.modules.cms.service.LabelService;

public class LabelUtil {
	
	private static LabelService labelservice=SpringContextHolder.getBean(LabelService.class);
		
	
	
	/*
	 * 
	 * 获取标签关联知识表中前5条数据
	 * 
	 */
	public static List<Label> getHotLabelData(){
		return labelservice.getHotLabelData();
	}
	/*
	 * 获取两个标签集合的不同标签集合
	 * 
	 */
	public static List<Label> diffierentlabel(List<Label> list1,List<Label> list2){
		List<Label> diff=new ArrayList<Label>();
		List<Label> maxList=list1;
		List<Label> minList=list2;
		if(list2.size()>list1.size()){
			maxList=list2;
			maxList=list1;
		}
		Map<String,Integer> map=new HashMap<String,Integer>();
		for(Label label:maxList)
			map.put(label.getId(),1);
		for(Label label:minList){
			if(map.get(label.getId())!=null){
				map.put(label.getId(),2);
				continue;
			}
			diff.add(label);
		}
		for(Map.Entry<String,Integer> entry:map.entrySet()){
			if(entry.getValue()==1){
				String id=entry.getKey();
				for(int i=0;i<maxList.size();i++){
					if(id.equals(maxList.get(i).getId()))
						diff.add(maxList.get(i));
				}
			}	
		}
//		for(Label label:diff)
//			System.out.println("两者差异为:"+label.getLabelvalue());
		return diff;
	}
}
