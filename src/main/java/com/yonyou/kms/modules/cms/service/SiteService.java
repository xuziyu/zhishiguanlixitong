/**
 * 
 */
package com.yonyou.kms.modules.cms.service;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yonyou.kms.common.persistence.BaseEntity;
import com.yonyou.kms.common.persistence.Page;
import com.yonyou.kms.common.service.CrudService;
import com.yonyou.kms.modules.cms.dao.SiteDao;
import com.yonyou.kms.modules.cms.entity.Site;
import com.yonyou.kms.modules.cms.utils.CmsUtils;

/**
 * 站点Service
 * @author hotsum
 * @version 2013-01-15
 */
@Service
@Transactional(readOnly = true)
public class SiteService extends CrudService<SiteDao, Site> {

	@Override
	public Page<Site> findPage(Page<Site> page, Site site) {
//		DetachedCriteria dc = siteDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(site.getName())){
//			dc.add(Restrictions.like("name", "%"+site.getName()+"%"));
//		}
//		dc.add(Restrictions.eq(Site.FIELD_DEL_FLAG, site.getDelFlag()));
//		//dc.addOrder(Order.asc("id"));
//		return siteDao.find(page, dc);
		
		site.getSqlMap().put("site", dataScopeFilter(site.getCurrentUser(), "o", "u"));
		
		return super.findPage(page, site);
	}

	@Override
	@Transactional(readOnly = false)
	public String save(Site site) {
		if (site.getCopyright()!=null){
			site.setCopyright(StringEscapeUtils.unescapeHtml4(site.getCopyright()));
		}
		super.save(site);
		CmsUtils.removeCache("site_"+site.getId());
		CmsUtils.removeCache("siteList");
		return "";
	}
	
	@Transactional(readOnly = false)
	public void delete(Site site, Boolean isRe) {
		//siteDao.updateDelFlag(id, isRe!=null&&isRe?Site.DEL_FLAG_NORMAL:Site.DEL_FLAG_DELETE);
		site.setDelFlag(isRe!=null&&isRe?BaseEntity.DEL_FLAG_NORMAL:BaseEntity.DEL_FLAG_DELETE);
		super.delete(site);
		//siteDao.delete(id);
		CmsUtils.removeCache("site_"+site.getId());
		CmsUtils.removeCache("siteList");
	}
	
}
