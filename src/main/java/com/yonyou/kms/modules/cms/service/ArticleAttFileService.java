/**
 * 
 */
package com.yonyou.kms.modules.cms.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yonyou.kms.common.config.Global;
import com.yonyou.kms.common.persistence.BaseEntity;
import com.yonyou.kms.common.service.CrudService;
import com.yonyou.kms.modules.cms.dao.ArticleAttFileDao;

import com.yonyou.kms.modules.cms.entity.Article;
import com.yonyou.kms.modules.cms.entity.ArticleAttFile;
import com.yonyou.kms.modules.cms.entity.ArticleData;
import com.yonyou.kms.modules.cms.entity.Category;
import com.yonyou.kms.modules.sys.utils.UserUtils;


/**
 * 附件Service
 * @author huangmj
 * @version 2015-10-15
 */
@Service
@Transactional(readOnly = true)
public class ArticleAttFileService extends CrudService<ArticleAttFileDao, ArticleAttFile> {
	
	
	
	@Transactional(readOnly = false)
	public void save_update(ArticleAttFile articleAttFile) {
	
		articleAttFile.setUpdateBy(UserUtils.getUser());
		articleAttFile.setUpdateDate(new Date());
		//System.out.println("进入update更新articleAttFile");
		articleAttFile.preUpdate();
		dao.update(articleAttFile);
		}
	
	//找关联文章的所有有效附件
	public List<ArticleAttFile> findListFile(ArticleAttFile articleAttFile) {
		List<ArticleAttFile> list  = dao.findListFile(articleAttFile);
		return list;
	}

	//根据附件ID找附件
	public ArticleAttFile findFile(ArticleAttFile articleAttFile) {
		ArticleAttFile file  = dao.findFile(articleAttFile);
		return file;
	}
	
	//删除附件
	public void deleteAttfile(ArticleAttFile articleAttFile) {
		super.delete(articleAttFile);
	}
	
	//批量删除关联文章附件
	public void deleteList(ArticleAttFile articleAttFile){
		dao.deleteList(articleAttFile);
	}
	
	//保存文章，使上传的附件生效
	public void postattfile(String temp_guid){
		dao.postattfile(temp_guid);
	}
	
	//获取文章附件数
	public String getArticleAttFileNumber(String articleID){
		return dao.getArticleAttFileNumber(articleID);
	}
	
	//add hefeng
	public ArticleAttFile findRecordByAttFileKey(ArticleAttFile articleAttFile) {
		if(articleAttFile==null){
			articleAttFile=new ArticleAttFile();
		}
		return dao.findRecordByAttFileKey(articleAttFile);
	}
	
	public List<ArticleAttFile> finddiffByGuid(String temp_guid) {
		if(temp_guid==null){
			temp_guid="";
		}
		return dao.finddiffByGuid(temp_guid);
	}
	//end
}
	
	

