package com.yonyou.kms.modules.cms.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yonyou.kms.common.service.CrudService;
import com.yonyou.kms.modules.cms.dao.LabelDao;
import com.yonyou.kms.modules.cms.dao.UserLabelDao;
import com.yonyou.kms.modules.cms.entity.Label;
import com.yonyou.kms.modules.cms.entity.UserLabel;
/**
 * 
 * 用户关联标签Sercice
 * @author yangshiwei
 *
 */
@Service
@Transactional(readOnly = false)
public class UserLabelService extends CrudService<UserLabelDao, UserLabel> {
	@Autowired
	private UserLabelDao userlabeldao;
	@Autowired
	private LabelDao labeldao;
	//插入记录到表中,
	public void save(List<UserLabel> newlist){
		//id没有赋值
		for(int i=0;i<newlist.size();i++){
			newlist.get(i).preInsert();
		}
		userlabeldao.batchInsert(newlist);	
	}
	//增加用户关联的标签,不显示用户已经关注的标签
	public List<UserLabel> addLabel(UserLabel userlabel){//实体，让flag=0,标识分页用户不关注标签
		List<UserLabel> addlist=new ArrayList<UserLabel>();
		List<Label> list=new ArrayList<Label>();
		List<UserLabel> labellist=new ArrayList<UserLabel>();
		labellist=userlabeldao.findList(userlabel);	//用户关联标签
		list=labeldao.findList(new Label());
//		for(int i=0;i<list.size();i++){
//			System.out.println("list:"+list.get(i).getLabelvalue());
//		}
		if(list.size()==labellist.size()){
			//System.out.println("list.size()==labellist.size()");
			return addlist;
		}
		for(int i=0;i<list.size();i++){
			String id=list.get(i).getId();
			int count=0;
				for(int j=0;j<labellist.size();j++){
					String labelid=labellist.get(j).getLabelid();
					if(id.equals(labelid))
						continue;
					count++;                                                                                                                                                                                                                                                                                                                                                                                                                  
				}
				//System.out.println("count"+count);
			if(count==labellist.size()){
				UserLabel ul=new UserLabel();
				ul.setLabelid(list.get(i).getId());
				ul.setLabelvalue(list.get(i).getLabelvalue());
				addlist.add(ul);
			}
		}
		//System.out.println("addlist.size()"+addlist.size());
//		for(int i=0;i<addlist.size();i++){
//			System.out.println("addlist:"+addlist.get(i).getLabelvalue());
//		}
		return addlist;
	}
	
}
