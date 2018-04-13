package com.yonyou.kms.modules.cms.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yonyou.kms.common.mapper.JsonMapper;
import com.yonyou.kms.modules.sys.entity.User;

@Service
@Transactional(readOnly = true)
public class FrontDataService {
	


	
	public String getUserLoginName(User user){
		return user.getLoginName();
		
	}
	public String getUserName(User user){
		//System.out.println("---------UserName()方法取得登录名-------------------"+user.getLoginName());
		//System.out.println("---------user-------------------"+JsonMapper.toJsonString(user));
		return user.getName();
		
	}

	public String getUserRoleName(User user){
	
		
		String s= user.getRoleNames().replace(",","|");
		// System.out.println("===================s========"+s.toString());
		return s;
	}
}
