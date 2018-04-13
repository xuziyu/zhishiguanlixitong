package com.yonyou.kms.modules.cms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yonyou.kms.common.service.CrudService;
import com.yonyou.kms.modules.cms.dao.ArticleCountDao;
import com.yonyou.kms.modules.cms.entity.ArticleCount;
import com.yonyou.kms.modules.cms.utils.ArticleCountUtil;
import com.yonyou.kms.modules.cms.utils.ContributionUtil;
/**
 * 
 * 
 * 知识统计表service
 * @author Administrator
 *
 */
@Service
@Transactional(readOnly = false)
public class ArticleCountService extends CrudService<ArticleCountDao, ArticleCount>{
	@Autowired
	private ArticleCountDao articlecountdao;
	/*
	 * 统计各个表中的数据，在取出存入到知识统计表中
	 * 
	 */
	public boolean saveData(){
		boolean flag=false;
		List<ArticleCount>  list=new ArrayList<ArticleCount>();
		List<ArticleCount>  insertlist=new ArrayList<ArticleCount>();
		List<ArticleCount>  updatelist=new ArrayList<ArticleCount>();
		list=articlecountdao.getDataFromTable();
		
//		for(int i=0;i<list.size();i++){
//			System.out.println("list:"+list.get(i).getArticleid()+"--"+list.get(i).getArticletitle()+"--"+list.get(i).getCountclick());
//		}
		//System.out.println("list:"+list.toString()+"--"+list.size());
		List<String> idfromCount=new ArrayList<String>();
		idfromCount=articlecountdao.getAllid();
		List<String> idfromlist=new ArrayList<String>();
		for(int i=0;i<list.size();i++){
			idfromlist.add(list.get(i).getArticleid());
			
//			System.out.println("ArticleCount:"+list.get(i).getArticletitle());
//			String judge=idfromCount.get(i);
			int count=0;
			String id=list.get(i).getArticleid();
			for(int j=0;j<idfromCount.size();j++){
				String did=idfromCount.get(j);
				if(id.equals(did)){
					count++;
					break;
				}
			}
			if(count==0){
				//System.out.println("judge为null");
				ArticleCount ac=ArticleCountUtil.copy(list.get(i));
				insertlist.add(ac);
				//System.out.println("ArticleCount中insertlistpc的值为"+ac.toString());
			}else if(count==1){
				//System.out.println("judge不为null");
				ArticleCount ac=ArticleCountUtil.copy(list.get(i));
				updatelist.add(ac);
				//System.out.println("ArticleCount中updatelistpc的值为"+ac.toString());
			}
		}
		//查出差异的id，批量删除，校正数据正误
		List<String> diff=ContributionUtil.getDiffrent(idfromCount, idfromlist);
		if(diff.size()>0){
			articlecountdao.deleteData(diff);
		}
		if(insertlist.size()>0){
			//System.out.println("计算ArticleCount出的插入数据为:"+insertlist.toString()+"-"+insertlist.size());
			int insert=articlecountdao.insertArticleCount(insertlist);
			//System.out.println("insert"+insert);
		}
		if(updatelist.size()>0){
			//System.out.println(updatelist.size()+"---"+"计算ArticleCount出的更新数据为:"+updatelist.toString()+"-"+updatelist.size());
			int update=articlecountdao.updateArticleCount(updatelist);
			//System.out.println("update"+update);
		}
		flag=true;
		return flag;
	}
	/*
	 * 从知识统计表按权重取出前几条的数据
	 * @param categorylist 知识分类id（权限控制）
	 * 
	 */
	public List<ArticleCount>  getArticleCountData(List<String> categorylist){
		List<ArticleCount> list=new ArrayList<ArticleCount>();
		list=articlecountdao.getArticleCountData(categorylist);
		//System.out.println("从cms_article_acount表中读取的数据为:"+JsonMapper.toJsonString(list));
		return list;
	}
	/*
	 * 
	 * 取出知识统计表中，关于分享数的前几条数据，按分享数排行
	 * @param categoryid:知识分类id
	 * 
	 */
	public List<ArticleCount> getArticleCountShareData(List<String> categoryid){
		List<ArticleCount> list=new ArrayList<ArticleCount>();
		list=articlecountdao.getArticleShareData(categoryid);
		//System.out.println("从cms_article_acount表中读取的数据为:"+JsonMapper.toJsonString(list));
		return list;
	}
	
	/*
	 * 
	 * 取出知识统计表中最新的知识,根据权限取出用户下的知识分类
	 * 
	 * @param categoryidlis 知识分类id(权限控制)
	 * 
	 */
	public List<ArticleCount> getNewArticleCountData(List<String> categoryid){
		List<ArticleCount> list=new ArrayList<ArticleCount>();
		list=articlecountdao.getNewArticleCountData(categoryid);
		//System.out.println("从cms_article_acount表中读取的数据为:"+JsonMapper.toJsonString(list));
		return list;
	}
	
	@Override
	public String save(ArticleCount entity) {
		ArticleCount articleCount= articlecountdao.get(entity);
//		if(articleCount!=null){
//			entity.setIsNewRecord(false);
//		}
//		if (entity.getIsNewRecord()){
//			entity.preInsert();
//			dao.insert(entity);
//		}else{
		entity.setIsNewRecord(false);
			entity.preUpdate();
			dao.update(entity);
//		}
			return "";
	}
	
	//add hefeng
	public int findArticleRecommendCount(ArticleCount entity){
		ArticleCount articleCount=new ArticleCount();
		articleCount=dao.get(entity.getArticleid());
		if(articleCount==null){
			return 0;
		}
		return dao.get(entity).getCountreco();
	}
	
	public int findArticleShareCount(ArticleCount entity){
		if(dao.get(entity)==null){
			return 0;
		}
		return dao.get(entity).getCountshare();
	}
	//end
	
	//计算并插入(更新)单条知识数据到知识统计表中,当知识变成已发布状态,不需要已发布的全部知识全部导入
	public void insertSingleData(String articleid){
		ArticleCount article=articlecountdao.getSingleData(articleid);//计算出该知识的数据
		//System.out.println("insertSingleData..."+JsonMapper.toJsonString(article));
		String id=articlecountdao.getData(articleid);
		if(id==null){
			//System.out.println("已进入。。。。插入");
			articlecountdao.insert(article);
		}else{
			return;
		}
	}
	/*
	 * 点击推荐,收藏,点赞,分享或发表评论时,数据统计表加一或减一
	 *	@param 	type:1为推荐,2为收藏,3为分享,4为评论,5为点击,6为点赞
	 * 	@param	change:1为+1,0为-1,记录加1，记录减1
	 */
	
	public void updateSingleData(int type,int change,String articleid){
		ArticleCount ac=null;
		switch(type){
		case 1:
			if(change==1){
				ac=new ArticleCount();
				ac.setCountreco(1);
				ac.setArticleid(articleid);
				articlecountdao.updateSingleAdd(ac);
			}else{
				ac=new ArticleCount();
				ac.setCountreco(1);
				ac.setArticleid(articleid);
				articlecountdao.updateSingleReduce(ac);
			}
			break;
		case 2:
			if(change==1){
				ac=new ArticleCount();
				ac.setCountcollect(1);
				ac.setArticleid(articleid);
				articlecountdao.updateSingleAdd(ac);
			}else{
				ac=new ArticleCount();
				ac.setCountcollect(1);
				ac.setArticleid(articleid);
				articlecountdao.updateSingleReduce(ac);
			}
			break;
		case 3:
			if(change==1){
				ac=new ArticleCount();
				ac.setCountshare(1);
				ac.setArticleid(articleid);
				articlecountdao.updateSingleAdd(ac);
			}else{
				ac=new ArticleCount();
				ac.setCountshare(1);
				ac.setArticleid(articleid);
				articlecountdao.updateSingleReduce(ac);
			}
			break;
		case 4:
			if(change==1){
				ac=new ArticleCount();
				ac.setCountcomm(1);
				ac.setArticleid(articleid);
				articlecountdao.updateSingleAdd(ac);
			}else{
				ac=new ArticleCount();
				ac.setCountcomm(1);
				ac.setArticleid(articleid);
				articlecountdao.updateSingleReduce(ac);
			}
			break;
		case 5:
			if(change==1){
				ac=new ArticleCount();
				ac.setCountclick(1);
				ac.setArticleid(articleid);
				articlecountdao.updateSingleAdd(ac);
			}
			break;
		case 6:
			if(change==1){
				ac=new ArticleCount();
				ac.setCountthumbs(1);
				ac.setArticleid(articleid);
				articlecountdao.updateSingleAdd(ac);
			}else{
				ac=new ArticleCount();
				ac.setCountthumbs(1);
				ac.setArticleid(articleid);
				articlecountdao.updateSingleReduce(ac);
			}
			break;	
		}	
	}
	public void deleteSingleData(String articleid){
		articlecountdao.deleteSingleData(articleid);
	}
	
	public int getThumbsNumByArticleId(String articleId,String articleTitle){
		int k = 0;
		ArticleCount articleCount = articlecountdao.get(articleId);
		if (null != articleCount) {
			k = articleCount.getCountthumbs();
		}else{
			ArticleCount ac = new ArticleCount(articleId,articleTitle,0,0,0,0,0,0);
			int kk = articlecountdao.insert(ac);
		}
		return k;
	}
}
