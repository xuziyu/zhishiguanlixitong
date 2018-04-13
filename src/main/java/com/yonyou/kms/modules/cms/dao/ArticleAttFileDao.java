
package com.yonyou.kms.modules.cms.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.yonyou.kms.common.persistence.CrudDao;
import com.yonyou.kms.common.persistence.annotation.MyBatisDao;
import com.yonyou.kms.modules.cms.entity.Article;
import com.yonyou.kms.modules.cms.entity.ArticleAttFile;

/**
 * 文章附件DAO接口
 * @author huangmj
 * @version 2015-10-15
 */
@MyBatisDao
public interface ArticleAttFileDao extends CrudDao<ArticleAttFile> {
	
	public List<ArticleAttFile> findListFile(ArticleAttFile articleAttFile);
	
	public ArticleAttFile findFile(ArticleAttFile articleAttFile);
	
	public void deleteList(ArticleAttFile articleAttFile);
	
	public void postattfile(String temp_guid);
	
	public String getArticleAttFileNumber(String articleID);
	
	//add hefeng
	public ArticleAttFile findRecordByAttFileKey(ArticleAttFile articleAttFile);
	
	public List<ArticleAttFile> finddiffByGuid(String temp_guid);
	//end
	
}
