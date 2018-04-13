/**
 * 
 */
package com.yonyou.kms.common.utils.excel.fieldtype;

import java.util.List;

import com.google.common.collect.Lists;
import com.yonyou.kms.common.utils.Collections3;
import com.yonyou.kms.common.utils.SpringContextHolder;
import com.yonyou.kms.modules.sys.entity.Role;
import com.yonyou.kms.modules.sys.service.SystemService;

/**
 * 字段类型转换
 * @author hotsum
 * @version 2013-5-29
 */
public class RoleListType {

	private static SystemService systemService = SpringContextHolder.getBean(SystemService.class);
	
	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
		List<Role> roleList = Lists.newArrayList();
		List<Role> allRoleList = systemService.findAllRole();
		for (String s : org.apache.commons.lang3.StringUtils.split(val, ",")){
			for (Role e : allRoleList){
				if (org.apache.commons.lang3.StringUtils.trimToEmpty(s).equals(e.getName())){
					roleList.add(e);
				}
			}
		}
		return roleList.size()>0?roleList:null;
	}

	/**
	 * 设置对象值（导出）
	 */
	public static String setValue(Object val) {
		if (val != null){
			@SuppressWarnings("unchecked")
			List<Role> roleList = (List<Role>)val;
			return Collections3.extractToString(roleList, "name", ", ");
		}
		return "";
	}
	
}
