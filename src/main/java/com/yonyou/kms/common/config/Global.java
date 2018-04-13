/**
 * 
 */
package com.yonyou.kms.common.config;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.springframework.core.io.DefaultResourceLoader;

import com.ckfinder.connector.ServletContextFactory;
import com.google.common.collect.Maps;
import com.yonyou.kms.common.utils.PropertiesLoader;

/**
 * 鍏ㄥ眬閰嶇疆绫� * @author hotsum
 * @version 2014-06-25
 */
public class Global {

	/**
	 * 褰撳墠瀵硅薄瀹炰緥
	 */
	private static Global global = new Global();
	
	/**
	 * 淇濆瓨鍏ㄥ眬灞炴�鍊�	 */
	private static Map<String, String> map = Maps.newHashMap();
	
	/**
	 * 灞炴�鏂囦欢鍔犺浇瀵硅薄
	 */
	private static PropertiesLoader loader = new PropertiesLoader("kms.properties");

	/**
	 * 鏄剧ず/闅愯棌
	 */
	public static final String SHOW = "1";
	public static final String HIDE = "0";

	/**
	 * 鏄�鍚�	 */
	public static final String YES = "1";
	public static final String NO = "0";
	
	/**
	 * 瀵�閿�	 */
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	
	/**
	 * 涓婁紶鏂囦欢鍩虹铏氭嫙璺緞
	 */
	public static final String USERFILES_BASE_URL = "/userfiles/";
	
	/**
	 * 鑾峰彇褰撳墠瀵硅薄瀹炰緥
	 */
	public static Global getInstance() {
		return global;
	}
	
	/**
	 * 鑾峰彇閰嶇疆
	 * @see ${fns:getConfig('adminPath')}
	 */
	public static String getConfig(String key) {
		String value = map.get(key);
		if (value == null){
			value = loader.getProperty(key);
			map.put(key, value != null ? value : org.apache.commons.lang3.StringUtils.EMPTY);
		}
		return value;
	}
	
	/**
	 * 鑾峰彇绠＄悊绔牴璺緞
	 */
	public static String getAdminPath() {
		return getConfig("adminPath");
	}
	
	/**
	 * 鑾峰彇鍓嶇鏍硅矾寰�	 */
	public static String getFrontPath() {
		return getConfig("frontPath");
	}
	
	/**
	 * 鑾峰彇URL鍚庣紑
	 */
	public static String getUrlSuffix() {
		return getConfig("urlSuffix");
	}
	
	/**
	 * 鏄惁鏄紨绀烘ā寮忥紝婕旂ず妯″紡涓嬩笉鑳戒慨鏀圭敤鎴枫�瑙掕壊銆佸瘑鐮併�鑿滃崟銆佹巿鏉�	 */
	public static Boolean isDemoMode() {
		String dm = getConfig("demoMode");
		return "true".equals(dm) || "1".equals(dm);
	}
	
	/**
	 * 鍦ㄤ慨鏀圭郴缁熺敤鎴峰拰瑙掕壊鏃舵槸鍚﹀悓姝ュ埌Activiti
	 */
	public static Boolean isSynActivitiIndetity() {
		String dm = getConfig("activiti.isSynActivitiIndetity");
		return "true".equals(dm) || "1".equals(dm);
	}
    
	/**
	 * 椤甸潰鑾峰彇甯搁噺
	 * @see ${fns:getConst('YES')}
	 */
	public static Object getConst(String field) {
		try {
			return Global.class.getField(field).get(null);
		} catch (Exception e) {
			// 寮傚父浠ｈ〃鏃犻厤缃紝杩欓噷浠�箞涔熶笉鍋�	
			}
		return null;
	}

	/**
	 * 鑾峰彇涓婁紶鏂囦欢鐨勬牴鐩綍
	 * @return
	 */
	public static String getUserfilesBaseDir() {
		String dir = getConfig("userfiles.basedir");
		if (org.apache.commons.lang3.StringUtils.isBlank(dir)){
			try {
				dir = ServletContextFactory.getServletContext().getRealPath("/");
			} catch (Exception e) {
				return "";
			}
		}
		if(!dir.endsWith("/")) {
			dir += "/";
		}
//		System.out.println("userfiles.basedir: " + dir);
		return dir;
	}
	
    /**
     * 鑾峰彇宸ョ▼璺緞
     * @return
     */
    public static String getProjectPath(){
    	// 濡傛灉閰嶇疆浜嗗伐绋嬭矾寰勶紝鍒欑洿鎺ヨ繑鍥烇紝鍚﹀垯鑷姩鑾峰彇銆�		
    	String projectPath = Global.getConfig("projectPath");
		if (org.apache.commons.lang3.StringUtils.isNotBlank(projectPath)){
			return projectPath;
		}
		try {
			File file = new DefaultResourceLoader().getResource("").getFile();
			if (file != null){
				while(true){
					File f = new File(file.getPath() + File.separator + "src" + File.separator + "main");
					if (f == null || f.exists()){
						break;
					}
					if (file.getParentFile() != null){
						file = file.getParentFile();
					}else{
						break;
					}
				}
				projectPath = file.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return projectPath;
    }
	
}
