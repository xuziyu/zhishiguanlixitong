/**
 * 
 */
package com.yonyou.kms.modules.sys.web;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yonyou.kms.common.config.Global;
import com.yonyou.kms.common.utils.Collections3;
import com.yonyou.kms.common.web.BaseController;
import com.yonyou.kms.modules.sys.entity.Audit;
import com.yonyou.kms.modules.sys.entity.Office;
import com.yonyou.kms.modules.sys.entity.User;
import com.yonyou.kms.modules.sys.service.OfficeService;
import com.yonyou.kms.modules.sys.service.SystemService;
import com.yonyou.kms.modules.sys.utils.DictUtils;
import com.yonyou.kms.modules.sys.utils.UserUtils;

/**
 * 机构Controller
 * @author hotsum
 * @version 2013-5-15
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/office")
public class OfficeController extends BaseController {

	@Autowired
	private OfficeService officeService;
	@Autowired
	private SystemService systemService; 
	
	
	@ModelAttribute("office")
	public Office get(@RequestParam(required=false) String id) {
		if (org.apache.commons.lang3.StringUtils.isNotBlank(id)){
			return officeService.get(id);
		}else{
			return new Office();
		}
	}

	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = {""})
	public String index(Office office, Model model) {
		List<Office> offices =officeService.findAll();
		wrapIfNecessary(offices);
		model.addAttribute("list", offices);
		return "modules/sys/officeIndex";
	}

	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = {"list"})
	public String list(Office office, Model model) {
		if(office==null){
			return "redirect:${adminPath}/sys/office";
		}
		List<Office> offices = officeService.findList(office);
		wrapIfNecessary(offices);
		model.addAttribute("list", offices);
		return "modules/sys/officeList";
	}
	/**
	 * 添加一个其他的属性  需要经过计算的
	 * @param offices
	 */
	protected void wrapIfNecessary(List<Office> offices){
		List<Audit> audits = officeService.findAllAudit();
		//key = 机构id  value=知识审核员
		Map<String,String> map2office=new HashMap<String, String>();
		Map<Serializable, User> mapusers = mapUser(systemService.finalAllUsers());
		if(audits!=null&&offices.size()>0){
			for(Audit a:audits){
				if(map2office.containsKey(a.getOfficeId())){
					map2office.put(a.getOfficeId(), map2office.get(a.getOfficeId())+","+mapusers.get(a.getUserId()).getName());
				}else{
					map2office.put(a.getOfficeId(),mapusers.get(a.getUserId()).getName());
				}
			}
		}
		
		//2  将属性设置到offices
		if(offices!=null&&offices.size()>0){
			for(Office o:offices){
				o.setAudits(map2office.get(o.getId()));
			}
		}
		
	}
	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = "form")
	public String form(Office office, Model model) {
//		System.out.println("aaa");
		User user = UserUtils.getUser();
		if (office.getParent()==null || office.getParent().getId()==null){
			office.setParent(user.getOffice());
		}
		office.setParent(officeService.get(office.getParent().getId()));
		if (office.getArea()==null){
			office.setArea(user.getOffice().getArea());
		}
		// 自动获取排序号
		if (org.apache.commons.lang3.StringUtils.isBlank(office.getId())&&office.getParent()!=null){
			int size = 0;
			List<Office> list = officeService.findAll();
			for (int i=0; i<list.size(); i++){
				Office e = list.get(i);
				if (e.getParent()!=null && e.getParent().getId()!=null
						&& e.getParent().getId().equals(office.getParent().getId())){
					size++;
				}
			}
			office.setCode(office.getParent().getCode() + org.apache.commons.lang3.StringUtils.leftPad(String.valueOf(size > 0 ? size+1 : 1), 3, "0"));
		}
		model.addAttribute("office", office);
		return "modules/sys/officeForm";
	}
	
	@RequiresPermissions("sys:office:edit")
	@RequestMapping(value = "save")
	public String save(Office office, Model model, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/office/";
		}
		if (!beanValidator(model, office)){
			return form(office, model);
		}
		officeService.save(office);
		
		if(office.getChildDeptList()!=null){
			Office childOffice = null;
			for(String id : office.getChildDeptList()){
				childOffice = new Office();
				childOffice.setName(DictUtils.getDictLabel(id, "sys_office_common", "未知"));
				childOffice.setParent(office);
				childOffice.setArea(office.getArea());
				childOffice.setType("2");
				childOffice.setGrade(String.valueOf(Integer.valueOf(office.getGrade())+1));
				childOffice.setUseable(Global.YES);
				officeService.save(childOffice);
			}
		}
		
		addMessage(redirectAttributes, "保存机构'" + office.getName() + "'成功");
		String id = "0".equals(office.getParentId()) ? "" : office.getParentId();
		return "redirect:" + adminPath + "/sys/office/list?id="+id+"&parentIds="+office.getParentIds();
	}
	
	@RequiresPermissions("sys:office:edit")
	@RequestMapping(value = "delete")
	public String delete(Office office, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/office/list";
		}
		if(StringUtils.isNoneBlank(office.getId())&&office.getId().length()<=20){
			addMessage(redirectAttributes, "不允许删除主系统数据!!!");
			return "redirect:" + adminPath + "/sys/office/list?id=&parentIds=";
		}
		
//		if (Office.isRoot(id)){
//			addMessage(redirectAttributes, "删除机构失败, 不允许删除顶级机构或编号空");
//		}else{
			officeService.delete(office);
			addMessage(redirectAttributes, "删除机构成功");
//		}
		return "redirect:" + adminPath + "/sys/office/list?id="+office.getParentId()+"&parentIds="+office.getParentIds();
	}

	/**
	 * 获取机构JSON数据。
	 * @param extId 排除的ID
	 * @param type	类型（1：公司；2：部门/小组/其它：3：用户）
	 * @param grade 显示级别
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, @RequestParam(required=false) String type,
			@RequestParam(required=false) Long grade, @RequestParam(required=false) Boolean isAll, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Office> list = officeService.findList(isAll);
		for (int i=0; i<list.size(); i++){
			Office e = list.get(i);
			if ((org.apache.commons.lang3.StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1))
					&& (type == null || (type != null && (type.equals("1") ? type.equals(e.getType()) : true)))
					&& (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))
					&& Global.YES.equals(e.getUseable())){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("pIds", e.getParentIds());
				map.put("name", e.getName());
				if (type != null && "3".equals(type)){
					map.put("isParent", true);
				}
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	/**
	 * 
	 * @param office   部门
	 * 给部门分配知识审核员
	 * 
	 */
	@RequestMapping(value = "user2office")
	public String selectUserToOffice(Office office, Model model) {
		//1  获取部门的知识审核员
		List<User> userList=officeService.findAllAudit(office.getId());
		model.addAttribute("office", office);
		model.addAttribute("userList", userList);
		model.addAttribute("selectIds", Collections3.extractToString(userList, "id", ","));
		model.addAttribute("officeList", officeService.findAll());
		return "modules/sys/selectUser2Office";
	}
	/**
	 * 对部门分配知识审核员
	 * @param ids
	 * @param officeId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="officeAssign",method=RequestMethod.POST)
	@ResponseBody
	public List<User> officeAssign(@RequestParam("ids")String ids,@RequestParam("officeId")String officeId){
		try {
			//1  分配一批知识审核员
			officeService.assignUser2Office(ids,officeId);
			
			/*//①  映射
			Map<Serializable, User> mapusers = mapUser(systemService.finalAllUsers());
			//② 设置值
			List<User> users=Lists.newArrayList();
			if(StringUtils.isNoneBlank(ids)&&mapusers.size()>0){
				for(String userId:ids.split(",")){
					users.add(mapusers.get(userId));
				}
			}
			return users;*/
			
			//2 查询一批用户
			List<User> userList=officeService.findAllAudit(officeId);
			return userList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return Lists.newArrayList();
	}
	/**
	 * 将List映射成Map
	 * @param users
	 * @return
	 */
	private Map<Serializable,User> mapUser(List<User> users){
		Map<Serializable,User> maps=new HashMap<Serializable, User>();
		if(users!=null&&users.size()>0){
			for(User user:users){
				maps.put(user.getId(), user);
			}
		}
		return maps;
	}
	/**
	 * 查询已经分配了的知识审核员
	 * @param officeId
	 * @return
	 */
	@RequestMapping(value="officeAssigned",method=RequestMethod.GET)
	@ResponseBody
	public List<User> officeAssigned(@RequestParam("officeId")String officeId){
		List<User> userList=officeService.findAllAudit(officeId);
		return userList;
	}
}
