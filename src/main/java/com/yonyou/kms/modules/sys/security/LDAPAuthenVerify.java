package com.yonyou.kms.modules.sys.security;

import java.security.Security;
import java.util.Hashtable;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

import org.springframework.beans.factory.annotation.Value;

import com.yonyou.kms.modules.sys.dao.LdapCfgH;

import com.yonyou.kms.common.utils.StringUtils;
import com.yonyou.kms.modules.sys.entity.User;

/**
 * 进行LDAP用户名身份验证
 * 
 * 
 */

public class LDAPAuthenVerify {

	protected LdapCfgH ldapCfg = new LdapCfgH();
	protected InitialDirContext dirContext;
	
	public LDAPAuthenVerify(LdapCfgH cfg){
		ldapCfg = cfg;
	}

	public void verify(String userdn, String pwd) throws Exception {

			String ldapurl = ldapCfg.getLdap_server();
			String ldapRoot = ldapCfg.getTop_dn(); 
			if (StringUtils.isEmpty(ldapurl)||StringUtils.isEmpty(ldapRoot)) {
				throw new Exception("AD配置信息不对！");
			}
		
			if (StringUtils.isEmpty(userdn)) {
				throw new Exception("登录用户不能为空！");				
			}

			try {
				DirContext dirContext ;
				if (!this.ldapCfg.getUsessl())
			      {
			        Hashtable env = new Hashtable();
			        env.put("java.naming.factory.initial", this.ldapCfg.getContext_factory());
			        env.put("java.naming.provider.url", this.ldapCfg.getLdap_server());
			        env.put("java.naming.security.authentication", this.ldapCfg.getAuthentication());
			        env.put("java.naming.security.principal", userdn);
			        env.put("java.naming.security.credentials", pwd);
			        dirContext = new InitialDirContext(env);
			      }
			      else
			      {
			        Hashtable env = new Hashtable();

					try {
				        System.setProperty("javax.net.ssl.trustStore", this.ldapCfg.getKeystorefile());
						Class.forName("sun.security.ssl.SSLSocketFactoryImpl");
						Security.setProperty("ssl.SocketFactory.provider","sun.security.ssl.SSLSocketFactoryImpl");
					} catch (ClassNotFoundException e1) {
					}
	
			        env.put("java.naming.factory.initial", this.ldapCfg.getContext_factory());
			        env.put("java.naming.provider.url", this.ldapCfg.getLdap_server());
			        env.put("java.naming.security.authentication", this.ldapCfg.getAuthentication());
			        env.put("java.naming.security.principal", userdn);
			        env.put("java.naming.security.credentials", pwd);
			        env.put("java.naming.security.protocol", "ssl");

			        dirContext = new InitialLdapContext(env, null);
			      }
				dirContext.close();
				
			} catch (Exception e) {
				throw new Exception("用户密码验证失败[用户名：" + userdn + "]: "
						+ e.getMessage());	
			}

	}

	public InitialDirContext getDirContext() throws NamingException {
		if (this.dirContext == null) {
			if (!ldapCfg.getUsessl()){
				Hashtable env = new Hashtable();
				env.put("java.naming.factory.initial", ldapCfg
						.getContext_factory());
				env.put("java.naming.provider.url", ldapCfg
						.getLdap_server());
				env.put("java.naming.security.authentication", this
						.ldapCfg.getAuthentication());
				env.put("java.naming.security.principal", ldapCfg
						.getLdap_admin_user());
				env.put("java.naming.security.credentials", ldapCfg
						.getLdap_admin_pwd());
				this.dirContext = new InitialDirContext(env);
			} else {
				Hashtable env = new Hashtable();
				System.setProperty("javax.net.ssl.trustStore", this
						.ldapCfg.getKeystorefile());
				try {
					Class.forName("sun.security.ssl.SSLSocketFactoryImpl");
					Security.setProperty("ssl.SocketFactory.provider",
							"sun.security.ssl.SSLSocketFactoryImpl");
				} catch (ClassNotFoundException localClassNotFoundException) {
				}
				env.put("java.naming.factory.initial", ldapCfg
						.getContext_factory());
				env.put("java.naming.provider.url", ldapCfg
						.getLdap_server());
				env.put("java.naming.security.authentication", this
						.ldapCfg.getAuthentication());
				env.put("java.naming.security.principal", ldapCfg
						.getLdap_admin_user());
				env.put("java.naming.security.credentials", ldapCfg
						.getLdap_admin_pwd());
				env.put("java.naming.security.protocol", "ssl");

				this.dirContext = new InitialLdapContext(env, null);
			}
		}

		return this.dirContext;
	}
	
}
