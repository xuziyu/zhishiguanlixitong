/**
 * 
 */
package com.yonyou.kms.modules.oa.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yonyou.kms.common.persistence.Page;
import com.yonyou.kms.common.service.CrudService;
import com.yonyou.kms.common.utils.DateUtils;
import com.yonyou.kms.modules.cms.dao.ArticleDao;
import com.yonyou.kms.modules.cms.dao.RecommendDao;
import com.yonyou.kms.modules.cms.entity.Article;
import com.yonyou.kms.modules.cms.entity.Comment;
import com.yonyou.kms.modules.cms.entity.Label;
import com.yonyou.kms.modules.cms.entity.Recommend;
import com.yonyou.kms.modules.cms.entity.Share;
import com.yonyou.kms.modules.cms.entity.Store;
import com.yonyou.kms.modules.cms.utils.CmsUtils;
import com.yonyou.kms.modules.oa.dao.OaNotifyDao;
import com.yonyou.kms.modules.oa.dao.OaNotifyRecordDao;
import com.yonyou.kms.modules.oa.entity.OaNotify;
import com.yonyou.kms.modules.oa.entity.OaNotifyRecord;
import com.yonyou.kms.modules.sys.entity.User;
import com.yonyou.kms.modules.sys.utils.UserUtils;

/**
 * 通知通告Service
 * @author hotsum
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class OaNotifyService extends CrudService<OaNotifyDao, OaNotify> {

	@Autowired
	private OaNotifyRecordDao oaNotifyRecordDao;
	@Autowired 
	ArticleDao articleDao;
	@Autowired 
	RecommendDao recommendDao;

	@Override
	public OaNotify get(String id) {
		OaNotify entity = dao.get(id);
		return entity;
	}
	
	/**
	 * 获取通知发送记录
	 * @param oaNotify
	 * @return
	 */
	public OaNotify getRecordList(OaNotify oaNotify) {
		oaNotify.setOaNotifyRecordList(oaNotifyRecordDao.findList(new OaNotifyRecord(oaNotify)));
		return oaNotify;
	}
	
	public Page<OaNotify> find(Page<OaNotify> page, OaNotify oaNotify) {
		oaNotify.setPage(page);
		page.setList(dao.findList(oaNotify));
		return page;
	}
	
	/**
	 * 获取通知数目
	 * @param oaNotify
	 * @return
	 */
	public Long findCount(OaNotify oaNotify) {
		return dao.findCount(oaNotify);
	}
	
	@Override
	@Transactional(readOnly = false)
	public String save(OaNotify oaNotify) {
		super.save(oaNotify);
		
		// 更新发送接受人记录
		oaNotifyRecordDao.deleteByOaNotifyId(oaNotify.getId());
		if (oaNotify.getOaNotifyRecordList().size() > 0){
			oaNotifyRecordDao.insertAll(oaNotify.getOaNotifyRecordList());
		}
		return "";
	}
	
	/**
	 * 更新阅读状态
	 */
	@Transactional(readOnly = false)
	public void updateReadFlag(OaNotify oaNotify) {
		OaNotifyRecord oaNotifyRecord = new OaNotifyRecord(oaNotify);
		oaNotifyRecord.setUser(oaNotifyRecord.getCurrentUser());
		oaNotifyRecord.setReadDate(new Date());
		oaNotifyRecord.setReadFlag("1");
		oaNotifyRecordDao.update(oaNotifyRecord);
	}
	
	//add hefeng
	
	/**
	 * 
	 * 
	 * @return
	 */
	public Long findUnReadMsg(String userId) {
		if(userId==null){
			userId="";
		}
		return dao.findUnReadMsg(userId);
	}
	
	/**
	 * 获取当前通知阅读时间
	 */
	public Date getReadDateByOaNotifyId(String oaNotifyId) {
		if(oaNotifyId==null){
			oaNotifyId="";
		}
		OaNotifyRecord oaNotifyRecord=new OaNotifyRecord();
		OaNotify oaNotify=new OaNotify();
		oaNotify.setId(oaNotifyId);
		oaNotifyRecord.setOaNotify(oaNotify);
		oaNotifyRecord.setUser(UserUtils.getUser());
		return oaNotifyRecordDao.getReadDateByOaNotifyId(oaNotifyRecord);
	}
	
	/**
	 * 物理删除已读消息
	 */
	public void Physicsdelete(OaNotify oaNotify) {
		oaNotifyRecordDao.Physicsdelete(oaNotify);
	}
	
	/**
	 * 批量物理删除已读消息
	 */
	public void BatchPhysicsdelete() {
		oaNotifyRecordDao.BatchPhysicsdelete(UserUtils.getUser().getId());
	}
	
	/**
	 * 批量更新阅读状态
	 */
	public void BatchupdateReadFlag() {
		OaNotifyRecord oaNotifyRecord=new OaNotifyRecord();
		oaNotifyRecord.setUser(UserUtils.getUser());
		oaNotifyRecord.setReadDate(new Date());
		oaNotifyRecord.setReadFlag("1");
		oaNotifyRecordDao.Batchupdate(oaNotifyRecord);
	}
	/**
	 * 预处理不同的消息通知(1) 知识、附件更改
	 * @param Article article 封装article实体
	 * @param String type 消息类型
	 * @param String OaNotifyRecordIds 发送给谁(1,2,3...)
	 */
	public OaNotify PretreatmentMsgBeforeSave(Article article,String type,String editType,String OaNotifyRecordIds) {
		OaNotify oaNotify=new OaNotify();
		oaNotify.setType(type);
		oaNotify.setTitle(article.getTitle());
		oaNotify.setContent("您"+(type.equals("1")?"收藏":"推荐")+"的这篇知识，用户："+article.getUpdateBy().getName()+"更新了"+(editType.equals("1")?"内容":"附件"));
		oaNotify.setStatus("1");
		oaNotify.setRemarks(article.getId());//传入知识id备用
		oaNotify.setOaNotifyRecordIds(OaNotifyRecordIds);
		return oaNotify;
	}
	
	/**
	 * 预处理不同的消息通知(2) 
	 * @param Recommend recommend 收藏通知
	 */
	public OaNotify PretreatmentMsgBeforeSave(Store store) {
		OaNotify oaNotify=new OaNotify();
		oaNotify.setType("1");
		oaNotify.setTitle(articleDao.get(store.getTitleId()).getTitle());
		oaNotify.setContent("用户："+UserUtils.getUser().getName()+"收藏了您的这篇知识");
		oaNotify.setStatus("1");
		oaNotify.setRemarks(store.getTitleId());//传入知识id备用
		String thisstoreuserid=articleDao.get(store.getTitleId()).getCreateBy().getId();
		if(UserUtils.getUser().getId().equals(thisstoreuserid)){
			
		}else{
			oaNotify.setOaNotifyRecordIds(articleDao.get(store.getTitleId()).getCreateBy().getId());
		}
		return oaNotify;
	}
	/**
	 * 预处理不同的消息通知(2) 
	 * @param Recommend recommend 推荐通知
	 */
	public OaNotify PretreatmentMsgBeforeSave(Recommend recommend) {
		OaNotify oaNotify=new OaNotify();
		oaNotify.setType("2");
		oaNotify.setTitle(articleDao.get(recommend.getTitleId()).getTitle());
		oaNotify.setContent("用户："+UserUtils.getUser().getName()+"推荐了您的这篇知识");
		oaNotify.setStatus("1");
		oaNotify.setRemarks(recommend.getTitleId());//传入知识id备用
		String thisrecommenduserid=articleDao.get(recommend.getTitleId()).getCreateBy().getId();
		if(UserUtils.getUser().getId().equals(thisrecommenduserid)){
			
		}else{
			oaNotify.setOaNotifyRecordIds(articleDao.get(recommend.getTitleId()).getCreateBy().getId());
		}
		return oaNotify;
	}
	
	/**
	 * 预处理不同的消息通知(2) 
	 * @param Share share 分享通知
	 */
	public OaNotify PretreatmentMsgBeforeSave(Share share) {
		OaNotify oaNotify=new OaNotify();
		oaNotify.setType("4");//新增分享类型（待定）
		oaNotify.setTitle(articleDao.get(share.getTitleId()).getTitle());
		oaNotify.setContent("用户："+UserUtils.getUser().getName()+"分享了您的这篇知识");
		oaNotify.setStatus("1");
		oaNotify.setRemarks(share.getTitleId());//传入知识id备用
		String thisshareuserid=articleDao.get(share.getTitleId()).getCreateBy().getId();
		if(UserUtils.getUser().getId().equals(thisshareuserid)){
			
		}else{
		oaNotify.setOaNotifyRecordIds(articleDao.get(share.getTitleId()).getCreateBy().getId());
		}
		return oaNotify;
	}
	
	/**
	 * 预处理不同的消息通知(2) 
	 * @param Comment comment 评论通知
	 */
	public OaNotify PretreatmentMsgBeforeSave(Comment comment) {
		OaNotify oaNotify=new OaNotify();
		oaNotify.setType("3");
		oaNotify.setTitle(articleDao.get(comment.getContentId()).getTitle());
		oaNotify.setContent("用户："+(UserUtils.getUser().getName()==null?"匿名用户":UserUtils.getUser().getName())+"评论了您的这篇知识");
		oaNotify.setStatus("1");
		oaNotify.setRemarks(comment.getContentId());//传入知识id备用
		String thisshareuserid=articleDao.get(comment.getContentId()).getCreateBy().getId();
		if(UserUtils.getUser().getId()==null){
			User user=new User();
			user.setId("niminguser");
			oaNotify.setCreateBy(user);
			//oaNotify.setCreateDate(new Date());
			oaNotify.setUpdateBy(user);
			//oaNotify.setUpdateDate(new Date());
			oaNotify.setOaNotifyRecordIds(articleDao.get(comment.getContentId()).getCreateBy().getId());
		}else if(UserUtils.getUser().getId().equals(thisshareuserid)){
			
		}else{
		oaNotify.setOaNotifyRecordIds(articleDao.get(comment.getContentId()).getCreateBy().getId());
		}
		return oaNotify;
	}
	
	/**
	 * 预处理不同的消息通知(2) 
	 * @param Label label 标签审核通知
	 */
	public OaNotify PretreatmentMsgBeforeSave(Label label) {
		OaNotify oaNotify=new OaNotify();
		oaNotify.setType("5");
		oaNotify.setTitle("标签");
		oaNotify.setContent("你创建的标签<"+label.getLabelvalue()+">已经"+(label.getDelFlag().equals("0")?"通过":"未通过")+"审核");
		oaNotify.setStatus("1");
		oaNotify.setRemarks("标签");//传入知识id备用
		String thislabeluserid=label.getUserid();
		if(UserUtils.getUser().getId().equals(thislabeluserid)){
			
		}else{
		oaNotify.setOaNotifyRecordIds(thislabeluserid);
		}
		return oaNotify;
	}
	//end hefeng
	//add luqibao 批量删除消息记录
		public boolean DelMegAll(String msgIds,int flag){
			try{
				List<String> ids=CmsUtils.transform(msgIds, ",");
				Map<String,Object> map=new HashMap<String,Object>();
				User user=UserUtils.getUser();
				
				map.put("readFlag", flag);
				map.put("readDate", new Date());
				map.put("userId", user.getId());
				map.put("ids", ids);
				oaNotifyRecordDao.delList(map);
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
			return true;
		}
}