package com.yonyou.kms.common.job;

import javax.xml.rpc.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.yonyou.kms.modules.cms.service.ArticleCountService;
import com.yonyou.kms.modules.cms.service.DepartContributionService;
import com.yonyou.kms.modules.cms.service.LabelService;
import com.yonyou.kms.modules.cms.service.PersonContributionService;
import com.yonyou.kms.modules.sys.dao.StatusDao;
import com.yonyou.kms.modules.sys.service.OfficeService;
import com.yonyou.kms.modules.sys.service.SystemService;


/**
 * 
 * @author luqibao
 *
 */
@Service
@Lazy(false)
public class DoJob {
	
	
	@Autowired
	private StatusDao statusDao;
	@Autowired
	private PersonContributionService personContributionService;
	@Autowired
	private DepartContributionService departContributionService;
	@Autowired
	private ArticleCountService articleCountService;
	@Autowired
	private LabelService labelService;
	
	@Autowired
	SystemService systemService;
	@Autowired
	OfficeService officeService;
	@Scheduled(cron="${jobs.schedule}")

	public void doJob(){
		try {
			officeService.synchroNCInfo();
			
			systemService.synchroNCInfo();
			//同步标签
			labelService.importLable();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
//		
		
		personContributionService.saveData();
		departContributionService.saveData();
		articleCountService.saveData();
		labelService.saveData();
//		System.out.println("=============调用定时任务===============");
		//end by yangshw6
		
		
	} 
}
