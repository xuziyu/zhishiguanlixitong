/**
 * 
 */
package com.yonyou.kms.modules.sys.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.Maps;
import com.yonyou.kms.common.config.Global;
import com.yonyou.kms.common.security.shiro.session.SessionDAO;
import com.yonyou.kms.common.servlet.ValidateCodeServlet;
import com.yonyou.kms.common.utils.CacheUtils;
import com.yonyou.kms.common.utils.CookieUtils;
import com.yonyou.kms.common.utils.IdGen;
import com.yonyou.kms.common.utils.StringUtils;
import com.yonyou.kms.common.web.BaseController;
import com.yonyou.kms.modules.cms.service.ArticleService;
import com.yonyou.kms.modules.cms.service.CommentService;
import com.yonyou.kms.modules.cms.service.LabelService;
import com.yonyou.kms.modules.sys.entity.User;
import com.yonyou.kms.modules.sys.security.FormAuthenticationFilter;
import com.yonyou.kms.modules.sys.security.PLStrategy;
import com.yonyou.kms.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.yonyou.kms.modules.sys.utils.UserUtils;

/**
 * 登录Controller
 * @author 
 */
@Controller
public class Login2Controller2 extends BaseController{
	
	@Autowired
	private SessionDAO sessionDAO;
	@Autowired
	private ArticleService articleService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private LabelService lableService;
	
	/**
	 * 管理登录
	 */
	@RequestMapping(value = "${adminPath}/login1", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
		Principal principal = UserUtils.getPrincipal();
		
		
		//articleService.search(category, keywords, labels);
		
		
//		// 默认页签模式
//		String tabmode = CookieUtils.getCookie(request, "tabmode");
//		if (tabmode == null){
//			CookieUtils.setCookie(response, "tabmode", "1");
//		}
		
		if (logger.isDebugEnabled()){
			logger.debug("login, active session size: {}", sessionDAO.getActiveSessions(false).size());
		}
		
//		-----------
////		 如果已登录，再次访问主页，则退出原账号。
//		if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))){
//			CookieUtils.setCookie(response, "LOGINED", "false");
//		}
		
		// 如果已经登录，则跳转到管理首页
		if(principal != null && !principal.isMobileLogin()){
			
			return "redirect:" + adminPath;
		}
		//---------------
		
		
//		//add by luqibao
//		// 当请求是GET的时候，退出当前的账号，需要重新登录
//		if(principal != null && !principal.isMobileLogin()){
//			UserUtils.getSubject().logout();
//		}
//		//end
//		//UserUtils.getSubject().logout();
		//snowing
		
			model.addAttribute("ncLogin", true);
		
		
		return "modules/sys/sysLogin";
	}}
	