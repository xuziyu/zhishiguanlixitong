/**
 * 
 */
package com.yonyou.kms.modules.cms.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yonyou.kms.common.persistence.Page;
import com.yonyou.kms.common.service.BaseService;
import com.yonyou.kms.common.service.CrudService;
import com.yonyou.kms.common.utils.CacheUtils;
import com.yonyou.kms.common.utils.StringUtils;
import com.yonyou.kms.modules.cms.dao.ArticleCountDao;
import com.yonyou.kms.modules.cms.dao.ArticleDao;
import com.yonyou.kms.modules.cms.dao.ArticleDataDao;
import com.yonyou.kms.modules.cms.dao.ArticleLabelDao;
import com.yonyou.kms.modules.cms.dao.CategoryDao;
import com.yonyou.kms.modules.cms.dao.RecommendDao;
import com.yonyou.kms.modules.cms.dao.StoreDao;
import com.yonyou.kms.modules.cms.entity.Article;
import com.yonyou.kms.modules.cms.entity.ArticleAttFile;
import com.yonyou.kms.modules.cms.entity.ArticleCount;
import com.yonyou.kms.modules.cms.entity.ArticleData;
import com.yonyou.kms.modules.cms.entity.Category;
import com.yonyou.kms.modules.cms.entity.Store;
import com.yonyou.kms.modules.cms.utils.CmsUtils;
import com.yonyou.kms.modules.oa.service.OaNotifyService;
import com.yonyou.kms.modules.sys.dao.OfficeDao;
import com.yonyou.kms.modules.sys.dao.UserDao;
import com.yonyou.kms.modules.sys.entity.Audit;
import com.yonyou.kms.modules.sys.entity.Office;
import com.yonyou.kms.modules.sys.entity.User;
import com.yonyou.kms.modules.sys.utils.FileStorageUtils;
import com.yonyou.kms.modules.sys.utils.UserUtils;

/**
 * 文章Service
 * @author hotsum
 * @version 2013-05-15
 */
@Service
@Transactional(readOnly = true)
public class ArticleService extends CrudService<ArticleDao, Article> {

	@Autowired
	private ArticleDataDao articleDataDao;
	
	@Autowired
	private ArticleCountDao articlecountdao;
	
	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private StoreDao storeDao;
	@Autowired
	private OaNotifyService oaNotifyService;
	@Autowired
	private ArticleAttFileService articleAttFileService;
	@Autowired
	private ArticleLabelService articleLabelService;
	@Autowired
	private ArticleLabelDao articleLabelDao;
	@Autowired
	private RecommendDao recommendDao;
	//@Autowired
	//private ArticleAttFileDao articleAttFileDao;
	@Autowired
	private CategoryService categoryService;
	//huangmj 2015.11.12
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private OfficeDao officeDao;
	
	
	//审核变发布，huangmj6 2015.10.22
	public void update(Article article){
		dao.update(article);
	}
	
	public User getArticleUser(User user){
		return userDao.get(user);
	}
	
	@Transactional(readOnly = false)
	public Page<Article> findPage(Page<Article> page, Article article, boolean isDataScopeFilter) {
		page=articlePage(page, article, isDataScopeFilter);
		//设置分页数 15 huangmj 2015 11 29
		page.setPageSize(12);
		return super.findPage(page, article);
		
	}
	
	/**
	 * 修改文章是否同步app
	 * 
	 */
	public void updateCopyFrom(String id,String copyfrom){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("id",id);
		map.put("copyfrom",copyfrom);
		articleDao.updateCopyFrom(map);
	}
	
	/**
	 * 组合参数
	 * @param page
	 * @param article
	 * @param isDataScopeFilter
	 * @return
	 */
	protected Page<Article> articlePage(Page<Article> page, Article article, boolean isDataScopeFilter){
		// 更新过期的权重，间隔为“6”个小时
				Date updateExpiredWeightDate =  (Date)CacheUtils.get("updateExpiredWeightDateByArticle");
				if (updateExpiredWeightDate == null || (updateExpiredWeightDate != null 
						&& updateExpiredWeightDate.getTime() < new Date().getTime())){
					dao.updateExpiredWeight(article);
					CacheUtils.put("updateExpiredWeightDateByArticle", DateUtils.addHours(new Date(), 6));
				}
				if (article.getCategory()!=null && org.apache.commons.lang3.StringUtils.isNotBlank(article.getCategory().getId()) && !Category.isRoot(article.getCategory().getId())){
					Category category = categoryDao.get(article.getCategory().getId());
					if (category==null){
						category = new Category();
					}
					category.setParentIds(category.getId());
					category.setSite(category.getSite());
					article.setCategory(category);
				}
				else{
					
					article.setCategory(new Category());
				}
				
				return page;
	}
	
	public long count(Page<Article> page, Article article, boolean isDataScopeFilter){
		page=articlePage(page, article, isDataScopeFilter);
		page.setPageSize(Integer.MAX_VALUE-6);
		Page<Article> list = super.findPage(page, article);
		
		long count=list==null?0:list.getList().size();
		
		if(count==0)
			return 0;
		
		List<Category> categorys = categoryService.findByUser(true, null,BaseService.CATEGORY_PLACE_SYS);
        List<String> ids=Lists.newArrayList();
        for(Category a:categorys){
        	ids.add(a.getId());
        }
        
		if(list!=null){
        	for(Article a:list.getList()){
        		if(!ids.contains(a.getCategory().getId())){
        			count--;
        		}
        	}
        }
		
		return count;
		
	}
	/**
	 * 计算专家组需要的总数
	 * @param user		     查看的用户
	 * @param onlyNeedAudit  是否只包含带审核的
	 * @return
	 */
	public int countProfessorRead(User user,String delFlag){
//		
//		page=articlePage(page, article, isDataScopeFilter);
//		page.setPageSize(Integer.MAX_VALUE-6);
//		Page<Article> list = super.findPage(page, article);
//		
//		long count=list==null?0:list.getList().size();
//		
//		if(count==0)
//			return 0;
//		
//		List<Category> categorys = categoryService.findByUser(true, null,BaseService.CATEGORY_PLACE_SYS);
//        List<String> ids=Lists.newArrayList();
//        for(Category a:categorys){
//        	ids.add(a.getId());
//        }
//        
//		if(list!=null){
//        	for(Article a:list.getList()){
//        		if(!ids.contains(a.getCategory().getId())){
//        			count--;
//        		}else if(!"3".equals(a.getDelFlag())){
//        			count--;
//        		}
//        		//只有状态是3的才是需要审核的
//        		
//        	}
//        }
		
		return articleDao.professorNeedAudit(UserUtils.getUser().getId(),delFlag);
		
	}
	
	
	/**
	 * 
	 * @param page
	 * @param article
	 * @param isDataScopeFilter
	 * @return
	 */
	@Transactional(readOnly = false)
	public Page<Article> findPageNeedAudit(Page<Article> page, Article article, boolean isDataScopeFilter) {
		// 更新过期的权重，间隔为“6”个小时
		Date updateExpiredWeightDate =  (Date)CacheUtils.get("updateExpiredWeightDateByArticle");
		if (updateExpiredWeightDate == null || (updateExpiredWeightDate != null 
				&& updateExpiredWeightDate.getTime() < new Date().getTime())){
			dao.updateExpiredWeight(article);
			CacheUtils.put("updateExpiredWeightDateByArticle", DateUtils.addHours(new Date(), 6));
		}
		if (article.getCategory()!=null && org.apache.commons.lang3.StringUtils.isNotBlank(article.getCategory().getId()) && !Category.isRoot(article.getCategory().getId())){
			Category category = categoryDao.get(article.getCategory().getId());
			if (category==null){
				category = new Category();
			}
			category.setParentIds(category.getId());
			category.setSite(category.getSite());
			article.setCategory(category);
		}
		else{
			
			article.setCategory(new Category());
		}
		page.setPageSize(12);
		/**
		 * 三期需求  修改为指定审核人的知识
		 * @author Hotusm
		 * @date 2016/09/12
		 * 
		 */
		article.setPage(page);
		
		return page.setList(articleDao.findNeedAuditList(article));
	}
	

	@Override
	@Transactional(readOnly = false)
	public String save(Article article) {
		ArticleData articleData2 = article.getArticleData();
			String content = articleData2.getContent();
		if (StringUtils.isNoneBlank(content)){
			article.getArticleData().setContent(StringEscapeUtils.unescapeHtml4(
					content.replace("?", "&^^").replace("？", "&^^")));
		}
		// 改为待审核状态
		//article.setDelFlag(BaseEntity.DEL_FLAG_AUDIT);
//		if (!UserUtils.getSubject().isPermitted("cms:article:audit")){
//			
//		}
		// 如果栏目不需要审核，则将该内容设为发布状态
//		if (article.getCategory()!=null&&org.apache.commons.lang3.StringUtils.isNotBlank(article.getCategory().getId())){
//			Category category = categoryDao.get(article.getCategory().getId());
//			if (!Global.YES.equals(category.getIsAudit())){
//				article.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
//			}
//		}
		article.setUpdateBy(UserUtils.getUser());
		article.setUpdateDate(new Date());
		if(null == article.getCreateDate()){
			article.setCreateDate(new Date());
		}
        if (org.apache.commons.lang3.StringUtils.isNotBlank(article.getViewConfig())){
            article.setViewConfig(StringEscapeUtils.unescapeHtml4(article.getViewConfig()));
        }
        
        ArticleData articleData = new ArticleData();;
		if (org.apache.commons.lang3.StringUtils.isBlank(article.getId())){
			article.preInsert();
			articleData = article.getArticleData();
			articleData.setId(article.getId());
			dao.insert(article);
			articleDataDao.insert(articleData);
			// doAfterSave(article);
		}else{
			article.preUpdate();
			articleData = article.getArticleData();
			articleData.setId(article.getId());
			dao.update(article);
			articleDataDao.update(article.getArticleData());
		}
		return "";
	}
	/**
	 * 在知识保存之后   还需要很多的操作  目前的操作就是在知识保存以后   帮助这个知识查找到知识审核员
	 * @author Hotusm
	 * @param article
	 */
	protected void doAfterSave(Article article){
		
		
		//1 ① 查找所有的机构 作为一个映射 KEY= officeId  VALUE=office.parent
		Map<String, Office> mapParents = mapOffice2Parent(officeDao.findAll());
		//  ② 查找所有的用户映射
		Map<String, User> mapUsers = mapUser2Id(userDao.findList(new User()));
		//  ③ 查找所有的审核信息 
		List<Audit> audits = officeDao.findAllAudits();
		// 
		//机构和审核管理员的映射
		Map<String,List<Audit>> auditMapOffice=new HashMap<String, List<Audit>>();
		List<Audit> singleOffice=null;
		for(Audit audit:audits){
			if(auditMapOffice.containsKey(audit.getOfficeId())){
				singleOffice = auditMapOffice.get(audit.getOfficeId());
				singleOffice.add(audit);
			}else{
				singleOffice=Lists.newArrayList();
				singleOffice.add(audit);
				auditMapOffice.put(audit.getOfficeId(), singleOffice);
			}
		}
		
		//2 根据本知识发布人查找到部门
		User user = mapUsers.get(article.getCreateBy().getId());
		if(user!=null){
			Office office = user.getOffice();
			List<String> userIds = findAuditUser(office, mapParents, auditMapOffice);
			Map<String,Object> param=new HashMap<String, Object>();
			param.put("list", userIds);
			param.put("articleId",article.getId());
			articleDao.insertArticleAudit(param);
		}
		
	}

	/**
	 * 
	 * @param office  				当前的机构
	 * @param mapoffices    		机构和机构的父级的映射
	 * @param auditMapOffice		审核管理员和机构的映射
	 * @return
	 */
	public List<String> findAuditUser(Office office,Map<String, Office> mapoffices,Map<String,List<Audit>> auditMapOffice){
		if(office==null||mapoffices==null)return null;
		//开始查找
		if(auditMapOffice.get(office.getId())!=null){
			List<Audit> list = auditMapOffice.get(office.getId());
			List<String> userIds=Lists.newArrayList();
			for(Audit a:list){
				userIds.add(a.getUserId());
			}
			return userIds;
		}else{
			return findAuditUser(mapoffices.get(office.getId()), mapoffices,auditMapOffice);
		}
	}
	/**
	 *将部门和父级部门作为hash表 
	 */
	public Map<String,Office> mapOffice2Parent(List<Office> offices){
		Map<String,Office> officesmap=new HashMap<String,Office>();
		if(offices!=null&&offices.size()>0){
			for(Office office:offices){
				if(office.getParent()==null){
					continue;
				}
				officesmap.put(office.getId(), office.getParent());
			}
		}
		return officesmap;
	}
	/**
	 * 将用户id和用户做Hash
	 * @param users
	 * @return
	 */
	public Map<String,User> mapUser2Id(List<User> users){
		Map<String,User> officesmap=new HashMap<String,User>();
		if(users!=null&&users.size()>0){
			for(User user:users){
				officesmap.put(user.getId(), user);
			}
		}
		return officesmap;
	}
	//新增文章状态下调用 huangmj 2015.10.21
	@Transactional(readOnly = false)
	public void save_insert(Article article,String temp_article_id) {
		if (article.getArticleData().getContent()!=null){
			article.getArticleData().setContent(StringEscapeUtils.unescapeHtml4(
					article.getArticleData().getContent()));
		}
		// 如果没有审核权限，则将当前内容改为待审核状态
	//	article.setDelFlag(BaseEntity.DEL_FLAG_AUDIT);
//		if (!UserUtils.getSubject().isPermitted("cms:article:audit")){
//			
//		}
		// 如果栏目不需要审核，则将该内容设为发布状态
//		if (article.getCategory()!=null&&org.apache.commons.lang3.StringUtils.isNotBlank(article.getCategory().getId())){
//			Category category = categoryDao.get(article.getCategory().getId());
//			if (!Global.YES.equals(category.getIsAudit())){
//				article.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
//			}
//		}
		
		article.setUpdateBy(UserUtils.getUser());
		article.setUpdateDate(new Date());
		if(null == article.getCreateDate()){
			article.setCreateDate(new Date());
		}
        if (org.apache.commons.lang3.StringUtils.isNotBlank(article.getViewConfig())){
            article.setViewConfig(StringEscapeUtils.unescapeHtml4(article.getViewConfig()));
        }
        
        	ArticleData articleData = new ArticleData();
        	//设置新增文章ID
        	if (null != temp_article_id && !"".equals(temp_article_id)) {
        		article.setId(temp_article_id);
			}else{
				article.preInsert();
			}
        	System.out.println(article.getId());
			article.preInsert_insert();
			articleData = article.getArticleData();
			articleData.setId(article.getId());
			dao.insert(article);
			articleDataDao.insert(articleData);
		
	}
	
	/**
	 * 保存一键转知识的内容
	 * @param article
	 * @param temp_article_id
	 * @return
	 */
	@Transactional(readOnly = false)
	public Article save_exchange(Article article,String temp_article_id) {
		if (article.getArticleData().getContent()!=null){
			article.getArticleData().setContent(StringEscapeUtils.unescapeHtml4(
					article.getArticleData().getContent()));
		}
		article.setCreateBy(new User("1"));
		article.setExaminer(new User("1"));
		article.setUpdateBy(new User("1"));
		article.setUpdateDate(new Date());
        if (org.apache.commons.lang3.StringUtils.isNotBlank(article.getViewConfig())){
            article.setViewConfig(StringEscapeUtils.unescapeHtml4(article.getViewConfig()));
        }
        
        	ArticleData articleData = new ArticleData();
        	//设置新增文章ID
        	if (null != temp_article_id && !"".equals(temp_article_id)) {
        		article.setId(temp_article_id);
			}else{
				article.preInsert();
			}
			article.preInsert_insert();
			articleData = article.getArticleData();
			articleData.setCopyfrom("0");
			articleData.setContent(article.getArticleData().getContent());
			articleData.setId(article.getId());
			dao.insert(article);
			articleDataDao.insert(articleData);
			return article;
		
	}
	
	@Transactional(readOnly = false)
	public void delete(Article article, Boolean isRe) {
//		dao.updateDelFlag(id, isRe!=null&&isRe?Article.DEL_FLAG_NORMAL:Article.DEL_FLAG_DELETE);
		// 使用下面方法，以便更新索引。
		//Article article = dao.get(id);
		//article.setDelFlag(isRe!=null&&isRe?Article.DEL_FLAG_NORMAL:Article.DEL_FLAG_DELETE);
		//dao.insert(article);
		super.delete(article);
	}
	
	/**
	 * 通过编号获取内容标题
	 * @return new Object[]{栏目Id,文章Id,文章标题}
	 */
	public List<Object[]> findByIds(String ids) {
		if(ids == null){
			return new ArrayList<Object[]>();
		}
		List<Object[]> list = Lists.newArrayList();
		String[] idss = org.apache.commons.lang3.StringUtils.split(ids,",");
		Article e = null;
		for(int i=0;(idss.length-i)>0;i++){
			e = dao.get(idss[i]);
			list.add(new Object[]{e.getCategory().getId(),e.getId(),StringUtils.abbr(e.getTitle(),50)});
		}
		return list;
	}
	
	//add hefeng
	/**
	 * 还原消息标志
	 */
	public void revertMsgFlag(String articleId){
		dao.revertMsgFlag(articleId,"00");
	}
	
	public void MergeArticle(String originalcategoryId,String categoryId,String articleId){
		dao.MergeArticle(originalcategoryId, categoryId, articleId);
	}
	/**
	 * 预处理调用不同更新消息
	 * //标志简介：无更新（00），内容更新（10），附件更新（01），内容和附件更新（11）
	 */
	public void PretreatmentUpdateUserMsg(String articleId) {
		String flag="";
		if(articleId==null){
			articleId="";
		}
		Article article=new Article();
		article=this.get(articleId);
		if(article==null){
			
		}else{
			flag=article.getRemarks();
		}
		if(flag==null){
			flag="00";
		}
		if("10".equals(flag)){
			this.updateUserMessage(article);
		}else if("01".equals(flag)){
			this.updateUserMessage(article, 0);//第二个参数暂时不用
		}else if("11".equals(flag)){
			this.updateUserMessage(article);
			this.updateUserMessage(article, 0);
		}else{
			
		}
	}
	
		public void deleteUserArticle(Article article, Boolean isRe) {
			if(article==null){
				article=new Article();
			}
			dao.deleteUserArticle(article);
		}
		/**
		 * 获取当前用户所有文章的编号
		 * @return String{编号集合 "1,2,3..."}
		 * @author hefeng
		 */
		public String getArticleIdByUserId(){
			
			String currentId=UserUtils.getUser().getId();
			String ArticleListId = dao.findArticleId(currentId);
			return ArticleListId;
		}
		
		/**
		 * 更新文章内容后发送消息给收藏、推荐这篇知识的所有用户
		 * @return 
		 * @author hefeng
		 */
		public void updateUserMessage(Article article) {
		if (article == null) {
			article = new Article();
		}
//		SimpleDateFormat df =  new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",Locale.US);
//		Date d1 = null;
//		Date d2 = null;
		StringBuffer stringbufferstore=new StringBuffer();
		for (int i = 0; i < storeDao.getStore(article.getId()).size(); i++) {
//			try {
//				d1 = df.parse(articleDao.get(article.getId()).getUpdateDate()
//						.toString());
//				d2 = df.parse(storeDao.getStore(article.getId()).get(i)
//						.getUpdateDate().toString());
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			if (d1.getTime() > d2.getTime()) {
			String thisstoreuserid=articleDao.get(article.getId()).getCreateBy().getId();
			if(storeDao.getStore(article.getId()).get(i).getCreateBy().getId().equals(thisstoreuserid)){
				
			}else{
				stringbufferstore.append(storeDao.getStore(article.getId()).get(i).getCreateBy()+",");
			}
				
//			}
		}
		Article articlestore=new Article();
		articlestore.setTitle(article.getTitle());
		articlestore.setCreateBy(UserUtils.getUser());
		articlestore.setUpdateBy(UserUtils.get(article.getUpdateBy().getId()));
		articlestore.setId(article.getId());
		oaNotifyService.save(oaNotifyService.PretreatmentMsgBeforeSave(articlestore, "1", "1", stringbufferstore.toString()));
		
		StringBuffer stringbufferrecommend=new StringBuffer();
		for(int j=0;j<recommendDao.getRecommendList(article.getId()).size();j++){
			String thisrecommenduserid=articleDao.get(article.getId()).getCreateBy().getId();
			if(recommendDao.getRecommendList(article.getId()).get(j).getCreateBy().getId().equals(thisrecommenduserid)){
				
			}else{
				stringbufferrecommend.append(recommendDao.getRecommendList(article.getId()).get(j).getCreateBy()+",");
			}
		}
		Article articlerecommend=new Article();
		articlerecommend.setTitle(article.getTitle());
		articlerecommend.setCreateBy(UserUtils.getUser());
		articlerecommend.setUpdateBy(UserUtils.get(article.getUpdateBy().getId()));
		articlerecommend.setId(article.getId());
		oaNotifyService.save(oaNotifyService.PretreatmentMsgBeforeSave(articlerecommend, "2", "1", stringbufferrecommend.toString()));
	}	
		/**
		 * 更新文章附件后发送消息给收藏、推荐这篇知识的所有用户(待定)
		 * @return 
		 * @author hefeng
		 */
		public void updateUserMessage(Article article,int sumdiff) {
		if (article == null) {
			article = new Article();
		}
		StringBuffer stringbufferstore=new StringBuffer();
		for (int i = 0; i < storeDao.getStore(article.getId()).size(); i++) {
			String thisstoreuserid=articleDao.get(article.getId()).getCreateBy().getId();
			if(storeDao.getStore(article.getId()).get(i).getCreateBy().getId().equals(thisstoreuserid)){
				
			}else{
			stringbufferstore.append(storeDao.getStore(article.getId()).get(i).getCreateBy()+",");
			}
		}
		Article articlestore=new Article();
		articlestore.setTitle(article.getTitle());
		articlestore.setCreateBy(UserUtils.getUser());
		articlestore.setUpdateBy(UserUtils.get(article.getUpdateBy().getId()));
		articlestore.setId(article.getId());
		oaNotifyService.save(oaNotifyService.PretreatmentMsgBeforeSave(articlestore, "1", "2", stringbufferstore.toString()));
		
		StringBuffer stringbufferrecommend=new StringBuffer();
		for(int j=0;j<recommendDao.getRecommendList(article.getId()).size();j++){
			String thisrecommenduserid=articleDao.get(article.getId()).getCreateBy().getId();
			if(recommendDao.getRecommendList(article.getId()).get(j).getCreateBy().getId().equals(thisrecommenduserid)){
				
			}else{
			stringbufferrecommend.append(recommendDao.getRecommendList(article.getId()).get(j).getCreateBy()+",");
			}
		}
		Article articlerecommend=new Article();
		articlerecommend.setTitle(article.getTitle());
		articlerecommend.setCreateBy(UserUtils.getUser());
		articlerecommend.setUpdateBy(UserUtils.get(article.getUpdateBy().getId()));
		articlerecommend.setId(article.getId());
		oaNotifyService.save(oaNotifyService.PretreatmentMsgBeforeSave(articlerecommend, "2", "2", stringbufferrecommend.toString()));
	}
	// add yinshh3
		/**
		 * @param 用户对象
		 * return 某个用户所有知识状态的集合()
		 */
		public List<String> getAllDelFlagByUserId(User user){
			
			List<Article> articlelist=articleDao.getArticleID(user);
			List<String> delFlagList=Lists.newArrayList();
			String delFlag=null;
			for(Article article:articlelist){
				delFlag=article.getDelFlag();
				delFlagList.add(delFlag);
			}
			return delFlagList;
		}
		//end
		
		@Transactional(readOnly = false)
		public Page<Article> findArticleListPage(Page<Article> page, Article article, boolean isDataScopeFilter) {
			article.setCreateBy(UserUtils.getUser());
			article.setPage(page);
			page.setList(dao.findListPage(article));
			return page;
		}
	//end
		
	/**
	 * 点击数加一
	 */
	@Transactional(readOnly = false)
	public void updateHitsAddOne(String id) {
		dao.updateHitsAddOne(id);
	}
	
	/**
	 * 更新索引
	 */
	public void createIndex(){
		//dao.createIndex();
	}
	
	/**
	 * 全文检索
	 */
	//FIXME 暂不提供检索功能
	public Page<Article> search(Page<Article> page, String q, String categoryId, String beginDate, String endDate){
		
		// 设置查询条件
//		BooleanQuery query = dao.getFullTextQuery(q, "title","keywords","description","articleData.content");
//		
//		// 设置过滤条件
//		List<BooleanClause> bcList = Lists.newArrayList();
//
//		bcList.add(new BooleanClause(new TermQuery(new Term(Article.FIELD_DEL_FLAG, Article.DEL_FLAG_NORMAL)), Occur.MUST));
//		if (StringUtils.isNotBlank(categoryId)){
//			bcList.add(new BooleanClause(new TermQuery(new Term("category.ids", categoryId)), Occur.MUST));
//		}
//		
//		if (StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate)) {   
//			bcList.add(new BooleanClause(new TermRangeQuery("updateDate", beginDate.replaceAll("-", ""),
//					endDate.replaceAll("-", ""), true, true), Occur.MUST));
//		}   
		
		//BooleanQuery queryFilter = dao.getFullTextQuery((BooleanClause[])bcList.toArray(new BooleanClause[bcList.size()]));

//		System.out.println(queryFilter);
		
		// 设置排序（默认相识度排序）
		//FIXME 暂时不提供lucene检索
		//Sort sort = null;//new Sort(new SortField("updateDate", SortField.DOC, true));
		// 全文检索
		//dao.search(page, query, queryFilter, sort);
		// 关键字高亮
		//dao.keywordsHighlight(query, page.getList(), 30, "title");
		//dao.keywordsHighlight(query, page.getList(), 130, "description","articleData.content");
		
		return page;
	}
	/**
	 * 
	 * 
	 * @param categoryId 分类ids
	 * @param q keywords
	 * @param labelId 标签ids
	 * @param userIdOrContainContent  查询指定用户id的知识(默认条件下是当前的用户,如果传入两个参数的话  那么就是不对内容进行查询)
	 * @return 
	 * 
	 */
	//add by luqibao
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Article> search(String categoryId, String q, String labelId,String...userIdOrContainContent){
		
		/**
		 * start
		 * add by luqibao
		 * 2016-08-04
		 * 新增业务需求  需要根据不同的用户来进行查询  以为涉及到第三方的调用   所以在系统的权限之外  
		 * 所以现在增加了一个userId的参数  意思是说查找用户具有权限的知识
		 * 代号:kms0  (后面很好的识别)
		 * 
		 */
		String searchUser="";		//当前调用查询服务的用户的ID
		if(userIdOrContainContent==null||userIdOrContainContent.length<=0){
			searchUser=UserUtils.getUser().getId();
		}else{
			searchUser=userIdOrContainContent[0];
		}
		/**
		 * end kms0
		 * */
		List<Article> articles=Lists.newLinkedList();//存放文章
		List<Map<String,Object>> maps=Lists.newArrayList();//阿里云返回字段的map集合
		List<Article> articleTemp=null;
		List<Article> labelArticles=Lists.newArrayList();//存放标签的文章
		List<Article> categoryArticles=Lists.newArrayList();//存放知识分类的文章
		List<Article> keywordsArticles=Lists.newArrayList();//关键字的文章
		/*
		 * 对传进来的参数进行加工  返回id的集合
		 */
		final List<String> categoryIds=CmsUtils.transform(categoryId, ",");//分类id
		final List<String> labelIds=CmsUtils.transform(labelId, ",");//标签id
		final List<String> qs=CmsUtils.transform(q, " ");//keywords 集合
		final List<String> articleIds=Lists.newArrayList(); //文章id集合
		
		//start   从阿里云取出数据
		//取出阿里云返回的map集合
		if(qs.size()>0&&qs!=null){
			for(String keyword:qs){
				//System.out.println("keyword::|"+keyword+"|");
				List<Map<String,Object>> temp=Lists.newArrayList();
				try{
					temp=FileStorageUtils.search(keyword);
				}catch(Exception e){
					e.printStackTrace();
				}
				
				//System.out.println(JsonMapper.toJsonString(temp));
				if(temp==null||temp.size()<=0){
					continue;
				}
				for(Map map:temp){
					maps.add(map);
				}
			}
		}
		//获取本地数据库附件表中的id和一些附件相关的信息
		ArticleAttFile articleAttFile=null;
		if(userIdOrContainContent.length<=1){
			for(Map map:maps){
				//System.out.println("identifier:"+map.get("identifier"));
				articleAttFile=new ArticleAttFile();
				articleAttFile.setAttfilekey((String)map.get("identifier"));
				ArticleAttFile temp=articleAttFileService.findRecordByAttFileKey(articleAttFile);
				if(temp!=null){
					articleIds.add(temp.getActicleid());
				}
			}
		}

		//System.out.println("==000==");
		if(articleIds!=null&&articleIds.size()>0){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("ids",articleIds);
			/**
			 * start kms0
			 * old map.put("userid",UserUtils.getUser().getId());
			 */
			map.put("userid",searchUser);
			/**
			 * end kms0
			 */
			articleTemp=articleDao.findListByIds(map);
			//System.out.println("查询完成。。。。。。");
			if(articleTemp!=null&&articleTemp.size()>0){
				keywordsArticles.addAll(articleTemp);
				//articles.addAll(articleTemp);
			}
		}

		//end   从阿里云取出数据
		
		//start 根据分类id的集合取出文章的集合
		if(categoryIds!=null&&categoryIds.size()>0){
			for(String category:categoryIds){
				/**
				 * start kms0
				 * 将原来的 articleTemp=articleDao.findArticlesByCategoryId(category,UserUtils.getUser().getId());
				 * 改成        articleTemp=articleDao.findArticlesByCategoryId(category,searchUser);
				 */
				articleTemp=articleDao.findArticlesByCategoryId(category,searchUser);
				/**
				 * end kms0
				 */
				
				if(articleTemp!=null&&articleTemp.size()>0){
					//articles.addAll(articleTemp);
					categoryArticles.addAll(articleTemp);
				}
			}
		}
		//System.out.println("==222===");
		//end 根据标签id的集合取出文章的集合
		if(labelIds.size()>0&&labelIds!=null){
			for(String label:labelIds){
				//System.out.println("id:"+label);
				/**
				 * start kms0
				 * 将原来的 articleTemp=articleLabelDao.findArticlesByLabelId(label,UserUtils.getUser().getId());
				 * 改为       articleTemp=articleLabelDao.findArticlesByLabelId(label,searchUser);
				 */
				articleTemp=articleLabelDao.findArticlesByLabelId(label,searchUser);
				/**
				 * end kms0
				 */
				
				//System.out.println("标签id："+articleTemp.size());
				if(articleTemp!=null&&articleTemp.size()>0){
					//articles.addAll(articleTemp);
					labelArticles.addAll(articleTemp);
				}
			}
		}

		//System.out.println("==333===");
		if(qs.size()>0){
			//当搜索框中是标题的时候
			/**
			 * start kms0
			 */
				articleTemp=articleDao.findListByTitle(containsSql(qs),searchUser);
			/**
			 * end kms0
			 */
			
		
		if(articleTemp!=null&&articleTemp.size()>0){
			keywordsArticles.addAll(articleTemp);
		}
		//System.out.println("==444===");
		//当搜索框中是标签的时候
		/**
		 * start kms0
		 * old articleTemp=articleDao.findListByLabelValue(containsSql(qs),UserUtils.getUser().getId());
		 */
		articleTemp=articleDao.findListByLabelValue(containsSql(qs),searchUser);
		/**
		 * end kms0
		 */
		
		//System.out.println("标签："+articleTemp.size());
		if(articleTemp!=null&&articleTemp.size()>0){
			keywordsArticles.addAll(articleTemp);
		}
		//System.out.println("==555===");
		//当搜索框中是内容的时候
		/**
		 * start kms0
		 * old articleTemp=articleDao.findListByContent(containsSql(qs),UserUtils.getUser().getId());
		 *  更新2016-01-18 李宁杰: 当传入的参数多余两个的时候  那么就不按照内容来进行查询
		 */
		if(userIdOrContainContent.length<=1){
			articleTemp=articleDao.findListByContent(containsSql(qs),searchUser);
		}
		
		/**
		 * end kms0
		 */
		
		if(articleTemp!=null&&articleTemp.size()>0){
			keywordsArticles.addAll(articleTemp);
		}
		}
		
		//System.out.println("==666===");
		//start 将数据作为AND的关系
		articles.addAll(keywordsArticles);
		articles.addAll(labelArticles);
		articles.addAll(categoryArticles);
//		for(Article a:articles){
//			System.out.println(a.getTitle());
//		}
		if(keywordsArticles!=null&&!StringUtils.isEmpty(q)){
			articles.retainAll(keywordsArticles);
		}
		if(labelArticles!=null&&!StringUtils.isEmpty(labelId)){
			articles.retainAll(labelArticles);
		}
		if(categoryArticles!=null&&!StringUtils.isEmpty(categoryId)){
			articles.retainAll(categoryArticles);
		}
		Map<String,String> labels = new HashMap<String,String>();
		Map<String,String> titles = new HashMap<String,String>();
		Map<String,String> contents = new HashMap<String,String>();
		for(Article a:articles){
			labels.put(a.getId(), a.getPosid());
			titles.put(a.getId(),a.getTitle());
			contents.put(a.getId(),a.getArticleData().getContent());
		}
		//sort-end
		//end
		//System.out.println("777");
		/**
		 * start kms0
		 * old 	User user=UserUtils.getUser();
		 */
		User user=UserUtils.get(searchUser);
		/**
		 * end kms0
		 */
	
		//System.out.println("888");
		List<Article> temp=Lists.newArrayList();
		Store store=null;
		Category category=null;
		for(Article article:articles){
			if(!temp.contains(article)){
				String articleId=article.getId();
				//start 将收藏信息放进去
				store=new Store();
				store.setCreateBy(user);
				store.setTitleId(articleId);
				store=storeDao.get(store);
				article.setStore(store);	
				String categoryid=CmsUtils.getArticlecid(articleId);
				String path=CmsUtils.getCategoryStringByIds(categoryid);
				String categoryName=CmsUtils.getArticleCategoryName(articleId);
				String fPath=path+categoryName;
				category=article.getCategory();
				category.setName(categoryName);
				article.setCategory(category);
				article.setPath(fPath);
				//end 
				temp.add(article);
			}
		}
		//add by yangshw6
		//System.out.println("搜索:"+JsonMapper.toJsonString(temp.size()));
		/**
		 * start kms0
		 */
		
		/**
		 * end kms0
		 */
		List<Article>	newtemp=new LinkedList<Article>(CmsUtils.filterArticle(temp,user));
				
		//System.out.println("搜索:"+JsonMapper.toJsonString(newtemp.size()));
//		for(Article artic:newtemp){
//			System.out.println("搜索结果为:"+JsonMapper.toJsonString(artic));
//		}
		//end by yangshw6
		if(qs.size() > 0){
			//sort(articles,labels,titles,contents,labelIds,qs);
			sort(newtemp, qs);
		}
		return newtemp;
		
	}
	
	//拼接sql 这部分sql是contains（）后面的条件
//	private String baseSql(List<String> list){
//		
//		StringBuffer sb=new StringBuffer();
//		if(list.size()<=0||list==null){
//			return null;
//		}
//		sb.append("'");
//		for(int i=0;i<=list.size()-2;i++){
//			sb.append(list.get(i)+",");
//		}
//		sb.append(list.get(list.size()-1)+"'");
//		return sb.toString();
//	}
	private String containsSql(List<String> list){
		
		StringBuffer sb=new StringBuffer();
		if(list==null||list.size()<=0){
			return null;
		}
		for(int i=0;i<=list.size()-2;i++){
			sb.append(list.get(i)+",");
		}
		sb.append(list.get(list.size()-1));
		return sb.toString();
	}
	/**
	 * 
	 * @return 最新的200条知识.
	 */
	public List<Article> getNewestArticle(){
		List<Article> articleList=Lists.newArrayList();
		Map<String,Object> map=new HashMap<String,Object>();
		List<String> categoryids=categoryService.findCategoryIdByUser(null);
		if(categoryids==null || categoryids.size()==0){
			return articleList;
		}
		map.put("userid",UserUtils.getUser().getId());
		map.put("categoryids",categoryids);
		articleList=articleDao.getNewestArticle(map);
		for(Article article:articleList){
			String articleId=article.getId();
			String categoryid=CmsUtils.getArticlecid(articleId);
			String path=CmsUtils.getCategoryStringByIds(categoryid);
			String categoryName=CmsUtils.getArticleCategoryName(articleId);
			String fPath=path+categoryName;
			article.setPath(fPath);
		}
		return articleList;
	}

	public void MergeCategory(String originalcategoryId, String categoryId) {
		dao.MergeCategory(originalcategoryId, categoryId);
	}
	/*
	 * 获取热门知识的前200条数据
	 * 
	 */
	public List<Article> getHotestArticle(){
		List<Article> articlelist=new ArrayList<Article>();
		Map<String,Object> map=new HashMap<String,Object>();
		List<String> categoryids=categoryService.findCategoryIdByUser(null);
		if(categoryids==null || categoryids.size()==0){
			return articlelist;
		}
		map.put("userid",UserUtils.getUser().getId());
		map.put("categoryids",categoryids);
		articlelist=articleDao.getHotestArticle(map);
		for(Article article:articlelist){
			String articleId=article.getId();
			String categoryid=CmsUtils.getArticlecid(articleId);
			String path=CmsUtils.getCategoryStringByIds(categoryid);
			String categoryName=CmsUtils.getArticleCategoryName(articleId);
			String fPath=path+categoryName;
			article.setPath(fPath);
		}
		return articlelist;
	}

	/**
	 * 该方法用于添加知识弹出窗的搜索
	 * @param map
	 * @return
	 */
	public List<Article> addArticleSearch(Map<String, Object> map) {
		List<Article> articles = new ArrayList<Article>();
		List<Article> articleTemple = new ArrayList<Article>();
		/*System.out.println(listAllArticleLables().toString());
		List<Map<String,String>> list = listAllArticleLables();
		List<String> labelIds = new ArrayList<String>();
		List<String> articleIds = new ArrayList<String>();
		for(Map<String,String> row : list){
			labelIds.add(row.get("LABEL_ID"));
			articleIds.add(row.get("ARTICLE_ID"));
		}*/
//		final List<String> labelIds=CmsUtils.transform(labelId, ",");//标签id
		@SuppressWarnings("unchecked")
		List<String> keyWords = (List<String>) map.get("paramArr");
		
		List<Article> labelAticles = new ArrayList<Article>();
		Map<String,Integer> labels = new HashMap<String,Integer>();
		
		Map<String,String> titles = new HashMap<String,String>();
		//查询标签中包含所有关键字的知识
		for(int i = 0 ; i < keyWords.size() ; i++ ){
			articleTemple = articleDao.findListByLabel(keyWords.get(i), UserUtils.getUser().getId());
			for(Article a:articleTemple){
//				labels.put(a.getId(),a.getPosid());
				titles.put(a.getId(),a.getTitle());
			}
			if(i==0){
				if(articleTemple != null && articleTemple.size()>0){
					labelAticles.addAll(articleTemple);
				}
			}else{
				labelAticles.retainAll(articleTemple);//取交集（获取标签中同时包含多个关键字的知识）
			}
		}
		if(labelAticles != null && labelAticles.size() > 0){
//			labelAticles.removeAll(articles);
//			articles.addAll(labelAticles);
			articles.addAll(labelAticles);
		}
		
//		articles = articleDao.addArticleSearch(map);
		//查询标题中包含所有关键字的知识
		articleTemple = articleDao.addArticleSearch(map);
		if(articleTemple != null && articleTemple.size() > 0){
			articleTemple.removeAll(articles);
			
			for(Article a:articleTemple){
//				labels.put(a.getId(),a.getPosid());
				titles.put(a.getId(),a.getTitle());
			}
			articles.addAll(articleTemple);
			if(keyWords.size() > 0){
//				labels = articleSuitLableMapping2(keyWords);
				labels = articleSuitLableMapping(keyWords);
				sort(articles,labels,titles,keyWords);//对查到的知识集合排序
			}
		}
//		articles = new ArrayList<Article>(new HashSet<Article>(articles));//去除重复知识
				
		/*Map<String,String> labels = new HashMap<String,String>();
		Map<String,String> titles = new HashMap<String,String>();
		for(Article a:articles){
			labels.put(a.getId(),a.getPosid());
			titles.put(a.getId(),a.getTitle());
		}
		if(keyWords.size() > 0){
			sort(articles,labels,titles,keyWords);//对查到的知识集合排序
		}*/
		return articles;
	}
	//匹配在字符串出现的次数
	public int hit(String a, String b) {
	    if (a.length() < b.length()) {
	      return 0;
	    }
	    char[] a_t = a.toCharArray();
	    char[] b_t = b.toCharArray();
	    int count = 0, temp = 0, j = 0;

	    for (int i = 0; i < a_t.length; i++) {
	      // 保证一个连续的字符串 b 跟 a中某段相匹配
	      if (a_t[i] == b_t[j] && j < b_t.length) {
	        temp++;
	        j++;
	        // 此时连续的字符串 b 跟 已跟 a 中某段相匹配
	        if (temp == b_t.length) {
	          count++;
	          temp = 0;
	          j = 0;
	        }
	      }
	      // 只要有一个字符不匹配，temp计数从来
	      else {
	        temp = 0;
	        j = 0;
	      }
	    }

	    return count;
	  }
	//计算一组字符串 分别出现在被匹配字符串的次数的总和
	public int hit2(String a ,List<String> b){
		int count = 0;
		for(int i =0;i<b.size();i++){
			count = count + hit(a,b.get(0));
		}
		
		return count ;
	}
	/***
	 * 
	 * @param articles
	 * @param lables
	 * @param titles
	 * @param contents
	 */
	public void sort(List<Article> articles , 
			final Map<String,String> labels,
			final Map<String,String> titles,
			final Map<String,String> contents,
			final List<String> labelIds,
			final List<String> qs){
		
	//sort排序,addby  yinheng
	//第一步 查询所有的知识  key=知识id   value=标签id集合
	Collections.sort(articles,new Comparator<Article>(){
		public int compare(Article article, Article other) {
			//比较知识关联标签的个数
			String a_label= labels.get(article.getId());
			String o_label= labels.get(other.getId());
			if(Integer.valueOf(a_label) >Integer.valueOf(o_label)){
				return 1;
				
			}else if(Integer.valueOf(a_label) == Integer.valueOf(o_label)){
				//统计关键字在知识标题中的次数
				int	a_count = hit2(titles.get(article.getId()),qs);
				int	o_count = hit2(titles.get(other.getId()),qs);			
				if(a_count>o_count){
					return 1;
				}else if(a_count ==  o_count){
					//统计关键字在正文中的次数		
					int acontent_count = hit2(contents.get(article.getId()),qs);
					int ocontent_count = hit2(contents.get(other.getId()),qs);
					if(acontent_count > ocontent_count){
						return 1;
					}else if(acontent_count == ocontent_count){
						return 0;
					}
					return -1;
				}
				return -1;
			}
			return -1;
		}
	});
	}
	
	public void sort(List<Article> articles,List<String> qs){
		
		//2.查询出每篇知识包含标签的个数  用Hash记录  没有的话 那么就0
		Map<String, Integer> articleSuit = articleSuitLableMapping(qs);
		//3.记录每篇知识的标题有没有其中一个  有的话 放在一个List中
		Map<String,Integer> ifContains = ifContainsArticleMapping(qs);
		//4.进行逻辑排序
		
		Collections.sort(articles,new ComparatorImpl<Article>(articleSuit,ifContains,qs));
	}
	
	private static class ComparatorImpl<T extends Article> implements Comparator<Article>{

		private Map<String, Integer> articleSuit;
		private Map<String,Integer> ifContains;
		private List<String> qs;
		
		
		public ComparatorImpl(Map<String, Integer> articleSuit,
				Map<String,Integer> ifContains,List<String> qs) {
			super();
			this.articleSuit = articleSuit;
			this.ifContains = ifContains;
			this.qs=qs;
		}

		public int compare(Article o1, Article o2) {
			Integer first = articleSuit.get(o1.getId())==null?0:articleSuit.get(o1.getId());
			Integer second=articleSuit.get(o2.getId())==null?0:articleSuit.get(o2.getId());
			
			if(first<second)
				return 1;
			else if(first>second)
				return -1;
			else{
				Integer fir=ifContains.get(o1.getId())==null?0:ifContains.get(o1.getId());
				Integer sec=ifContains.get(o2.getId())==null?0:ifContains.get(o2.getId());
				if(fir<sec){
					return 1;
				}else if(fir>sec){
					return -1;
				}else{
					
					if(contain(qs,o1.getArticleData().getContent())&&!contain(qs,o2.getArticleData().getContent())){
						return -1;
					}else if(!contain(qs,o1.getArticleData().getContent())&&contain(qs,o2.getArticleData().getContent())){
						return 1;
					}else{
						return 0;
					}
				}
			}
		}
	}
	
	/**
	 * 将qs集合全部作为标签的时候  在每一遍知识中出现的次数
	 * @param qs
	 * @return
	 */
	private Map<String,Integer> articleSuitLableMapping(List<String> qs){
		//articleId lable
		List<Map<String, Object>> maps = articleDao.listAllArticleLables();maps.get(0).keySet();
		Map<String,Integer> result=Maps.newHashMap();
		/*for(int i=0;i<maps.size();i++){
			Label lable=(Label) maps.get(i).get("lable");
			String articleId=(String)maps.get(i).get("articleId");
			if(contain(qs,lable.getLabelvalue())){
				if(result.get(articleId)!=null){
					result.put(articleId, result.get(articleId)+1);
				}else{
					result.put(articleId, 1);
				}
			}
		}*/
		for(int i=0;i<maps.size();i++){
			String lableValue = (String) maps.get(i).get("label_value");
			String articleId=(String)maps.get(i).get("articleId");
			if(contain(qs,lableValue)){
				if(result.get(articleId)!=null){
					result.put(articleId, result.get(articleId)+1);
				}else{
					result.put(articleId, 1);
				}
			}
		}
		return result;
	}
	
	private static boolean contain(List<String> qs,String value){
		
		for(String q:qs){
			
			if(value.contains(q))
				return true;
		}
		
		return false;
	}
	/**
	 * 只要标题中出现了集合中任何的一个 那么就应该放入到集合中
	 * @param qs
	 * @return
	 */
	private Map<String,Integer> ifContainsArticleMapping(List<String> qs){
		
		Map<String,Integer> titlesMaps=Maps.newHashMap();
		
		List<Article> lists = articleDao.findAllList(new Article());
		for(Article a:lists){
			for(String q:qs){
				if(a.getTitle().contains(q)){
					if(!titlesMaps.containsKey(a.getId())){
						titlesMaps.put(a.getId(), 1);
					}else{
						titlesMaps.put(a.getId(), titlesMaps.get(a.getId())+1);
					}
				}
			}
		}
		return titlesMaps;
	}
	//add by yangshw6
	/**
	 *根据条件(专题id,标题模糊查询,标签id) 获取知识列表(分页)
	 *
	 */
	public List<Map<String,Object>> getListByCondition(Map<String,Object> map){
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		list=articleDao.getAllListByCondition(map);
		return list;
	}
	/**
	 * 根据user(id) 条件取出原创,转载,全部的知识
	 * 
	 */
	public List<Article> getListByUser(Map<String,Object> param){
		return articleDao.findListByUser(param);
	}
	
	public List<Article> getListByExamer(Map<String,Object> param){
		
		return articleDao.findListByExamer(param);
	}
	
	public int getCountArticleByExamer(Map<String,Object> param){
		
		return articleDao.getCountArticleByExamer(param);
	}
	/**
	 * 根据条件获取总记录数 用于上面方法分页
	 * 
	 */
	public int getCountArticleByUser(Map<String,Object> param){
		return articleDao.getCountArticleByUser(param);
	}


	public boolean saveByApp(Article article,List<String> labellist){
		if (org.apache.commons.lang3.StringUtils.isNotBlank(article.getViewConfig())){
            article.setViewConfig(StringEscapeUtils.unescapeHtml4(article.getViewConfig()));
        }
		ArticleData articleData = article.getArticleData();
		articleData.setAllowshare("1");
		articleData.setId(article.getId());
		article.setCreateDate(new Date());
		dao.insert(article);
		articleDataDao.insert(articleData);
		articleLabelService.saveLabel(labellist,article);
		ArticleCount ac = new ArticleCount(article.getId(),article.getTitle(),0,0,0,0,0,0);
		int kk = articlecountdao.insert(ac);
		return true;
	}
	
	public  List<String> findUserNeedAudit(User user){
		if(user==null||user.getId()==null){
			throw new RuntimeException("用户不能为空");
		}
		return articleDao.findArticleNeedAuditByUserId(user.getId());
	}
	
	/**
	 * 添加知识弹出框中搜索结果优先级排序
	 * 按照关键子在标签中标签出现次数和在标题中出现次数进行排序
	 * @param articles	知识集合
	 * @param labels	标签在知识中出现的次数
	 * @param titles	知识标题
	 * @param qs		用户搜索的关键字集合
	 */
	public void sort(List<Article> articles,final Map<String,Integer> labels,final Map<String,String> titles,final List<String> qs){
		//sort排序,addby  yinheng
		//第一步 查询所有的知识  key=知识id   value=标签id集合
		Collections.sort(articles,new Comparator<Article>(){
			public int compare(Article article, Article other) {
				//比较每一篇知识的标签中包含关键字的个数
				Integer a_label = labels.get(article.getId()) == null ? 0 : labels.get(article.getId());
				Integer o_label = labels.get(other.getId()) == null ? 0 : labels.get(other.getId()); 
				
				if(a_label > o_label){
					return -1;
				}else if(a_label == o_label){
					//统计关键字在知识标题中的次数
					int	a_count = hit2(titles.get(article.getId()),qs);
					int	o_count = hit2(titles.get(other.getId()),qs);			
					if(a_count>o_count){
						return -1;
					}
					return 1;
				}
				return 1;
			}
				
				
		});
	}
	
//	@Deprecated
//	public List<Map<String,String>> listAllArticleLables(){
//		List<Map<String,String>> articleAndLabels = articleDao.listAllArticleLables();
//		return articleAndLabels;
//	}
	
	/**
	 * 将qs集合全部作为标签的时候  在每一遍知识中出现的次数
	 * @param qs
	 * @return
	 */
	private Map<String,Integer> articleSuitLableMapping2(List<String> qs){
		//articleId lable
		List<Map<String, Object>> maps = articleDao.listAllArticleLables2();
		Map<String,Integer> result=Maps.newHashMap();
		for(int i=0;i<maps.size();i++){
			String lableValue = (String) maps.get(i).get("label_value");
			String articleId=(String)maps.get(i).get("articleId");
			if(contain(qs,lableValue)){
				if(result.get(articleId)!=null){
					result.put(articleId, result.get(articleId)+1);
				}else{
					result.put(articleId, 1);
				}
			}
		}
		return result;
	}
	
	/**
	 * 查看未审核知识知识审核人
	 * @param articleId
	 * @return
	 */
	public List<User> articlePreExamers(String articleId,boolean needExamer){
		
		Preconditions.checkArgument(StringUtils.isNoneBlank(articleId),"知识ID不能为空");
		
		List<User> users = articleDao.articlePreExamers(articleId,needExamer);
		if(Objects.equal(users, null)){
			
			users=Lists.newArrayList();
		};
		return users;
	}
	
	/*******************************2017-11-08新增*****************************************/
	public List<Map<String, Object>> getKnowledgeListByCondition(Map<String, Object> map) {
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		list=articleDao.getKnowledgeListByCondition(map);
		return list;
	}
	
	public List<Map<String, Object>> getProductListByCondition(Map<String, Object> map) {
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		list=articleDao.getProductListByCondition(map);
		return list;
	}

	public List<Map<String, Object>> getBusinessListByCondition(Map<String, Object> map) {
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		list=articleDao.getBusinessListByCondition(map);
		return list;
	}
	
}
