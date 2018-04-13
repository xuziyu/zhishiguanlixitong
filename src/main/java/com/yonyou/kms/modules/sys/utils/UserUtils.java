/**
 * 
 */
package com.yonyou.kms.modules.sys.utils;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.yonyou.kms.common.service.BaseService;
import com.yonyou.kms.common.utils.CacheUtils;
import com.yonyou.kms.common.utils.SpringContextHolder;
import com.yonyou.kms.modules.sys.dao.AreaDao;
import com.yonyou.kms.modules.sys.dao.MenuDao;
import com.yonyou.kms.modules.sys.dao.OfficeDao;
import com.yonyou.kms.modules.sys.dao.RoleDao;
import com.yonyou.kms.modules.sys.dao.UserDao;
import com.yonyou.kms.modules.sys.entity.Area;
import com.yonyou.kms.modules.sys.entity.Menu;
import com.yonyou.kms.modules.sys.entity.Office;
import com.yonyou.kms.modules.sys.entity.Role;
import com.yonyou.kms.modules.sys.entity.User;
import com.yonyou.kms.modules.sys.security.SystemAuthorizingRealm.Principal;

/**
 * 用户工具类
 * @author hotsum
 * @version 2013-12-05
 */
public class UserUtils {

	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
	private static RoleDao roleDao = SpringContextHolder.getBean(RoleDao.class);
	private static MenuDao menuDao = SpringContextHolder.getBean(MenuDao.class);
	private static AreaDao areaDao = SpringContextHolder.getBean(AreaDao.class);
	private static OfficeDao officeDao = SpringContextHolder.getBean(OfficeDao.class);

	public static final String USER_CACHE = "userCache";
	public static final String USER_CACHE_ID_ = "id_";
	public static final String USER_CACHE_LOGIN_NAME_ = "ln";
	public static final String USER_CACHE_LIST_BY_OFFICE_ID_ = "oid_";

	public static final String CACHE_ROLE_LIST = "roleList";
	public static final String CACHE_MENU_LIST = "menuList";
	public static final String CACHE_AREA_LIST = "areaList";
	public static final String CACHE_OFFICE_LIST = "officeList";
	public static final String CACHE_OFFICE_ALL_LIST = "officeAllList";
	
	/**
	 * 根据ID获取用户
	 * @param id
	 * @return 取不到返回null
	 */
	public static User get(String id){
		User user =null;
		if (user ==  null){
			user = userDao.get(id);
			if (user == null){
				return null;
			}
			user.setRoleList(roleDao.findList(new Role(user)));
			//CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
			//CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
			//UserUtils.putCache("userInfo", user);
		}
		return user;
	}
	
	/**
	 * 根据登录名获取用户
	 * @param loginName
	 * @return 取不到返回null
	 */
	public static User getByLoginName(String loginName){
		User user = (User)CacheUtils.get(USER_CACHE, USER_CACHE_LOGIN_NAME_ + loginName);
		if (user == null){
			user = userDao.getByLoginName(new User(null, loginName));
			if (user == null){
				return null;
			}
			user.setRoleList(roleDao.findList(new Role(user)));
			CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
			CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
		}
		return user;
	}
	
	/**
	 * 清除当前用户缓存
	 */
	public static void clearCache(){
		removeCache(CACHE_ROLE_LIST);
		removeCache(CACHE_MENU_LIST);
		removeCache(CACHE_AREA_LIST);
		removeCache(CACHE_OFFICE_LIST);
		removeCache(CACHE_OFFICE_ALL_LIST);
		UserUtils.clearCache(getUser());
	}
	
	/**
	 * 清除指定用户缓存
	 * @param user
	 */
	public static void clearCache(User user){
		CacheUtils.remove(USER_CACHE, USER_CACHE_ID_ + user.getId());
		CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName());
		CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getOldLoginName());
		if (user.getOffice() != null && user.getOffice().getId() != null){
			CacheUtils.remove(USER_CACHE, USER_CACHE_LIST_BY_OFFICE_ID_ + user.getOffice().getId());
		}
	}
	
	/**
	 * 获取当前用户
	 * @return 取不到返回 new User()
	 */
	public static User getUser(){
		Principal principal = getPrincipal();
		if (principal!=null){
			User user = get(principal.getId());
			if (user != null){
				return user;
			}
			return new User();
		}
		// 如果没有登录，则返回实例化空的User对象。
		return new User();
	}
	
	//add by yinshh3
	/**
	 * 获取当前用户是否是超级管理员
	 * @return 
	 */
	public static boolean isAdmin(){
	 return getUser().isAdmin();
	}
	/**
	 * 得到当前用户的角色id集合
	 * 
	 * @return
	 */
	public  static List<String> getRoleIdList(){ 
		return getUser().getRoleIdList();
	}
	/**
	 * @return	是否为普通用户
	 */
	public static Boolean IsOrdinaryUser(){
		boolean flag=true;
		List<Menu> list = UserUtils.getMenuList();
		for (Menu menu : list){
			if (StringUtils.isNotBlank(menu.getPermission())){
				// 添加基于Permission的权限信息
				for (String permission : StringUtils.split(menu.getPermission(),",")){
					if("sys:manager".equals(permission)){
						flag=false;	
					}
				}
			}
		}
		
//		for(int i=0;i<getRoleIdList().size();i++){
//			//如果角色id集合长度为1,并且只等于角色id为6的"普通用户"
//			if(getRoleIdList().size()==1&&getRoleIdList().get(i).equals("6")){
//				//System.out.println("------------已经判断是普通用户了---getRoleIdList().size()="+getRoleIdList().size()+"----------------getRoleIdList().get(i):"+getRoleIdList().get(i)+"-----------"+flag);
//			}else{
//				flag=false;	
//				//System.out.println("------------不是普通用户是管理员---getRoleIdList().size()="+getRoleIdList().size()+"----------------getRoleIdList().get(i):"+getRoleIdList().get(i)+"-----------"+flag);
//			}
//		
//		}
//		
		return flag;
	}
	

	/**
	 * 获取当前用户角色列表
	 * @return
	 */
	//update by yinshh3
	public static List<Role> getRoleList(){
		List<Role> roleList = null;
		if (roleList == null){
			User user = getUser();
			if (user.isAdmin()){
				roleList = roleDao.findAllList(new Role());
			}else{
				Role role = new Role();
				role.getSqlMap().put("dsf", BaseService.dataScopeFilter(user.getCurrentUser(), "o", "u"));
				roleList = roleDao.findList(role);
			}
			//putCache(CACHE_ROLE_LIST, roleList);
		}
		return roleList;
	}
	
	public static boolean hasSystemRole(User user){
		Role role = new Role();
		role.getSqlMap().put("dsf", BaseService.dataScopeFilter(user.getCurrentUser(), "o", "u"));
		List<Role> roleList = roleDao.findList(role);
		if(roleList==null||roleList.size()<=0)return false;
		for(Role r:roleList){
			if("1".equals(r.getSysData())){
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * 获取当前用户授权菜单
	 * @return
	 */
	public static List<Menu> getMenuList(){
		@SuppressWarnings("unchecked")
		List<Menu> menuList = (List<Menu>)getCache(CACHE_MENU_LIST);
		if (menuList == null){
			User user = getUser();
			if (user.isAdmin()){
				menuList = menuDao.findAllList(new Menu());
			}else{
				Menu m = new Menu();
				m.setUserId(user.getId());
				menuList = menuDao.findByUserId(m);
			}
			putCache(CACHE_MENU_LIST, menuList);
		}
		return menuList;
	}
	
	/**
	 * 获取当前用户授权的区域
	 * @return
	 */
	public static List<Area> getAreaList(){
		@SuppressWarnings("unchecked")
		List<Area> areaList = (List<Area>)getCache(CACHE_AREA_LIST);
		if (areaList == null){
			areaList = areaDao.findAllList(new Area());
			putCache(CACHE_AREA_LIST, areaList);
		}
		return areaList;
	}
	
	/**
	 * 获取当前用户有权限访问的部门
	 * @return
	 */
	public static List<Office> getOfficeList(){
		@SuppressWarnings("unchecked")
		List<Office> officeList = (List<Office>)getCache(CACHE_OFFICE_LIST);
		if (officeList == null){
			User user = getUser();
			if (user.isAdmin()){
				officeList = officeDao.findAllList(new Office());
			}else{
				Office office = new Office();
				office.getSqlMap().put("dsf", BaseService.dataScopeFilter(user, "a", ""));
				officeList = officeDao.findList(office);
			}
			putCache(CACHE_OFFICE_LIST, officeList);
		}
		return officeList;
	}

	/**
	 * 获取当前用户有权限访问的部门
	 * @return
	 */
	public static List<Office> getOfficeAllList(){
		@SuppressWarnings("unchecked")
		List<Office> officeList = (List<Office>)getCache(CACHE_OFFICE_ALL_LIST);
		if (officeList == null){
			officeList = officeDao.findAllList(new Office());
		}
		return officeList;
	}
	
	/**
	 * 获取授权主要对象
	 */
	public static Subject getSubject(){
		return SecurityUtils.getSubject();
	}
	
	/**
	 * 获取当前登录者对象
	 */
	public static Principal getPrincipal(){
		try{
			Subject subject = SecurityUtils.getSubject();
			Principal principal = (Principal)subject.getPrincipal();
			if (principal != null){
				return principal;
			}
//			subject.logout();
		}catch (UnavailableSecurityManagerException e) {
			
		}catch (InvalidSessionException e){
			
		}
		return null;
	}
	
	public static Session getSession(){
		try{
			Subject subject = SecurityUtils.getSubject();
			Session session = subject.getSession(false);
			if (session == null){
				session = subject.getSession();
			}
			if (session != null){
				return session;
			}
//			subject.logout();
		}catch (InvalidSessionException e){
			
		}
		return null;
	}
	
	// ============== User Cache ==============
	
	public static Object getCache(String key) {
		return getCache(key, null);
	}
	
	public static Object getCache(String key, Object defaultValue) {
//		Object obj = getCacheMap().get(key);
		Object obj = getSession().getAttribute(key);
		return obj==null?defaultValue:obj;
	}

	public static void putCache(String key, Object value) {
//		getCacheMap().put(key, value);
		getSession().setAttribute(key, value);
	}

	public static void removeCache(String key) {
//		getCacheMap().remove(key);
		getSession().removeAttribute(key);
	}
	
//	public static Map<String, Object> getCacheMap(){
//		Principal principal = getPrincipal();
//		if(principal!=null){
//			return principal.getCacheMap();
//		}
//		return new HashMap<String, Object>();
//	}
	//add by luqibao
	//获取部门的名字
	public static String getOffice(String id){
		Office office=new Office();
		office.setId(id);
		Office temp=officeDao.get(office);
		if(temp!=null){
			return temp.getName();
		}
		return "";
	}
	//end
	//add by yangshw6
	//判断是否为系统管理员
	public static boolean findSysRole(){
		boolean flag=false;
		String roleid=userDao.findRoleId(UserUtils.getUser().getId());
		if(roleid!=null){
			flag=true;
		};
		return flag;
	}
	//end by yangshw6
}
