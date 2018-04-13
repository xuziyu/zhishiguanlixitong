/**
 * 
 */
package com.yonyou.kms.modules.cms.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.Model;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.yonyou.kms.common.config.Global;
import com.yonyou.kms.common.mapper.JsonMapper;
import com.yonyou.kms.common.persistence.BaseEntity;
import com.yonyou.kms.common.persistence.Page;
import com.yonyou.kms.common.service.BaseService;
import com.yonyou.kms.common.utils.CacheUtils;
import com.yonyou.kms.common.utils.SpringContextHolder;
import com.yonyou.kms.modules.cms.dao.CategoryDao;
import com.yonyou.kms.modules.cms.entity.Article;
import com.yonyou.kms.modules.cms.entity.ArticleCount;
import com.yonyou.kms.modules.cms.entity.Category;
import com.yonyou.kms.modules.cms.entity.Label;
import com.yonyou.kms.modules.cms.entity.Link;
import com.yonyou.kms.modules.cms.entity.Site;
import com.yonyou.kms.modules.cms.service.ArticleCountService;
import com.yonyou.kms.modules.cms.service.ArticleService;
import com.yonyou.kms.modules.cms.service.CategoryService;
import com.yonyou.kms.modules.cms.service.CategoryTreeService;
import com.yonyou.kms.modules.cms.service.CommentService;
import com.yonyou.kms.modules.cms.service.DepartContributionService;
import com.yonyou.kms.modules.cms.service.LabelService;
import com.yonyou.kms.modules.cms.service.LinkService;
import com.yonyou.kms.modules.cms.service.PersonContributionService;
import com.yonyou.kms.modules.cms.service.RecommendService;
import com.yonyou.kms.modules.cms.service.ShareService;
import com.yonyou.kms.modules.cms.service.SiteService;
import com.yonyou.kms.modules.cms.service.StoreService;
import com.yonyou.kms.modules.oa.service.OaNotifyService;
import com.yonyou.kms.modules.sys.entity.Office;
import com.yonyou.kms.modules.sys.entity.Role;
import com.yonyou.kms.modules.sys.entity.User;
import com.yonyou.kms.modules.sys.utils.UserUtils;

/**
 * 内容管理工具类
 * @author hotsum
 * @version 2013-5-29
 */
public class CmsUtils {
	
	private static SiteService siteService = SpringContextHolder.getBean(SiteService.class);
	private static CategoryService categoryService = SpringContextHolder.getBean(CategoryService.class);
	private static ArticleService articleService = SpringContextHolder.getBean(ArticleService.class);
	private static LinkService linkService = SpringContextHolder.getBean(LinkService.class);
    private static ServletContext context = SpringContextHolder.getBean(ServletContext.class);
    //add by luqibao
    private static CategoryDao categorydao=SpringContextHolder.getBean(CategoryDao.class);;
    //end
    //add hefeng
    private static ArticleCountService articlecountService = SpringContextHolder.getBean(ArticleCountService.class);
    private static OaNotifyService oaNotifyService = SpringContextHolder.getBean(OaNotifyService.class);
    private static StoreService storeService = SpringContextHolder.getBean(StoreService.class);
    private static ShareService shareService = SpringContextHolder.getBean(ShareService.class);
    private static CommentService commentService = SpringContextHolder.getBean(CommentService.class);
    private static RecommendService recommendService = SpringContextHolder.getBean(RecommendService.class);
    //end
    //add
    private static LabelService labelService=SpringContextHolder.getBean(LabelService.class);
    private static CategoryTreeService categoryTreeService=SpringContextHolder.getBean(CategoryTreeService.class);
    //end
	private static final String CMS_CACHE = "cmsCache";
	private static DepartContributionService depaetService=SpringContextHolder.getBean(DepartContributionService.class);
	private static PersonContributionService personService=SpringContextHolder.getBean(PersonContributionService.class);
	/**
	 * 获得站点列表
	 */
	public static List<Site> getSiteList(){
		@SuppressWarnings("unchecked")
		List<Site> siteList = (List<Site>)CacheUtils.get(CMS_CACHE, "siteList");
		if (siteList == null){
			Page<Site> page = new Page<Site>(1, -1);
			page = siteService.findPage(page, new Site());
			siteList = page.getList();
			CacheUtils.put(CMS_CACHE, "siteList", siteList);
		}
		return siteList;
	}
	
	/**
	 * 获得站点信息
	 * @param siteId 站点编号
	 */
	public static Site getSite(String siteId){
		String id = "1";
		if (org.apache.commons.lang3.StringUtils.isNotBlank(siteId)){
			id = siteId;
		}
		for (Site site : getSiteList()){
			if (site.getId().equals(id)){
				return site;
			}
		}
		return new Site(id);
	}
	
	/**
	 * 获得主导航列表
	 * @param siteId 站点编号
	 */
	public static List<Category> getMainNavList(String siteId){
		@SuppressWarnings("unchecked")
		List<Category> mainNavList = (List<Category>)CacheUtils.get(CMS_CACHE, "mainNavList_"+siteId);
		if (mainNavList == null){
			Category category = new Category();
			category.setSite(new Site(siteId));
			category.setParent(new Category("1"));
			category.setInMenu(Global.SHOW);
			Page<Category> page = new Page<Category>(1, -1);
			page = categoryService.find(page, category);
			mainNavList = page.getList();
			CacheUtils.put(CMS_CACHE, "mainNavList_"+siteId, mainNavList);
		}
		return mainNavList;
	}
	/***
	 * 判断office是否是nc导入的
	 */
	public static boolean isNc(String id){
		//f960c2f92754471798796a70e3fc69a7
		//10016910000000003JZ1
		//System.out.println(id);
		if(id.length()<=20){
			return true;
		}
		return false;
	}
	/**
	 * 获取栏目
	 * @param categoryId 栏目编号
	 * @return
	 */
	public static Category getCategory(String categoryId){
		return categoryService.get(categoryId);
	}
	
	/**
	 * 获得栏目列表
	 * @param siteId 站点编号
	 * @param parentId 分类父编号
	 * @param number 获取数目
	 * @param param  预留参数，例： key1:'value1', key2:'value2' ...
	 */
	public static List<Category> getCategoryList(String siteId, String parentId, int number, String param){
		Page<Category> page = new Page<Category>(1, number, -1);
		Category category = new Category();
		category.setSite(new Site(siteId));
		category.setParent(new Category(parentId));
		if (org.apache.commons.lang3.StringUtils.isNotBlank(param)){
			@SuppressWarnings({ "unused", "rawtypes" })
			Map map = JsonMapper.getInstance().fromJson("{"+param+"}", Map.class);
		}
		page = categoryService.find(page, category);
		return page.getList();
	}

	/**
	 * 获取栏目
	 * @param categoryIds 栏目编号
	 * @return
	 */
	public static List<Category> getCategoryListByIds(String categoryIds){
		return categoryService.findByIds(categoryIds);
	}
	
	/**
	 * 获取文章
	 * @param articleId 文章编号
	 * @return
	 */
	public static Article getArticle(String articleId){
		return articleService.get(articleId);
	}
	
	//add hefeng
	
	/**
	 * 获取当前通知阅读时间
	 * @return String UserName
	 */
	public static Date getReadDateByOaNotifyId(String oaNotifyId){
		return oaNotifyService.getReadDateByOaNotifyId(oaNotifyId);
	}
	
	/**
	 * 获取用户name通过Articleid
	 * @return String UserName
	 */
	public static String getUserNameByArticleId(String articleId){
		String UserName="";
		if(articleId==null){
			articleId="";
		}
		if(articleService.get(articleId)==null){
		
		}else{
			UserName=UserUtils.get(articleService.get(articleId).getCreateBy().getId()).getName();
		}
		return UserName;
	}
	
	/**
	 * 获取当前用户未读消息数
	 * @return UnReadMsgNum
	 */
	public static Long getCurrentUserUnReadMsg(){
		String userId=UserUtils.getUser().getId();
		Long UnReadMsgNum=oaNotifyService.findUnReadMsg(userId)==null?0L:oaNotifyService.findUnReadMsg(userId);
		return UnReadMsgNum;
		
	}
	/**
	 * 获取文章标题（不再使用，停止维护2015.11.19）
	 * @param articleId 文章编号
	 * @return 知识标题
	 */
	public static String getArticletitle(String articleId){
		if(articleId==null){
			articleId="";
		}
		if(articleService.get(articleId)==null){
			return "";
		}
		return articleService.get(articleId).getTitle();
	}
	
	/**
	 * 获取文章栏目id（不再使用，停止维护2015.11.19）
	 * @param articleId 文章编号
	 * @return 知识分类id
	 */
	public static String getArticlecid(String articleId){
		if(articleId==null){
			articleId="";
		}
		if(articleService.get(articleId)==null){
			return "";
		}
		return articleService.get(articleId).getCategory().getId();
	}
	
	/**
	 * 获取文章分类名称（不再使用，停止维护2015.11.19）
	 * @param articleId 文章编号
	 * @return 知识分类名称
	 */
	public static String getArticleCategoryName(String articleId){
		if(articleId==null){
			articleId="";
		}
		//update by yinshh3 11月3日9:06
		if(articleService.get(articleId)==null){
			return "";
		}
		return articleService.get(articleId).getCategory().getName();
	}

	/**
	 * 获取栏目路径string
	 * @param id 知识分类id
	 * @return 路径string字符串(不包含当前分类名称)1级知识库/2级知识库/...
	 */
	public static String getCategoryStringByIds(String id){
		StringBuilder categorystring=new StringBuilder();
		if(id!=null){
			
			String categoryIds=categoryService.findparentIdsById(id);
			if(categoryIds!=null){
				List<Category> list=categoryService.findByIds(categoryIds);
				for(int i=0;i<list.size();i++){
					if(!list.get(i).getId().equals("1")){
						categorystring.append(list.get(i).getName()+"/");
						
					}
				}
			}
		}else{
			return "";
		}
		return categorystring.toString();
	}
	
	/**
	 * 获取文章推荐次数（不再使用，停止维护2015.11.19）
	 * @param articleId 文章编号
	 * @return 文章推荐次数
	 */
	public static int getArticleRecommendCount(String articleId){
		ArticleCount articleCount=new ArticleCount();
		if(articleId!=null){
			articleCount.setArticleid(articleId);
		}else{
			articleCount.setArticleid("");
		}
		return articlecountService.findArticleRecommendCount(articleCount);
	}
	
	/**
	 * 获取文章分享次数（不再使用，停止维护2015.11.19）
	 * @param articleId 文章编号
	 * @return 分享次数
	 */
	public static int getArticleShareCount(String articleId){
		ArticleCount articleCount=new ArticleCount();
		if(articleId!=null){
			articleCount.setArticleid(articleId);
		}else{
			articleCount.setArticleid("");
		}
		return articlecountService.findArticleShareCount(articleCount);
	}
	
	/**
	 * @author hefeng
	 * 合并知识分类下的知识
	 * @param originalcategoryId 原始知识分类Id
	 * @param categoryId 知识分类Id（合并后的Id）
	 * @param articleId 知识Id
	 * @return 是否合并成功 Boolean
	 */
	public static Boolean MergeArticle(String originalcategoryId,String categoryId,String articleId){
		storeService.MergeArticle(originalcategoryId, categoryId, articleId);
		shareService.MergeArticle(originalcategoryId, categoryId, articleId);
		commentService.MergeArticle(originalcategoryId, categoryId, articleId);
		recommendService.MergeArticle(originalcategoryId, categoryId, articleId);
		labelService.MergeArticle(originalcategoryId, categoryId, articleId);
		articleService.MergeArticle(originalcategoryId, categoryId, articleId);
		
		return true;
	}
	
	/**
	 * @author hefeng
	 * 合并知识分类
	 * @param originalcategoryId 原始知识分类Id
	 * @param categoryId 知识分类Id（合并后的Id）
	 * @return 是否合并成功 Boolean
	 */
	public static Boolean MergeCategory(String originalcategoryId,String categoryId){
		storeService.MergeCategory(originalcategoryId, categoryId);
		shareService.MergeCategory(originalcategoryId, categoryId);
		commentService.MergeCategory(originalcategoryId, categoryId);
		recommendService.MergeCategory(originalcategoryId, categoryId);
		labelService.MergeCategory(originalcategoryId, categoryId);
		articleService.MergeCategory(originalcategoryId, categoryId);
		
			//删除原始分类
			Category category=new Category();
			category.setId(originalcategoryId);
			category.setSite(new Site());
			try{
				categoryService.delete(category);
			}catch(Exception e){
				e.printStackTrace();
			}
			
		
		return true;
	}
	//end
	
	//add by yinshh3
	/**
	 * 通过文章id得到文章的状态.(审核2,发布1,删除0)
	 * 可以加额外的判断来得到发布的文章数,已经删除的文章数.
	 * @param user
	 * @return 该用户的文章数是审核状态的数目
	 */
	public  static Integer getNumOfExamingArticle(){
		User user=UserUtils.getUser();
		List<String> delflagList=articleService.getAllDelFlagByUserId(user);
		int count=0;
		for(String s:delflagList){
			if(s.equals("2")){
				count++;
			}
		}
		
		return count;
	}
	/**
	 * @return 根据当前用户得到能够访问的2级分类ID集合(还包含了一些该有的数据.)
	 */
	public static List<String> getSecondCategoryListByCurrentAdmin(){
		User user=UserUtils.getUser();
		List<String> categorylist=categoryService.findCategoryIdByUser(user);
		List<String> secondcategorylsit=categoryService.getSecondCategoryID(categorylist);
		return secondcategorylsit;

	}
	
	/**
	 * @return 当前用户在首页上显示的库,一级分类,二级分类的id及相关数据的集合.
	 */
	public static List<Map<String,Object>> getCategoryTreeListMap(){
		List<Map<String,Object>> categoryTreeListMap=Lists.newArrayList();
		Map<String,Object> categoryMap=new HashMap<String,Object>();
		categoryMap=categoryService.categoryMap(categoryService.findCategoryIdByUser(UserUtils.getUser()));
		if(categoryMap!=null&&categoryMap.size()>0){
			categoryTreeListMap=categoryTreeService.getCategoryTreeData((List<String>)categoryMap.get("secList"),(List<String>)categoryMap.get("firList"),(List<String>)categoryMap.get("libList"));
		}
		//System.out.println("知识库结构数据"+JsonMapper.toJsonString(categoryTreeListMap));
		return categoryTreeListMap;
	}
	
	/**
	 * 
	 */
	public static List<Label> getAllLabel(){
		List<Label> labelList=Lists.newArrayList();
		labelList=labelService.FindAllLabel();
		return labelList;
	}
	
	
	/**
	 *  
	 * @return 根据当前管理员得到它需要审核的文章数.
	 */
	public static Integer getNumOfExamingArticleByAdmin(){
		List<String> delflagList=Lists.newArrayList();
		//commentService.commentList(UserUtils.getUser())得到用户管理的末级知识分类id集合
		//delflagList=categoryService.getDelFlagByCategoryID(commentService.commentList(UserUtils.getUser()));
		//System.out.println(JsonMapper.toJsonString(categoryService.findByUser2(true, "article", CategoryService.CATEGORY_PLACE_SYS)));
		List<Category> temp=categoryService.findByUser2(true, "article", CategoryService.CATEGORY_PLACE_SYS);
		List<String> ids=Lists.newArrayList();
		for(Category c:temp){
			
			//选择知识分类  过滤知识库
			if("article".equals(c.getModule())){
				ids.add(c.getId());
			}
		}
		delflagList=categoryService.getDelFlagByCategoryID(ids);
		int count=0;
		for(String s:delflagList){
			if(s.equals("2")){
				count++;
			}
		}
		String professorId = Global.getConfig("cms.professorId");
		List<String> roleIds = UserUtils.getRoleIdList();
		
		//System.out.println("cmsUtil-count:"+articleService.countProfessorRead(UserUtils.getUser(),"3"));
		//如果具有权专家审核的权限 那么就显示专家的待审核的知识数
		if(roleIds.contains(professorId))
			return articleService.countProfessorRead(UserUtils.getUser(),"3");
		
		//System.out.println("not P!!!");
		return count;
	}
	
	//end
	/**
	 * 获取文章列表
	 * @param siteId 站点编号
	 * @param categoryId 分类编号
	 * @param number 获取数目
	 * @param param  预留参数，例： key1:'value1', key2:'value2' ...
	 * 			posid	推荐位（1：首页焦点图；2：栏目页文章推荐；）
	 * 			image	文章图片（1：有图片的文章）
	 *          orderBy 排序字符串
	 * @return
	 * ${fnc:getArticleList(category.site.id, category.id, not empty pageSize?pageSize:8, 'posid:2, orderBy: \"hits desc\"')}"
	 */
	public static List<Article> getArticleList(String siteId, String categoryId, int number, String param){
		Page<Article> page = new Page<Article>(1, number, -1);
		Category category = new Category(categoryId, new Site(siteId));
		category.setParentIds(categoryId);
		Article article = new Article(category);
		if (org.apache.commons.lang3.StringUtils.isNotBlank(param)){
			@SuppressWarnings({ "rawtypes" })
			Map map = JsonMapper.getInstance().fromJson("{"+param+"}", Map.class);
			if (new Integer(1).equals(map.get("posid")) || new Integer(2).equals(map.get("posid"))){
				article.setPosid(String.valueOf(map.get("posid")));
			}
			if (new Integer(1).equals(map.get("image"))){
				article.setImage(Global.YES);
			}
			if (org.apache.commons.lang3.StringUtils.isNotBlank((String)map.get("orderBy"))){
				page.setOrderBy((String)map.get("orderBy"));
			}
		}
		article.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
		page = articleService.findPage(page, article, false);
		return page.getList();
	}
	
	/**
	 * 获取链接
	 * @param linkId 文章编号
	 * @return
	 */
	public static Link getLink(String linkId){
		return linkService.get(linkId);
	}
	
	/**
	 * 获取链接列表
	 * @param siteId 站点编号
	 * @param categoryId 分类编号
	 * @param number 获取数目
	 * @param param  预留参数，例： key1:'value1', key2:'value2' ...
	 * @return
	 */
	public static List<Link> getLinkList(String siteId, String categoryId, int number, String param){
		Page<Link> page = new Page<Link>(1, number, -1);
		Link link = new Link(new Category(categoryId, new Site(siteId)));
		if (org.apache.commons.lang3.StringUtils.isNotBlank(param)){
			@SuppressWarnings({ "unused", "rawtypes" })
			Map map = JsonMapper.getInstance().fromJson("{"+param+"}", Map.class);
		}
		link.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
		page = linkService.findPage(page, link, false);
		return page.getList();
	}
	
	// ============== Cms Cache ==============
	
	public static Object getCache(String key) {
		return CacheUtils.get(CMS_CACHE, key);
	}

	public static void putCache(String key, Object value) {
		CacheUtils.put(CMS_CACHE, key, value);
	}

	public static void removeCache(String key) {
		CacheUtils.remove(CMS_CACHE, key);
	}

    /**
     * 获得文章动态URL地址
   	 * @param article
   	 * @return url
   	 */
    public static String getUrlDynamic(Article article) {
        if(org.apache.commons.lang3.StringUtils.isNotBlank(article.getLink())){
            return article.getLink();
        }
        StringBuilder str = new StringBuilder();
        str.append(context.getContextPath()).append(Global.getFrontPath());
        str.append("/view-").append(article.getCategory().getId()).append("-").append(article.getId()).append(Global.getUrlSuffix());
        return str.toString();
    }

    /**
     * 获得栏目动态URL地址
   	 * @param category
   	 * @return url
   	 */
    public static String getUrlDynamic(Category category) {
        if(org.apache.commons.lang3.StringUtils.isNotBlank(category.getHref())){
            if(!category.getHref().contains("://")){
                return context.getContextPath()+Global.getFrontPath()+category.getHref();
            }else{
                return category.getHref();
            }
        }
        StringBuilder str = new StringBuilder();
        str.append(context.getContextPath()).append(Global.getFrontPath());
        str.append("/list-").append(category.getId()).append(Global.getUrlSuffix());
        return str.toString();
    }

    /**
     * 从图片地址中去除ContextPath地址
   	 * @param src
   	 * @return src
   	 */
    public static String formatImageSrcToDb(String src) {
        if(org.apache.commons.lang3.StringUtils.isBlank(src)) return src;
        if(src.startsWith(context.getContextPath() + "/userfiles")){
            return src.substring(context.getContextPath().length());
        }else{
            return src;
        }
    }

    /**
     * 从图片地址中加入ContextPath地址
   	 * @param src
   	 * @return src
   	 */
    public static String formatImageSrcToWeb(String src) {
        if(org.apache.commons.lang3.StringUtils.isBlank(src)) return src;
        if(src.startsWith(context.getContextPath() + "/userfiles")){
            return src;
        }else{
            return context.getContextPath()+src;
        }
    }
    
    @SuppressWarnings("rawtypes")
    public static void addViewConfigAttribute(Model model, String param){
        if(org.apache.commons.lang3.StringUtils.isNotBlank(param)){
			Map map = JsonMapper.getInstance().fromJson(param, Map.class);
            if(map != null){
                for(Object o : map.keySet()){
                    model.addAttribute("viewConfig_"+o.toString(), map.get(o));
                }
            }
        }
    }

    public static void addViewConfigAttribute(Model model, Category category){
        List<Category> categoryList = Lists.newArrayList();
        Category c = category;
        boolean goon = true;
        do{
            if(c.getParent() == null || c.getParent().isRoot()){
                goon = false;
            }
            categoryList.add(c);
            c = c.getParent();
        }while(goon);
        Collections.reverse(categoryList);
        for(Category ca : categoryList){
        	addViewConfigAttribute(model, ca.getViewConfig());
        }
    }
    //add by luqibao
    /**
     * @param target 目标字符串
     * @param condition 分割字符
     */
    public static List<String> transform(String target,String condition){
    	List<String> list=Lists.newArrayList();
    	if(target==null&&!"".equals(target)){
    		return list;
    	}
    	
    	Pattern p = Pattern.compile("\\s+");
    	Matcher m = p.matcher(target);
    	String targets= m.replaceAll(" ");
    	if(targets.equals("")){
    		return list;
    	}
    	for(String str:targets.split(condition)){
    		if(!condition.equals(str)|!str.equals("")){
    			list.add(str);
    		}
    	}
    	return list;
    }
    /**
     * 是否具有该分类的审核权限
     * @param c
     * @return
     */
    @SuppressWarnings("unchecked")
    public static boolean hasPermission(String categoryId){
    	
    	//具有权限的集合
    	List<String> ids=(List<String>) UserUtils.getCache("category:permisssion");
    	
    	if(ids==null){
    		List<Category> categorys=findByUser2(true, "article",BaseService.CATEGORY_PLACE_SYS);
    		ids=Lists.newArrayList();
        	for(Category cc:categorys){
        		if(cc.getModule().equals("article")){
        			ids.add(cc.getId());
        		}
        	}
        	UserUtils.putCache("category:permisssion", ids);
        	
    	}
    	
    	if(ids.contains(categoryId)){
    		return true;
    	}
    	
    	return false;
    }
    @SuppressWarnings("unchecked")
	public static List<Category> findByUser2(boolean isCurrentSite, String module,String place){
		List<String> categoryAll=Lists.newArrayList();
		User user = UserUtils.getUser();
		//List<Category> list = (List<Category>)UserUtils.getCache(CACHE_CATEGORY_LIST);
		List<Category> list =null;
		List<String> tempList=Lists.newArrayList();
		if (list == null){
			Category category = new Category();
			category.setOffice(new Office());
			
			category.setSite(new Site());
			category.setParent(new Category());
			
			if(BaseService.CATEGORY_PLACE_SYS.equals(place)){			
				category.getSqlMap().put("dsf", BaseService.dataScopeFilter(user, "o", ""));
				list=Lists.newArrayList();
			}else{
				category.getSqlMap().put("dsf", BaseService.dataScopeFilter1(user, "o", ""));
				list = categorydao.findList(category);
				List<Category> categorys=getCategoryByUser(UserUtils.getUser());
				for(Category c:categorys){
					if(!list.contains(c)){
						list.add(c);
					}
				}
				
			}
			//list = dao.findList(category);
//			for(Category c:list){
//				System.out.println(c.getName());
//			}
			//全部的
			
			//add by luqibao  这里单独处理后台管理新增
			try{
				if(BaseService.CATEGORY_PLACE_SYS.equals(place)){
					list=categorydao.findCategorysByUser(user);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			//判断是否是超级管理员  如果是的话  走原来的体系
			boolean flag=false;
			List<Role> roles=user.getRoleList();
			for(Role r:roles){
				if(Role.DATA_SCOPE_ALL.equals(r.getDataScope())){
					flag=true;
				}
			}
			
			//如果是系统管理员  还是走原来的逻辑
			if(user.isAdmin()|flag){
				category.getSqlMap().put("dsf", BaseService.dataScopeFilter(user, "o", ""));
				list = categorydao.findList(category);
			}
			
			//end    这里单独处理后台管理新增
			

			for(Category c:list){
				tempList.add(c.getId());
			}
			//add by luqibao  修改代码   能够显示
			Set<String> set=new TreeSet<String>();
			for(Category category1:list){
				String parentIds[]=category1.getParentIds().split(",");
				for(String str:parentIds){
					if(!"0".equals(str)){
						set.add(str);
					}
				}
			}
			
			/**categoryAll=findCategoryIdByUser1(user);
			for(String str:categoryAll){
				set.add(str);
			}**/
			categoryAll.clear();
			for(String str:set){
				categoryAll.add(str);
			}
			List<Category> list1=null;
			//判空操作 huangmj 2015 11 23 satrt
			if(categoryAll.size()>0){
				list1=categorydao.findAllByIds(categoryAll);
			}
			//判空操作 huangmj 2015 11 23 end
			if(list1!=null&&list1.size()>0){
				list.addAll(list1);
			}
			List<Category> temp=Lists.newArrayList();
			for(Category c:list){
				if(!temp.contains(c)){
					temp.add(c);
				}
			}
			list=temp;
			for(int i=0;i<list.size();i++){
				if(tempList.contains(list.get(i).getId())){
					list.get(i).setIsAdmin("1");
				}else{
					list.get(i).setIsAdmin("0");
				}
			}
			//end
			// 将没有父节点的节点，找到父节点
			Set<String> parentIdSet = Sets.newHashSet();
			for (Category e : list){
				if (e.getParent()!=null && StringUtils.isNotBlank(e.getParent().getId())){
					boolean isExistParent = false;
					for (Category e2 : list){
						if (e.getParent().getId().equals(e2.getId())){
							isExistParent = true;
							break;
						}
					}
					if (!isExistParent){
						parentIdSet.add(e.getParent().getId());
					}
				}
			}
			if (parentIdSet.size() > 0){
				//FIXME 暂且注释，用于测试
//				dc = dao.createDetachedCriteria();
//				dc.add(Restrictions.in("id", parentIdSet));
//				dc.add(Restrictions.eq("delFlag", Category.DEL_FLAG_NORMAL));
//				dc.addOrder(Order.asc("site.id")).addOrder(Order.asc("sort"));
//				list.addAll(0, dao.find(dc));
			}
		}
		if (isCurrentSite){
			List<Category> categoryList = Lists.newArrayList(); 
			for (Category e : list){
				if (Category.isRoot(e.getId()) || (e.getSite()!=null && e.getSite().getId() !=null 
						&& e.getSite().getId().equals(Site.getCurrentSiteId()))){
					if (StringUtils.isNotEmpty(module)){
						if (module.equals(e.getModule()) || "".equals(e.getModule())){
							categoryList.add(e);
						}
					}else{
						categoryList.add(e);
					}
				}
			}
			return categoryList;
		}
		
		return list;
	}
    
    public static List<Category> getCategoryByUser(User user){
		List<Category> list=Lists.newArrayList();
		list=categorydao.findCategorysByUser(user);
		if(list==null){
			return Lists.newArrayList();
		}
		return list;
	}
    //end
//add by yangshw6
    
    //权限过滤,过滤掉不在用户权限下的知识集合
	public static List<Article> filterArticle(List<Article> list,User...user){
  		List<Article> newlist=new ArrayList<Article>();
  		List<String> categoryid=null;
  		if(user!=null){
  			categoryid=categoryService.findCategoryIdByUser(user[0]);
  		}else{
  			categoryid=categoryService.findCategoryIdByUser(null);
  		}
  		
  		for(int i=0;i<list.size();i++){
  			String id=list.get(i).getCategory().getId();
  			int count=0;
  			for(int j=0;j<categoryid.size();j++){
  				String cid=categoryid.get(j);
  				if(id.equals(cid)){
  					count++;
  					break;
  				}				
  			}
  			if(count==1){
  				newlist.add(list.get(i));
  			}
  		}
  		return newlist;
  	}

  	/*
  	 * 获取本部门的父级部门名
  	 * 
  	 */
  	public static String getParentName(String departid){
  		String name=new String();
  		name=depaetService.getParentName(departid);
  		
  		if(name ==null || name.length()==0){
  	  		return name="";
  		}
  		StringBuffer sb=new StringBuffer();
  		sb.append("上级:"+name);
  		return sb.toString();
  		
  	}
  	public static String getUserOfficeName(String userid){
  		String name=new String();
  		name=personService.getOfficeName(userid);
  		if(name ==null || name.length()==0){
  	  		return name="";
  		}
  		StringBuffer sb=new StringBuffer();
  		sb.append("所属部门:"+name);
  		return sb.toString();
  	}
      //end by yangshw6
}