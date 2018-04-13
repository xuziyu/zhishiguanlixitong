/**
 * 
 */
package com.yonyou.kms.modules.test.web;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.rpc.ServiceException;

import nc.itf.kms.synchro.ISynchroNCInfoLocator;
import nc.itf.kms.synchro.ISynchroNCInfoPortType;
import nc.vo.kms.entity.OrgInfo;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yonyou.kms.common.persistence.Page;
import com.yonyou.kms.common.web.BaseController;
import com.yonyou.kms.modules.sys.entity.Area;
import com.yonyou.kms.modules.sys.entity.Office;
import com.yonyou.kms.modules.sys.entity.User;
import com.yonyou.kms.modules.sys.service.OfficeService;
import com.yonyou.kms.modules.sys.utils.SynchroNCUtil;
import com.yonyou.kms.modules.sys.utils.UserUtils;
import com.yonyou.kms.modules.test.entity.Test;
import com.yonyou.kms.modules.test.service.TestService;

/**
 * 测试Controller
 * 
 * @author hotsum
 * @version 2013-10-17
 */
@Controller
@RequestMapping(value = "${adminPath}/test/test")
public class TestController extends BaseController {

	@Autowired
	private TestService testService;

	@Autowired
	private OfficeService officeService;

	@ModelAttribute
	public Test get(@RequestParam(required = false)
	String id) {
		if (org.apache.commons.lang3.StringUtils.isNotBlank(id)) {
			return testService.get(id);
		} else {
			return new Test();
		}
	}

	/**
	 * 显示列表页
	 * 
	 * @param test
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("test:test:view")
	@RequestMapping(value = { "list", "" })
	public String list(Test test, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		return "modules/test/testList";
	}

	/**
	 * 获取硕正列表数据（JSON）
	 * 
	 * @param test
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("test:test:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<Test> listData(Test test, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			test.setCreateBy(user);
		}
		Page<Test> page = testService.findPage(
				new Page<Test>(request, response), test);
		return page;
	}

	/**
	 * 新建或修改表单
	 * 
	 * @param test
	 * @param model
	 * @return
	 */
	@RequiresPermissions("test:test:view")
	@RequestMapping(value = "form")
	public String form(Test test, Model model) {
		model.addAttribute("test", test);
		return "modules/test/testForm";
	}

	/**
	 * 表单保存方法
	 * 
	 * @param test
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("test:test:edit")
	@RequestMapping(value = "save")
	public String save(Test test, Model model,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, test)) {
			return form(test, model);
		}
		// testService.save(test);
		addMessage(redirectAttributes, "保存测试'" + test.getName() + "'成功");
		return "redirect:" + adminPath + "/test/test/?repage";
	}

	/**
	 * 删除数据方法
	 * 
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("test:test:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(Test test, RedirectAttributes redirectAttributes) {
		// testService.delete(test);
		// addMessage(redirectAttributes, "删除测试成功");
		// return "redirect:" + adminPath + "/test/test/?repage";
		return "true";
	}

	@RequestMapping(value = "test")
	public String test() throws MalformedURLException {
		Logger logger = LoggerFactory.getLogger(getClass());

		// String url = "http://127.0.0.1/uapws/service/IHelloWorld";
		//		
		// IHelloWorld service = null;
		//		
		// Service serviceModel = new ObjectServiceFactory()
		// .create(IHelloWorld.class);
		//
		// service = (IHelloWorld) new XFireProxyFactory().create(serviceModel,
		// url);
		//		
		// String result = service.sayHelloName("001", "zhangsan");
		//		
		// System.out.println(result);

		try {
			/*********************获取NC业务单元***************/
			// 1.调用NC接口获取组织数据
			ISynchroNCInfoLocator service = new ISynchroNCInfoLocator();
			ISynchroNCInfoPortType client = service
					.getISynchroNCInfoSOAP11port_http();
			logger.debug("============开始调用NC远程接口：getNCOrgInfo()========================");
			OrgInfo[] orgs = (OrgInfo[]) client.getNCOrgInfo("design");
			logger.debug("============结束调用NC远程接口,返回数据size:"+orgs.length+"========================");
			
			// 2.将NC组织信息转换成Office
			List<Office> list = new ArrayList<Office>();

			Office office = null;
			Office parent = null;
			OrgInfo orgInfo = null;
			Map<String, String> relations = new HashMap<String, String>();//存储key:机构id value:机构的所有parentId
			for (int i = 0; i < orgs.length; i++) {
				orgInfo = orgs[i];
				office = new Office();
				office.setId(orgInfo.getPk_org());

				// 判断是否为根节点
//				if (!StringUtils.isEmpty(orgInfo.getPk_fatherorg())
//						&& (!"0".endsWith(orgInfo.getPk_fatherorg()))) {

					parent = new Office();
					parent.setId(orgInfo.getPk_fatherorg());
					office.setParent(parent);
//				}

				office.setCode(orgInfo.getCode());
				office.setName(orgInfo.getName());
				
				office.setParentIds(orgInfo.getPk_fatherorg());
				office.setSort(10);
				
				Area area = new Area();
				area.setId("857c22660e5e4648b8db1942915affd2");
				
				office.setArea(area);
				office.setType("1");
				office.setGrade("1");
				office.setCreateBy(UserUtils.getUser());
				office.setCreateDate(new Date());
				office.setUpdateBy(UserUtils.getUser());
				office.setUpdateDate(new Date());
				
				//设置在上下级关系
				relations.put(office.getId(), orgInfo.getPk_fatherorg());
				list.add(office);
			}
			
			//3.构建机构的parentIds
			Map<String, String> newRelations = new HashMap<String, String>(); //存储key:机构id value:机构的所有parentIds","分割
			String parentIds = "";
			for(Office temp:list){
				parentIds = SynchroNCUtil.getParentIds(temp.getParentId(), relations);
				temp.setParentIds(parentIds);
				newRelations.put(temp.getId(), parentIds);
			}
			
			//4.批量保存组织
			//officeService.batchSave(list);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		return "true";
	}
}
