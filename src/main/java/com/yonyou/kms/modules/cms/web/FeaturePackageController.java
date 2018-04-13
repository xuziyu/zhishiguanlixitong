package com.yonyou.kms.modules.cms.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yonyou.kms.common.persistence.Page;
import com.yonyou.kms.common.utils.Collections3;
import com.yonyou.kms.common.web.BaseController;
import com.yonyou.kms.modules.cms.entity.Article;
import com.yonyou.kms.modules.cms.entity.Category;
import com.yonyou.kms.modules.cms.entity.FeaturePackage;
import com.yonyou.kms.modules.cms.entity.Site;
import com.yonyou.kms.modules.cms.service.ArticleService;
import com.yonyou.kms.modules.cms.service.CategoryService;
import com.yonyou.kms.modules.cms.service.FeaturePackageService;
import com.yonyou.kms.modules.sys.entity.User;
import com.yonyou.kms.modules.sys.service.OfficeService;
import com.yonyou.kms.modules.sys.service.SystemService;
import com.yonyou.kms.modules.sys.utils.UserUtils;

@RequestMapping(value = "${adminPath}/cms/featurePackage")
@Controller
public class FeaturePackageController extends BaseController{
	
	@Autowired
	private FeaturePackageService featurePackageService;
	
	@Autowired
	private ArticleService articleService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private SystemService systemService;
	
	/**
	 * 执行目标方法前先执行该方法 
	 * @param id
	 * @return
	 */
	@ModelAttribute
	public FeaturePackage get(@RequestParam(required=false) String id) {
		if (org.apache.commons.lang3.StringUtils.isNotBlank(id)){
			return featurePackageService.get(id);
		}else{
			return new FeaturePackage();
		}
	}
	
	/**
	 * 分页显示专题包列表数据 
	 * @param featurePackage
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"list", ""})
	public String list(FeaturePackage featurePackage, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<FeaturePackage> page = featurePackageService.findPage(new Page<FeaturePackage>(request, response), featurePackage); 
        model.addAttribute("page", page);
		return "modules/cms/featurePackageList";
	}
	
	
	/**
	 * 进入新增修改页面
	 * @param featurePackage
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "form")
	public String form(FeaturePackage featurePackage, Model model) {
		if(null==featurePackage.getId()){
			model.addAttribute("isNew", true);
			featurePackage.setSort(Integer.valueOf(featurePackageService.getMaxSort()) + Integer.valueOf(1));
		}
		model.addAttribute("featurePackage", featurePackage);
		return "modules/cms/featurePackageForm";
	}
	
	/**
	 * 验证专题包名称是否已存在
	 * @param oldName
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkName")
	public String checkName(String oldName, String name) {
		if (name!=null && name.equals(oldName)) {
			return "true";
		} else if (name!=null && featurePackageService.getFeaturePackageByName(name) == null) {
			return "true";
		}
		return "false";
	}
	
	/**
	 * 保存新增/修改
	 * @param role
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(FeaturePackage featurePackage, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, featurePackage)){
			return form(featurePackage, model);
		}
		/*if (!"true".equals(checkName(featurePackage.getOldName(), role.getName()))){
			addMessage(model, "保存角色'" + featurePackage.getName() + "'失败, 角色名已存在");
			return form(featurePackage, model);
		}*/
		featurePackageService.saveFeaturePackage(featurePackage,"1");
		addMessage(redirectAttributes, "保存专题包'" + featurePackage.getName() + "'成功");
		return "redirect:" + adminPath + "/cms/featurePackage";
	}
	
	
	@RequestMapping(value = "delete")
	public String delete(FeaturePackage featurePackage, RedirectAttributes redirectAttributes) {
//		featurePackageService.deleteFeaturePackage(featurePackage);//删除专题包及专题包与知识的关系
		//如果专题下有知识，提示不能删除，要先移除专题包下的知识后才能删除专题
		Article article = new Article();
        article.setCategory(new Category());
        article.setFeaturePackage(featurePackage);
		List<Article> articleList = featurePackageService.findArticlesFromFeaturePackage(article);
		 if(articleList.size()>0){
			addMessage(redirectAttributes, "该专题包下有已添加的知识，不能删除！");
		}else{
			featurePackageService.deleteFeaturePackage(featurePackage);//删除专题包及专题包与知识的关系
			addMessage(redirectAttributes, "删除专题包成功");
		}
		
		return "redirect:" + adminPath + "/cms/featurePackage";
	}
	
	@RequestMapping(value = "initcomment")
	public String page(@ModelAttribute("featurePackage")FeaturePackage featurePackage,@RequestParam(value="flag",defaultValue="",required=false) String flag, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<FeaturePackage> page = featurePackageService.findInitFeaturePackage(new Page<FeaturePackage>(request, response), featurePackage);
        model.addAttribute("page", page);
        String url = "";
        if(flag.equals("moreFeaturePackage")){
        	url = "modules/cms/moreFeaturePackageList";
        }else{
        	url = "modules/cms/featurePackageList";
        }
        return url;
	}
	
	/**
	 * 进入添加知识页面
	 */
	@RequestMapping(value = "addArticle")
	public String addArticle(FeaturePackage featurePackage,@RequestParam(value="flag",defaultValue="",required=false) String flag,HttpServletRequest request, HttpServletResponse response, Model model) {
		//获取专题下已添加的知识
        Page<Article> page = new Page<Article>(request, response);
        Article article = new Article();
        article.setCategory(new Category());
        article.setFeaturePackage(featurePackage);
        article.setPage(page);
		List<Article> articleList = featurePackageService.findArticlesFromFeaturePackage(article);
		model.addAttribute("articleList", articleList);
		model.addAttribute("page", page);
		page.setList(articleList);
		String url = "";
		if("userCenterFeaturePackage".equals(flag)||"userCenterFeaturePackage".equals(request.getParameter("flag"))){
			url = "modules/sys/addArticle";
		}else{
			url = "modules/cms/addArticle";
		}
		return url;
	}
	
	
	/**
	 * 添加知识 保存操作
	 * @param role
	 * @param idsArr
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "addArticleSave",method=RequestMethod.POST)
	public String addArticleSave(FeaturePackage featurePackage, String[] idsArr, @RequestParam(value="flag",defaultValue="",required=false) String flag,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes) {
		StringBuilder msg = new StringBuilder();
		int newNum = 0;
		for (int i = 0; i < idsArr.length; i++) {
			Article article = featurePackageService.addArticleToFeaturePackage(featurePackage,articleService.get(idsArr[i]));
//			Article article = systemService.assignUserToRole(featurePackage, systemService.getUser(idsArr[i]));
			if (null != article) {
				msg.append("<br/>新增知识【" + article.getTitle() + "】到专题【" + featurePackage.getName() + "】！");
				newNum++;
			}
		}
		addMessage(redirectAttributes, "已成功添加 "+newNum+" 个知识"+msg);
		if("userCenterFeaturePackage".equals(flag)||"userCenterFeaturePackage".equals(request.getParameter("flag"))){
			return "redirect:" + adminPath + "/cms/featurePackage/addArticle?id="+featurePackage.getId()+"&flag=userCenterFeaturePackage";
		}else{
			return "redirect:" + adminPath + "/cms/featurePackage/addArticle?id="+featurePackage.getId();
		}
		
	}
	
	@RequestMapping(value = "addArticleManage")
	public String addArticleManage(String managers,HttpServletRequest request, HttpServletResponse response,Model model,RedirectAttributes redirectAttributes){
		
		List<Article> articleList = featurePackageService.findAllArticle(new Article());
		
		//界面上不显示出来  默认就有管理权限
		//manager.remove(UserUtils.getUser());
		model.addAttribute("articleList",articleList);
		return "modules/cms/addArticleManage";
	}
	
	
	/**
	 * 添加知识 -- 打开添加知识对话框
	 * @param featurePackage
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "articletofeaturepackage")
	public String selectArticleToFeaturePackage(FeaturePackage featurePackage, @RequestParam(value="flag",defaultValue="",required=false) String flag,Model model) {
		Article article = new Article();
		article.setCategory(new Category());
		article.setFeaturePackage(featurePackage);
		//打开添加知识对话框的时候先将该专题包下的知识列表查询出来 
		List<Article> articleList = featurePackageService.findArticlesFromFeaturePackage(article);
		model.addAttribute("featurePackage", featurePackage);
		model.addAttribute("articleList", articleList);
		model.addAttribute("selectIds", Collections3.extractToString(articleList, "title", ","));
		if(flag.equals("userCenterFeaturePackage")){
			return "modules/sys/selectArticleToFeaturePackage";
		}else{
			return "modules/cms/selectArticleToFeaturePackage";
		}
	}
	
	/**
	 * 该方法用于处理添加知识弹出框中的搜索
	 * 
	 * 比如用户输入两个关键字，中间用逗号(英文状态下)：NC,审批
	 * 首先使用逗号将"NC,审批"拆分为数组[NC,审批]
	 * 
	 * 然后从当前用户具有权限的知识中，查询	 * 
	 * label同时包含'NC'、'审批'关键字或者title中同时包含'NC'、'审批'关键字的知识
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getSearchNodes")
	public List<Map<String,Object>> getSearchNodes(HttpServletRequest request) {
		List<Map<String,Object>> mapList = Lists.newArrayList();
		List<Article> articleList=Lists.newArrayList();
		
		String searchParam = request.getParameter("searchParam");
		List<String> paramArr = null;
		if(searchParam!=null && !"".equals(searchParam)){
			paramArr = splitSearchParam(searchParam);
		}
		//获取当前用户下的所有知识分类id
		List<String> categoryidlist = null;//categoryService.findCategoryIdByUser(null);
			
		boolean isAdmin=systemService.findSysUserByRole(UserUtils.getUser());
		
		if(isAdmin){
			Category category=new Category();
			category.setDelFlag(Category.DEL_FLAG_NORMAL);
			category.setParent(category);
			Site site=new Site();
			category.setSite(site);
			
			try {
				categoryidlist=warpEntitys2Ids(categoryService.findList(category));
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
		}
			
		else
			categoryidlist=categoryService.findCategoryIdByUser(null);

		
		categoryidlist = new ArrayList<String>(new HashSet<String>(categoryidlist));//list去重 categoryidlist是当前用户具有权限的所有知识分类的id集合
		/*String ids = "";
		for(String str : categoryidlist){
			ids = "'" + str + "',";
		}System.out.println(categoryidlist.toString());*/
		Map<String,Object> mapParams = new HashMap<String,Object>();
		mapParams.put("categoryids", categoryidlist);
		mapParams.put("paramArr", paramArr);
		//通过知识标题和知识标签模糊查询符合要求的知识
		try{
			
			articleList = articleService.addArticleSearch(mapParams);//查询标签包含所有关键字的知识
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
//		articleList = articleService.search("", searchParam, "",UserUtils.getUser().getId(),"mark");//获取的是全文检索包含用户输入的搜索关键字的知识
//		
//		List<Article> list = Lists.newArrayList();//用来保存标题或标签中包含用户输入的搜索关键字的知识
		/*
		for(Article e : articleList){
			if(e.getTitle().contains(searchParam)){
				list.add(e);
			}
		}*/
		
		for (Article e : articleList) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("name", e.getTitle());
			map.put("title", e.getTitle());
			map.put("categoryId",e.getCategory().getId());
			mapList.add(map);			
		}
		return mapList;
	}
	
	
	/**
	 * 
	 * 
	 * 该方法用于处理添加知识弹出框中的搜索
	 * 
	 * 比如用户输入两个关键字，中间用逗号(英文状态下)：NC,审批
	 * 首先使用逗号将"NC,审批"拆分为数组[NC,审批]
	 * 
	 * 然后从当前用户具有权限的知识中，查询	 * 
	 * label同时包含'NC'、'审批'任意一个关键字或者title中同时包含'NC'、'审批'任意一个关键字的知识
	 * 
	 * 此处需求已经变动多次
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getSearchNodes2")
	public List<Map<String,Object>> getSearchNodes2(HttpServletRequest request) {
		List<Map<String,Object>> mapList = Lists.newArrayList();
		List<Article> articleList=Lists.newArrayList();
		String searchParam = request.getParameter("searchParam");
		articleList = articleService.search("", searchParam, "",UserUtils.getUser().getId(),"mark");//获取的是全文检索包含用户输入的搜索关键字的知识
		List<Article> list = Lists.newArrayList();//用来保存标题或标签中包含用户输入的搜索关键字的知识
		
		for(Article e : articleList){
			if(e.getTitle().contains(searchParam)){
				list.add(e);
			}
		}
				
		for (Article e : articleList) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("name", e.getTitle());
			map.put("title", e.getTitle());
			map.put("categoryId",e.getCategory().getId());
			mapList.add(map);			
		}
		return mapList;
	}
	
	private List<String> warpEntitys2Ids(List<Category> categorys){
		
		if(categorys==null||categorys.size()<=0)return null;
		
		List<String> ids=Lists.newArrayList();
		
		for(Category category:categorys){
			ids.add(category.getId());
		}
		return ids;
	}
	//处理搜索参数
	private List<String> splitSearchParam(String searchParam) {
		String[] paramArr = searchParam.split(" ");
		List<String> paramList = new ArrayList<String>();
		for(String arr : paramArr){
			if(arr!=null&&!"".equals(arr)){
				paramList.add(arr);
			}
		}
		return paramList;
	}

	/**
	 * 添加知识 保存操作
	 * @param role
	 * @param idsArr
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "deleteArtileFeaturePackage")
	public String deleteArtileFeaturePackage(@RequestParam(value="featuerPackageId",required=false) String featuerPackageId,@RequestParam(value="aritcleId",required=false) String aritcleId,  @RequestParam(value="flag",defaultValue="",required=false) String flag,RedirectAttributes redirectAttributes) {
		featurePackageService.deleteArtileFeaturePackage(featuerPackageId,aritcleId);
		if(flag.equals("userCenterFeaturePackage")){
			return "redirect:" + adminPath + "/cms/featurePackage/addArticle?id=" + featuerPackageId+"&flag=userCenterFeaturePackage";
		}else{
			return "redirect:" + adminPath + "/cms/featurePackage/addArticle?id=" + featuerPackageId;
		}
	}
	
	/**
	 * 批量删除操作 删除知识和专题包关联关系
	 * @param resq
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "delBatch")
	public String delBatch(@RequestParam(value="featuerPackageId",required=false) String featuerPackageId,@RequestParam(value="ids",required=false) String ids, Model model) {
		try{
			featurePackageService.delBatch(ids,featuerPackageId);
			return "success";
		}catch(Exception e){
			e.printStackTrace();
			return "fail";
		}
//		return "redirect:" + adminPath + "/cms/featurePackage/addArticle?id=" + featuerPackageId;
		
	}
	
	@RequestMapping(value = "assignuser",method=RequestMethod.GET)
	public String featurePackageUserAssign(@RequestParam(value="id") String id,Model model) {
		FeaturePackage featurePackage = featurePackageService.get(id);
		List<User> userList = featurePackageService.findAllUserFromFeaturePackageID(id);
		model.addAttribute("featurePackage", featurePackage);
		model.addAttribute("userList", userList);
		return "modules/cms/featurePackageUserAssign";
	}
	
	/**
	 * 用户分配 -- 打开用户分配对话框
	 * @param role
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "usertorole")
	public String selectUserToRole(String id, Model model) {
		List<User> userList = featurePackageService.findAllUserFromFeaturePackageID(id);
		FeaturePackage role = featurePackageService.get(id);
		model.addAttribute("role", role);
		model.addAttribute("userList", userList);
		model.addAttribute("selectIds", Collections3.extractToString(userList, "name", ","));
//		ArrayList<Office> offices = Lists.newArrayList();
//		for(User user :userList){
//			offices.add(user.getOffice());
//		}
		model.addAttribute("officeList", officeService.findAll());
		return "modules/cms/selectUserToFeaturePackage";
	}
	
	
	
	
	/**
	 * 用户分配
	 * @param role
	 * @param idsArr
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "assignrole")
	public String assignRole(@RequestParam(value="id") String id, String[] idsArr, RedirectAttributes redirectAttributes) {
		StringBuilder msg = new StringBuilder();
		int newNum = 0;
		for (int i = 0; i < idsArr.length; i++) {
			featurePackageService.assignusertofeaturepackage(idsArr[i],id);
			newNum++;
		}
		addMessage(redirectAttributes, "已成功分配 "+newNum+" 个用户"+msg);
		return "redirect:" + adminPath + "/cms/featurePackage/assignuser?id="+id;
	}
	
	
	/**
	 * 取消用户分配
	 * @param role
	 * @param idsArr
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "unassign")
	public String unassign(@RequestParam(value="userId") String userId,@RequestParam(value="featurePackageId") String featurePackageId,RedirectAttributes redirectAttributes) {
			User user = UserUtils.get(userId);
			FeaturePackage featurePackage = featurePackageService.get(featurePackageId);
		try{
			featurePackageService.unassignuser(userId, featurePackageId);
			addMessage(redirectAttributes, "用户【" + user.getName() + "】从专题包【" + featurePackage.getName() + "】中移除成功！");
		}catch(Exception e){
			addMessage(redirectAttributes, "用户【" + user.getName() + "】从专题包【" + featurePackage.getName() + "】中移除失败！");
		}
		return "redirect:" + adminPath + "/cms/featurePackage/assignuser?id="+featurePackage.getId();
	}
	
	
	/**
	 * 
	 * 点击更多时显示所有专题
	 */
	@RequestMapping(value = "moreFeaturePackage")
	public String moreFeaturePackage(FeaturePackage featurePackage, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<FeaturePackage> page = new Page<FeaturePackage>(request, response);
		//List<FeaturePackage> featurePackageList = new ArrayList<FeaturePackage>();
		featurePackage.setPage(page);
		/*
		 * 
		 获取当前用户已被授权的所有专题
		featurePackage.setUser(UserUtils.getUser());
		featurePackageList = featurePackageService.findFeaturePackageByUserId(featurePackage);
		model.addAttribute("featurePackageList", featurePackageList);
		model.addAttribute("page", page);
		page.setList(featurePackageList);
		*/
		//获取所有专题
	    page = featurePackageService.findPage(new Page<FeaturePackage>(request, response), featurePackage);
	    featurePackageService.setCount(page.getList());
//		model.addAttribute("featurePackageList", featurePackageList);
		model.addAttribute("page", page);
		return "modules/cms/moreFeaturePackageList";
	} 
	
	/**
	 * 获取专题包列表
	 * @param featurePackages
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "addFeaturePackage")
	public String addFeaturePackage(FeaturePackage featurePackage,@RequestParam(value="string",required=false)String string,HttpServletRequest request, HttpServletResponse response,Model model,RedirectAttributes redirectAttributes){
		List<FeaturePackage> featurePackageList = new ArrayList<FeaturePackage>();
		//应该获取当前用户下的专题包列表，现在暂时获取所有专题包
		boolean isAdmin=systemService.findSysUserByRole(UserUtils.getUser());
		if(isAdmin){
			featurePackageList=featurePackageService.findList(new FeaturePackage());
		}else{
			List<String> ids = featurePackageService.getFeaturePackageByUser(UserUtils.getUser().getId());
			if(ids.size()>0){
				featurePackage.setIds(ids);
				featurePackageList = featurePackageService.findList(featurePackage);
			}
		}
		
		//list:在添加知识页面已经添加的专题id
		if(string !=null &&  !string.equals("")){
			List<String> list=new ArrayList<String>();
			Collections.addAll(list, string.split(","));
			for(int i=0;i<featurePackageList.size();i++){
				String fid=featurePackageList.get(i).getId();
				for(int j=0;j<list.size();j++){
					String lid=list.get(j);
					if(fid.equals(lid)){
						featurePackageList.get(i).setIsChecked("1");
						list.remove(j);
						break;
					}
				}
			}
		}
		model.addAttribute("featurePackageList",featurePackageList);
		return "modules/cms/addFeaturePackage";
	}
	
	//个人中心我的专题 专题包搜索
	@RequestMapping(value = "featurePackageSearch")
	public String featurePackageSearch(@ModelAttribute("featurePackage")FeaturePackage featurePackage,@RequestParam(value="flag",defaultValue="",required=false) String flag, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<FeaturePackage> page = featurePackageService.findInitFeaturePackage(new Page<FeaturePackage>(request, response), featurePackage);
        model.addAttribute("page", page);
        String url = "";
        if(flag.equals("moreFeaturePackage")){
        	url = "modules/cms/moreFeaturePackageList";
        }else{
        	url = "modules/cms/featurePackageList";
        }
        return url;
	}
}
