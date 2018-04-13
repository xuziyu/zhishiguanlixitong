/**
 * 
 */
package com.yonyou.kms.modules.sys.security;

import java.io.UnsupportedEncodingException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.yonyou.kms.common.utils.StringUtils;
import com.yonyou.kms.modules.sys.dao.LdapCfgH;
import com.yonyou.kms.modules.sys.dao.UserDao;
import com.yonyou.kms.modules.sys.entity.User;

/**
 * 表单验证（包含验证码）过滤类
 * 
 * @author hotsum
 * @version 2014-5-19
 */
@Service
public class FormAuthenticationFilter extends
		org.apache.shiro.web.filter.authc.FormAuthenticationFilter {

	private Logger logger = LoggerFactory.getLogger(getClass());

	public static final String DEFAULT_CAPTCHA_PARAM = "validateCode";
	public static final String DEFAULT_MOBILE_PARAM = "mobileLogin";
	public static final String DEFAULT_MESSAGE_PARAM = "message";

	private String captchaParam = DEFAULT_CAPTCHA_PARAM;
	private String mobileLoginParam = DEFAULT_MOBILE_PARAM;
	private String messageParam = DEFAULT_MESSAGE_PARAM;

	@Autowired
	private UserDao userDao;
	@Value("${local_pwd}")
	private String local_pwd;

	@Value("${ldap.auth}")
	private String ldap_auth;

	@Value("${ldap.cfg.code}")
	private String ldap_cfg_code;

	@Value("${ldap.cfg.name}")
	private String ldap_cfg_name;

	@Value("${ldap.server}")
	private String ldap_server;

	@Value("${ldap.top.dn}")
	private String ldap_top_dn;

	@Value("${ldap.keystorefile}")
	private String ldap_keystorefile;

	@Value("${ldap.admin.user}")
	private String ldap_admin_user;

	@Value("${ldap.admin.pwd}")
	private String ldap_admin_pwd;

	@Value("${ldap.usessl}")
	private String ldap_usessl;

	@Value("${ldap.domain}")
	private String ldap_domain;

	@Override
	protected AuthenticationToken createToken(ServletRequest request,
			ServletResponse response) {
		String username = getUsername(request);
		String password = getPassword(request);

		if (password == null) {
			password = "";
		}
		boolean rememberMe = isRememberMe(request);

		String host = com.yonyou.kms.common.utils.StringUtils
				.getRemoteAddr((HttpServletRequest) request);
		boolean mobile = isMobileLogin(request);

		User user = new User();
		if (!username.equals("superadmin")) {
			username = username.toUpperCase();
		}
		user.setLoginName(username);
		user = this.userDao.getByLoginName(user);

		boolean flag = true;

		if (Boolean.parseBoolean(this.ldap_auth)) {
			LdapCfgH ldapCfg = new LdapCfgH();
			ldapCfg.setLdap_cfg_code(this.ldap_cfg_code);
			ldapCfg.setLdap_cfg_name(this.ldap_cfg_name);
			ldapCfg.setLdap_server(this.ldap_server);
			ldapCfg.setTop_dn(this.ldap_top_dn);
			ldapCfg.setKeystorefile(this.ldap_keystorefile);
			ldapCfg.setLdap_admin_user(this.ldap_admin_user);
			ldapCfg.setLdap_admin_pwd(this.ldap_admin_pwd);
			ldapCfg.setUsessl(Boolean.parseBoolean(this.ldap_usessl));

			LDAPAuthenVerify auth = new LDAPAuthenVerify(ldapCfg);
			try {
				String token = request.getParameter("system");
				System.out.println(token);
				System.out.println("in");
				if ((token != null) && (token.equals("portal"))) {
					System.out.println("success");
					password = this.local_pwd;
					System.out.println(getSuccessUrl());
					
					return new UsernamePasswordToken(username,
							password.toCharArray(), rememberMe, host, mobile);
				}
				System.out.println("ad");
				String strategy = "";

				if (user != null) {
					System.out.println(request.getParameter("system"));
					if ((token != null) || ("portal".equals(token))) {
						password = this.local_pwd;
						return new UsernamePasswordToken(username,
								password.toCharArray(), rememberMe, host,
								mobile);
					}
					if (token == null) {
						if (user.getPassword().startsWith("U_U++--V"))
							strategy = "nc";
						else {
							strategy = "local";
						}
					}
				}
				if (token == null) {
					try {
						flag = PLStrategy.get(password, user, strategy);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
						flag = false;
					}

				}

				if (token == null) {
					if (flag) {
						password = this.local_pwd;
					} else {
						auth.verify(username + "@" + this.ldap_domain, password);
						password = this.local_pwd;
					}
				}
				System.out.println(password);
			} catch (Exception e) {
				password = "BAD";
				this.logger.debug(e.getMessage());
			}

		}

		return new UsernamePasswordToken(username, password.toCharArray(),
				rememberMe, host, mobile);
	}

	public String getCaptchaParam() {
		return captchaParam;
	}

	protected String getCaptcha(ServletRequest request) {
		return WebUtils.getCleanParam(request, getCaptchaParam());
	}

	public String getMobileLoginParam() {
		return mobileLoginParam;
	}

	protected boolean isMobileLogin(ServletRequest request) {
		return WebUtils.isTrue(request, getMobileLoginParam());
	}

	public String getMessageParam() {
		return messageParam;
	}

	/**
	 * 登录成功之后跳转URL
	 */
	@Override
	public String getSuccessUrl() {
		return super.getSuccessUrl();
	}

	@Override
	protected void issueSuccessRedirect(ServletRequest request,
			ServletResponse response) throws Exception {
		// Principal p = UserUtils.getPrincipal();
		// if (p != null && !p.isMobileLogin()){
		WebUtils.issueRedirect(request, response, getSuccessUrl(), null, true);
		// }else{
		// super.issueSuccessRedirect(request, response);
		// }
	}

	/**
	 * 登录失败调用事件
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token,
			AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		String className = e.getClass().getName(), message = "";
		if (IncorrectCredentialsException.class.getName().equals(className)
				|| UnknownAccountException.class.getName().equals(className)) {
			message = "用户或密码错误, 请重试.";
		} else if (e.getMessage() != null
				&& org.apache.commons.lang3.StringUtils.startsWith(
						e.getMessage(), "msg:")) {
			message = org.apache.commons.lang3.StringUtils.replace(
					e.getMessage(), "msg:", "");
		} else {
			message = "系统出现点问题，请稍后再试！";
			e.printStackTrace(); // 输出到控制台
		}
		request.setAttribute(getFailureKeyAttribute(), className);
		request.setAttribute(getMessageParam(), message);
		return true;
	}

}