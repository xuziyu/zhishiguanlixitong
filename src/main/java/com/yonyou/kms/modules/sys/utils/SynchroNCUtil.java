package com.yonyou.kms.modules.sys.utils;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.yonyou.kms.modules.sys.entity.Office;

/**
 * 同步NC工具类
 * 
 * @author xiongbo
 * 
 */
public class SynchroNCUtil {

	/**
	 * 获取所有父编号","分割
	 * 
	 * @param pid
	 * @param relations
	 * @return
	 */
	public static String getParentIds(String pid, Map<String, String> relations) {

		String parentIds = "";
		if ((!StringUtils.isEmpty(pid)) && ("0".equals(pid))) {
			parentIds = "0";
		} else if ((!StringUtils.isEmpty(pid)) && (!relations.containsKey(pid))) {
			parentIds = pid;
		} else {
			parentIds = getParentIds(relations.get(pid), relations) + pid;
		}
		return parentIds + ",";
	}

}
