/**
 * 
 */
package com.yonyou.kms.modules.sys.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yonyou.kms.common.config.Global;
import com.yonyou.kms.common.persistence.Page;
import com.yonyou.kms.common.utils.Collections3;
import com.yonyou.kms.common.web.BaseController;
import com.yonyou.kms.modules.sys.entity.Office;
import com.yonyou.kms.modules.sys.entity.Role;
import com.yonyou.kms.modules.sys.entity.User;
import com.yonyou.kms.modules.sys.service.OfficeService;
import com.yonyou.kms.modules.sys.service.SystemService;
import com.yonyou.kms.modules.sys.utils.UserUtils;

/**
 * 角色Controller
 * @author hotsum
 * @version 2013-12-05
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/role")
public class RoleController extends BaseController {

	@Autowired
	private SystemService systemService;
	
	@Autowired
	private OfficeService officeService;
	
	private final String IS_SHOW="isShow";
	
	@ModelAttribute("role")
	public Role get(@RequestParam(required=false) String id) {
		if (org.apache.commons.lang3.StringUtils.isNotBlank(id)){
			return systemService.getRole(id);
		}else{
			return new Role();
		}
	}
	
	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = {"list", ""})
	public String list(Role role, Model model) {
		List<Role> list = systemService.findAllRole();
		model.addAttribute("list", list);
		//add by luqibao 查询不是系统角色的角色 2015-12-30
		List<Role> roles=Lists.newArrayList();//所有的非系统数据
		for(Role r:list){
			String sysD=r.getSysData();
			if(sysD!=null){
				if(sysD.equals("0")){
					roles.add(r);
				}
			}
		}
//		for(Role r:roles){
//			System.out.println(r.getName());
//		}
	
		//end
		return "modules/sys/roleList";
	}

	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "form")
	public String form(Role role, Model model,HttpServletRequest request) {
		if (role.getOffice()==null){
			role.setOffice(UserUtils.getUser().getOffice());
		}
		if(null==role.getId()){
			model.addAttribute("isNew", true);
		}
		/**
		 * add by luqibao 
		 * @date 2016-08-29
		 * 
		 * @description 针对于角色管理的功能树 因为客户环境中
		 * 需要时刻的调整  所以需要手动的来控制url来控制
		 * 功能树:
		 * 例如: domain/a/sys/role/form?id=1&isShow=0
		 * 那么就会显示功能树
		 * 如果: domain/a/sys/role/form?id=1
		 * 不会显示功能树
		 */
		String isShow = request.getParameter(IS_SHOW);
		
		if("0".equals(isShow)){
			model.addAttribute(IS_SHOW, true);
		}
		
		/**
		 * end
		 */
		model.addAttribute("role", role);
		model.addAttribute("menuList", systemService.findAllMenu());
		model.addAttribute("officeList", officeService.findAll());
		return "modules/sys/roleForm";
	}
	
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "save")
	public String save(Role role, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) {
//		if(!UserUtils.getUser().isAdmin()&&role.getSysData().equals(Global.YES)){
//			addMessage(redirectAttributes, "越权操作，只有超级管理员才能修改此数据！");
//			return "redirect:" + adminPath + "/sys/role/?repage";
//		}
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/role/?repage";
		}
		if (!beanValidator(model, role)){
			return form(role, model,request);
		}
		if (!"true".equals(checkName(role.getOldName(), role.getName()))){
			addMessage(model, "保存角色'" + role.getName() + "'失败, 角色名已存在");
			return form(role, model,request);
		}
		if (!"true".equals(checkEnname(role.getOldEnname(), role.getEnname()))){
			addMessage(model, "保存角色'" + role.getName() + "'失败, 英文名已存在");
			return form(role, model,request);
		}
		systemService.saveRole(role);
		addMessage(redirectAttributes, "保存角色'" + role.getName() + "'成功");
		return "redirect:" + adminPath + "/sys/role/?repage";
	}
	
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "delete")
	public String delete(Role role, RedirectAttributes redirectAttributes) {
		List<String> ids=systemService.hasUser(role);
		if(!UserUtils.getUser().isAdmin() && role.getSysData().equals(Global.YES)){
			addMessage(redirectAttributes, "越权操作，只有超级管理员才能修改此数据！");
			return "redirect:" + adminPath + "/sys/role/?repage";
		}else if(ids.size()>0){
			addMessage(redirectAttributes, "角色下面有用户,不能删除");
			return "redirect:" + adminPath + "/sys/role/?repage";
		}
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/role/?repage";
		}
//		if (Role.isAdmin(id)){
//			addMessage(redirectAttributes, "删除角色失败, 不允许内置角色或编号空");
////		}else if (UserUtils.getUser().getRoleIdList().contains(id)){
////			addMessage(redirectAttributes, "删除角色失败, 不能删除当前用户所在角色");
//		}else{
			systemService.deleteRole(role);
			addMessage(redirectAttributes, "删除角色成功");
//		}
		return "redirect:" + adminPath + "/sys/role/?repage";
	}
	
	/**
	 * 角色分配页面
	 * @param role
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "assign")
	public String assign(Role role, Model model) {
		List<User> userList = systemService.findUser(new User(new Role(role.getId())));
		model.addAttribute("userList", userList);
		return "modules/sys/roleAssign";
	}
	
	/**
	 * 角色分配 -- 打开角色分配对话框
	 * @param role
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "usertorole")
	public String selectUserToRole(Role role, Model model) {
		List<User> userList = systemService.findUser(new User(new Role(role.getId())));
		model.addAttribute("role", role);
		model.addAttribute("userList", userList);
		model.addAttribute("selectIds", Collections3.extractToString(userList, "name", ","));
		model.addAttribute("officeList", officeService.findAll());
		return "modules/sys/selectUserToRole";
	}
	
	/**
	 * 角色分配 -- 根据部门编号获取用户列表
	 * @param officeId
	 * @param response
	 * @return
	 */
	@RequiresPermissions("sys:role:view")
	@ResponseBody
	@RequestMapping(value = "users")
	public List<Map<String, Object>> users(String officeId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		User user = new User();
		user.setOffice(new Office(officeId));
		Page<User> page = systemService.findUser(new Page<User>(1, -1), user,true);
		List<String> ids = systemService.findUserHasSystemRole();
		//这里面有管理员权限的才能够显示出来
		
		for (User e : page.getList()) {
			if(!ids.contains(e.getId()))continue;
			
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", 0);
			map.put("name", e.getName());
			map.put("officename", e.getOffice().getName());
			mapList.add(map);			
		}
		return mapList;
	}
	
	@RequiresPermissions("sys:role:view")
	@ResponseBody
	@RequestMapping(value = "managers")
	public List<Map<String, Object>> managers(String officeId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("officeid",officeId);
		List<User> page = systemService.findExamer(param);
		for (User e : page) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", 0);
			map.put("name", e.getName());
			map.put("officename", e.getOffice().getName());
			mapList.add(map);			
		}
		return mapList;
	}
	
	/**
	 * 显示所有上传者信息,根据部门过滤
	 * 
	 */
	@RequiresPermissions("sys:role:view")
	@ResponseBody
	@RequestMapping(value = "uploaders")
	public List<Map<String, Object>> uploaders(String officeId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		User user=new User();
		user.setOffice(new Office(officeId));
		List<User> page=systemService.getUserList(user);
		for (User e : page) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", 0);
			map.put("name", e.getName());
			map.put("officename", e.getOffice().getName());
			mapList.add(map);			
		}
		return mapList;
	}
	
	
	/***
	 * 
	 * @param username
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "findcategory")
	public List<String> findcategory(@RequestParam(value="username")String username, HttpServletResponse response) {
		List<String> mapList = Lists.newArrayList();
		//返回id的集合.
		if(systemService.findOfficeNameByUserName(username)!=null){
			mapList=systemService.findOfficeNameByUserName(username);
		};
		return mapList;
	}
	
	@ResponseBody
	@RequestMapping(value = "getSearchNodes")
	public List<Map<String,Object>> getSearchNodes(HttpServletRequest request) {
		List<Map<String,Object>> mapList = Lists.newArrayList();
		List<User> userList=Lists.newArrayList();
		
		String name=request.getParameter("name");
		//拿到一组用户
		if(systemService.findUserBySearch(name)!=null){
			userList=systemService.findUserBySearch(name);
		};
		for (User e : userList) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", 0);
			map.put("name", e.getName());
			map.put("officename", e.getOffice().getName());
			mapList.add(map);			
		}
		return mapList;
	}
	/**
	 * 角色分配 -- 从角色中移除用户
	 * @param userId
	 * @param roleId
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "outrole")
	public String outrole(String userId, String roleId, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/role/assign?id="+roleId;
		}
		Role role = systemService.getRole(roleId);
		User user = systemService.getUser(userId);
		if (UserUtils.getUser().getId().equals(userId)) {
			addMessage(redirectAttributes, "无法从角色【" + role.getName() + "】中移除用户【" + user.getName() + "】自己！");
		}else {
			if (user.getRoleList().size() <= 1){
				addMessage(redirectAttributes, "用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除失败！这已经是该用户的唯一角色，不能移除。");
			}else{
				Boolean flag = systemService.outUserInRole(role, user);
				if (!flag) {
					addMessage(redirectAttributes, "用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除失败！");
				}else {
					addMessage(redirectAttributes, "用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除成功！");
				}
			}		
		}
		return "redirect:" + adminPath + "/sys/role/assign?id="+role.getId();
	}
	
	/**
	 * 角色分配
	 * @param role
	 * @param idsArr
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "assignrole")
	public String assignRole(Role role, String[] idsArr, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/role/assign?id="+role.getId();
		}
		StringBuilder msg = new StringBuilder();
		int newNum = 0;
		for (int i = 0; i < idsArr.length; i++) {
			User user = systemService.assignUserToRole(role, systemService.getUser(idsArr[i]));
			if (null != user) {
				msg.append("<br/>新增用户【" + user.getName() + "】到角色【" + role.getName() + "】！");
				newNum++;
			}
		}
		addMessage(redirectAttributes, "已成功分配 "+newNum+" 个用户"+msg);
		return "redirect:" + adminPath + "/sys/role/assign?id="+role.getId();
	}

	/**
	 * 验证角色名是否有效
	 * @param oldName
	 * @param name
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "checkName")
	public String checkName(String oldName, String name) {
		if (name!=null && name.equals(oldName)) {
			return "true";
		} else if (name!=null && systemService.getRoleByName(name) == null) {
			return "true";
		}
		return "false";
	}

	/**
	 * 验证角色英文名是否有效
	 * @param oldName
	 * @param name
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "checkEnname")
	public String checkEnname(String oldEnname, String enname) {
		if (enname!=null && enname.equals(oldEnname)) {
			return "true";
		} else if (enname!=null && systemService.getRoleByEnname(enname) == null) {
			return "true";
		}
		return "false";
	}

}
