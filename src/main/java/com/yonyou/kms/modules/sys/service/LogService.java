/**
 * 
 */
package com.yonyou.kms.modules.sys.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yonyou.kms.common.persistence.Page;
import com.yonyou.kms.common.service.CrudService;
import com.yonyou.kms.common.utils.DateUtils;
import com.yonyou.kms.modules.sys.dao.LogDao;
import com.yonyou.kms.modules.sys.entity.Log;

/**
 * 日志Service
 * @author hotsum
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class LogService extends CrudService<LogDao, Log> {

	@Override
	public Page<Log> findPage(Page<Log> page, Log log) {
		
		// 设置默认时间范围，默认当前月
		if (log.getBeginDate() == null){
			log.setBeginDate(org.apache.commons.lang3.time.DateUtils.setDays(DateUtils.parseDate(DateUtils.getDate()), 1));
		}
		if (log.getEndDate() == null){
			log.setEndDate(org.apache.commons.lang3.time.DateUtils.addMonths(log.getBeginDate(), 1));
		}
		
		return super.findPage(page, log);
		
	}
	
}
