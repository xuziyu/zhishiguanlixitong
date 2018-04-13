package com.yonyou.kms.modules.sys.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yonyou.kms.common.beanvalidator.BeanValidators;
import com.yonyou.kms.common.config.Global;
import com.yonyou.kms.common.mapper.JsonMapper;
import com.yonyou.kms.common.persistence.Page;
import com.yonyou.kms.common.utils.CutImageUtil;
import com.yonyou.kms.common.utils.DateUtils;
import com.yonyou.kms.common.utils.IdGen;
import com.yonyou.kms.common.utils.excel.ExportExcel;
import com.yonyou.kms.common.utils.excel.ImportExcel;
import com.yonyou.kms.common.web.BaseController;
import com.yonyou.kms.modules.cms.entity.Article;
import com.yonyou.kms.modules.cms.entity.ArticleCount;
import com.yonyou.kms.modules.cms.entity.ArticleLabel;
import com.yonyou.kms.modules.cms.entity.Category;
import com.yonyou.kms.modules.cms.entity.Comment;
import com.yonyou.kms.modules.cms.entity.FeaturePackage;
import com.yonyou.kms.modules.cms.entity.Label;
import com.yonyou.kms.modules.cms.entity.Recommend;
import com.yonyou.kms.modules.cms.entity.Share;
import com.yonyou.kms.modules.cms.entity.Store;
import com.yonyou.kms.modules.cms.entity.UserLabel;
import com.yonyou.kms.modules.cms.service.ArticleAttFileService;
import com.yonyou.kms.modules.cms.service.ArticleCountService;
import com.yonyou.kms.modules.cms.service.ArticleLabelService;
import com.yonyou.kms.modules.cms.service.ArticleService;
import com.yonyou.kms.modules.cms.service.CategoryService;
import com.yonyou.kms.modules.cms.service.CommentService;
import com.yonyou.kms.modules.cms.service.FeaturePackageService;
import com.yonyou.kms.modules.cms.service.LabelService;
import com.yonyou.kms.modules.cms.service.RecommendService;
import com.yonyou.kms.modules.cms.service.ShareService;
import com.yonyou.kms.modules.cms.service.StoreService;
import com.yonyou.kms.modules.cms.service.UserLabelService;
import com.yonyou.kms.modules.cms.utils.CmsUtils;
import com.yonyou.kms.modules.oa.entity.OaNotify;
import com.yonyou.kms.modules.oa.service.OaNotifyService;
import com.yonyou.kms.modules.sys.entity.Office;
import com.yonyou.kms.modules.sys.entity.Role;
import com.yonyou.kms.modules.sys.entity.User;
import com.yonyou.kms.modules.sys.service.OfficeService;
import com.yonyou.kms.modules.sys.service.SystemService;
import com.yonyou.kms.modules.sys.utils.FileStorageUtils;
import com.yonyou.kms.modules.sys.utils.UserUtils;

/**
 * 用户Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user")
public class UserController extends BaseController {

	@Autowired
	private SystemService systemService;
	
	@Autowired
	private ArticleService articleService;
	@Autowired
	private OfficeService officeService;
	
	//begin zhengyu
	@Autowired
	private ArticleCountService articleCountService;
	@Autowired
	private OaNotifyService oaNotifyService;
	@Autowired
	private ArticleAttFileService articleattfileService;
	@Autowired
	private ShareService shareservice;
	@Autowired
	private StoreService storeService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private RecommendService recommendservice;
	@Autowired
	private LabelService labelservice;
	@Autowired
	private UserLabelService userlabelservice;
	@Autowired
	private ArticleLabelService articlelabelservice;
	@Autowired
	private CategoryService categoryService;
	
	@Value("${default.user.passowrd}")
	private String defaultUserPassowrd;
	
	public static final  String DEFAULT_IMG_URL="http://kms.gd-tianhe.com/e6a77c70709d42ea8e848cb92d42a71b.png";
	//end
	
	//add by linj
	@Autowired
	private FeaturePackageService featurePackageService;
	
	@ModelAttribute
	public User get(@RequestParam(required=false) String id) {
		if (org.apache.commons.lang3.StringUtils.isNotBlank(id)){
			return systemService.getUser(id);
		}else{
			return new User();
		}
	}

	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = {"index"})
	public String index(User user, Model model) {
		return "modules/sys/userIndex";
	}

	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = {"list", ""})
	public String list(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<User> page = systemService.findUser(new Page<User>(request, response), user,true);
		//systemService.findUserByOffice(new Page<User>(request, response), user);
		model.addAttribute("page", page);
		//return "";
		return "modules/sys/userList";
	}

	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "form")
	public String form(User user, Model model) {
		if (user.getCompany()==null || user.getCompany().getId()==null){
			user.setCompany(UserUtils.getUser().getCompany());
		}
		if (user.getOffice()==null || user.getOffice().getId()==null){
			user.setOffice(UserUtils.getUser().getOffice());
		}
		model.addAttribute("list", categoryService.getUserCategoryByUser(user));
		model.addAttribute("user", user);
		model.addAttribute("allRoles", systemService.findAllRole());
		return "modules/sys/userForm";
	}

	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "save")
	public String save(User user, @RequestParam(value="selectnodes",required=false)String[] string, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/user/list?repage";
		}
		// 修正引用赋值问题，不知道为何，Company和Office引用的一个实例地址，修改了一个，另外一个跟着修改。
		user.setCompany(new Office(request.getParameter("company.id")));
		user.setOffice(new Office(request.getParameter("office.id")));
		user.setNo(request.getParameter("loginName"));
		user.setLoginName(user.getLoginName().toUpperCase());
		//如果user密码为空则给定一个默认密码
		if(user.getPassword()==null){
			user.setPassword(defaultUserPassowrd);
			user.setPhoto(DEFAULT_IMG_URL);
			if (org.apache.commons.lang3.StringUtils.isNotBlank(user.getPassword())) {
				user.setPassword(SystemService.entryptPassword(user.getPassword()));
			}
		}
		// 如果新密码为空，则不更换密码
		if (org.apache.commons.lang3.StringUtils.isNotBlank(user.getNewPassword())) {
			user.setPassword(SystemService.entryptPassword(user.getNewPassword()));
		}
		if (!beanValidator(model, user)){
			return form(user, model);
		}
		if (!"true".equals(checkLoginName(user.getOldLoginName(), user.getLoginName()))){
			addMessage(model, "保存用户'" + user.getLoginName() + "'失败，登录名已存在");
			return form(user, model);
		}
		// 角色数据有效性验证，过滤不在授权内的角色
		List<Role> roleList = Lists.newArrayList();
		List<String> roleIdList = user.getRoleIdList();
		for (Role r : systemService.findAllRole()){
			if (roleIdList.contains(r.getId())){
				roleList.add(r);
			}
		}
		
		//add by wuwq
		List<String> list = new ArrayList<String>();
		if(string!=null){
			list = Arrays.asList(string);	
		}
		categoryService.batchInsertUserCategory(list, user);
		//end
		
		user.setRoleList(roleList);
		// 保存用户信息
		systemService.saveUser(user);
		// 清除当前用户缓存
		if (user.getLoginName().equals(UserUtils.getUser().getLoginName())){
			UserUtils.clearCache();
			//UserUtils.getCacheMap().clear();
		}
		addMessage(redirectAttributes, "保存用户'" + user.getLoginName() + "'成功");
		return "redirect:" + adminPath + "/sys/user/list?repage";
	}
	//modify by zouzhenkun
	
	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "delete")
	public String delete(User user, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/user/list?repage";
		}
		if (UserUtils.getUser().getId().equals(user.getId())){
			addMessage(redirectAttributes, "删除用户失败, 不允许删除当前用户");
		}else if (User.isAdmin(user.getId())){
			addMessage(redirectAttributes, "删除用户失败, 不允许删除超级管理员用户");
		}else{
			systemService.deleteUser(user);
			addMessage(redirectAttributes, "删除用户成功");
		}
		return "redirect:" + adminPath + "/sys/user/list?repage";
	}
	
	/**
	 * 导出用户数据
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:view")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(User user, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "用户数据"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<User> page = systemService.findUser(new Page<User>(request, response, -1), user,true);
    		new ExportExcel("用户数据", User.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/sys/user/list?repage";
    }

	/**
	 * 导入用户数据
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/user/list?repage";
		}
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<User> list = ei.getDataList(User.class);
			for (User user : list){
				try{
					if ("true".equals(checkLoginName("", user.getLoginName()))){
						user.setPassword(SystemService.entryptPassword("123456"));
						BeanValidators.validateWithException(validator, user);
						systemService.saveUser(user);
						successNum++;
					}else{
						failureMsg.append("<br/>登录名 "+user.getLoginName()+" 已存在; ");
						failureNum++;
					}
				}catch(ConstraintViolationException ex){
					failureMsg.append("<br/>登录名 "+user.getLoginName()+" 导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append(message+"; ");
						failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("<br/>登录名 "+user.getLoginName()+" 导入失败："+ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条用户，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条用户"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入用户失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/sys/user/list?repage";
    }
	
	/**
	 * 下载导入用户数据模板
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:view")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "用户数据导入模板.xlsx";
    		List<User> list = Lists.newArrayList(); list.add(UserUtils.getUser());
    		new ExportExcel("用户数据", User.class, 2).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/sys/user/list?repage";
    }

	/**
	 * 验证登录名是否有效
	 * @param oldLoginName
	 * @param loginName
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "checkLoginName")
	public String checkLoginName(String oldLoginName, String loginName) {
		if (loginName !=null && loginName.equals(oldLoginName)) {
			return "true";
		} else if (loginName !=null && systemService.getUserByLoginName(loginName) == null) {
			return "true";
		}
		return "false";
	}

	/**
	 * 用户信息显示及保存
	 * @param user
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "info")
	public String info(User user, HttpServletResponse response, Model model) {
		User currentUser = UserUtils.getUser();
		if (org.apache.commons.lang3.StringUtils.isNotBlank(user.getName())){
			if(Global.isDemoMode()){
				model.addAttribute("message", "演示模式，不允许操作！");
				return "modules/sys/userInfo";
			}
			currentUser.setEmail(user.getEmail());
			
			currentUser.setMobile(user.getMobile());
			currentUser.setRemarks(user.getRemarks());
			//add by luqibao 当头像没有的时候  将头像设置为null，这样在页面中可以进行判断
//			String path=user.getPhone();
//			if(path!=null){
//				File temp=new File(path);
//				if(!temp.exists()){
//					path=null;
//				}
//			}
//			currentUser.setPhone(path);
			//end
			currentUser.setPhoto(user.getPhoto());
			systemService.updateUserInfo(currentUser);
			model.addAttribute("message", "保存用户信息成功");
		}
		
		//add by luqibao 当头像没有的时候 将头像设置为null，这样在页面中可以进行判断
//		//使用默认的头像
//		String path=currentUser.getPhoto();
//		URL urlStr;
//		int state=-1;
//		HttpURLConnection connection=null;
//		try {
//			urlStr = new URL(path);
//			connection = (HttpURLConnection) urlStr.openConnection();
//			state=connection.getResponseCode();
//		} catch (Exception e) {
//			
//		}finally{
//			if(connection!=null){
//				connection.disconnect();
//			}
//		}
//		
//		if(state!=200){
//			path=null;
//		}
//		currentUser.setPhoto(path);
		//end
		model.addAttribute("user", currentUser);
		model.addAttribute("Global", new Global());
		return "modules/sys/userInfo";
	}

	/**
	 * 返回用户信息
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "infoData")
	public User infoData() {
		
		return UserUtils.getUser();
	}
	
	/**
	 * 修改个人用户密码
	 * @param oldPassword
	 * @param newPassword
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "modifyPwd")
	public String modifyPwd(String id,String oldPassword, String newPassword, Model model, RedirectAttributes redirectAttributes) {
		User user=UserUtils.get(id);
		if (org.apache.commons.lang3.StringUtils.isNotBlank(oldPassword) && org.apache.commons.lang3.StringUtils.isNotBlank(newPassword)){
			if(Global.isDemoMode()){
				model.addAttribute("message", "演示模式，不允许操作！");
				return "modules/sys/userModifyPwd";
			}
			if (SystemService.validatePassword(oldPassword, user.getPassword())){
				systemService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
				model.addAttribute("message", "修改密码成功");
			}else{
				model.addAttribute("message", "修改密码失败，旧密码错误");
			}
		}
		model.addAttribute("user", user);
		return "modules/sys/userModifyPwd";
	}
	//add by hefeng
	//modify by zouzhenkun
	
	//重置密码
	@RequiresPermissions("user")
	@RequestMapping(value = "resetPwd")
	public void resetPwd(String id,HttpServletResponse response) {
		User user=UserUtils.get(id);
		String newPassword=IdGen.uuid().substring(0,8);
		String newPwd=newPassword;
		systemService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
		try {
			response.getWriter().write(newPwd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//add by zouzhenkun
	
	//我的已上传
	@RequestMapping(value = "uploaded")
	public String uploaded(Article article, HttpServletRequest request, HttpServletResponse response, Model model) {
		//在构造方法中设置分页大小new Page<Article>(request, response,10),第三个参数
		article.setCreateBy(UserUtils.getUser());
		Page<Article> page = articleService.findPage(new Page<Article>(request, response), article, true); 
        model.addAttribute("page", page);
		return "modules/sys/userUploaded";
	}
	
	//我的推荐
	
	@RequestMapping(value = "userRecommend")
	public String userRecommend(Recommend recommend, HttpServletRequest request, HttpServletResponse response, Model model) {
		recommend.setCreateBy(UserUtils.getUser());
		recommend.setDelFlag("2");
		Page<Recommend> page = recommendservice.findPage(new Page<Recommend>(request, response), recommend); 
        model.addAttribute("page", page);
		return "modules/sys/userRecommend";
	}
	
	//我的分享
	
	@RequestMapping(value = "userShare")
	public String userShare(Share share, HttpServletRequest request, HttpServletResponse response,Model model) {
		share.setCreateBy(UserUtils.getUser());
		Page<Share> page = shareservice.findPage(new Page<Share>(request, response), share); 
        model.addAttribute("page", page);
		return "modules/sys/userShare";
	}
	
	//我的消息通知
	
	@RequestMapping(value = "userMessage")
	public String userMessage(OaNotify oaNotify, HttpServletRequest request, HttpServletResponse response,Model model) {
		if(oaNotify.getReadFlag()==null){
			oaNotify.setReadFlag("0");
		}
		oaNotify.setSelf(true);
		Page<OaNotify> page = oaNotifyService.find(new Page<OaNotify>(request, response), oaNotify); 
		model.addAttribute("page", page);
		return "modules/sys/userMessage";
	}
	
	//评论
	
	@RequestMapping(value = "userComment")
	public String userComment(Comment comment, HttpServletRequest request, HttpServletResponse response, Model model) {
		comment.setArticleCreater(UserUtils.getUser().getName());
//		Category category=new Category();
//		category.setId("3d22343421404443a6b28a616ca3f48f");
//		comment.setCategory(category);
		comment.setArticleCreaterId(UserUtils.getUser().getId());
		Page<Comment> page = commentService.findUserCommentPage(new Page<Comment>(request, response), comment); 
        model.addAttribute("page", page);
		return "modules/sys/userComment";
	}
	
	//我的收藏
	
	@RequestMapping(value = "userCollect")
	public String userCollect(Store store, HttpServletRequest request, HttpServletResponse response, Model model) {
		store.setCreateBy(UserUtils.getUser());
		Page<Store> page = storeService.findPage(new Page<Store>(request, response), store);
//		System.out.println(page);
		model.addAttribute("page", page);
		return "modules/sys/userCollect";
	}
	
	/**
	 * 取消收藏知识,个人中心
	 */
	@RequestMapping(value = "storedelete")
	public String storedelete(String id,String titleid,@RequestParam(required=false) Boolean isRe, RedirectAttributes redirectAttributes) {
		Store store=new Store();
		
		ArticleCount articleCount=new ArticleCount();
		articleCount=articleCountService.get(titleid);
		if(articleCountService.get(titleid)==null){
			
		}else{
			int i = articleCount.getCountcollect();
			i=i-1;
			articleCount.setCountcollect(i);
			articleCountService.save(articleCount);
		}
		store.setDelFlag("1");
		store.setId(id);
		store.setUpdateBy(UserUtils.getUser());
		store.setUpdateDate(new Date());
		storeService.deleteUserStore(store);
		addMessage(redirectAttributes, "取消收藏成功");
		return "redirect:" + adminPath + "/sys/user/userCollect?repage";
	}
	
	/**
	 * 取消推荐知识,个人中心
	 */
	@RequestMapping(value = "recommenddelete")
	public String recommenddelete(String id,String titleid,@RequestParam(required=false) Boolean isRe, RedirectAttributes redirectAttributes) {
		Recommend recommend=new Recommend();
		ArticleCount articleCount=new ArticleCount();
		articleCount=articleCountService.get(titleid);
		if(articleCountService.get(titleid)==null){
			
		}else{
			int i = articleCount.getCountreco();
			i=i-1;
			articleCount.setCountreco(i);
			articleCountService.save(articleCount);
		}
		
//		recommend.setStoreCount(recommendcount-1);
		recommend.setDelFlag("1");
		recommend.setId(id);
//		recommend.setUpdateBy(UserUtils.getUser());
		recommend.setUpdateDate(new Date());
		recommendservice.deleteUserRecommend(recommend);
		addMessage(redirectAttributes, "取消推荐成功");
		return "redirect:" + adminPath + "/sys/user/userRecommend?repage";
	}
	//标签
	@RequestMapping(value = "myTag")
	public String myTag(@ModelAttribute("label")UserLabel userlabel,Model model,HttpServletRequest request, HttpServletResponse response) {
		//add by yangshw6
//		UserLabel userlabel=new UserLabel();
//		System.out.println("userid="+UserUtils.getUser().getId());
//		userlabel.setUserid(UserUtils.getUser().getId());
//		Page<UserLabel> page=userlabelservice.findPage(new Page<UserLabel>(request, response), userlabel);
////		Page<Label> page = labelservice.getLabelConnUser(new Page<Label>(request, response), label);;
//		model.addAttribute("page",page);
		if(userlabel==null){
			userlabel=new UserLabel();
		}
		System.out.println("userid="+UserUtils.getUser().getId());
		userlabel.setFlag("1");
		userlabel.setUserid(UserUtils.getUser().getId());
		if(userlabelservice.findList(userlabel)==null){
			return "modules/sys/myTag";
		}
		Page<UserLabel> page=userlabelservice.findPage(new Page<UserLabel>(request, response), userlabel);
//		Page<Label> page = labelservice.getLabelConnUser(new Page<Label>(request, response), label);
		model.addAttribute("page",page);
		//end by yangshw6
		return "modules/sys/myTag";
	}
	//add by yangshw6
	//增加关注的标签
	@RequestMapping(value = "addmyTag")
	public String addmyTag(UserLabel ul,Model model,HttpServletRequest request, HttpServletResponse response) {
		//add by yangshw6
		
		System.out.println("addmyTag");
		List<UserLabel> userlabel=new ArrayList<UserLabel>();
		if(ul==null)
			ul=new UserLabel();
		ul.setFlag("1");
		ul.setUserid(UserUtils.getUser().getId());
		userlabel=userlabelservice.addLabel(ul);
		model.addAttribute("userlabellist", userlabel);
		//end by yangshw6
		return "modules/sys/addmyTag";
//		Label label=new Label();
//		label.setUserid(UserUtils.getUser().getId());
//		Page<Label> page = labelservice.findDiffrentLabel(new Page<Label>(request, response), label); 
//		for(int i=0;i<page.getList().size();i++){
//			System.out.println("label"+page.getList().get(i).getLabelcontent());
//		}
//		model.addAttribute("page", page);
//		//end by yangshw6
//		return "modules/sys/addmyTag";
	}
	@RequestMapping(value = "addmyTag_article")
	public String addmyTag_article(@ModelAttribute("userlabel")UserLabel usl,Model model,HttpServletRequest request, HttpServletResponse response) {
		//add by yangshw6
		//System.out.println("addmyTag_article");
		String newString=JsonMapper.toJsonString(usl.getSelectedTagString());
		//System.out.println("newString"+selectedTagString);
		List<String> slist=new ArrayList<String>();
		if(usl.getSelectedTagString()!=null){
			String[] string=usl.getSelectedTagString().split("\\.");
			slist.addAll(Arrays.asList(string));
		}		
//		for(int i=0;i<string.length;i++){
//			System.out.println("newString"+string[i]);
//		}
		//System.out.println("articleId"+articleId);
		String  articleId=usl.getArticleid();
		Label la=new Label();
		la.setLabelvalue(usl.getLabelvalue());
		if(articleId==null || articleId.equals("")){
			System.out.println("articleId==null || articleId==0");
			List<Label>	label=new ArrayList<Label>();
			label=labelservice.getAllLabel(la);
			List<Label> unexalabel=labelservice.getUnexamineLabel(la);			
			List<UserLabel>	userlabel=new ArrayList<UserLabel>();
			for(int i=0;i<label.size();i++){
				String id=label.get(i).getId();
				//System.out.println("label:"+label.get(i).getLabelvalue());
				for(int j=0;j<slist.size();j++){
					String lid=slist.get(j);
					if(id.equals(lid)){
						label.get(i).setIschecked(1);
					}
				}
				UserLabel ul=new UserLabel();
				ul.setLabelid(label.get(i).getId());
				ul.setLabelvalue(label.get(i).getLabelvalue());
				ul.setIschecked(label.get(i).getIschecked());
				ul.setDelFlag(label.get(i).getDelFlag());
				//System.out.println("userlabel1:"+JsonMapper.toJsonString(ul));
				userlabel.add(ul);
			}
			for(int i=0;i<unexalabel.size();i++){
				String id=unexalabel.get(i).getId();
				//System.out.println("unexalabel:"+unexalabel.get(i).getLabelvalue());
				for(int j=0;j<slist.size();j++){
					String lid=slist.get(j);
					if(id.equals(lid)){
						unexalabel.get(i).setIschecked(1);
					}
				}
				UserLabel ul=new UserLabel();
				ul.setLabelid(unexalabel.get(i).getId());
				StringBuffer sb=new StringBuffer();
				sb.append(unexalabel.get(i).getLabelvalue());
				ul.setLabelvalue(sb.toString());
				ul.setIschecked(unexalabel.get(i).getIschecked());
				ul.setDelFlag(unexalabel.get(i).getDelFlag());
				//System.out.println("userlabel2:"+JsonMapper.toJsonString(ul));
				userlabel.add(ul);
			}
			model.addAttribute("userlabellist", userlabel);
		}else{
			//System.out.println("articleId!=null");
			List<Label>	label=new ArrayList<Label>();
			label=labelservice.getAllLabel(la);
			List<Label> unexalabel=labelservice.getUnexamineLabel(la);
			List<UserLabel>	userlabel=new ArrayList<UserLabel>();
			for(int i=0;i<label.size();i++){
				String id=label.get(i).getId();
				for(int j=0;j<slist.size();j++){
					String lid=slist.get(j);
					if(id.equals(lid)){
						label.get(i).setIschecked(1);
					}
				}
				UserLabel ul=new UserLabel();
				ul.setLabelid(label.get(i).getId());
				ul.setLabelvalue(label.get(i).getLabelvalue());
				ul.setIschecked(label.get(i).getIschecked());
				ul.setDelFlag(label.get(i).getDelFlag());
				//System.out.println("userlabel:"+JsonMapper.toJsonString(ul));
				userlabel.add(ul);
			}
			for(int i=0;i<unexalabel.size();i++){
				String id=unexalabel.get(i).getId();
				for(int j=0;j<slist.size();j++){
					String lid=slist.get(j);
					if(id.equals(lid)){
						unexalabel.get(i).setIschecked(1);
					}
				}
				UserLabel ul=new UserLabel();
				ul.setLabelid(unexalabel.get(i).getId());
				StringBuffer sb=new StringBuffer();
				sb.append(unexalabel.get(i).getLabelvalue());
				ul.setLabelvalue(sb.toString());
				ul.setIschecked(unexalabel.get(i).getIschecked());
				ul.setDelFlag(unexalabel.get(i).getDelFlag());
				//System.out.println("userlabel:"+JsonMapper.toJsonString(ul));
				userlabel.add(ul);
			}
			model.addAttribute("userlabellist",userlabel);
		}
		//end by yangshw6
		return "modules/sys/addmyTag";
	}
	@RequestMapping(value = "selectmyTag")
	public String selectmyTag(@ModelAttribute("userlabel")UserLabel userlabel,Model model,HttpServletRequest request, HttpServletResponse response){
		
		
		return "modules/sys/addmyTag";
	}
	
	
	@ResponseBody
	@RequestMapping(value = "savemyTag",method=RequestMethod.POST)
	public String savemyTag(@RequestParam("addtag") String addtag,Model model,HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes) {
		//add by yangshw6
//		System.out.println("--savemyTag");
//		String[] checkvalue=request.getParameterValues("selectTag");
//		String flag=labelservice.getUserData(UserUtils.getUser().getId());
//		List<String>  labellist=new ArrayList<String>();
//		for(int i=0;i<checkvalue.length;i++){
//			System.out.println("checkvalue:"+checkvalue[i]);
//			labellist.add(checkvalue[i]);
//		}
//		if(checkvalue.length==0){
//			System.out.println("length:"+checkvalue.length);			
//			addMessage(redirectAttributes, "信息错误，请重新填写");
//			return "redirect:"+adminPath+"/cms/addmyTag";
//		}else{
//			if(flag==null){
//				System.out.println("该用户没有数据中表中，执行插入");
//				labelservice.insertLabelConnUser(labellist, UserUtils.getUser().getId());
//			}else{
//				System.out.println("该用户有数据中表中，执行更新");
//				labelservice.updateLabelConnUser(labellist, UserUtils.getUser().getId());
//			}
//			
//			addMessage(redirectAttributes, "保存成功");
//			return "redirect:"+adminPath+"/sys/user/myTag";
//		}
		String add=JsonMapper.toJsonString(addtag);
		String add2=add.replace("\"", "");
		String[] adds=add2.split("&");
		List<String> idlist=new ArrayList<String>();
		String userid=UserUtils.getUser().getId();
		for(int i=0;i<adds.length;i++){
			int length=adds[i].indexOf("=");
			String id=adds[i].substring(length+1);
			idlist.add(id);
			System.out.println("id"+id);
		}
		List<UserLabel> ullist=new ArrayList<UserLabel>();
		for(int i=0;i<idlist.size();i++){
			UserLabel ul=new UserLabel();
			ul.setLabelid(idlist.get(i));
			ul.setUserid(userid);
			ullist.add(ul);
		}
		userlabelservice.save(ullist);
		addMessage(redirectAttributes, "保存成功");
		return "redirect:"+adminPath+"/sys/user/myTag";
		//end by yangshw6
	}
	//显示我增加标签，审批和未审批
	@RequestMapping(value="userTag")
	public String userTag(@ModelAttribute("label")Label label,Model model,HttpServletRequest request, HttpServletResponse response){
		if(label==null)
			label=new Label();
		label.setUserid(UserUtils.getUser().getId());
		Page<Label> page = labelservice.findPage(new Page<Label>(request, response), label); 
        model.addAttribute("page", page);
		return "modules/sys/userTag";
	}
	//删除关注的标签
	@RequestMapping(value = "deletemyTag", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	public String deletemyTag(String id,String labelvalue,Model model,RedirectAttributes redirectAttributes) {
		//add by yangshw6
//		System.out.println("deletemyTag");
//		System.out.println("userid="+UserUtils.getUser().getId());
//		System.out.println("labelvalue="+labelvalue);
//		
//		List<Label>	label=new ArrayList<Label>();
//		label=labelservice.getLabelConnUser(UserUtils.getUser().getId());
//		List<String> labellist=new ArrayList<String>();
//		System.out.println("size:"+label.size());
//		for(int i=0;i<label.size();i++){
//			String value=label.get(i).getLabelvalue();
//			if(value.equals(labelvalue))
//				continue;
//			labellist.add(value);
//			System.out.println("labellist="+value);
//		}
//		labelservice.deleteLabelConnUser(labellist,UserUtils.getUser().getId());
//		addMessage(redirectAttributes, "删除成功");
		UserLabel userlabel=new UserLabel();
		userlabel.setLabelid(id);
		userlabel.setUserid(UserUtils.getUser().getId());
		userlabelservice.delete(userlabel);
		//end by yangshw6
		return "redirect:"+adminPath+"/sys/user/myTag";
	}
	//显示标签下的文章列表
	@RequestMapping(value = "articlelist")
	public String articlelist(@ModelAttribute("article")ArticleLabel al,Model model,HttpServletRequest request,HttpServletResponse response){
		List<String>  category=new ArrayList<String>();
		category=CmsUtils.getSecondCategoryListByCurrentAdmin();
		StringBuffer sb=new StringBuffer();//把权限下的知识分类改成一条字符串
		for(int i=0;i<category.size();i++){
			if(i==category.size()-1){
				sb.append("'"+category.get(i)+"'");
				break;
			}
			sb.append("'"+category.get(i)+"'"+",");
		}
		//ArticleLabel al=new ArticleLabel();
		//al.setLabelid(labelid);
		al.setCategorylist(sb.toString());
		String labelid = request.getParameter("labelid");
		if(al.getLabelid().equals("")){
			al.setLabelid(labelid);
		}
		if(al.getCategorylist().equals("") || al.getCategorylist()==null ){
			return "modules/sys/articlelist";
		}
		if(al.getCategory()==null){
			al.setCategory(new Category());
		}
		Page<ArticleLabel> page = articlelabelservice.findPage(new Page<ArticleLabel>(request, response),al);		
		model.addAttribute("page", page);
		model.addAttribute("labelid",al.getLabelid());
		return "modules/sys/articlelist";
	}
	//end
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String officeId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<User> list = systemService.findUserByOfficeId(officeId);
		for (int i=0; i<list.size(); i++){
			User e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", "u_"+e.getId());
			map.put("pId", officeId);
			map.put("name", org.apache.commons.lang3.StringUtils.replace(e.getName(), " ", ""));
			mapList.add(map);
		}
		return mapList;
	}
	//add by luqibao
	@ResponseBody
	@RequestMapping(method=RequestMethod.POST,value="leadUser")
	public String leadUser(RedirectAttributes ra){
		try {
			officeService.synchroNCInfo();
			systemService.synchroNCInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
    //end
    
//	@InitBinder
//	public void initBinder(WebDataBinder b) {
//		b.registerCustomEditor(List.class, "roleList", new PropertyEditorSupport(){
//			@Autowired
//			private SystemService systemService;
//			@Override
//			public void setAsText(String text) throws IllegalArgumentException {
//				String[] ids = StringUtils.split(text, ",");
//				List<Role> roles = new ArrayList<Role>();
//				for (String id : ids) {
//					Role role = systemService.getRole(Long.valueOf(id));
//					roles.add(role);
//				}
//				setValue(roles);
//			}
//			@Override
//			public String getAsText() {
//				return Collections3.extractToString((List) getValue(), "id", ",");
//			}
//		});
//	}
	/**
	 * 
	 */
	@RequestMapping(value = "userPhoto")
	public String fixUserPhoto(){
		
		return "modules/sys/fixUserPhoto";
	}
	
	@RequestMapping(value = "upload",method=RequestMethod.POST)
    public String upload(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, Model model) {  
  
        String path = request.getSession().getServletContext().getRealPath("upload");  
        String fileName =file.getOriginalFilename();
        //判断是不是图片
		boolean flag=false;
        String suffixs[]={"JPG","GIF","JPEG","PNG"};
        for(String temp:suffixs){
        	if(fileName.toUpperCase().contains(temp)){
        		flag=true;
        	};
        }
		if(!flag){
			model.addAttribute("error", "上传格式有错");
			 return "modules/sys/fixUserPhoto";
		}
        //获取图片的后缀名
        int photoIndex=fileName.lastIndexOf(".");
        String photoName=fileName.substring(photoIndex, fileName.length());

        fileName=IdGen.uuid()+photoName;
        File targetFile = new File(path, fileName);  
        if(!targetFile.exists()){  
            targetFile.mkdirs();  
        }  
        //保存  
        try {  
            file.transferTo(targetFile);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }

        //String imagePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
        String url="upload/"+fileName;
        model.addAttribute("url", url);  
  
        return "modules/sys/fixUserPhoto";  
    }
	
	@ResponseBody
	@RequestMapping(value = "saveImg")
	public String saveImg(HttpServletRequest request,Integer x,Integer y,Integer width,Integer height,String src){
		String suffixs[]={"JPG","GIF","JPEG","PNG"};
		User user=UserUtils.getUser();
		//获取文件夹的路径
		String uploadPath =request.getSession().getServletContext().getRealPath("/")+"upload/";
		
		//获取原来图片的名称
		int photoIndex=src.lastIndexOf("/");
		String photoName=src.substring(photoIndex, src.length());
		//图片后缀
		int lastIndex=src.lastIndexOf(".");
		String suffix=src.substring(lastIndex+1, src.length());
		//判断是不是上面的几种格式
		boolean flag=false;
		for(String temp:suffixs){
			if(temp.equals(suffix.toUpperCase())){
				flag=true;
			}
		}
		if(!flag){
			return user.getPhoto();
		}
		//原来没有截取图片的位置
		String realPath=request.getRealPath("/")+"upload\\"+photoName;
		//设置截取后图片的名字
		String fileName=IdGen.uuid()+"."+suffix;
		//存放截取图片的地址
		String targetFile=uploadPath+"images/"+fileName;
		//截取后图片的网络地址
		String imgUrl=src.substring(0,photoIndex)+"/images/"+fileName;
		
		CutImageUtil.cutImg(realPath,targetFile, x, y, width, height,suffix);
		
		//删除原来的那张图片
		File temp=new File(realPath);
		if(temp.exists()){
			temp.delete();
		}
		//将图片存放到阿里云
		fileName=IdGen.uuid()+"."+suffix;
		try {
			imgUrl=FileStorageUtils.putObject(fileName, targetFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//将截取的那张图片删除
		temp=new File(targetFile);
		if(temp.exists()){
			temp.delete();
		}
		//如果这张图片存在,那么更新个人信息
		user.setPhoto(imgUrl);
		
		systemService.updateUserInfo(user);
		return imgUrl;
	}
	
	//我的专题	
	@RequestMapping(value = "featurePackageList")
	public String userFeaturePackage(FeaturePackage featurePackage,HttpServletRequest request, HttpServletResponse response, Model model) {
		List<String> ids = featurePackageService.getFeaturePackageByUser(UserUtils.getUser().getId());
		if(ids.size()>0){
			featurePackage.setIds(ids);
			Page<FeaturePackage> page = featurePackageService.findPage(new Page<FeaturePackage>(request, response), featurePackage);
	        model.addAttribute("page", page);
		}
		return "modules/sys/featurePackageList";
	}
	
	/**
	 * 将NC用户同步到知识库中
	 * @param ra
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET,value="syncUser")
	public Map<String,Object> syncUser(RedirectAttributes ra){
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			systemService.synchroNCInfo();
			result.put("status", 1);
			result.put("msg", "同步成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 1);
			result.put("msg", "同步失败");
		}
		return result;
	}
}
