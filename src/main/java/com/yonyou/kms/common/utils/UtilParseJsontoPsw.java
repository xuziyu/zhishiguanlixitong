package com.yonyou.kms.common.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Administrator
 *
 */
public class UtilParseJsontoPsw {
	public static boolean parseJson(net.sf.json.JSONObject json) {
        StringBuilder sb = new StringBuilder();
        String data = json.getString("data");   
        String ipAddress = json.getString("ipAddress");
        String platform = json.getString("platform");
        String timestamp = json.getString("timestamp");
        String version = json.getString("version");
        String sign = json.getString("sign");
        sb.append("data=").append(data).append("&").append("ipAddress=").append(ipAddress).append("&").append("platform=").append(platform).append("&").append("timestamp=").append(timestamp).append("&").append("version=").append(version);   
        String psw;		//这个变量相当于加密后的数据
        String str1 = string2Unicode(sb.toString());
        psw = UtilMd5Util.encrypt(str1);              
        if (psw.equals(sign)) {		//对数据进行加密与接收的sign比对
            return true;
        } else {
            return false;
        }

    }

    /**
     * 字符串转换unicode
     * 
     * @param str
     * @return
     */
    private static String string2Unicode(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            // 取出每一个字符
            char c = str.charAt(i);
            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }
        return unicode.toString();
    }
}