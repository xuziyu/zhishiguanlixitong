package com.yonyou.kms.modules.sys.security;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.yonyou.kms.common.security.Digests;
import com.yonyou.kms.common.utils.Encodes;
import com.yonyou.kms.common.utils.SpringContextHolder;
import com.yonyou.kms.modules.sys.entity.User;
import com.yonyou.kms.modules.sys.service.SystemService;

/**
 * 第三方加密策略
 * @author Hotusm
 *
 */
public class PLStrategy {
	
	private String strategy;
	
	
	private static SystemService systemService=SpringContextHolder.getBean("systemService");
	
	public final static String MD5PWD_PREFIX = "U_U++--V";
	

	public  static boolean get(String plainPassword,User user,String strategy) throws UnsupportedEncodingException{
		boolean flag=false;
		String password="";
		if(user==null||StringUtils.isBlank(user.getId())){
			return false;
		}
		if(strategy.equals("nc")){
			password=NCStrategy(plainPassword,user.getId());
		}else if(strategy.equals("local")){
			password=LocalStrategy(plainPassword,user.getPassword());
		}
		
		if(user.getPassword().equals(password)){
			return true;
		}
		return flag;
	}
	private static String NCStrategy(String plainPassword,String id){
        String codecPWD = DigestUtils.md5Hex(id +
        		StringUtils.stripToEmpty(plainPassword));
        codecPWD=MD5PWD_PREFIX+codecPWD;
		
		return codecPWD;
	}
	public  static String LocalStrategy(String plainPassword, String password) {
		byte[] salt = Encodes.decodeHex(password.substring(0,16));
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, SystemService.HASH_INTERATIONS);
		String codecPWD=Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword);
		return codecPWD;
	} 
}
