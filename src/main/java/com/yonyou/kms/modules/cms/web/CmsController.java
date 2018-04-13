/**
 * 
 */
package com.yonyou.kms.modules.cms.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yonyou.kms.common.service.BaseService;
import com.yonyou.kms.common.web.BaseController;
import com.yonyou.kms.modules.cms.entity.Category;
import com.yonyou.kms.modules.cms.entity.Label;
import com.yonyou.kms.modules.cms.entity.Switch;
import com.yonyou.kms.modules.cms.service.CategoryService;
import com.yonyou.kms.modules.cms.service.LabelService;
import com.yonyou.kms.modules.cms.service.SwitchService;
import com.yonyou.kms.modules.oa.service.OaNotifyService;
import com.yonyou.kms.modules.sys.service.SystemService;
import com.yonyou.kms.modules.sys.utils.UserUtils;


/**
 * 内容管理Controller
 * @author hotsum
 * @version 2013-4-21
 */
@Controller
@RequestMapping(value = "${adminPath}/cms")
public class CmsController extends BaseController {

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private LabelService labelservice;
	@Autowired
	private OaNotifyService oaNotifyService;
	@Autowired
	private SwitchService switchService;
	@Autowired
	private SystemService systemService; 
	
	/*
	 * 这段代码移步ArticleController->171
	 * huangmj 2015.10.23,首页发布知识
	
	@RequiresPermissions("cms:view")
	@RequestMapping(value = "add_article")
	public String index1(Article article, Model model) {
		// 如果当前传参有子节点，则选择取消传参选择
		if (article.getCategory()!=null && org.apache.commons.lang3.StringUtils.isNotBlank(article.getCategory().getId())){
			List<Category> list = categoryService.findByParentId(article.getCategory().getId(), Site.getCurrentSiteId());
			if (list.size() > 0){
				article.setCategory(null);
			}else{
				article.setCategory(categoryService.get(article.getCategory().getId()));
			}
		}

        model.addAttribute("article_DEFAULT_TEMPLATE",Article.DEFAULT_TEMPLATE);
		model.addAttribute("article", article);
		//model.addAttribute("listArticleAttFile",listArticleAttFile);
		model.addAttribute("native", "1");
		model.addAttribute("realize", null);
		CmsUtils.addViewConfigAttribute(model, article.getCategory());
		
		return "modules/cms/articleForm";
	}
	 */
	
	/*
	 * 后台知识管理，huangmj 2015.10.23
	 */
	@RequiresPermissions("cms:view")
	@RequestMapping(value = "")
	public String index(HttpServletRequest request ,HttpServletResponse response) {

		return "modules/cms/cmsIndex";
	}
	
	//add by luqibao 
	@RequiresPermissions("cms:view")
	@RequestMapping(value = "tree")
	public String tree(Model model) {
	//需要进行变更   变成审核管理员具有的权限的分类列表
		List<Category> list = categoryService.findByUser(true, null,BaseService.CATEGORY_PLACE_SYS);
//		if(systemService.findSysUserByRole(UserUtils.getUser())){
//			List<Category> calist=categoryService.getAppCategory();
//			if(calist !=null && calist.size()!=0)
//				list.addAll(calist);
//		}
		if(list==null||list.size()==0){
			
			model.addAttribute("Message","1" );
			return "error/404";
		}
		
		model.addAttribute("categoryList",list);
	/*
	 * 三期需求更改  审核管理员的功能修改
	 * @author  HOTUSM
	 * @date   2016/09/12
	 * 
	 * */	
		
//		List<Category> categorysNeedAudit =null;
//		//但是  如果是系统管理员的话  那么还是走以前的逻辑
//		User currentUser = UserUtils.getUser();
//		if(currentUser!=null){
//			List<Role> roleList = currentUser.getRoleList();
//			boolean isAdmin=false;
//			for(Role role:roleList){
//				if(role.getName().equals("系统管理员")){
//					isAdmin=true;
//				}else{
//					categorysNeedAudit = categoryService.findCategoryCanAudit(UserUtils.getUser());
//				}
//			}
//			
//			if(isAdmin){
//				categorysNeedAudit=categoryService.findByUser(true, null,BaseService.CATEGORY_PLACE_SYS);
//			}
//		}
//		model.addAttribute("categoryList",categorysNeedAudit);
		/*
		 * end 三期需求更改  审核管理员的功能修改
		 * */
		
		/**
		 * 专家组角色需要具有权
		 */
		return "modules/cms/cmsTree";
	}
	
	@RequiresPermissions("cms:view")
	@RequestMapping(value = "none")
	public String none() {
		return "modules/cms/cmsNone";
	}
	//add hefeng
	@RequiresPermissions("cms:view")
	@RequestMapping(value = "index2")
	public String index2(HttpServletRequest request ,HttpServletResponse response) {
		return "modules/cms/cmsIndex2";
	}
	
	//add by luqibao
	@RequiresPermissions("cms:view")
	@RequestMapping(value = "tree2")
	public String tree2(Model model) {
		model.addAttribute("categoryList", categoryService.findByUser(true, null,BaseService.CATEGORY_PLACE_SYS));
		return "modules/cms/cmsTree2";
	}
	
	@RequiresPermissions("cms:view")
	@RequestMapping(value = "none2")
	public String none2() {
		return "modules/cms/cmsNone2";
	}
	//end
	//add by luqibao
	@RequestMapping(value="f")
	public String frontIndex(RedirectAttributes ra,HttpServletRequest request){
//		UserUtils.putCache(IS_LOGIN, "1");
		//HttpSession session = request.getSession(false);
		//System.out.println("CmsController:SESSION:"+session);
		//System.out.println("跳转到首页中:"+UserUtils.getUser());
		return "redirect:/f";
	}
	@RequestMapping("classifyList")
	public String classifyList(){
		return "modules/cms/cmsClassifyList";
	}
	
	/**
	 * 
	 * 标签管理的controller
	 * 
	 */
	@RequestMapping(value="tagControl")
	public String tagControl(Label label,Model model,HttpServletRequest request, HttpServletResponse response){
		//add by yangshw6
		//System.out.println("---------tagControl------------");
//		List<Label> LabelData=new ArrayList<Label>();
//		LabelData=labelservice.getLabelCountData();
//		for(int i=0;i<LabelData.size();i++){
//			System.out.println("用户标签为:"+LabelData.get(i).getId()+"--"+LabelData.get(i).getLabelvalue()+"--"+LabelData.get(i).getLabelcontent());
//		}
//		model.addAttribute("labeldata",LabelData);
//		if(label==null)
//			label=new Label();
//		Page<Label> page = labelservice.findPage(new Page<Label>(request, response), label); 
//        model.addAttribute("page", page);
//		model.addAttribute("tagflag","1");
//		//end by yangshw6
//		return "modules/sys/tagControl";
		if(label.getId()!=null){
			Label la=labelservice.get(label);
			model.addAttribute("label",la);
			List<Label> list=new ArrayList<Label>();
			list=labelservice.findRelationLabel(label.getId());
			model.addAttribute("labellist",list);
			return "modules/sys/NewTagControl";
		}
		model.addAttribute("label",new Label());
		return "modules/sys/NewTagControl";
	}
	@RequestMapping(value="TagIndex")
	public String cmsIndex(Model model,HttpServletRequest request, HttpServletResponse response){
		return "modules/sys/TagIndex";
	}
	@RequestMapping(value="tagtree")
	public String tagtree(Model model,HttpServletRequest request, HttpServletResponse response){
		model.addAttribute("url","/cms/TagtreeData");
		model.addAttribute("checked","checked");
		model.addAttribute("module","article");
		return "modules/sys/TagTree";
	}
	@RequestMapping(value="tagtree2")
	public String tagtree2(Model model,HttpServletRequest request, HttpServletResponse response){
		//合并标签使用的标签组件jsp
		String id=request.getParameter("id");
		model.addAttribute("url","/cms/TagtreeData2?flag=2&id="+id);
		model.addAttribute("module","article");
		return "modules/sys/tagTreeselect3";
	}
	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "TagtreeData")
	public List<Map<String, Object>> TagtreeData(HttpServletRequest request,HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		//List<Category> list = categoryService.findByUser(true, "article",BaseService.CATEGORY_PLACE_USER);
		List<Label> list=labelservice.getLabelTreeData();
		for (int i=0; i<list.size(); i++){
				Label e = list.get(i);
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getPid()!=null?e.getPid():0);
				map.put("name", e.getLabelvalue());
				//map.put("module","article");
				map.put("url","/cms/tagControl?id="+e.getId());
				map.put("target", "cmsMainFrame");
				if(e.getIssys().equals("0")){
					map.put("nocheck","true");//动态设定是否出现复选框
				}
				else{
					map.put("nocheck","false");
				}
				if(e.getId().equals("3")){
					map.put("nocheck","true");
				}
				if(e.getDelFlag().equals("0")){
					map.put("nocheck","true");
				}
				mapList.add(map);
		}
		return mapList;
	}
	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "TagtreeData2")
	public List<Map<String,Object>> TagtreeData2(HttpServletRequest request,HttpServletResponse response) {
		String flag=request.getParameter("flag");
		//flag=1:不能有待审核的标签
		if("1".equals(flag)){
			response.setContentType("application/json; charset=UTF-8");
			List<Map<String, Object>> mapList = Lists.newArrayList();
			//List<Category> list = categoryService.findByUser(true, "article",BaseService.CATEGORY_PLACE_USER);
			List<Label> list=labelservice.getLabelTreeData();
			List<Label> labellist=new ArrayList<Label>();
			for(int i=0;i<list.size();i++){
				String delflag=list.get(i).getDelFlag();
				String id=list.get(i).getId();
				String issys=list.get(i).getIssys();
				if(!delflag.equals("1") && !id.equals("2")){
					if(!issys.equals("0"))
						labellist.add(list.get(i));
				}	
			}
			for(int i=0; i<labellist.size(); i++){
					Label e = labellist.get(i);
					Map<String, Object> map = Maps.newHashMap();
					map.put("id", e.getId());
					map.put("pId", e.getPid()!=null?e.getPid():0);
					map.put("name", e.getLabelvalue());
					map.put("deflg", e.getDelFlag());
					mapList.add(map);
			}
			return mapList;
		}
		//flag=2:不能有待审核和NC系统导入的标签
		if("2".equals(flag)){
			String pid=request.getParameter("pid");  //包含此分类的下级分类,需要去除的分类
			String lid=new String();
//			if(request.getParameter("id")!=null)
//				lid=request.getParameter("id");
			response.setContentType("application/json; charset=UTF-8");
			List<Map<String, Object>> mapList = Lists.newArrayList();
			//List<Category> list = categoryService.findByUser(true, "article",BaseService.CATEGORY_PLACE_USER);
			List<Label> list=labelservice.getLabelTreeData();
			List<Label> labellist=new ArrayList<Label>();
			for(int i=0;i<list.size();i++){
				String delflag=list.get(i).getDelFlag();
				String id=list.get(i).getId();
				String lpids=list.get(i).getPids();
				String isys=list.get(i).getIssys();
				if(!delflag.equals("1") && !id.equals("2")&&!id.equals("3")&& !isys.equals("0")){
//					if(!list.get(i).getPid().equals(pid)){
//						if(!id.equals(lid)){
//							labellist.add(list.get(i));
//						}
//					}
					if(pid==null){
						labellist.add(list.get(i));
					}else{
						if(lpids.indexOf(pid) ==-1){
							labellist.add(list.get(i));
						}
					}
				}
			}
			for (int i=0; i<labellist.size(); i++){
					Label e = labellist.get(i);
					Map<String, Object> map = Maps.newHashMap();
					map.put("id", e.getId());
					map.put("pId", e.getPid()!=null?e.getPid():0);
					map.put("name", e.getLabelvalue());
					map.put("deflg", e.getDelFlag());
					map.put("module","article");
					//map.put("url",e.getId());
					//map.put("target", "cmsMainFrame");
					mapList.add(map);
			}
			return mapList;
		}
		//flag:除去不是自己添加的待审核的标签
		//判断是否为作物类,是则显示作物标签，不是显示知识库标签
		String mark=new String();
		if(request.getParameter("remarks") !=null && !request.getParameter("remarks").equals("")){
			mark=request.getParameter("remarks");
		}else{
			Category category=categoryService.get(new Category(request.getParameter("categoryid")));
			mark=category.getRemarks();
		}
		//判断是哪一个页面的请求,1为创建的分类页面,非1为创建知识的页面
		String iscategory=request.getParameter("iscategory");
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		//List<Category> list = categoryService.findByUser(true, "article",BaseService.CATEGORY_PLACE_USER);
		List<Label> list=labelservice.getLabelTreeData();
		List<Label> labellist=new ArrayList<Label>();
		String userid=UserUtils.getUser().getId();
		//1:表示为作物类，非1表示知识库标签
		if("1".equals(mark)){
			if("1".equals(iscategory)){
				for(int i=0;i<list.size();i++){
					String issys=list.get(i).getIssys();
					String id=list.get(i).getId();
					if(!issys.equals("1") && !id.equals("2")){
						labellist.add(list.get(i));
					}
				}
			}else{
				for(int i=0;i<list.size();i++){
					String delflag=list.get(i).getDelFlag();
					String uid=list.get(i).getUserid();
					String issys=list.get(i).getIssys();
					if(delflag.equals("1") &&uid.equals(userid)){
						labellist.add(list.get(i));
						continue;
					}		
					if(!issys.equals("1")){
						labellist.add(list.get(i));
					}
				}
			}
		}else{
			if("1".equals(iscategory)){
				for(int i=0;i<list.size();i++){
					String delflag=list.get(i).getDelFlag();
					String id=list.get(i).getId();
					String uid=list.get(i).getUserid();
					String issys=list.get(i).getIssys();
					if(!issys.equals("0")){
						if(delflag.equals("1")){
							continue;
						}
						labellist.add(list.get(i));
					}
				}
			}else{
				for(int i=0;i<list.size();i++){
					String delflag=list.get(i).getDelFlag();
					String id=list.get(i).getId();
					String uid=list.get(i).getUserid();
					String issys=list.get(i).getIssys();
					if(!issys.equals("0")){
						if(delflag.equals("1") && !uid.equals(userid)){
							continue;
						}
						labellist.add(list.get(i));
					}
				}
			}
		}
		for (int i=0; i<labellist.size(); i++){
				Label e = labellist.get(i);
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getPid()!=null?e.getPid():0);
				map.put("name", e.getLabelvalue());
				map.put("deflg", e.getDelFlag());
				map.put("module","article");
				//map.put("url",e.getId());
				//map.put("target", "cmsMainFrame");
				mapList.add(map);
		}
		return mapList;
	}
	
	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "relatedlable")
	public List<Map<String,String>> relatedlable(HttpServletRequest request,HttpServletResponse response){
		response.setContentType("application/json; charset=UTF-8");
		String labelid = request.getParameter("id");
		List<Map<String, String>> mapList = Lists.newArrayList();
		List<Label> list=labelservice.findRelationLabel(labelid);
		for (int i=0; i<list.size(); i++){
			Label e = list.get(i);
			Map<String,String> map = Maps.newHashMap();
			map.put("labelid", e.getId());
			map.put("labelname", e.getLabelvalue());
			map.put("deflg", e.getDelFlag());
			mapList.add(map);
		}
		return mapList;
	}
	
	@RequestMapping("addTag")
	public String addTag(String id,String tagflag,Model model){
		//System.out.println("addTag");
		Label label=new Label();
		label=labelservice.get(id);
		if(tagflag==null)
			tagflag="1";
		//System.out.println("tagflag:"+tagflag);
		model.addAttribute("label",label);
		model.addAttribute("tagflag",tagflag);
		return "modules/sys/addTag";
	}
	//add by yangshw6
	@RequestMapping("deleteTag")
	public String deleteTag(String id,String delFlag,String flag,RedirectAttributes redirectAttributes){
		//System.out.println("----------deleteTag-----------");
		Label label=new Label();
		label.setId(id);
		labelservice.delete(label);
		//System.out.println("deleteTag:delFlag"+delFlag);
		if(flag.equals("0")){
			return "redirect:"+adminPath+"/sys/user/userTag?delFlag="+delFlag;
		}
		return "redirect:"+adminPath+"/cms/TagControl";
	}
	//end by yangshw6
	/*
	 * 后台管理批量删除标签(ajax传递)
	 * 
	 */
	//通过ajax传递删除标签
	@ResponseBody
	@RequestMapping(value ="batchdeleteTag",method=RequestMethod.POST)
	public String batchdeleteTag(@RequestParam String id,RedirectAttributes redirectAttributes){
//		String[] string=list.split(",");
//		List<String> data=Arrays.asList(string);
//		List<Label> la=labelservice.batchgetLabelData(data);
//		StringBuffer nodelete=new StringBuffer();
//		List<String> delete=new ArrayList<String>();
//		for(int i=0;i<la.size();i++){
//			int countar=Integer.valueOf(la.get(i).getCountarticle());
//			int countuser=Integer.valueOf(la.get(i).getCountuser());
//			if(countar !=0 || countuser !=0){
//				nodelete.append(la.get(i).getLabelvalue()+",");
//			}
//			if(countar ==0&& countuser==0){
//				delete.add(la.get(i).getId());
//			}
//		}
//		String s=nodelete.toString();
//		if(!s.equals("")){
//			return nodelete.toString();
//		}
//		if(delete !=null && delete.size()>0){
//			labelservice.batchdeleteLabelData(delete);
//		}
		labelservice.delete(new Label(id));
		return "1";
	}
	
	//管理员增加标签,不需要审核
	@RequestMapping(value="save",method=RequestMethod.POST)
	public String save(Label label,RedirectAttributes redirectAttributes,HttpServletRequest request,HttpServletResponse response){
		//System.out.println("保存标签");
		//System.out.println("label"+JsonMapper.toJsonString(label));
		//add by yangshw6																	
		if(label.getId().equals("")){
			if(label.getLabelvalue().equals("")){
				//System.out.println("label...空"+label.getLabelvalue());
				addMessage(redirectAttributes, "信息错误，请重新填写");
				return "redirect:"+adminPath+"/cms/addTag";
			}else{
				//System.out.println("size:"+label.getLabelvalue().length());
				if(label.getLabelvalue().length()>10){
					addMessage(redirectAttributes, "增加的标签限制在10个字符内");
					return "redirect:"+adminPath+"/cms/addTag";
				}
				boolean flag=labelservice.findRepeatLabelName(label.getLabelvalue());
				//System.out.println("标签名为:"+label.getLabelvalue());
				if(flag){
					addMessage(redirectAttributes, "标签名已被现有的标签所使用,请重新填写");
					return "redirect:"+adminPath+"/cms/addTag";
				}
				//System.out.println("label...插入"+label.getLabelvalue());
				label.setDelFlag("0");
				label.setUserid(UserUtils.getUser().getId());
				Label la=labelservice.get(new Label(label.getPid()));
				label.setPids(la.getPids()+label.getPid()+",");
				labelservice.insert(label);
				addMessage(redirectAttributes, "保存成功");
				return "redirect:"+adminPath+"/cms/addTag";
			}
		}else{
			//System.out.println("size:"+label.getLabelvalue().length());
			if(label.getLabelvalue().length()>10){
				addMessage(redirectAttributes, "修改的标签限制在10个字符内");
				return "redirect:"+adminPath+"/cms/tagControl?id="+label.getId();
			}
			boolean flag=labelservice.findRepeatLabelName(label.getLabelvalue(),label.getId());
			//System.out.println("标签名为:"+label.getLabelvalue());
			if(flag){
				addMessage(redirectAttributes, "标签名已被现有的标签所使用,请重新填写");
				return "redirect:"+adminPath+"/cms/tagControl?id="+label.getId();
			}
			//System.out.println("label...更新"+label.getLabelvalue());
			if(label.getLabellist()!=null){
				String[] string=label.getLabellist().split(",");
				List<String> relalabel=Arrays.asList(string);
				labelservice.savaRelationData(label.getId(), relalabel);
			}
			Label la=labelservice.get(new Label(label.getPid()));
			label.setPids(la.getPids()+label.getPid()+",");
			labelservice.update(label);
			addMessage(redirectAttributes, "更新成功");
			return "redirect:"+adminPath+"/cms/tagControl?id="+label.getId();
			}
		}
		//end by yangshw6
	//在上传知识的界面保存用户自己增加的标签,但是未审核
	@ResponseBody
	@RequestMapping(value="saveUnexamineLabel",method=RequestMethod.POST)
	public String saveUnexamineLabel(String labelflag,Label label,RedirectAttributes redirectAttributes,HttpServletRequest request,HttpServletResponse response){
//			System.out.println("saveUnexamineLabel");
//			System.out.println("labelflag"+labelflag);
//			System.out.println("label"+JsonMapper.toJsonString(label));
			if(label.getLabelvalue().equals("")){
				//System.out.println("label...空"+label.getLabelvalue());
				return "1";
			}else{
				if(label.getLabelvalue().length()>4){
					return "3";
				}
				boolean flag=labelservice.findRepeatLabelName(label.getLabelvalue());
				//System.out.println("标签名为:"+label.getLabelvalue());
				if(flag){
					return "2";
				}
				//System.out.println("label...插入"+label.getLabelvalue());
				label.setDelFlag("1");
				label.setPid("2");
				label.setIssys("1");
				label.setPids("0,1,2,");
				label.setUserid(UserUtils.getUser().getId());
				labelservice.insert(label);
				return "0";
			}
		
		//end by yangshw6
		}
	//审核标签，0为通过，1为不通过
	@ResponseBody
	@RequestMapping(value="pass",method=RequestMethod.POST)
	public String pass(Label label,RedirectAttributes redirectAttributes,HttpServletRequest request,HttpServletResponse response){
		//System.out.println("----pass----");
		//System.out.println("tagflag:"+label.getDelFlag());
		//System.out.println("id:"+label.getId());
//		if(label==null){
//			return "modules/sys/tagControl?delFlag="+label.getDelFlag();
//		}
//		if(label.getDelFlag().equals("0")){
//			labelservice.update(label);
//			addMessage(redirectAttributes, "成功修改!审批通过");
//			//add hefeng
//			Label labelmsg=new Label();
//			labelmsg=labelservice.get(label.getId());
//			//oaNotifyService.save(oaNotifyService.PretreatmentMsgBeforeSave(labelmsg));
//			//end
//			if(label.getIschecked()==1){
//				return "redirect:"+adminPath+"/cms/tagControl?delFlag=1";
//			}else{
//				return "redirect:"+adminPath+"/cms/tagControl?delFlag=2";
//			}
//		}else if(label.getDelFlag().equals("2")){
//			labelservice.update(label);
//			addMessage(redirectAttributes, "成功修改!审批不通过");
//			//add hefeng
//			Label labelmsg=new Label();
//			labelmsg=labelservice.get(label.getId());
//			//oaNotifyService.save(oaNotifyService.PretreatmentMsgBeforeSave(labelmsg));
//			//end
//			return "redirect:"+adminPath+"/cms/tagControl?delFlag=1";
//		}else{
//			labelservice.update(label);
//			addMessage(redirectAttributes, "成功弃审");
//			return "redirect:"+adminPath+"/cms/tagControl?delFlag="+label.getDelFlag();
//		}
		List<String> idlist=new ArrayList<String>();
		if(label.getLabellist()!=null){
			String[] string=label.getLabellist().split(",");
			idlist=Arrays.asList(string);
		}
		List<Label> list=new ArrayList<Label>();
		for(int i=0;i<idlist.size();i++){
			Label ls=new Label();
			ls.setId(idlist.get(i));
			ls.setDelFlag("0");
			ls.setPid(label.getPid());
			list.add(ls);
		}
		try{
			labelservice.batchUpdate(list);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return "1";
		
	}
	
	
	@RequestMapping(value = "frontswitch")
	public String frontswitch(HttpServletRequest request, HttpServletResponse response, Model model) {
		//在构造方法中设置分页大小new Page<Article>(request, response,10),第三个参数
		List<Switch> frontswitch=Lists.newArrayList();
		frontswitch=switchService.getAll();
		//System.out.println("首页轮播");
		model.addAttribute("frontswitch", frontswitch);
		//System.out.println("frontswitch"+JsonMapper.toJsonString(frontswitch));
		return "modules/sys/frontswitch";
	}
	//增加和修改轮播
	@RequestMapping(value = "frontswitchmodify")
	public String frontswitchmodify(String id, Model model) {
		//得到id.通过id,查询出对应的switch vo 返回去
		Switch switch_modify =new Switch();
		switch_modify=switchService.getByID(id);
		model.addAttribute("switch_modify", switch_modify);
		//System.out.println("switch_modify===="+JsonMapper.toJsonString(switch_modify));
		return "modules/sys/frontswitch_modify";
	}
	
	//保存轮播图的设置
	@RequestMapping(value = "frontswitchsave",method=RequestMethod.POST)
	public String frontswitchsave(Switch switch_save,RedirectAttributes redirectAttributes,HttpServletRequest request,HttpServletResponse response, Model model) {
		//得到id.通过id,查询出对应的switch vo 返回去
		//System.out.println("switch_save===="+JsonMapper.toJsonString(switch_save));
//		System.out.println("switch_save===="+JsonMapper.toJsonString(switch_save));
		switchService.update(switch_save);
		addMessage(redirectAttributes, "修改成功");
		return "redirect:"+adminPath+"/cms/frontswitch";
	}
	//禁用和启用某张图
	@RequestMapping(value = "frontswitchdisabled")
	public String frontswitchdisabled(String id,String delFlag,RedirectAttributes redirectAttributes,Model model){
		//delFlag等于0表示已经是启用状态,1表示已经是禁用状态
		//System.out.println("//禁用和启用某张图"+"id=="+id+"  delFlag=="+delFlag);
		Switch switch_abled =new Switch();
		switch_abled=switchService.getByID(id);
		if(delFlag.equals("0")){
			switch_abled.setDelFlag("1");
			//System.out.println("switch_abled===="+JsonMapper.toJsonString(switch_abled.getDelFlag()));
		}
		else{
			switch_abled.setDelFlag("0");
			//System.out.println("switch_abled===="+JsonMapper.toJsonString(switch_abled.getDelFlag()));
		}
		switchService.update(switch_abled);
		addMessage(redirectAttributes, "修改成功");
		return "redirect:"+adminPath+"/cms/frontswitch";
	}
	@RequestMapping(value = "mergeTaglist")
	public String mergeTaglist(Label label,RedirectAttributes redirectAttributes,Model model){
//		System.out.println("labelid="+label.getId());
		List<Label> labellist=labelservice.findUnMergeLabel(label.getId());
		if(labellist==null || labellist.size()==0){
			return "modules/sys/mergeTag";
		}
		model.addAttribute("labellist",labellist);
		return "modules/sys/mergeTag";
	}
	@ResponseBody
	@RequestMapping(value="mergeTag",method=RequestMethod.POST)
	public String mergeTag(String firstlabelid,String secondlabelid,String newname,RedirectAttributes redirectAttributes,HttpServletRequest request,HttpServletResponse response){
//		System.out.println("firstlabelid"+firstlabelid+"-"+secondlabelid+"-"+newname);
		if(labelservice.findRepeatLabelByMerge(newname,secondlabelid)){
			return "1";
		}
		if(secondlabelid==null){
			return "2";
		}
		labelservice.MergeLabel(firstlabelid, secondlabelid, newname);
		return "0";
	}
	
	

}
	//end

