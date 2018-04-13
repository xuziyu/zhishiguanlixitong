package com.yonyou.kms.modules.cms.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yonyou.kms.common.persistence.Page;
import com.yonyou.kms.common.service.CrudService;
import com.yonyou.kms.modules.cms.dao.ArticleDao;
import com.yonyou.kms.modules.cms.dao.FeaturePackageDao;
import com.yonyou.kms.modules.cms.entity.Article;
import com.yonyou.kms.modules.cms.entity.ArticleFeaturePackage;
import com.yonyou.kms.modules.cms.entity.FeaturePackage;
import com.yonyou.kms.modules.cms.utils.HttpIvokerUtils;
import com.yonyou.kms.modules.sys.entity.User;
import com.yonyou.kms.modules.sys.utils.UserUtils;

/**
 * 专题包管理Service
 */
@Service
@Transactional(readOnly = true)
public class FeaturePackageService extends CrudService<FeaturePackageDao, FeaturePackage> {

	@Autowired
	private FeaturePackageDao featurePackageDao;
	
	@Autowired
	private ArticleDao articleDao;
	
	@Value("${web.http.get}")
    private  String GET_URL;   

	@Value("${web.http.post}")
    private  String POST_URL;
	
	@Value("${web.app.token}")
    private  String KEY;
	/**
	 * 通过专题包名称查询
	 * 
	 * @param name 专题包名称
	 * @return
	 */
	public FeaturePackage getFeaturePackageByName(String name) {
		FeaturePackage fp = new FeaturePackage();
		fp.setName(name);
		return featurePackageDao.getByName(fp);
	}
	
	/**
	 * 保存新增或修改的专题包数据
	 * @param featurePackage
	 */
	@Transactional(readOnly = false)
	public void saveFeaturePackage(FeaturePackage featurePackage,String flag) {
		if (org.apache.commons.lang3.StringUtils.isBlank(featurePackage.getId())){
			featurePackage.preInsert();
			featurePackageDao.insert(featurePackage);
		}else{
			featurePackage.preUpdate();
			featurePackageDao.update(featurePackage);
		}
		if(flag.equals("1")){
			sendHttpByApp(featurePackage);
		}
		
	}
	
	/**
	 * 
	 * 发送请求app后台  同步专题数据
	 * 
	 */
	public boolean sendHttpByApp(FeaturePackage featurePackage){
		JSONObject json=new JSONObject();
		json.put("id",featurePackage.getId());
		json.put("title",featurePackage.getName());
		json.put("issync",Integer.valueOf(featurePackage.getCanShare()));
		
		Map<String,Object>	 map=new HashMap<String,Object>();
		map.put("json", json.toString());
		map.put("key",KEY);
		JSONObject data=HttpIvokerUtils.httpPost(POST_URL,map,false);
		return true;
	}
	
	/**
	 * 逻辑删除专题包
	 * @param featurePackage
	 */
	@Transactional(readOnly = false)
	public void deleteFeaturePackage(FeaturePackage featurePackage) {
		featurePackageDao.delete(featurePackage);
		featurePackageDao.deleteArticleFeaturePackage(featurePackage);
		featurePackageDao.deleteUserFeaturePackage(featurePackage);
	}
	/**
	 *连接app后台 同步专题记录
	 * 
	 * 
	 */
	public boolean SynchFeaturePackageByApp(FeaturePackage feature){
		
		return true;
	}
	
	public Page<FeaturePackage> findInitFeaturePackage(Page<FeaturePackage> page, FeaturePackage featurePackage) {
		page=super.findPage(page, featurePackage);
		return page;
	}
	
	public List<Article> findAllArticle(Article article){
		List<Article> articleList = articleDao.findList(article);
		return articleList;
	}
	public List<User> findAllUserFromFeaturePackageID(String id){
		List<User> users = featurePackageDao.findAllUserFromFeaturePackageID(id);
		return users;
	}

	@Transactional(readOnly = false)
	public Article addArticleToFeaturePackage(FeaturePackage featurePackage,
			Article article) {

		if (article == null){
			return null;
		}
		
		//获取知识所关联的专题包
		List<String> featurePackageIds = featurePackageDao.getFeaturePackageIdsList(article);
		
		if (featurePackageIds.contains(featurePackage.getId())) {
			return null;
		}
		
		ArticleFeaturePackage articleFeaturePackage = new ArticleFeaturePackage();
		articleFeaturePackage.setArticle(article);
		articleFeaturePackage.setFeaturePackage(featurePackage);
		saveArticleFeature(articleFeaturePackage);
		return article;
	}

	@Transactional(readOnly=false)
	private void saveArticleFeature(ArticleFeaturePackage articleFeaturePackage) {
			// 更新专题包与知识关联
			featurePackageDao.insertArticleFeature(articleFeaturePackage);
	}

	//获取专题下已添加的知识
	public List<Article> findArticlesFromFeaturePackage(Article article) {
		List<Article> articleList = articleDao.getArticlesFromFeaturePackage(article);
		return articleList;
	}

	/**
	 * 移除专题包下一条知识
	 * @param featuerPackageId 专题包id
	 * @param aritcleId 知识id
	 */
	@Transactional(readOnly=false)
	public void deleteArtileFeaturePackage(String featuerPackageId,
			String aritcleId) {
		// TODO Auto-generated method stub
		featurePackageDao.deleteArtileFeaturePackage(featuerPackageId,aritcleId);
	}

	/**
	 * 批量删除知识和专题的关系
	 * @param ids
	 * @param featuerPackageId
	 */
	public void delBatch(String ids,String featuerPackageId) {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<String,Object>();
		List<String> list = new ArrayList<String>();
		String[] idsArr = ids.split(",");
		for(String id : idsArr){
			list.add(id);
		}
		map.put("list", list);
		map.put("featuerPackageId", featuerPackageId);
		featurePackageDao.delBatch(map);
	}

	public void assignusertofeaturepackage(String userid, String fid) {
		featurePackageDao.assignusertofeaturepackage(userid,fid);
	}

	public void unassignuser(String userId, String featurePackageId) {
		featurePackageDao.unassignuser(userId,featurePackageId);
	}

	/**
	 * 获取所有专题的最大sort值 
	 * @return
	 */
	public String getMaxSort() {
		// TODO Auto-generated method stub
		String sort = featurePackageDao.getMaxSort();
		if(sort ==null || sort.equals("")){
			sort="0";//如果为空则人为赋值
		}
		return sort;
	}

	/**
	 * 此方法用于首页获取按照sort字段排序好的前五条专题栏目数据
	 * @return
	 */
	public List<FeaturePackage> getFeaturePackageListData() {
		List<FeaturePackage> featurePackageList = new ArrayList<FeaturePackage>();
		featurePackageList = featurePackageDao.getFeaturePackageListData(UserUtils.getUser().getId());
		/*for(FeaturePackage f : featurePackageList){
			String count = featurePackageDao.getCount(f);
			if(count == null || count == ""){
				count = "0";
			}
			f.setCount(count);
		}*/
		if(null != featurePackageList && featurePackageList.size() > 0){
			setCount(featurePackageList);
		}
		return featurePackageList;
	}
	
	/**
	 * 给专题包列表设置专题包中知识数量
	 * @param featurePackageList
	 */
	public void setCount(List<FeaturePackage> featurePackageList){
		for(FeaturePackage f : featurePackageList){
			String count = featurePackageDao.getCount(f);//查询专题包下的知识数量
			if(count == null || count == ""){
				count = "0";
			}
			f.setCount(count);
		}
	}
	
	/**
	 * 自制分页:取出条件下允许的所有专题的条件限制的知识列表
	 * 条件:
	 * 1.是否取可同步app所有专题: isApp  	1:取 0:不取
	 * 2.是否取可分享的知识列表: isShare 	1:取 0:不取
	 * pageLess:页首
	 * pageMore:页尾
	 */
	public List<Article> findAllListByFeature(String isApp,String isShare,int pageSize,int pageNum){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("isApp", isApp);
		map.put("isShare", isShare);
		map.put("pageMore",(pageNum-1)*pageSize+1);
		map.put("pageLess",pageNum*pageSize);
		featurePackageDao.findAllListByFeature(map);
		return null;
	}


	//取出可同步app的所有专题 add by yangshw6
	public List<FeaturePackage> getFeaturePackageByApp(){
		FeaturePackage fp=new FeaturePackage();
		List<FeaturePackage> list=new ArrayList<FeaturePackage>();
		fp.setCanShare("1");
		list=featurePackageDao.findList(fp);
		return list;
	}
	
	/**
	 * 批量删除知识下的所有专题
	 * 
	 */
	public void batchDeleteForArticle(String articleId){
		featurePackageDao.deleteArticle(articleId);
	}
	
	
	
	/**
	 * 获取当前用户具有管理权限的专题
	 * @param userId 当前用户id
	 * @return
	 */
	public List<String> getFeaturePackageByUser(String userId) {
		return featurePackageDao.getFeaturePackageByUser(userId);
	}
	
	/*public List<Article> findArticleByTitle(String title){
		//当搜索框中是标题的时候
		Article article = new Article();
		article.setCategory(new Category());
		article.setTitle(title);
		List<Article> articleList =articleDao.findListByArticleTitle(article);
		return articleList;
	}*/
	/*public List<Article> findArticle(Article article){
		List<Article> articleList = articleDao.findList(article);
		return articleList;
	}*/
	
	
	
	//保存或更新 知识与专题的关联管理
	@Transactional(readOnly = false)
	public boolean saveRelationInFeatureAndArticle(String articleId,List<String> ids){
		Map<String,Object> map=new HashMap<String,Object>();
		List<String> idlist=new ArrayList<String>();
		idlist.add(articleId);
		map.put("featuerPackageId", "");
		map.put("list",idlist);
		featurePackageDao.delBatch(map);
		Article article=new Article(articleId);
		List<ArticleFeaturePackage> list=new ArrayList<ArticleFeaturePackage>();
		ArticleFeaturePackage af=null;
		for(int i=0;i<ids.size();i++){
			String id=ids.get(i);
			af=new ArticleFeaturePackage();
			af.setArticle(article);
			af.setFeaturePackage(new FeaturePackage(id));
			list.add(af);
		}
		batchInsert(list);
		return true;
	}
	
	
	//批量插入关联关系
	@Transactional(readOnly = false)
	public boolean batchInsert(List<ArticleFeaturePackage> list){
		featurePackageDao.batchinsert(list);
		return false;
	}





	/**
	 * 取出知识关联的所有专题数据
	 * 
	 */
	public List<FeaturePackage> getListByArticle(Article article){
		List<FeaturePackage> list=Arrays.asList();
		list=featurePackageDao.getListByArticle(article);
		return list;
	}

	public List<FeaturePackage> findFeaturePackageByUserId(FeaturePackage featurePackage) {
		List<FeaturePackage> list=Arrays.asList();
		list=featurePackageDao.findFeaturePackageByUserId(featurePackage);
		return list;
	}
	
	/****************************2017-11-07新增**************************************/
	//取出可同步app的知识圈专题
	public List<FeaturePackage> getcurrentFeaturePackage(Integer type){
		FeaturePackage fp=new FeaturePackage();
		List<FeaturePackage> list=new ArrayList<FeaturePackage>();
		fp.setCanShare("1");
		fp.setType(type);
		list=featurePackageDao.findKnowledgeList(fp);
		return list;
	}
	
	
}
