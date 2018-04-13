/**
 * 
 */
package com.yonyou.kms.modules.cms.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.yonyou.kms.common.persistence.Page;
import com.yonyou.kms.common.service.BaseService;
import com.yonyou.kms.common.utils.DateUtils;
import com.yonyou.kms.common.web.BaseController;
import com.yonyou.kms.modules.cms.entity.Article;
import com.yonyou.kms.modules.cms.entity.Category;
import com.yonyou.kms.modules.cms.service.ArticleService;
import com.yonyou.kms.modules.cms.service.CategoryService;
import com.yonyou.kms.modules.cms.service.StatsService;
import com.yonyou.kms.modules.sys.entity.User;
import com.yonyou.kms.modules.sys.service.OfficeService;
import com.yonyou.kms.modules.sys.service.SystemService;

/**
 * 统计Controller
 * @author hotsum
 * @version 2013-5-21
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/stats")
public class StatsController extends BaseController {

	@Autowired
	private StatsService statsService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private ArticleService articleService;
	/**
	 * 文章信息量
	 * @param paramMap
	 * @param model
	 * @return 	
	 */
	@RequiresPermissions("cms:stats:article")
	@RequestMapping(value = "article")
	public String article(@RequestParam Map<String, Object> paramMap, Model model) {
		List<Category> tempList = statsService.article(paramMap);
		List<Category> temp=Lists.newArrayList();
		List<String> ids=Lists.newArrayList();
		//categoryService.findByUser(true, "article", BaseService.CATEGORY_PLACE_SYS);
		// add by luqibao  2015-12-30显示具有权限的分类统计
		List<Category> list =categoryService.findByUser(true, "article", BaseService.CATEGORY_PLACE_SYS);
		for(Category c:list){
			ids.add(c.getId());
		}
		for(Category c:tempList){
//			System.out.println(c.getName());
			if(list.contains(c.getId())){
				temp.add(c);
			}
		}
		
		model.addAttribute("list", tempList);
		model.addAttribute("paramMap", paramMap);
		return "modules/cms/statsArticle";
	}
	
	@RequestMapping(value = "uploader")
	public String uploader(HttpSession session,Model model,HttpServletRequest request, HttpServletResponse response) {
		//清楚已选择的session值
		session.removeAttribute("selectids");
		return "modules/cms/uploaderArticle";
	}
	
	@RequestMapping(value = "examer")
	public String examer(HttpSession session,Model model) {
		session.removeAttribute("selectids");
		return "modules/cms/examerArticle";
	}
	
	
	@ResponseBody
	@RequestMapping(value = "setSession")
	public String setSession(HttpSession session,@RequestParam("selectids")String selectids){
		session.setAttribute("selectids", selectids);//存放上一次用户的ID
		return "success";
	}
	
	
	//uploader 页面的上传者的树插件页面
	@RequestMapping(value = "uploaderTree")
	public String uploaderTree(HttpSession session,@RequestParam("delflag")String delflag,Model model) {
		//List<User> userlist =systemService.findUser(new User());
		List<User> selelist=new ArrayList<User>();
		String[] selectIds=null;
		Map<String,Object> param=new HashMap<String,Object>();
		String select=String.valueOf(session.getAttribute("selectids"));
		if(select !=null && !select.equals("")){
			selectIds=select.split(",");
			if(selectIds.length >0){
				//in条件最大只支持1000,所以需要拼接字符串
				StringBuffer sb=new StringBuffer();
				String id=null;
				int length=selectIds.length;
				int count= (int)Math.ceil(selectIds.length/1000.0) ;
				for(int j=0;j<count;j++){
					int start=j*1000;
					int end=(j+1)*1000-1;
					if(j==0){
						sb.append("(id in (");
					}else{
						sb.append(" or id in (");
					}
					if(end>length){
						end=length;
					}
					for(int i=start;i<end;i++){
						id=selectIds[i];
						sb.append("'"+id+"'");
						if(i!=end-1){
							sb.append(",");
						}
					}
					sb.append(")");
				}
				sb.append(")");
				param.put("selectIds",sb.toString());
				selelist=systemService.batchget(param);
			}
		}
		//model.addAttribute("userList", userlist);
		model.addAttribute("selectlist", selelist);
		model.addAttribute("selectIds", selectIds);
		model.addAttribute("officeList", officeService.findAll());
		model.addAttribute("delflag",delflag);
		return "modules/cms/selectUploader";
	}
	
	@RequestMapping(value = "uploaderSearcher")
	public String uploaderSearcher(@RequestParam(value="flag",required=false)String flag,@RequestParam Map<String, Object> paramMap,Model model,HttpServletRequest request, HttpServletResponse response){
		
		
		List<Map<String,Object>> data=new ArrayList<Map<String,Object>>();
		
		//从session中取数据   
		String ids=String.valueOf(request.getSession().getAttribute("selectids"));
			//String.valueOf(paramMap.get("selectids"));
		String beginDate=String.valueOf(paramMap.get("beginDate"));
		String endDate=String.valueOf(paramMap.get("endDate"));
		String[] selectIds=ids.split(",");
		int pageSize;
		int pageNo;
		if(paramMap.get("pageSize")==null || paramMap.get("pageSize").equals("")){
			pageSize=30;
			pageNo=1;
		}else{
			pageSize=Integer.valueOf((String)paramMap.get("pageSize"));
			pageNo=Integer.valueOf((String)paramMap.get("pageNo"));
		}
		//拼接in查询字符串
		StringBuffer sb=new StringBuffer();
		if(selectIds.length !=0){//数据库in查询限制为1000需要 进行字符串拼接
			String id=null;
			int length=selectIds.length;
			int count= (int)Math.ceil(selectIds.length/1000.0) ;
			for(int j=0;j<count;j++){
				int start=j*1000;
				int end=(j+1)*1000-1;
				if(j==0){
					sb.append("(u.id in (");
				}else{
					sb.append(" or u.id in (");
				}
				if(end>length){
					end=length;
				}
				for(int i=start;i<end;i++){
					id=selectIds[i];
					sb.append("'"+id+"'");
					if(i!=end-1){
						sb.append(",");
					}
				}
				sb.append(")");
			}
			sb.append(")");
		}
		
		
		Date beginDateparse=DateUtils.parseDate(beginDate);
		Date endDateparse=DateUtils.parseDate(endDate);
		paramMap.put("endnum",pageSize*pageNo);
		paramMap.put("beginnum",pageSize*(pageNo-1)+1);
		paramMap.put("selectids", sb.toString());
		paramMap.put("beginDate",beginDateparse);
		paramMap.put("endDate",endDateparse);
		
		/**
		 * 分为审核者和上传者
		 */
		if("1".equals(flag)){
			data=statsService.examerStats(paramMap);
		}else{
			data=statsService.uploaderStats(paramMap);
		}
		
		for(int i=0;i<data.size();i++){
			Map<String,Object> result=data.get(i);
			result.put("beginDate", beginDate);
			result.put("endDate", endDate);
		}
		
		//model.addAttribute("data",data);
		
        Page<Map<String,Object>> page=new Page<Map<String,Object>>(pageNo, pageSize,selectIds.length);
		page.setList(data);
		page.setCount(selectIds.length);
		model.addAttribute("page",page);
		//model.addAttribute("selectids",ids);
		model.addAttribute("displaybtn",paramMap.get("displaybtn"));
		model.addAttribute("beginDate",beginDate);
		model.addAttribute("endDate",endDate);
		if("1".equals(flag)){
			return "modules/cms/examerArticle";
		}else{
			return "modules/cms/uploaderArticle";
		}
	}
	/**
	 * 
	 * 查看知识列表
	 * 
	 */
	@RequestMapping(value = "Articlelist")
	public String Articlelist(@RequestParam Map<String, Object> paramMap,Model model){
		
		boolean isExamer=false;
		
		if(paramMap.get("isExamer")!=null){
			isExamer=true;
		}
		
		String beginDate=String.valueOf(paramMap.get("beginDate"));
		String endDate=String.valueOf(paramMap.get("endDate"));
		paramMap.put("beginDate", DateUtils.parseDate(beginDate));
		paramMap.put("endDate", DateUtils.parseDate(endDate));
		
		
		
		int length=0;
		if(isExamer){
			length=articleService.getCountArticleByExamer(paramMap);
		}else{
			length=articleService.getCountArticleByUser(paramMap);
		}
		
		List<Article> list=new ArrayList<Article>();
		int pageSize;
		int pageNo;
		if(paramMap.get("pageSize")==null || paramMap.get("pageSize").equals("")){
			pageSize=30;
			pageNo=1;
		}else{
			pageSize=Integer.valueOf((String)paramMap.get("pageSize"));
			pageNo=Integer.valueOf((String)paramMap.get("pageNo"));
		}
		paramMap.put("endnum",pageSize*pageNo);
		paramMap.put("beginnum",pageSize*(pageNo-1)+1);
		
		if(isExamer){
			list=articleService.getListByExamer(paramMap);
		}else{
			list=articleService.getListByUser(paramMap);
		}
		
		Page<Article> page=new Page<Article>(pageNo, pageSize,length);
		page.setList(list);
		model.addAttribute("page",page);
		model.addAttribute("param",paramMap);
		return "modules/cms/statsArticleList";
	}
	
}
