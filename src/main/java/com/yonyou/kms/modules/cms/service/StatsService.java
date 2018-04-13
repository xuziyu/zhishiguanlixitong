/**
 * 
 */
package com.yonyou.kms.modules.cms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yonyou.kms.common.service.BaseService;
import com.yonyou.kms.common.utils.DateUtils;
import com.yonyou.kms.modules.cms.dao.ArticleDao;
import com.yonyou.kms.modules.cms.entity.Category;
import com.yonyou.kms.modules.cms.entity.Site;
import com.yonyou.kms.modules.sys.dao.UserDao;
import com.yonyou.kms.modules.sys.entity.Office;

/**
 * 统计Service
 * @author hotsum
 * @version 2013-05-21
 */
@Service
@Transactional(readOnly = true)
public class StatsService extends BaseService {

	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private UserDao	userDao;
	
	public List<Category> article(Map<String, Object> paramMap) {
		Category category = new Category();
		
		Site site = new Site();
		site.setId(Site.getCurrentSiteId());
		category.setSite(site);
		
		Date beginDate = DateUtils.parseDate(paramMap.get("beginDate"));
		if (beginDate == null){
			beginDate = org.apache.commons.lang3.time.DateUtils.setDays(new Date(), 1);
			paramMap.put("beginDate", DateUtils.formatDate(beginDate, "yyyy-MM-dd"));
		}
		category.setBeginDate(beginDate);
		Date endDate = DateUtils.parseDate(paramMap.get("endDate"));
		if (endDate == null){
			endDate = org.apache.commons.lang3.time.DateUtils.addDays(org.apache.commons.lang3.time.DateUtils.addMonths(beginDate, 1), -1);
			paramMap.put("endDate", DateUtils.formatDate(endDate, "yyyy-MM-dd"));
		}
		category.setEndDate(endDate);
		
		String categoryId = (String)paramMap.get("categoryId");
		if (categoryId != null && !("".equals(categoryId))){
			category.setId(categoryId);
			category.setParentIds(categoryId);
		}
		
		String officeId = (String)(paramMap.get("officeId"));
		Office office = new Office();
		if (officeId != null && !("".equals(officeId))){
			office.setId(officeId);
			category.setOffice(office);
		}else{
			category.setOffice(office);
		}
		
		List<Category> list = articleDao.findStats(category);
		return list;
	}
	
	
	/**
	 *上传者统计
	 * 
	 */
	public List<Map<String,Object>> uploaderStats(Map<String,Object> param){
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		list=userDao.findStatsListByUser(param);
		return list;
	}
	/**
	 * 审核者统计
	 */
	public List<Map<String,Object>> examerStats(Map<String,Object> param){
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		list=userDao.findExamerStatsList(param);
		return list;
	}
	
}
