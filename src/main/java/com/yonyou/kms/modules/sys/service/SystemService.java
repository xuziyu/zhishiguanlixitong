/**
 * 
 */
package com.yonyou.kms.modules.sys.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.itf.kms.ISynchroNCInfo.ISynchroNCInfoLocator;
import nc.itf.kms.ISynchroNCInfo.ISynchroNCInfoPortType;
import nc.vo.kms.entityN.NCUser;
import nc.vo.kms.entityN.OuterSystemRetVO;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yonyou.kms.common.config.Global;
import com.yonyou.kms.common.persistence.Page;
import com.yonyou.kms.common.security.Digests;
import com.yonyou.kms.common.security.shiro.session.SessionDAO;
import com.yonyou.kms.common.service.BaseService;
import com.yonyou.kms.common.service.ServiceException;
import com.yonyou.kms.common.utils.CacheUtils;
import com.yonyou.kms.common.utils.Encodes;
import com.yonyou.kms.common.utils.IdGen;
import com.yonyou.kms.modules.sys.dao.MenuDao;
import com.yonyou.kms.modules.sys.dao.RoleDao;
import com.yonyou.kms.modules.sys.dao.StatusDao;
import com.yonyou.kms.modules.sys.dao.UserDao;
import com.yonyou.kms.modules.sys.entity.Menu;
import com.yonyou.kms.modules.sys.entity.Office;
import com.yonyou.kms.modules.sys.entity.Role;
import com.yonyou.kms.modules.sys.entity.Status;
import com.yonyou.kms.modules.sys.entity.User;
import com.yonyou.kms.modules.sys.utils.LogUtils;
import com.yonyou.kms.modules.sys.utils.UserUtils;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 * @author luqibao
 * @version 2015-9-22
 */
@Service
@Transactional(readOnly = true)
public class SystemService extends BaseService implements InitializingBean {
	
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;
	/**
	 * 数据正常
	 */
	public static String DATA_NORMAL = "0";

	/**
	 * 数据异常
	 */
	public static String DATA_EXCEPTION = "1";

	/**
	 * 数据为空
	 */
	public static String DATA_NULL = "2";
	/**
	 * USER 表名
	 * 
	 */
	public static final String USERTAB="USER";
	
	/**
	 * 系统管理员的权限id
	 */
	@Value("${web.role.sysid}")
	public String sysId;
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private SessionDAO sessionDao;
	
	private static Gson gson = new Gson();
	@Autowired
	private StatusDao statusDao;
	//注入数据源
	@Value("${nc.datasource}")
	private String dataSource;
	//nc远程连接口的信息
	@Value("${nc.romateService}")
	private String romateService;
	
	public SessionDAO getSessionDao() {
		return sessionDao;
	}

	@Autowired
	private IdentityService identityService;

	//-- User Service --//
	
	/**
	 * 获取用户
	 * @param id
	 * @return
	 */
	public User getUser(String id) {
		return UserUtils.get(id);
	}
	
	/**
	 * 批量获取用户信息
	 * 
	 */
	public List<User> batchget(Map map){
		List<User> list=new ArrayList<User>();
		list=userDao.batchget(map);
		return list;
	}
	
	/**
	 * 根据条件获取用户
	 * 
	 */
	public List<User> getUserList(User user){
		return userDao.findList(user);
	}
	/**
	 * 根据登录名获取用户
	 * @param loginName
	 * @return
	 */
	public User getUserByLoginName(String loginName) {
		return UserUtils.getByLoginName(loginName);
	}
	/**
	 * 
	 * @param page
	 * @param user
	 * @param flag
	 * @return
	 */
	public Page<User> findUser(Page<User> page, User user,boolean flag) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));
		// 设置分页参数
		if(flag){
			user.setPage(page);
		}
		// 执行分页查询
//		List<User> us=userDao.findList(user);
////		for(User u:us){
////			System.out.println(u.getName());
////		}
//		System.out.println("us:"+us.size());
		page.setList(userDao.findList(user));
		return page;
	}
	
	/**
	 * 查询数据库的所有用户
	 * @return
	 */
	public List<User> finalAllUsers(){
		return userDao.findAllList(new User());
	}
	/**
	 * 获取所具有权限下的管理人员列表
	 * @return
	 */
	public List<User> findUserByOffice(Page<User> page, User user){
		List<User> tempUser=Lists.newArrayList();
		List<User> users=Lists.newArrayList();
		Page<User> pages=findUser(page,user,false);
		tempUser=pages.getList();
		//查询是非普通用户的所有用户
		List<User> sys=userDao.getUserRole();
		for(User u:tempUser){
			if(sys.contains(u)){
				users.add(u);
			}
		}
		return users;
	}
	/**
	 * 根据用户权限判断是否是系统管理员
	 * 
	 */
	public boolean findSysUserByRole(User user){
		boolean flag=false;
		List<Role> list=user.getRoleList();
		if(list ==null || list.size() ==0){
			return false;
		}
		for(Role role:list)
			if(role.getId().equals(sysId)){
				flag=true;
				break;
			}
		return flag;
	}
	/**
	 * 获取角色下面用户的id的集合
	 * @param role
	 * @return
	 */
	public List<String> hasUser(Role role){
		
		if(role==null||role.getId()==null||role.getId().equals("")){
			throw new RuntimeException("参数出错");
		}
		List<User> users=roleDao.hasUser(role);
		List<String> ids=Lists.newArrayList();
		if(users==null){
			users=Lists.newArrayList();
		}
		for(User user:users){
			ids.add(user.getId());
		}
		return ids;
	}
	
	/**
	 * 无分页查询人员列表
	 * @param user
	 * @return
	 */
	public List<User> findUser(User user){
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));
		List<User> list = userDao.findList(user);
		return list;
	}
	
	/**
	 * 查询所有具有审核权限的人员列表
	 * @yangswh6
	 */
	public List<User> findExamer(Map map){
		return userDao.findUserByRoleId(map);
	}
	
	
	
	/**
	 * 通过部门ID获取用户列表，仅返回用户id和name（树查询用户时用）
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<User> findUserByOfficeId(String officeId) {
		List<User> list = (List<User>)CacheUtils.get(UserUtils.USER_CACHE, UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + officeId);
		if (list == null){
			User user = new User();
			user.setOffice(new Office(officeId));
			list = userDao.findUserByOfficeId(user);
			CacheUtils.put(UserUtils.USER_CACHE, UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + officeId, list);
		}
		return list;
	}
	
	@Transactional(readOnly = false)
	public void saveUser(User user) {
		if (org.apache.commons.lang3.StringUtils.isBlank(user.getId())){
			user.preInsert();
			userDao.insert(user);
		}else{
			// 清除原用户机构用户缓存
			User oldUser = userDao.get(user.getId());
			if (oldUser.getOffice() != null && oldUser.getOffice().getId() != null){
				CacheUtils.remove(UserUtils.USER_CACHE, UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + oldUser.getOffice().getId());
			}
			// 更新用户数据
			user.preUpdate();
			userDao.update(user);
		}
		if (org.apache.commons.lang3.StringUtils.isNotBlank(user.getId())){
			// 更新用户与角色关联
			userDao.deleteUserRole(user);
			if (user.getRoleList() != null && user.getRoleList().size() > 0){
				userDao.insertUserRole(user);
			}else{
				throw new ServiceException(user.getLoginName() + "没有设置角色！");
			}
			// 将当前用户同步到Activiti
			saveActivitiUser(user);
			// 清除用户缓存
			UserUtils.clearCache(user);
//			// 清除权限缓存
//			systemRealm.clearAllCachedAuthorizationInfo();
		}
	}
	
	@Transactional(readOnly = false)
	public void updateUserInfo(User user) {
		user.preUpdate();
		userDao.updateUserInfo(user);
		// 清除用户缓存
		UserUtils.clearCache(user);
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
	}
	
	@Transactional(readOnly = false)
	public void deleteUser(User user) {
		userDao.delete(user);
		// 同步到Activiti
		deleteActivitiUser(user);
		// 清除用户缓存
		UserUtils.clearCache(user);
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
	}
	
	//查询所属机构的管理人员
	@Transactional(readOnly = false)
	public List<User> findUserIsSys(Office office){
		
		return null;
	}
	
	@Transactional(readOnly = false)
	public void updatePasswordById(String id, String loginName, String newPassword) {
		User user = new User(id);
		user.setPassword(entryptPassword(newPassword));
		userDao.updatePasswordById(user);
		// 清除用户缓存
		user.setLoginName(loginName);
		UserUtils.clearCache(user);
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
	}
	
	@Transactional(readOnly = false)
	public void updateUserLoginInfo(User user) {
		// 保存上次登录信息
		user.setOldLoginIp(user.getLoginIp());
		user.setOldLoginDate(user.getLoginDate());
		// 更新本次登录信息
		user.setLoginIp(UserUtils.getSession().getHost());
		user.setLoginDate(new Date());
		userDao.updateLoginInfo(user);
	}
	
	/**
	 * 获取所有的系统管理员id的集合
	 */
	public List<String> findUserHasSystemRole(){
		List<String> ids = roleDao.findUserHasSystemRole();
		return ids;
	}
	
	/**
	 * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
	 */
	public static String entryptPassword(String plainPassword) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword);
	}
	
	/**
	 * 验证密码
	 * @param plainPassword 明文密码
	 * @param password 密文密码
	 * @return 验证成功返回true
	 */
	public static boolean validatePassword(String plainPassword, String password) {
		byte[] salt = Encodes.decodeHex(password.substring(0,16));
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return password.equals(Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword));
	}
	
	/**
	 * 获得活动会话
	 * @return
	 */
	public Collection<Session> getActiveSessions(){
		return sessionDao.getActiveSessions(false);
	}
	
	//-- Role Service --//
	
	public Role getRole(String id) {
		return roleDao.get(id);
	}

	public Role getRoleByName(String name) {
		Role r = new Role();
		r.setName(name);
		return roleDao.getByName(r);
	}
	
	public Role getRoleByEnname(String enname) {
		Role r = new Role();
		r.setEnname(enname);
		return roleDao.getByEnname(r);
	}
	
	public List<Role> findRole(Role role){
		return roleDao.findList(role);
	}
	
	public List<Role> findAllRole(){
		return UserUtils.getRoleList();
	}
	
	@Transactional(readOnly = false)
	public void saveRole(Role role) {
		if (org.apache.commons.lang3.StringUtils.isBlank(role.getId())){
			role.preInsert();
			roleDao.insert(role);
			// 同步到Activiti
			saveActivitiGroup(role);
		}else{
			role.preUpdate();
			roleDao.update(role);
		}
		// 更新角色与菜单关联
		roleDao.deleteRoleMenu(role);
		if (role.getMenuList().size() > 0){
			roleDao.insertRoleMenu(role);
		}
		// 更新角色与部门关联
		roleDao.deleteRoleOffice(role);
		if (role.getOfficeList().size() > 0){
			roleDao.insertRoleOffice(role);
		}
		// 同步到Activiti
		saveActivitiGroup(role);
		// 清除用户角色缓存
		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
	}

	@Transactional(readOnly = false)
	public void deleteRole(Role role) {
		roleDao.delete(role);
		// 同步到Activiti
		deleteActivitiGroup(role);
		// 清除用户角色缓存
		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
	}
	
	@Transactional(readOnly = false)
	public Boolean outUserInRole(Role role, User user) {
		List<Role> roles = user.getRoleList();
		for (Role e : roles){
			if (e.getId().equals(role.getId())){
				roles.remove(e);
				saveUser(user);
				return true;
			}
		}
		return false;
	}
	
	@Transactional(readOnly = false)
	public User assignUserToRole(Role role, User user) {
		if (user == null){
			return null;
		}
		List<String> roleIds = user.getRoleIdList();
		if (roleIds.contains(role.getId())) {
			return null;
		}
		user.getRoleList().add(role);
		saveUser(user);
		return user;
	}

	//-- Menu Service --//
	
	public Menu getMenu(String id) {
		return menuDao.get(id);
	}

	public List<Menu> findAllMenu(){
		return UserUtils.getMenuList();
	}
	
	@Transactional(readOnly = false)
	public void saveMenu(Menu menu) {
		
		// 获取父节点实体
		menu.setParent(this.getMenu(menu.getParent().getId()));
		
		// 获取修改前的parentIds，用于更新子节点的parentIds
		String oldParentIds = menu.getParentIds(); 
		
		// 设置新的父节点串
		menu.setParentIds(menu.getParent().getParentIds()+menu.getParent().getId()+",");

		// 保存或更新实体
		if (org.apache.commons.lang3.StringUtils.isBlank(menu.getId())){
			menu.preInsert();
			menuDao.insert(menu);
		}else{
			menu.preUpdate();
			menuDao.update(menu);
		}
		
		// 更新子节点 parentIds
		Menu m = new Menu();
		m.setParentIds("%,"+menu.getId()+",%");
		List<Menu> list = menuDao.findByParentIdsLike(m);
		for (Menu e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
			menuDao.updateParentIds(e);
		}
		// 清除用户菜单缓存
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
		// 清除日志相关缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
	}

	@Transactional(readOnly = false)
	public void updateMenuSort(Menu menu) {
		menuDao.updateSort(menu);
		// 清除用户菜单缓存
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
		// 清除日志相关缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
	}

	@Transactional(readOnly = false)
	public void deleteMenu(Menu menu) {
		menuDao.delete(menu);
		// 清除用户菜单缓存
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
		// 清除日志相关缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
	}
	
	/**
	 * 获取Key加载信息
	 */
	public static boolean printKeyLoadMessage(){
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n======================================================================\r\n");
		sb.append("\r\n    知识库");
		sb.append("\r\n======================================================================\r\n");
//		System.out.println(sb.toString());
		return true;
	}
	
	///////////////// Synchronized to the Activiti //////////////////
	
	// 已废弃，同步见：ActGroupEntityServiceFactory.java、ActUserEntityServiceFactory.java

	/**
	 * 是需要同步Activiti数据，如果从未同步过，则同步数据。
	 */
	private static boolean isSynActivitiIndetity = true;
	public void afterPropertiesSet() throws Exception {
		if (!Global.isSynActivitiIndetity()){
			return;
		}
		if (isSynActivitiIndetity){
			isSynActivitiIndetity = false;
	        // 同步角色数据
			List<Group> groupList = identityService.createGroupQuery().list();
			if (groupList.size() == 0){
			 	Iterator<Role> roles = roleDao.findAllList(new Role()).iterator();
			 	while(roles.hasNext()) {
			 		Role role = roles.next();
			 		saveActivitiGroup(role);
			 	}
			}
		 	// 同步用户数据
			List<org.activiti.engine.identity.User> userList = identityService.createUserQuery().list();
			if (userList.size() == 0){
			 	Iterator<User> users = userDao.findAllList(new User()).iterator();
			 	while(users.hasNext()) {
			 		saveActivitiUser(users.next());
			 	}
			}
		}
	}
	
	private void saveActivitiGroup(Role role) {
		if (!Global.isSynActivitiIndetity()){
			return;
		}
		String groupId = role.getEnname();
		
		// 如果修改了英文名，则删除原Activiti角色
		if (org.apache.commons.lang3.StringUtils.isNotBlank(role.getOldEnname()) && !role.getOldEnname().equals(role.getEnname())){
			identityService.deleteGroup(role.getOldEnname());
		}
		
		Group group = identityService.createGroupQuery().groupId(groupId).singleResult();
		if (group == null) {
			group = identityService.newGroup(groupId);
		}
		group.setName(role.getName());
		group.setType(role.getRoleType());
		identityService.saveGroup(group);
		
		// 删除用户与用户组关系
		List<org.activiti.engine.identity.User> activitiUserList = identityService.createUserQuery().memberOfGroup(groupId).list();
		for (org.activiti.engine.identity.User activitiUser : activitiUserList){
			identityService.deleteMembership(activitiUser.getId(), groupId);
		}

		// 创建用户与用户组关系
		List<User> userList = findUser(new User(new Role(role.getId())));
		for (User e : userList){
			String userId = e.getLoginName();//ObjectUtils.toString(user.getId());
			// 如果该用户不存在，则创建一个
			org.activiti.engine.identity.User activitiUser = identityService.createUserQuery().userId(userId).singleResult();
			if (activitiUser == null){
				activitiUser = identityService.newUser(userId);
				activitiUser.setFirstName(e.getName());
				activitiUser.setLastName(org.apache.commons.lang3.StringUtils.EMPTY);
				activitiUser.setEmail(e.getEmail());
				activitiUser.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
				identityService.saveUser(activitiUser);
			}
			identityService.createMembership(userId, groupId);
		}
	}

	public void deleteActivitiGroup(Role role) {
		if (!Global.isSynActivitiIndetity()){
			return;
		}
		if(role!=null) {
			String groupId = role.getEnname();
			identityService.deleteGroup(groupId);
		}
	}

	private void saveActivitiUser(User user) {
		if (!Global.isSynActivitiIndetity()){
			return;
		}
		String userId = user.getLoginName();//ObjectUtils.toString(user.getId());
		org.activiti.engine.identity.User activitiUser = identityService.createUserQuery().userId(userId).singleResult();
		if (activitiUser == null) {
			activitiUser = identityService.newUser(userId);
		}
		activitiUser.setFirstName(user.getName());
		activitiUser.setLastName(org.apache.commons.lang3.StringUtils.EMPTY);
		activitiUser.setEmail(user.getEmail());
		activitiUser.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
		identityService.saveUser(activitiUser);
		
		// 删除用户与用户组关系
		List<Group> activitiGroups = identityService.createGroupQuery().groupMember(userId).list();
		for (Group group : activitiGroups) {
			identityService.deleteMembership(userId, group.getId());
		}
		// 创建用户与用户组关系
		for (Role role : user.getRoleList()) {
	 		String groupId = role.getEnname();
	 		// 如果该用户组不存在，则创建一个
		 	Group group = identityService.createGroupQuery().groupId(groupId).singleResult();
            if(group == null) {
	            group = identityService.newGroup(groupId);
	            group.setName(role.getName());
	            group.setType(role.getRoleType());
	            identityService.saveGroup(group);
            }
			identityService.createMembership(userId, role.getEnname());
		}
	}

	private void deleteActivitiUser(User user) {
		if (!Global.isSynActivitiIndetity()){
			return;
		}
		if(user!=null) {
			String userId = user.getLoginName();//ObjectUtils.toString(user.getId());
			identityService.deleteUser(userId);
		}
	}
	
	
	/**
	 * 同步用户信息
	 * @throws ServiceException
	 * 这里还需要设置几个参数
	 */
	public void synchroNCInfo() throws ServiceException {
		String nextMaxTs="";
		try {
			/** ************获取用户的同步记录表数据(用于判断是否第一次同步)************** */
			String maxTs = statusDao.selectStatus(USERTAB);
			logger.debug("maxTs:"+maxTs);
			//设置储存用户的list
			List<User> userAddList = new ArrayList<User>();
			List<User> userUpdateList=new ArrayList<User>();
			//用户同步记录
			Status userStatus = null;
			//连接nc远程服务   调取数据
			ISynchroNCInfoLocator service = new ISynchroNCInfoLocator();
			URL url=new URL(romateService);
			ISynchroNCInfoPortType client =service.getISynchroNCInfoSOAP11port_http(url);
			if (StringUtils.isEmpty(maxTs)) {// 第一次同步，所取数据为新增的数据

				/** *******************获取NC用户信息************** */
				//获取用户信息
				
				OuterSystemRetVO user = client.getNCUserInfo(dataSource, "1999-09-09 10:10:10", "", "");//调用接口getNCUserInfo
				// 调用接口成功且数据正常
				if ((user.isSuccess())
						&& (DATA_NORMAL.equals(user.getDataState()))) {
					// 获取传过来的用户数据
					 List<NCUser> retList = gson.fromJson(user.getData(),  
				                new TypeToken<List<NCUser>>() {  
				                }.getType());
					 logger.debug("更新的用户数量："+retList.size());
					 
					 /**
					  *转化用户格式
					  * 
					  */
					 userAddList =NCUser2LocalUser(retList,userAddList);
					 // System.out.println(userAddList);
					 nextMaxTs=user.getMaxTs();
				} else {
					return;
				}

			}else{
				//-----
				OuterSystemRetVO user = client.getNCUserInfo(dataSource, maxTs, "", "");//调用接口getNCGroupInfo
				
				List<User> userList = new ArrayList<User>();
				logger.debug("更新数据："+userList.size());
				// 调用接口成功且数据正常
				if ((user.isSuccess())
						&& (DATA_NORMAL.equals(user.getDataState()))) {
					// 获取传过来的集团数据
					 List<NCUser> retList = gson.fromJson(user.getData(),  
				                new TypeToken<List<NCUser>>() {  
				                }.getType());
					 logger.debug(user.getData());
					 //将数据格式转化为知识库对应的数据
					 userAddList=NCUser2LocalUser(retList,userAddList);
					 nextMaxTs=user.getMaxTs();
//					//设置更新的时间
//					 if(!userAddList.isEmpty()){
//						
//							userStatus = new Status();
//							userStatus.setId(IdGen.uuid());
//							userStatus.setTs(user.getMaxTs());
//							userStatus.setFromSys(USERTAB);
//							statusDao.insertStatus(userStatus);
//					 }

				}
			}
			
//			//查出知识库中所有的数据  
			 
			List<User> users= userDao.batchSelect();
			 
			//验证是否为新增还是修改  新增就直接添加  修改就 删除以后再增加
			for(User u1:userAddList){
				
				if(users.contains(u1)){
					//将更新的数据加入到updateList里面
					userUpdateList.add(u1);
					//同时将数据从addList中移除
					
				}
			}
			
			List<User> tempUsers=Lists.newArrayList();
			if((userAddList != null) && (!userAddList.isEmpty())){
				//System.out.println("插入");
				//调用user批量插入接口
				//userDao.batchInsert(userAddList);
				for(User u:userAddList){
					
					//为了防止多次插入导致相同数据报错
//					if(userDao.get(u)!=null){
//						continue;
//					}
					if(tempUsers.contains(u)||userUpdateList.contains(u)){
						continue;
					}
					tempUsers.add(u);
					userDao.insert(u);
					roleDao.insertUserRole(u.getId());
				}
			}
			tempUsers.clear();
			if((userUpdateList != null) && (!userUpdateList.isEmpty())){
				//调用user批量更新接口
				//使用单个操作  不然会出现一个出现问题  全部的数据都插不进去
				
				for(User u:userUpdateList){
					if(tempUsers.contains(u)){
						continue;
					}
					tempUsers.add(u);
					try {
						userDao.update(u);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}
			
			//储存更新的时间  下次更新从这个时间戳开始更新数据
			if(StringUtils.isNoneBlank(nextMaxTs)){
				userStatus = new Status();
				userStatus.setId(IdGen.uuid());
				userStatus.setTs(nextMaxTs);
				userStatus.setFromSys(USERTAB);
				statusDao.insertStatus(userStatus);
			}
			
		}catch(Exception e){
			/**
			 * 未完成的异常捕抓
			 */
			e.printStackTrace();
		}
		
	}
	/**
	 * 数据形式的转化
	 * add:luqibao
	 * @param ncUsers 
	 * @param localUsers
	 */
	private List<User> NCUser2LocalUser(List<NCUser> ncUsers,List<User> localUsers){
		 User u=null;
		 User uu=new User();
		 uu.setId("1");
		 for(NCUser nu:ncUsers){
				u=new User();
				u.setId(nu.getId());
				/*登录名都变为大写*/
				u.setLoginName(nu.getLoginName().toUpperCase());
				u.setPassword(nu.getPassword());
				u.setLoginFlag(nu.getLoginFlag());
				u.setNo(nu.getNo());
				//对于知识库系统来说  部门和公司都是使用Office
				u.setCompany(new Office(nu.getCompany()));
				//设置部门
				u.setOffice(new Office(nu.getOffice()));
				u.setDelFlag(nu.getDelFlag());
				if(nu.getName()==null){
					u.setName(nu.getLoginName());
				}else{
					u.setName(nu.getName());
				}

				u.setCreateBy(uu);
				u.setCreateDate(new Date());
				u.setUpdateBy(uu);
				u.setUpdateDate(new Date());
				u.setPhone("123");
				u.setEmail("123@qq.com");
				u.setRemarks("111");
				u.setPhoto("1");
				u.setMobile("13678");
				u.setUserType("1");
				
				localUsers.add(u);
				
			}
		return localUsers;
	}
	
	public List<String> findOfficeNameByUserName(String username){
		return userDao.findOfficeNameByUserName(username);
	}
	
	public List<User> findUserBySearch(String username){
		return userDao.findUserBySearch(username);
	}
	
}
