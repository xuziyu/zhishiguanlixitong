
package com.yonyou.kms.modules.cms.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aliyun.oss.model.PutObjectResult;
import com.google.common.collect.Lists;
import com.yonyou.kms.common.mapper.JsonMapper;
import com.yonyou.kms.common.persistence.Page;
import com.yonyou.kms.common.service.BaseService;
import com.yonyou.kms.common.utils.BaseImg64;
import com.yonyou.kms.common.utils.IdGen;
import com.yonyou.kms.common.utils.ReturnApp;
import com.yonyou.kms.common.utils.StandardData;
import com.yonyou.kms.common.utils.StringUtils;
import com.yonyou.kms.common.web.BaseController;
import com.yonyou.kms.modules.cms.entity.Article;
import com.yonyou.kms.modules.cms.entity.ArticleAttFile;
import com.yonyou.kms.modules.cms.entity.ArticleData;
import com.yonyou.kms.modules.cms.entity.Category;
import com.yonyou.kms.modules.cms.entity.ECategory;
import com.yonyou.kms.modules.cms.entity.FeaturePackage;
import com.yonyou.kms.modules.cms.entity.Label;
import com.yonyou.kms.modules.cms.entity.Site;
import com.yonyou.kms.modules.cms.entity.Switch;
import com.yonyou.kms.modules.cms.service.ArticleAttFileService;
import com.yonyou.kms.modules.cms.service.ArticleDataService;
import com.yonyou.kms.modules.cms.service.ArticleLabelService;
import com.yonyou.kms.modules.cms.service.ArticleService;
import com.yonyou.kms.modules.cms.service.CategoryService;
import com.yonyou.kms.modules.cms.service.FeaturePackageService;
import com.yonyou.kms.modules.cms.service.FileTplService;
import com.yonyou.kms.modules.cms.service.LabelService;
import com.yonyou.kms.modules.cms.service.SiteService;
import com.yonyou.kms.modules.cms.service.SwitchService;
import com.yonyou.kms.modules.cms.utils.CmsUtils;
import com.yonyou.kms.modules.cms.utils.ConvertVideo;
import com.yonyou.kms.modules.cms.utils.FileDelete;
import com.yonyou.kms.modules.cms.utils.ImportExcel;
import com.yonyou.kms.modules.cms.utils.Office2PdfUtil;
import com.yonyou.kms.modules.cms.utils.TplUtils;
import com.yonyou.kms.modules.cms.utils.UploadThreadUtils;
import com.yonyou.kms.modules.sys.entity.Office;
import com.yonyou.kms.modules.sys.entity.Role;
import com.yonyou.kms.modules.sys.entity.User;
import com.yonyou.kms.modules.sys.security.PLStrategy;
import com.yonyou.kms.modules.sys.service.OfficeService;
import com.yonyou.kms.modules.sys.service.SystemService;
import com.yonyou.kms.modules.sys.utils.FileStorageUtils;
import com.yonyou.kms.modules.sys.utils.UserUtils;

/**
 * 文章Controller
 * 
 * @author hotsum
 * @version 2013-3-23
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/article")
public class ArticleController extends BaseController {

	private static final Logger LOGGER = Logger.getLogger(ArticleController.class);

	@Autowired
	private ArticleService articleService;
	@Autowired
	private ArticleDataService articleDataService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private FileTplService fileTplService;
	@Autowired
	private SiteService siteService;
	@Autowired
	private ArticleAttFileService articleAttFileService;
	@Autowired
	private LabelService labelService;
	@Autowired
	private ArticleLabelService articleLabelService;
	@Autowired
	private SwitchService switchService;

	@Autowired
	private FeaturePackageService featureService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private OfficeService officeService;

	/**
	 * 专家组角色的ID
	 */
	@Value("${cms.professorId}")
	private String professorId;

	@ModelAttribute
	public Article get(@RequestParam(required = false) String id) {
		if (org.apache.commons.lang3.StringUtils.isNotBlank(id)) {
			return articleService.get(id);
		} else {
			return new Article();
		}
	}

	/**
	 * 正文定时5分钟自动保存
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "saveContent")
	public String saveContent(Article article, @RequestParam("content") String content) {
		article.getArticleData().setContent(content);
		articleService.save(article);
		return "";
	}

	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = { "list", "" })
	public String list(Article article, HttpServletRequest request, HttpServletResponse response, Model model) {

		/**
		 * 专家的时候 只有一个按钮
		 */
		// if(isProfessor()){
		//
		// article.setDelFlag("3");
		// }
		/**
		 * */
		Page<Article> page = articleService.findPage(new Page<Article>(request, response), article, true);

		/**
		 * start 三期变更 每一篇知识都需要查找审核管理员
		 */
		// article.setAuditUserId(UserUtils.getUser().getId());
		// Page<Article> page = articleService.findPageNeedAudit(new
		// Page<Article>(request, response), article, true);
		//
		// //但是系统管理员可以看到所有的 所以还是走以前的逻辑
		// User currentUser = UserUtils.getUser();
		// if(currentUser!=null){
		// //List<Role> roleLis6t = currentUser.getRoleList();
		// boolean isAdmin=false;
		//// for(Role role:roleList){
		//// if(role.getName().equals("系统管理员")){
		//// isAdmin=true;
		//// };
		//// }
		//
		// isAdmin=systemService.findSysUserByRole(currentUser);
		//
		// if(isAdmin){
		// page = articleService.findPage(new Page<Article>(request, response),
		// article, true);
		// }
		// }
		/**
		 * end 三期变更 每一篇知识都需要查找审核管理员
		 */

		// add by luqibao 将不符合情况的文章筛选出来

		List<Article> list = Lists.newArrayList();
		list = page.getList();

		// 这些都是具有权限的分类
		List<Category> categorys = categoryService.findByUser(true, null, BaseService.CATEGORY_PLACE_SYS);
		List<String> ids = Lists.newArrayList();

		for (Category a : categorys) {
			ids.add(a.getId());
		}
		List<Article> temp = Lists.newArrayList();
		// 如果这些知识是有权限的分类下面的 那么就显示 不然就不显示出来
		if (list != null) {
			for (Article a : list) {
				if (ids.contains(a.getCategory().getId())) {
					temp.add(a);
				}
			}
		}
		list.clear();
		list = temp;

		/**
		 * @date 2016/11/07
		 * @description SB的客户觉得专家也需要查看到那些他审核通过的 start
		 */

		/**
		 * end
		 */

		for (Article a : list) {
			String id = a.getCreateBy().getId();
			Office company = systemService.getUser(id).getCompany();
			a.getCreateBy().setCompany(company);
		}
		// 重新设置 分页参数
		page.setList(list);

		/**
		 * @date 2016-10-12
		 * @author Hotusm 这个list中的创建用户的所属部门,是否对外分享都为空 需要将这两个属性给加上去
		 * @see com.yonyou.kms.modules.cms.web.ArticleController.warpArticleLists(List<Article>)
		 *      start
		 */
		warpArticleLists(list);
		/***
		 * end
		 **/
		// 设置分页的总数 待审核是2 已发布0 已下架1
		page.setCount(articleService.count(new Page<Article>(request, response), article, true));

		/**
		 * @date 2016-10-12
		 * @author Hotusm 对于专家来说 因为专家的权限是只能够看到提交专家的知识 所以需要进行帅筛选 这里需要将list 和
		 *         count重新的计算过
		 */

		// LOGGER.info("管理员知识总数:"+page.getCount());

		// LOGGER.info("是否为专家:"+isProfessor());

		if (isProfessor()) {

			model.addAttribute("isProfessor", true);

			page.setCount(articleService.countProfessorRead(UserUtils.getUser(), article.getDelFlag()));

			page.setList(warpListAsProfessor(list));
			// List<Article> articles=Lists.newArrayList();
			//
			// for(Article a:list){
			//
			// if("3".equals(a.getDelFlag())){
			// articles.add(a);
			// }
			// }
			//
			// page.setList(articles);

			LOGGER.info("专家知识总数:" + page.getCount());

		}

		/**
		 * end
		 */
		page.initialize();
		// end
		model.addAttribute("page", page);
		// model.addAttribute("from_a_cms_article", from_a_cms_article);
		return "modules/cms/articleList";
	}

	// 这篇知识是提交专家审核了(del_flag=3)||审核人是这名专家
	private List<Article> warpListAsProfessor(List<Article> articles) {
		List<Article> realList = Lists.newArrayList();

		for (Article article : articles) {

			if ("3".equals(article.getDelFlag()) || UserUtils.getUser().getId()
					.equals(article.getExaminer() == null ? null : article.getExaminer().getId())) {

				realList.add(article);
			}
		}

		return realList;
	}

	//
	protected boolean isProfessor() {

		List<String> roleLists = UserUtils.getRoleIdList();
		if (roleLists.contains(professorId))
			return true;

		return false;
	}

	private void warpArticleLists(List<Article> articles) {
		Map<String, User> mapsusers = new HashMap<String, User>();
		// 查出所有的用户
		List<User> users = systemService.finalAllUsers();

		for (User user : users) {
			mapsusers.put(user.getId(), user);
		}

		for (Article article : articles) {
			User createBy = article.getCreateBy();

			User user = mapsusers.get(createBy.getId());
			if (user == null || StringUtils.isBlank(user.getId()))
				user = systemService.getUser(createBy.getId());

			article.getCreateBy().setOffice(user.getOffice());
		}
	}

	// add by wuwq
	@RequestMapping(value = "addReason")
	public String addReason() {
		return "modules/cms/addReason";
	}

	@RequestMapping(value = "showReason")
	public String showReason(Article article, Model model) {
		model.addAttribute("article", article);
		return "modules/cms/showReason";
	}
	// end

	// add hefeng
	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = { "articlelist" })
	public String articlelist(Article article, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Article> page = articleService.findPage(new Page<Article>(request, response), article, true);
		model.addAttribute("page", page);
		return "modules/cms/articleList2";
	}

	@RequiresPermissions("cms:article:edit")
	@RequestMapping(value = "deleteUserArticle")
	public String deleteUserArticle(Article article, String categoryId, @RequestParam(required = false) Boolean isRe,
			RedirectAttributes redirectAttributes) {

		ArticleAttFile articleAttFile = new ArticleAttFile();
		articleAttFile.setActicleid(article.getId());

		articleService.deleteUserArticle(article, isRe);

		// 关联已删除文章的一系列附件删除 huangmj6 2015.10.21
		articleAttFileService.deleteList(articleAttFile);

		addMessage(redirectAttributes, (isRe != null && isRe ? "发布" : "删除") + "文章成功");
		return "redirect:" + adminPath + "/sys/user/uploaded/?repage&delFlag=2&category.id=" + ("");
	}
	// end

	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = "form")
	public String form(Article article, Model model) {
		// System.out.println("1111");
		// 如果当前传参有子节点，则选择取消传参选择
		if (article.getCategory() != null
				&& org.apache.commons.lang3.StringUtils.isNotBlank(article.getCategory().getId())) {
			// System.out.println("getCategory");
			List<Category> list = categoryService.findByParentId(article.getCategory().getId(),
					Site.getCurrentSiteId());
			if (list.size() > 0) {
				article.setCategory(null);
			} else {
				// System.out.println("categoryService");
				article.setCategory(categoryService.get(article.getCategory().getId()));
				// System.out.println("getCategoryID:"+article.getCategory().getId()+":"+article.getCategory().getName());
			}
		}

		// article.setCategory(categoryService.get(article.getCategory().getId()));

		article.setArticleData(articleDataService.get(article.getId()));
		// if (article.getCategory()==null &&
		// StringUtils.isNotBlank(article.getCategory().getId())){
		// Category category =
		// categoryService.get(article.getCategory().getId());
		// }

		// 查出对应文章的附件记录 start huangmj 2015.10.21
		ArticleAttFile articleAttFile = new ArticleAttFile();
		articleAttFile.setActicleid(article.getId());
		List<ArticleAttFile> listArticleAttFile = articleAttFileService.findListFile(articleAttFile);
		// System.out.println("listArticleAttFile:"+listArticleAttFile.size());

		// 查出对应的专题记录
		List<FeaturePackage> fealist = featureService.getListByArticle(article);

		if (fealist != null && fealist.size() > 0) {
			model.addAttribute("featurelist", fealist);
		}

		// Label Service start huangmj 2015.10.21
		Article ar = new Article();
		ar = articleService.get(article);
		Label label = new Label();
		List<Label> listlabel = labelService.findList(label);
		label.setUserid(ar.getUser().getId());
		List<Label> unexalabel = labelService.getUnexamineLabel(label);
		for (Label la : unexalabel) {
			StringBuffer sb = new StringBuffer();
			sb.append(la.getLabelvalue());
			la.setLabelvalue(sb.toString());
			listlabel.add(la);
		}
		// 取出对应知识分类的绑定标签
		String categoryid = article.getCategory().getId();
		if (categoryid != null && !categoryid.equals("")) {
			List<Label> categorylabel = labelService.findLabelBycategoryId(categoryid);
			if (categorylabel != null && categorylabel.size() > 0) {
				for (int i = 0; i < categorylabel.size(); i++) {
					for (int j = 0; j < listlabel.size(); j++) {
						if (categorylabel.get(i).getId().equals(listlabel.get(j).getId())) {
							listlabel.get(j).setCategoryid("1");
						}
					}
				}
			}
		}

		// System.out.println("listlabel:"+listlabel.size()+":"+listlabel.toString());
		List<Label> listlabelofArticle = articleLabelService.findLabelByArticle(article.getId());

		for (int i = 0; i < listlabelofArticle.size(); i++) {
			for (int j = 0; j < listlabel.size(); j++) {
				if (listlabelofArticle.get(i).getId().equals(listlabel.get(j).getId())) {
					listlabel.get(j).setIschecked(1);
				}
			}
		}

		model.addAttribute("contentViewList", getTplContent());
		model.addAttribute("article_DEFAULT_TEMPLATE", Article.DEFAULT_TEMPLATE);

		ArticleData articleData = article.getArticleData();
		String content = articleData.getContent();
		if (StringUtils.isNoneBlank(content)) {
			articleData.setContent(content.replace("？", " ").replace("?", "").replace("&^^", "?"));
			article.setArticleData(articleData);
		}

		model.addAttribute("article", article);
		model.addAttribute("listArticleAttFile", listArticleAttFile);
		model.addAttribute("listlabelsize", listArticleAttFile.size());
		model.addAttribute("listlabel", listlabel);
		model.addAttribute("native", "1");
		model.addAttribute("realize", null);
		CmsUtils.addViewConfigAttribute(model, article.getCategory());
		return "modules/cms/articleForm";
	}

	/*
	 * @RequestParam String categoryid,@RequestParam String categoryname huangmj
	 * 2015.10.26,首页发布知识
	 */
	@RequiresPermissions("cms:view")
	@RequestMapping(value = "add_article")
	public String index1(Article article, Model model, RedirectAttributes redirectAttributes) {
		// 如果当前传参有子节点，则选择取消传参选择
		if (article.getCategory() != null
				&& org.apache.commons.lang3.StringUtils.isNotBlank(article.getCategory().getId())) {
			List<Category> list = categoryService.findByParentId(article.getCategory().getId(),
					Site.getCurrentSiteId());
			if (list.size() > 0) {
				article.setCategory(null);
			} else {
				article.setCategory(categoryService.get(article.getCategory().getId()));
			}
		}
		article.setArticleData(articleDataService.get(article.getId()));
		// 查出对应文章的附件记录 start huangmj 2015.10.21
		ArticleAttFile articleAttFile = new ArticleAttFile();
		articleAttFile.setActicleid(article.getId());
		List<ArticleAttFile> listArticleAttFile = articleAttFileService.findListFile(articleAttFile);
		// System.out.println("listArticleAttFile:"+listArticleAttFile.size());

		// Label Service start huangmj 2015.10.21
		Label label = new Label();
		List<Label> listlabel = labelService.findList(label);
		List<Label> unexalabel = labelService.getUnexamineLabel(new Label());
		for (Label la : unexalabel) {
			StringBuffer sb = new StringBuffer();
			sb.append(la.getLabelvalue());
			la.setLabelvalue(sb.toString());
			listlabel.add(la);
		}
		// System.out.println("listlabel:"+listlabel.size()+":"+listlabel.toString());
		List<Label> listlabelofArticle = articleLabelService.findLabelByArticle(article.getId());

		for (int i = 0; i < listlabelofArticle.size(); i++) {
			for (int j = 0; j < listlabel.size(); j++) {
				if (listlabelofArticle.get(i).getId().equals(listlabel.get(j).getId())) {
					listlabel.get(j).setIschecked(1);
				}
			}
		}

		model.addAttribute("article_DEFAULT_TEMPLATE", Article.DEFAULT_TEMPLATE);
		model.addAttribute("article", article);
		model.addAttribute("listArticleAttFile", listArticleAttFile);
		model.addAttribute("listlabelsize", listArticleAttFile.size());
		model.addAttribute("listlabel", listlabel);
		model.addAttribute("native", "1");
		model.addAttribute("front", "f");
		model.addAttribute("realize", null);
		if (article.getCategory() != null) {
			CmsUtils.addViewConfigAttribute(model, article.getCategory());
		}
		model.addAttribute("indexx", "0");
		// model.addAttribute("categoryid",categoryid);
		// model.addAttribute("categoryname",categoryname);
		return "modules/cms/articleForm";
	}

	/*
	 * @RequestParam String categoryid,@RequestParam String categoryname huangmj
	 * 2015.10.26,首页发布知识
	 */
	@RequiresPermissions("cms:view")
	@RequestMapping(value = "add_person_article")
	public String add_person_article(Article article, Model model, RedirectAttributes redirectAttributes) {
		// 如果当前传参有子节点，则选择取消传参选择
		if (article.getCategory() != null
				&& org.apache.commons.lang3.StringUtils.isNotBlank(article.getCategory().getId())) {
			List<Category> list = categoryService.findByParentId(article.getCategory().getId(),
					Site.getCurrentSiteId());
			if (list.size() > 0) {
				article.setCategory(null);
			} else {
				article.setCategory(categoryService.get(article.getCategory().getId()));
			}
		}
		article.setArticleData(articleDataService.get(article.getId()));
		// 查出对应文章的附件记录 start huangmj 2015.10.21
		ArticleAttFile articleAttFile = new ArticleAttFile();
		articleAttFile.setActicleid(article.getId());
		List<ArticleAttFile> listArticleAttFile = articleAttFileService.findListFile(articleAttFile);
		// System.out.println("listArticleAttFile:"+listArticleAttFile.size());

		// Label Service start huangmj 2015.10.21
		Label label = new Label();
		List<Label> listlabel = labelService.findList(label);
		List<Label> unexalabel = labelService.getUnexamineLabel(new Label());
		for (Label la : unexalabel) {
			StringBuffer sb = new StringBuffer();
			sb.append(la.getLabelvalue() + "(未审批)");
			la.setLabelvalue(sb.toString());
			listlabel.add(la);
		}
		// System.out.println("listlabel:"+listlabel.size()+":"+listlabel.toString());
		List<Label> listlabelofArticle = articleLabelService.findLabelByArticle(article.getId());

		for (int i = 0; i < listlabelofArticle.size(); i++) {
			for (int j = 0; j < listlabel.size(); j++) {
				if (listlabelofArticle.get(i).getId().equals(listlabel.get(j).getId())) {
					listlabel.get(j).setIschecked(1);
				}
			}
		}

		model.addAttribute("article_DEFAULT_TEMPLATE", Article.DEFAULT_TEMPLATE);
		model.addAttribute("article", article);
		model.addAttribute("listArticleAttFile", listArticleAttFile);
		model.addAttribute("listlabelsize", listArticleAttFile.size());
		model.addAttribute("listlabel", listlabel);
		model.addAttribute("native", "1");
		model.addAttribute("person", "p");
		model.addAttribute("realize", null);
		if (article.getCategory() != null) {
			CmsUtils.addViewConfigAttribute(model, article.getCategory());
		}
		model.addAttribute("indexx", "0");
		// model.addAttribute("categoryid",categoryid);
		// model.addAttribute("categoryname",categoryname);
		return "modules/cms/articleForm";
	}

	@RequiresPermissions("cms:article:edit")
	@RequestMapping(value = "save")
	public String save(Article article, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request,
			HttpServletResponse response) {

		if ((article.getIsOriginal()).equals("1")) {
			article.setOriginalreason("");
		}

		String temp_article_id = request.getParameter("cookie_guid");
		String article_id = request.getParameter("current_article_id");
		String attfile_temp_guid = request.getParameter("attfile_temp_guid");
		String save_key = request.getParameter("save_key");
		String personal_key = request.getParameter("person_save_key");
		String front_save_key = request.getParameter("front_save_key");
		
		String[] featurelist = request.getParameterValues("featurelist");

		// 查出专题历史记录

		String id = new String();
		if (article.getId() != null && !article.getId().equals("")) {
			id = article.getId();
		} else {
			id = temp_article_id;
		}
		if (featurelist != null && featurelist.length > 0) {
			List<String> list = Arrays.asList(featurelist);

			featureService.saveRelationInFeatureAndArticle(id, list);
		} else {
			List<FeaturePackage> fealist = featureService.getListByArticle(article);
			if (fealist != null && fealist.size() > 0) {
				if (featurelist == null || featurelist.length == 0) {
					featureService.batchDeleteForArticle(id);
				}
			}
		}
		// System.out.println("front_save_key:"+front_save_key);
		// System.out.println("personal_key:"+personal_key);
		// System.out.println("save_key:"+save_key);
		// System.out.println("getContent:"+article.getArticleData().getContent());
		// System.out.println("temp_article_id_t:"+temp_article_id+"article_id_t:"+article_id);

		String[] selectTag = null;

		if (request.getParameter("autoSave") != null) {
			String tags = request.getParameter("selectTag");
			if (StringUtils.isNoneBlank(tags)) {
				selectTag = CmsUtils.transform(tags, ",").toArray(new String[] {});
			}
		} else
			selectTag = request.getParameterValues("selectTag");

		// LOGGER.info("前台标签"+selectTag);
		List<String> labellist = new ArrayList<String>();
		if (selectTag != null) {
			for (int i = 0; i < selectTag.length; i++) {
				// String selectTageach=selectTag[i];
				labellist.add(selectTag[i]);
				// System.out.println("标签ID："+selectTageach);
			}
		}

		/*
		 * 读cookie article_id ：新增文章时为null，更新文章反之 temp_article_id
		 * ：新增文章时为文章ID，更新文章用不到该数据 huangmj6 2015.10.21
		 *
		 * String article_id = ""; String category_id=""; String temp_article_id
		 * = ""; Cookie[] cookies = request.getCookies(); if(cookies!=null){ for
		 * (int i = 0; i < cookies.length; i++){ Cookie c = cookies[i];
		 * if(c.getName().equalsIgnoreCase("cookie_guid")){ temp_article_id =
		 * c.getValue();
		 * //System.out.println("cookie->temp_article_id:"+temp_article_id);
		 * }else if(c.getName().equalsIgnoreCase("current_article_id")){
		 * article_id = c.getValue();
		 * //System.out.println("cookie->article_id:"+article_id); } } }
		 */
		// if (!beanValidator(model, article)){
		// return form(article, model);
		// }

		if (article_id != null && article_id.length() != 0) {

			// 修改文章状态调用save->update huangmj6 2015.10.21
			// System.out.println("just update");

			// add hefeng 知识更改保存之前，与数据库对比区别

			// add hefeng
			String flag = "00";// 标记知识和附件是否更改

			if (articleService.get(article.getId()) != null) {
				// ArticleData
				// articledata2=articleDataService.get(article.getId());
				String articledbdata = articleDataService.get(article.getId()).getContent();// 数据库的原始内容数据
				// 页面上的数据转码(不使用2016.3.4)
				/*
				 * if (article.getArticleData().getContent()!=null){
				 * article.getArticleData().setContent(StringEscapeUtils.
				 * unescapeHtml4( article.getArticleData().getContent())); }
				 */
				// 数据库的数据转码
				// StringEscapeUtils.escapeHtml4(s2);
				// String s1=article.getArticleData().getContent();//页面上的数据
				if (!article.getArticleData().getContent().equals(StringEscapeUtils.escapeHtml4(articledbdata))) {
					// articleService.updateUserMessage(article);
					flag = "10";
				}
			}

			// end hefeng

			// 知识附件更改(新增)保存之前（删除待定）
			int sumdiff = 0;// 临时文件总数
			List<ArticleAttFile> list = articleAttFileService.finddiffByGuid(attfile_temp_guid);
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getIspostarticle().equals("0")) {
					sumdiff++;
				}
			}
			if (sumdiff > 0) {
				// articleService.updateUserMessage(article,sumdiff);
				if (flag.equals("10")) {
					flag = "11";
				} else {
					flag = "01";
				}
			}
			// 标志简介：无更新（00），内容更新（10），附件更新（01），内容和附件更新（11）
			article.setRemarks(flag);// 把内容或附件是否更改写入数据库
			// end hefeng

			// add hefeng 如果更改知识分类，更新其他表的分类id
			String originalcategoryId = articleService.get(article.getId()).getCategory().getId().toString();
			String categoryId = article.getCategory().getId().toString();
			if (originalcategoryId.equals(categoryId)) {

			} else {
				CmsUtils.MergeArticle(originalcategoryId, categoryId, article.getId());
			}
			// end hefeng
			if (save_key == null) {
				article.setDelFlag("4");
			} else {
				article.setDelFlag("2");
			}

			/**
			 * 专家审核之后 专题必须还是待专家审核 start
			 */
			if (isProfessor())

				article.setDelFlag("3");

			/**
			 * end
			 */

			// 增加逻辑:当文章重新被修改后,articleData.copyfrom(是否同步app)改为未同步,已修改
			String copy = article.getArticleData().getCopyfrom();
			if (copy != null && !copy.equals("0")) {
				article.getArticleData().setCopyfrom("2");
			} else {
				article.getArticleData().setCopyfrom("0");
			}

			articleService.save(article);

			// 发布附附件
			articleAttFileService.postattfile(attfile_temp_guid);

			articleLabelService.save(labellist, article);

		} else {
			// System.out.println("new add");
			// 新增文章状态下调用sava_insert->新增的文章ID为前台页面request带的cookie['temp_article_id']
			// huangmj6 2015.10.21

			if (save_key == null) {
				article.setDelFlag("4");
			} else {
				article.setDelFlag("2");
			}
			/// 增加逻辑:新增文章未同步app(articleData.copyfrom)
			article.getArticleData().setCopyfrom("0");

			try {
				articleService.save_insert(article, temp_article_id);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 发布附附件
			articleAttFileService.postattfile(attfile_temp_guid);
			if (save_key == null) {
				addMessage(redirectAttributes, "暂存文章'" + StringUtils.abbr(article.getTitle(), 50) + "'成功");
				articleLabelService.save(labellist, article);
				/**
				 * 新增5分钟暂存
				 */
				if (request.getParameter("preFlag") != null) {
					try {
						response.getWriter().write("{'articleId':'" + article.getId() + "'}");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				;
				/**
				 * end
				 */
				return "redirect:" + adminPath + "/cms/article/add_article?id=" + article.getId();
			} else {
				addMessage(redirectAttributes, "保存文章'" + StringUtils.abbr(article.getTitle(), 50) + "'成功");
				articleLabelService.save(labellist, article);
				return "redirect:" + adminPath + "/?login";
			}
		}

		if (personal_key != null) {// 个人中心，我的上传，保存调回个人中心，我的上传
			if (save_key == null) {
				addMessage(redirectAttributes, "暂存文章'" + StringUtils.abbr(article.getTitle(), 50) + "'成功");
				articleLabelService.save(labellist, article);
				return "redirect:" + adminPath + "/cms/article/form?id=" + article.getId();
			} else {
				addMessage(redirectAttributes, "保存文章'" + StringUtils.abbr(article.getTitle(), 50) + "'成功");
				return "redirect:" + adminPath + "/sys/user/uploaded";
			}
		} else if (front_save_key != null) {// 主页过来，保存调回主页
			if (save_key == null) {
				addMessage(redirectAttributes, "暂存文章'" + StringUtils.abbr(article.getTitle(), 50) + "'成功");
				articleLabelService.save(labellist, article);
				return "redirect:" + adminPath + "/cms/article/form?id=" + article.getId();
			} else {
				addMessage(redirectAttributes, "保存文章'" + StringUtils.abbr(article.getTitle(), 50) + "'成功");
				return "redirect:" + adminPath + "/?login";
			}
		} else {// 后台管理过来，保存调回文章列表
			if (save_key == null) {
				addMessage(redirectAttributes, "暂存文章'" + StringUtils.abbr(article.getTitle(), 50) + "'成功");
				articleLabelService.save(labellist, article);
				return "redirect:" + adminPath + "/cms/article/form?id=" + article.getId();
			} else {
				addMessage(redirectAttributes, "保存文章'" + StringUtils.abbr(article.getTitle(), 50) + "'成功");
				return "redirect:" + adminPath + "/cms";// article/?"+"&delFlag="+2;//category.id="+article.getCategory().getId()+
			}
		}
		// addMessage(redirectAttributes, "保存文章'" +
		// StringUtils.abbr(article.getTitle(),50) + "'成功");
		// String categoryId =
		// article.getCategory()!=null?article.getCategory().getId():null;
		// return "redirect:" + adminPath +
		// "/cms/article/?repage&category.id="+(categoryId!=null?categoryId:"");

	}

	@RequiresPermissions("cms:article:edit")
	@RequestMapping(value = "delete")
	public String delete(Article article, String categoryId, @RequestParam(required = false) Boolean isRe,
			RedirectAttributes redirectAttributes) {
		// 如果没有审核权限，则不允许删除或发布。
		if (!UserUtils.getSubject().isPermitted("cms:article:audit")) {
			addMessage(redirectAttributes, "你没有删除或发布权限");
		}

		ArticleAttFile articleAttFile = new ArticleAttFile();
		articleAttFile.setActicleid(article.getId());

		articleService.delete(article, isRe);

		// 关联已删除文章的一系列附件删除 huangmj6 2015.10.21
		articleAttFileService.deleteList(articleAttFile);

		addMessage(redirectAttributes, (isRe != null && isRe ? "上架" : "删除") + "文章成功");
		return "redirect:" + adminPath + "/cms/article/?repage&category.id=" + (categoryId != null ? categoryId : "");
	}

	/**
	 * 处理知识审核状态-->发布
	 * 
	 * @param response
	 * @param request
	 * @return
	 * @throws IOException
	 * @author huangmj,2015.10.15
	 */
	@RequiresPermissions("cms:article:edit")
	@RequestMapping(value = "update")
	public String update(Article article, String categoryId, @RequestParam(required = false) Boolean isRe,
			RedirectAttributes redirectAttributes) {
		// 如果没有审核权限，则不允许删除或发布。
		if (!UserUtils.getSubject().isPermitted("cms:article:audit")) {
			addMessage(redirectAttributes, "你没有删除或发布权限");
		}
		articleService.update(article);
		// add hefeng 当知识发布审核上架后，更新消息
		articleService.PretreatmentUpdateUserMsg(article.getId());
		articleService.revertMsgFlag(article.getId());
		// end hefeng
		addMessage(redirectAttributes, (isRe != null && isRe ? "上架" : "删除") + "文章成功");
		return "redirect:" + adminPath + "/cms/article/?repage&category.id=" + (categoryId != null ? categoryId : "");
	}

	/**
	 * 文章选择列表
	 */
	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = "selectList")
	public String selectList(Article article, HttpServletRequest request, HttpServletResponse response, Model model) {
		list(article, request, response, model);
		return "modules/cms/articleSelectList";
	}

	/**
	 * 通过编号获取文章标题
	 */
	@RequiresPermissions("cms:article:view")
	@ResponseBody
	@RequestMapping(value = "findByIds")
	public String findByIds(String ids) {
		List<Object[]> list = articleService.findByIds(ids);
		return JsonMapper.nonDefaultMapper().toJson(list);
	}

	private List<String> getTplContent() {
		List<String> tplList = fileTplService
				.getNameListByPrefix(siteService.get(Site.getCurrentSiteId()).getSolutionPath());
		tplList = TplUtils.tplTrim(tplList, Article.DEFAULT_TEMPLATE, "");
		return tplList;
	}

	//
	// public void inputstreamtofile(InputStream ins,File file){
	// OutputStream os = null;
	// try {
	// os = new FileOutputStream(file);
	// } catch (FileNotFoundException e1) {
	//
	// e1.printStackTrace();
	// }
	// int bytesRead = 0;
	// byte[] buffer = new byte[8192];
	// try {
	// while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
	// os.write(buffer, 0, bytesRead);
	// }
	// } catch (IOException e) {
	//
	// e.printStackTrace();
	// }
	// try {
	// os.close();
	// } catch (IOException e) {
	//
	// e.printStackTrace();
	// }
	// try {
	// ins.close();
	// } catch (IOException e) {
	//
	// e.printStackTrace();
	// }
	// }
	//

	// plupload文件上传
	@ResponseBody
	@RequestMapping(value = "/plupload", method = RequestMethod.POST)
	public String plupload(@RequestParam MultipartFile file, HttpServletRequest request, HttpSession session)
			throws IOException {
		String tempArticleId = request.getParameter("cookie_guid");
		String articleId = request.getParameter("current_article_id");
		String attfileTempGuid = request.getParameter("attfile_temp_guid");
		String responseStr = "true";
		JSONObject json = new JSONObject();

		// 获取需上传文件
		// 获取原文件名
		String fileName = file.getOriginalFilename();
		long fileSize = file.getSize();

		// 获取文件类型
		String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
		String fileTypesave = new String(fileType);
		File outputFilePdf = null;
		InputStream inputStreanmPdf = null;
		// 临时存在本地的文件
		File inputFile = null;
		// 本地临时文件的路径
		String inputFilePath = null;

		// 临时文件缓存储在服务器下的D盘temp文件夹下，上传OSS后删除
		inputFile = new File(FileStorageUtils.kms_tempfile_path + fileName);
		inputFilePath = FileStorageUtils.kms_tempfile_path + fileName;
		// 将文件拷贝到inputFile临时文件夹下
		FileCopyUtils.copy(file.getBytes(), inputFile);

		// OSS显示的名字oss_file_key=>库名.知识名.时间.原文件名
		Date dt = new Date();
		Long time = dt.getTime();// 这就是距离1970年1月1日0点0分0秒的毫秒数
		String ossFileKey = "";
		// 如果是修改的话 知识ID+时间+原文件名
		// 如果是新增的话 cookID+时间+原文件名
		if (!StringUtils.isBlank(articleId)) {
			ossFileKey = articleId + "." + time + "." + fileName;
		} else {
			ossFileKey = tempArticleId + "." + time + "." + fileName;
		}
		// 保证线程安全,使用线程池的概念,cachedThreadPool:复用线程
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

		UploadThreadUtils upload = new UploadThreadUtils(file, articleId, tempArticleId, ossFileKey);
		cachedThreadPool.execute(upload);
		//
		//
		// if(FileStorageUtils.contentType(fileType)){
		// //FiletoPDF start
		// outputFilePdf = new
		// File(FileStorageUtils.kms_tempfile_path+fileName+".pdf");
		// SaveAsPDF op = new SaveAsPDF(inputFile,outputFilePdf);
		// responseStr=op.saveAsPDF();
		// inputStreanmPdf = new FileInputStream(outputFilePdf);
		// }
		// //FiletoPDF end
		//
		// // 上传Object
		// if(!FileStorageUtils.filevideo(fileType).equals("0")){
		//
		// //上传视频,mp4,avi,wmv
		// String outputFilePath = inputFilePath+".flv";
		// ConvertVideo convertVideo =new
		// ConvertVideo(inputFilePath,outputFilePath);
		// if (convertVideo.process()) {
		// //源文件上传
		// PutObjectResult
		// result=FileStorageUtils.upload(ossFileKey,inputFile,fileName,fileType,
		// fileSize,file.getInputStream());
		//
		// //flv文件上传
		// String oss_file_flv_key = ossFileKey+".flv";
		// File flvFile = new File(outputFilePath);
		// String flvFileName = "";
		// String flvFileType = "newflv";
		// Long flvSize = flvFile.length();
		// InputStream flvinputStream = new FileInputStream(outputFilePath);
		// PutObjectResult
		// resultFlv=FileStorageUtils.upload(oss_file_flv_key,flvFile,outputFilePath,flvFileType,flvSize,flvinputStream);
		//
		// }
		//
		// }else{
		// //上传其他
		// PutObjectResult result=FileStorageUtils.upload(ossFileKey,
		// inputFile,fileName,fileType,fileSize,file.getInputStream());
		// }
		//
		// //上传newPDF
		// if(FileStorageUtils.contentType(fileType)){
		// fileType="newpdf";
		// String ossFilePdfKey=ossFileKey+".pdf";
		// PutObjectResult resultPdf =FileStorageUtils.upload(ossFilePdfKey,
		// outputFilePdf,fileName,fileType,outputFilePdf.length(),inputStreanmPdf);
		// }
		//
		// //获取的文件流上传到阿里云并写入数据库--end
		//
		// FileDelete fileDelete = new FileDelete();
		// //删除临时文件
		// fileDelete.deleteFile(FileStorageUtils.kms_tempfile_path+fileName);
		// if(fileType.equals("newpdf")){
		// fileDelete.deleteFile(FileStorageUtils.kms_tempfile_path+fileName+".pdf");
		// }
		//
		// //删除flv视频
		// if(!FileStorageUtils.filevideo(fileType).equals("0")){
		// fileDelete.deleteFile(FileStorageUtils.kms_tempfile_path+fileName+".flv");
		// }

		// add by yangshw6
		// 写入数据库附件信息start
		ArticleAttFile articleAttFile = new ArticleAttFile();

		// 设置附件关联文章ID
		if (!StringUtils.isBlank(articleId)) {
			// 修改文章状态，启用原有文章ID
			articleAttFile.setActicleid(articleId);
		} else {
			// 新增文章状态，启用前端用cookie带过来js生成的GUID
			articleAttFile.setActicleid(tempArticleId);
		}

		// 设置OSS存放的文件key值
		articleAttFile.setAttfilekey(ossFileKey);

		// 设置附件原始名称
		articleAttFile.setAttfilename(fileName);

		// 设置附件时间戳
		articleAttFile.setAttfiletime(Long.toString(time));

		// 设置附件大小
		articleAttFile.setAttfilesize(Long.toString(fileSize));

		// 获取文件后缀名
		articleAttFile.setAttfiletype(fileTypesave);

		articleAttFile.setAttfile_temp_guid(attfileTempGuid);

		// 存入数据库Service
		String Attfileid = articleAttFileService.save(articleAttFile);
		// 写入数据库附件信息end

		System.out.println(Attfileid);
		json.put("flag", "true");
		json.put("Attfileid", Attfileid);
		return json.toString();

	}

	/**
	 * 处理文件上传
	 * 
	 * @param response
	 * @param request
	 * @return
	 * @throws IOException
	 * @author huangmj,2015.10.15
	 */
	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = "uploadFile", method = RequestMethod.POST)
	@ResponseBody
	public String upload(HttpServletResponse response, HttpServletRequest request) throws IOException {
		// System.out.println("处理文件上传 in /uploadFile");
		String temp_article_id = request.getParameter("cookie_guid");
		// String category_id = request.getParameter("category_idd");
		String article_id = request.getParameter("current_article_id");
		String attfile_temp_guid = request.getParameter("attfile_temp_guid");
		// System.out.println("attfile_temp_guid:"+attfile_temp_guid);
		// System.out.println("temp_article_id:"+temp_article_id);
		// System.out.println("category_id_t:"+category_id);
		// System.out.println("article_id:"+article_id);
		/*
		 * 读cookie category_id :文章归属库 article_id ：新增文章时为null，更新文章反之
		 * temp_article_id ：新增文章时为文章ID，更新文章用不到该数据
		 *
		 * String article_id = ""; String category_id=""; String temp_article_id
		 * = ""; Cookie[] cookies = request.getCookies(); if(cookies!=null){ for
		 * (int i = 0; i < cookies.length; i++){ Cookie c = cookies[i];
		 * if(c.getName().equalsIgnoreCase("cookie_guid")){ temp_article_id =
		 * c.getValue();
		 * System.out.println("cookie->temp_article_id:"+temp_article_id); }else
		 * if(c.getName().equalsIgnoreCase("category_id")){ category_id =
		 * c.getValue(); System.out.println("cookie->category_id:"+category_id);
		 * }else if(c.getName().equalsIgnoreCase("current_article_id")){
		 * article_id = c.getValue();
		 * System.out.println("cookie->article_id:"+article_id); } } }
		 */
		String responseStr = "";
		String fileType = "";
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		String fileName = null;
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {

			// 获取需上传文件
			MultipartFile mf = entity.getValue();
			// 获取原文件名
			fileName = mf.getOriginalFilename();

			// 获取文件类型
			fileType = mf.getOriginalFilename().substring(mf.getOriginalFilename().lastIndexOf(".") + 1);

			File outputFile_pdf = null;
			InputStream inputStreanm_pdf = null;
			// 临时存在本地的文件
			File inputFile = null;
			// 本地临时文件的路径
			String inputFilePath = null;

			// 临时文件缓存储在服务器下的D盘temp文件夹下，上传OSS后删除
			inputFile = new File(FileStorageUtils.kms_tempfile_path + fileName);
			inputFilePath = FileStorageUtils.kms_tempfile_path + fileName;
			// 将文件拷贝到inputFile临时文件夹下
			FileCopyUtils.copy(mf.getBytes(), inputFile);

			// FileCopyUtils.copy(mf.getBytes(), uploadFile);
			// OSS显示的名字oss_file_key=>库名.知识名.时间.原文件名
			Date dt = new Date();
			Long time = dt.getTime();// 这就是距离1970年1月1日0点0分0秒的毫秒数
			String oss_file_key = "";
			// 如果是修改的话 知识ID+时间+原文件名
			// 如果是新增的话 cookID+时间+原文件名
			if (article_id != null && article_id.length() != 0) {
				oss_file_key = article_id + "." + time + "." + fileName;
			} else {
				oss_file_key = temp_article_id + "." + time + "." + fileName;
			}

			// System.out.println("oss显示的原件名字=>"+oss_file_key);

			// 写入数据库附件信息start
			ArticleAttFile articleAttFile = new ArticleAttFile();

			// 设置附件关联文章ID
			if (article_id != null && article_id.length() != 0) {
				// 修改文章状态，启用原有文章ID
				articleAttFile.setActicleid(article_id);
			} else {
				// 新增文章状态，启用前端用cookie带过来js生成的GUID
				articleAttFile.setActicleid(temp_article_id);
			}

			// 设置OSS存放的文件key值
			articleAttFile.setAttfilekey(oss_file_key);

			// 设置附件原始名称
			articleAttFile.setAttfilename(fileName);

			// 设置附件时间戳
			articleAttFile.setAttfiletime(Long.toString(time));

			// 设置附件大小
			articleAttFile.setAttfilesize(Long.toString(mf.getSize()));

			// 获取文件后缀名
			articleAttFile
					.setAttfiletype(mf.getOriginalFilename().substring(mf.getOriginalFilename().lastIndexOf(".") + 1));

			articleAttFile.setAttfile_temp_guid(attfile_temp_guid);
			// articleAttFile.setIspostarticle("1");

			// 存入数据库Service
			articleAttFileService.save(articleAttFile);
			// 写入数据库附件信息end

			if (FileStorageUtils.contentType(fileType)) {
				// FiletoPDF start
				// System.out.println("File need to FDP ....");
				outputFile_pdf = new File(FileStorageUtils.kms_tempfile_path + fileName + ".pdf");
				Office2PdfUtil op = new Office2PdfUtil(inputFile, outputFile_pdf);
				op.docToPdf();

				// System.out.println("start upload to OSS...");
				inputStreanm_pdf = new FileInputStream(outputFile_pdf);
			}
			// FiletoPDF end

			// 上传Object
			if (!FileStorageUtils.filevideo(fileType).equals("0")) {
				// System.out.println("File need to flv");

				// 上传视频,mp4,avi,wmv
				String outputFilePath = inputFilePath + ".flv";
				ConvertVideo convertVideo = new ConvertVideo(inputFilePath, outputFilePath);
				if (convertVideo.process()) {
					// System.out.println("ok to flv");
					// 源文件上传
					PutObjectResult result = FileStorageUtils.upload(oss_file_key, inputFile, fileName, fileType,
							mf.getSize(), mf.getInputStream());

					// flv文件上传
					String oss_file_flv_key = oss_file_key + ".flv";
					File flvFile = new File(outputFilePath);
					String flvFileName = "";
					String flvFileType = "newflv";
					Long flvSize = flvFile.length();
					InputStream flvinputStream = new FileInputStream(outputFilePath);
					PutObjectResult result_flv = FileStorageUtils.upload(oss_file_flv_key, flvFile, outputFilePath,
							flvFileType, flvSize, flvinputStream);

					// System.out.println("result:"+result.getETag());
					// System.out.println("result_flv:"+result_flv.getETag());
				}

			} else {
				// 上传其他
				PutObjectResult result = FileStorageUtils.upload(oss_file_key, inputFile, fileName, fileType,
						mf.getSize(), mf.getInputStream());
				// System.out.println(result.getETag());
			}

			// 上传newPDF
			if (FileStorageUtils.contentType(fileType)) {
				fileType = "newpdf";
				String oss_file_pdf_key = oss_file_key + ".pdf";
				// System.out.println("oss显示的pdf件名字=>"+oss_file_pdf_key);
				PutObjectResult result_pdf = FileStorageUtils.upload(oss_file_pdf_key, outputFile_pdf, fileName,
						fileType, outputFile_pdf.length(), inputStreanm_pdf);
				// System.out.println("result_pdf:"+result_pdf.getETag());
			}

			// 获取的文件流上传到阿里云并写入数据库--end

			responseStr = fileName + "上传成功";
		}

		FileDelete fileDelete = new FileDelete();
		// 删除临时文件
		// System.out.println("删除"+FileStorageUtils.kms_tempfile_path+fileName);
		fileDelete.deleteFile(FileStorageUtils.kms_tempfile_path + fileName);
		if (fileType.equals("newpdf")) {
			// System.out.println("删除"+FileStorageUtils.kms_tempfile_path+fileName+".pdf");
			fileDelete.deleteFile(FileStorageUtils.kms_tempfile_path + fileName + ".pdf");
		}

		// 删除flv视频
		if (!FileStorageUtils.filevideo(fileType).equals("0")) {
			fileDelete.deleteFile(FileStorageUtils.kms_tempfile_path + fileName + ".flv");
		}
		// System.out.println(responseStr);
		return responseStr;
	}

	/**
	 * 处理文件删除，update DEL_FLAG为1=>已删除；DEL_FLAG为0=>正常
	 * 
	 * @param response
	 * @param request
	 * @return
	 * @throws @author
	 *             huangmj,2015.10.20
	 */
	@RequiresPermissions("cms:article:edit")
	@ResponseBody
	@RequestMapping(value = "deleteattfile")
	public String deleteattfile(ArticleAttFile articleAttFile, HttpServletResponse response) {
		// System.out.println("删除:"+articleAttFile.getId()+"文章ID:"+articleAttFile.getActicleid());
		// 如果没有审核权限，则不允许删除或发布。
		String deletResult = "";
		if (!UserUtils.getSubject().isPermitted("cms:article:audit")) {
			// addMessage(redirectAttributes, "你没有删除或发布权限");
			return "你没有删除或发布权限";
		} else {
			articleAttFileService.deleteAttfile(articleAttFile);
			deletResult = "删除成功";
			return deletResult;
		}

		// return "redirect:" + adminPath +
		// "/cms/article/form?id="+articleAttFile.getActicleid();
	}

	@RequiresPermissions("cms:article:view")
	@ResponseBody
	@RequestMapping(value = "viewattfile")
	public String viewattfile(ArticleAttFile articleAttFile) {
		// System.out.println("viewattfile");
		articleAttFile = articleAttFileService.findFile(articleAttFile);
		String attfileUrl = FileStorageUtils.gerObjectUrl(articleAttFile.getAttfilekey() + ".pdf");
		// System.out.println("viewattfile:"+attfileUrl);
		return attfileUrl;
	}

	/**
	 * 处理文件下载
	 * 
	 * @param response
	 * @param request
	 * @return
	 * @throws @author
	 *             huangmj,2015.10.20
	 */
	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = "downloadattfile")
	public void download(ArticleAttFile articleAttFile, HttpServletResponse response) {
		articleAttFile = articleAttFileService.findFile(articleAttFile);
		// System.out.println("下载id:"+articleAttFile.getId()+"filekey"+articleAttFile.getAttfilekey());
		String filepath = "";
		InputStream inputStream = null;
		OutputStream outputStream = null;
		byte[] b = new byte[1024];
		int len = 0;
		try {
			// 获取目标下载文件流
			inputStream = FileStorageUtils.download(articleAttFile.getAttfilekey());
			outputStream = response.getOutputStream();
			response.setContentType("application/force-download");
			String filename = articleAttFile.getAttfilename();
			response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
			response.setContentLength((int) Integer.parseInt(articleAttFile.getAttfilesize()));

			while ((len = inputStream.read(b)) != -1) {
				outputStream.write(b, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
					inputStream = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
					outputStream = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 处理pdf文件预览
	 * 
	 * @param response
	 * @param request
	 * @return
	 * @throws @author
	 *             huangmj,2015.10.20
	 */
	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = "viewfile")
	public String viewfile(ArticleAttFile articleAttFile, Model model, HttpServletResponse response) {
		articleAttFile = articleAttFileService.findFile(articleAttFile);
		// System.out.println("id:"+articleAttFile.getId()+"filekey"+articleAttFile.getAttfilekey());
		model.addAttribute("articleAttFile", articleAttFile);
		return "modules/cms/viewfile";
	}

	/**
	 * 处理文件上传
	 * 
	 * @param response
	 * @param request
	 * @return
	 * @throws IOException
	 * @author yinshh3,2015.12.7
	 */
	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = "/uploadpicture", method = RequestMethod.POST)
	@ResponseBody
	public String uploadpicture(HttpServletResponse response, HttpServletRequest request) throws IOException {
		// System.out.println("处理文件上传 in /uploadFile");
		String switch_id = request.getParameter("switch_id");
		String responseStr = "";
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		String fileName = null;
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {

			// 获取需上传文件
			MultipartFile mf = entity.getValue();
			// 获取原文件名
			fileName = mf.getOriginalFilename();
			int lastIndex = fileName.lastIndexOf(".");
			String suffix = fileName.substring(lastIndex + 1, fileName.length());

			File inputFile = null;
			String path = request.getSession().getServletContext().getRealPath("");
			String pic_url = path + "\\static\\dist\\images\\" + fileName;
			// System.out.println("pic_url:"+pic_url);
			inputFile = new File(pic_url);
			FileCopyUtils.copy(mf.getBytes(), inputFile);

			String imgUrl = "";
			String fileName1 = IdGen.uuid() + "." + suffix;
			try {
				imgUrl = FileStorageUtils.putObject(fileName1, pic_url);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			// System.out.println(imgUrl);
			// 写入数据库附件信息start
			Switch switchOnly = new Switch();
			switchOnly = switchService.get(switch_id);
			switchOnly.setImageUrl(imgUrl);
			// System.out.println("写入数据库附件信息!!!!!!!!!!"+JsonMapper.toJsonString(switchOnly));
			switchService.update(switchOnly);
			// 写入数据库附件信息end
			responseStr = imgUrl;
		}
		return responseStr;
	}

	@RequestMapping(value = "/excel", method = RequestMethod.POST)
	@ResponseBody
	public String excel(@RequestParam MultipartFile file, HttpServletRequest request) throws IOException {
		JSONObject json = new JSONObject();
		try {
			Map<Integer, List<?>> data = new ImportExcel().getData(file.getOriginalFilename(),
					new Class[] { ECategory.class }, file.getInputStream());
			List<ECategory> categorys = (List<ECategory>) data.get(0);// 文件页数
			json = categoryService.getlist(categorys);
		} catch (Exception e) {

			e.printStackTrace();
			logger.error("插入信息有误!", e);
			json.put("flag", "导入失败");
			json.put("msg", "插入信息有误!");
			json.put("why", "未知");
			json.put("list", "");
		}
		return json.toString();
	}

	/**
	 * @param articleId
	 *            知识ID
	 * @return
	 * @author Hotusm
	 * @date 2016/8/31
	 * @description 三期需求 选择分类的时候 不能将所有的标签都去掉 只能去掉原有的分类关联 但是用户自己选择的标签不需要 去掉
	 * 
	 * @see WebRoot/WEB-INF/tags/sys/treeselect2.tag
	 */
	@RequestMapping(value = "articleLabels", method = RequestMethod.POST)
	@ResponseBody
	public List<Label> articleLabels(@RequestParam("articleId") String articleId) {
		List<Label> lables = articleLabelService.findLabelByArticle(articleId);
		if (lables == null) {
			lables = Lists.newArrayList();
		}
		return lables;
	}

	@RequestMapping("isPro")
	@ResponseBody
	public boolean isPlatformAdd() {

		User user = UserUtils.getUser();

		if (StringUtils.isNoneBlank(user.getId())) {

			if (user.getPassword().startsWith(PLStrategy.MD5PWD_PREFIX)) {

				return false;
			}
		}

		return true;
	}

	/**
	 * 获取知识未审核人
	 * 
	 * @param articleId
	 * @return
	 */
	@RequestMapping("preexamers")
	@ResponseBody
	public String articlePreExamers(@RequestParam(value = "id", required = true) String articleId) {

		Article article = articleService.get(articleId);

		// flag表示的是待审核或者待专家审核
		boolean flag = false;
		if ("2".equals(article.getDelFlag()) || "3".equals(article.getDelFlag())) {
			flag = true;
		} else {
			// 只需要在待审核中显示
			return "";
		}

		// //如果是已上架和下架的 &&这个是专家审核的 那么
		// if(!flag){
		//
		// if(article.getExaminer().getId().equals(UserUtils.getUser().getId())){
		//
		// return UserUtils.getUser().getName();
		// }
		// }

		try {
			List<User> users = articleService.articlePreExamers(articleId, flag);

			// 专家的id的集合
			List<String> psIds = null;

			boolean flag1 = false;
			// 表示的是待专家审核
			if ("3".equals(article.getDelFlag())) {

				flag1 = true;
				Role role = new Role();
				role.setId(professorId);
				psIds = systemService.hasUser(role);
			} else
				flag1 = false;

			if (users.size() > 0) {

				StringBuilder sb = new StringBuilder();
				for (User user : users) {

					if (flag1) {
						if (!psIds.contains(user.getId()))
							continue;
					}
					sb.append(user.getName() + ",");
				}
				return sb.substring(0, sb.length() > 0 ? sb.length() - 1 : 0);
			}
		} catch (Exception e) {
			LOGGER.error(e);
		}

		return "";
	}
	
	/**
	 * @throws Exception
	 * @throws JSONException
	 * @Candy 上传缩略图
	 */
	@ResponseBody
	@RequestMapping(value = "/upLoadImg.do", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ReturnApp upLoadImg(@RequestBody String sd, HttpServletRequest request) {
		ReturnApp returnApp = new ReturnApp();
		String shareImg = sd.split("=")[1];
		try {
			shareImg = URLDecoder.decode(shareImg,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
		if(StringUtils.isNotBlank(shareImg)) {
			String[] image = shareImg.split(",");
			String suffix = image[0];
			if (suffix.indexOf("data:image/") != -1) {
				suffix = suffix.replace("data:image/", "");
			}
			if (suffix.indexOf(";base64") != -1) {
				suffix = suffix.replace(";base64", "");
			}
			// 即将上传阿里云的临时目录
			String path = FileStorageUtils.kms_tempfile_path;
			// System.out.println(path);
			File files = new File(path);
			if (!files.exists()) {
				try {
					files.mkdir();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// base64写入临时目录形成file文件
			String ss = BaseImg64.generateImage(image[1], files.getPath(), suffix);
			/**************************************************************/
			String imgUrl = "";
			String fileName1 = IdGen.uuid() + "." + suffix;
			try {
				// 调用阿里云上传图片的方法
				imgUrl = FileStorageUtils.putObject(fileName1, files.getPath() + "\\" + ss);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			//System.out.println(imgUrl);
			/*if (StringUtils.isNotBlank(ss)) {
				returnApp.setData(realUploadPath+"/" + ss);
			}else{
				return new ReturnApp("1","上传失败");
			}*/
						
			/**************************************************************/
			if (StringUtils.isNotBlank(imgUrl)) {
				returnApp.setData(imgUrl);
			} else {
				returnApp.setErrorCode("1");
				returnApp.setErrorMsg("上传失败");
			}
			// 删除上传临时目录下的临时文件
			FileStorageUtils.deleteFile(files.getPath() + "\\" + ss);
		}else {
			return new ReturnApp("1","请选择图片");
		}
		return returnApp;
	}
	
	
}
