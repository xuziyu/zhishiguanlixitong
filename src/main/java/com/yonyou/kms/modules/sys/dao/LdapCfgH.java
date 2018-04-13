/*     */ package com.yonyou.kms.modules.sys.dao;
/*     */ 
/*     */ /*     */ 
/*     */ public class LdapCfgH {
/*     */   private String pk_ldap_cfg;
/*     */   private String ldap_cfg_code;
/*     */   private String ldap_cfg_name;
/*     */   private String user_pkcorp_rule;
/*     */   private String ldap_data_mode;
/*     */   private String ldap_admin_user;
/*     */   private String ldap_admin_pwd;
/*     */   private String ldap_vender;
/*     */   private String ldap_server;
/*     */   private Integer ldap_port;
/*     */   private String context_factory;
/*     */   private String authentication;
/*     */   private boolean usessl;
/*     */   private String keystorefile;
/*     */   private String default_domain;
/*     */   private String top_dn;
/*     */   private String syn_direction;
/*     */   private String nc_obj_type_att;
/*     */   private String nc_obj_pk_att;
/*     */   private String corpattname_inuser;
/*     */   private String deptattname_inuser;
/*     */   private String ncsys_att_inuser;
/*     */   private String ncsys_value_inuser;
/*     */   private String ncsys_att_name;
/*     */   private String ldap_uid_att_name;
/*     */   private String hr_uid_att_name;
/*     */   private String sendmailto;
/*     */   private String usersearchscope;
/*     */   private String def1;
/*     */   private String def2;
/*     */   private String def3;
/*     */   private String def4;
/*     */   private String def5;
/*     */   public static final String PK_LDAP_CFG = "pk_ldap_cfg";
/*     */   public static final String TABLE_CODE = "csyn_ldap_cfg";
/*     */   public static final String LDAP_VENDER_AD = "AD";
/*     */   public static final String LDAP_VENDER_GENERAL = "GENERAL";
/*     */   public static final String LDAP_DATA_MODE_CORPDEPTPSN = "CORPDEPTPSN";
/*     */   public static final String LDAP_DATA_MODE_CORPPSN = "CORPPSN";
/*     */   public static final String LDAP_DATA_MODE_PSN = "PSN";
/*     */   public static final String SYN_DIRECTION_NC2LDAP = "NC2LDAP";
/*     */   public static final String SYN_DIRECTION_LDAP2NC = "LDAP2NC";
/*     */   public static final String USER_PKCORP_RULE_CLERK_CORP_FIRST = "CF";
/*     */   public static final String USER_PKCORP_RULE_USER_CREATE_CORP = "UC";
/*     */ 
/*     */   public String getPKFieldName()
/*     */   {
/*  81 */     return "pk_ldap_cfg";
/*     */   }
/*     */ 
/*     */   public String getParentPKFieldName()
/*     */   {
/*  87 */     return null;
/*     */   }
/*     */ 
/*     */   public String getTableName()
/*     */   {
/*  93 */     return "csyn_ldap_cfg";
/*     */   }
/*     */ 
/*     */   public String getPk_ldap_cfg()
/*     */   {
/*  98 */     return this.pk_ldap_cfg;
/*     */   }
/*     */ 
/*     */   public void setPk_ldap_cfg(String pkLdapCfg)
/*     */   {
/* 103 */     this.pk_ldap_cfg = pkLdapCfg;
/*     */   }
/*     */ 
/*     */   public String getLdap_data_mode()
/*     */   {
/* 108 */     return this.ldap_data_mode;
/*     */   }
/*     */ 
/*     */   public void setLdap_data_mode(String ldapDataMode)
/*     */   {
/* 113 */     this.ldap_data_mode = ldapDataMode;
/*     */   }
/*     */ 
/*     */   public String getLdap_admin_user()
/*     */   {
/* 118 */     return this.ldap_admin_user;
/*     */   }
/*     */ 
/*     */   public void setLdap_admin_user(String ldapAdminUser)
/*     */   {
/* 123 */     this.ldap_admin_user = ldapAdminUser;
/*     */   }
/*     */ 
/*     */   public String getLdap_admin_pwd()
/*     */   {
/* 128 */     return this.ldap_admin_pwd;
/*     */   }
/*     */ 
/*     */   public void setLdap_admin_pwd(String ldapAdminPwd)
/*     */   {
/* 133 */     this.ldap_admin_pwd = ldapAdminPwd;
/*     */   }
/*     */ 
/*     */   public String getLdap_vender()
/*     */   {
/* 138 */     return this.ldap_vender;
/*     */   }
/*     */ 
/*     */   public void setLdap_vender(String ldapVender)
/*     */   {
/* 143 */     this.ldap_vender = ldapVender;
/*     */   }
/*     */ 
/*     */   public String getLdap_server()
/*     */   {
/* 148 */     return this.ldap_server;
/*     */   }
/*     */ 
/*     */   public void setLdap_server(String ldapServer)
/*     */   {
/* 153 */     this.ldap_server = ldapServer;
/*     */   }
/*     */ 
/*     */   public boolean getUsessl()
/*     */   {
/* 162 */     return this.usessl;
/*     */   }
/*     */ 
/*     */   public void setUsessl(boolean usessl)
/*     */   {
/* 167 */     this.usessl = usessl;
/*     */   }
/*     */ 
/*     */   public String getKeystorefile()
/*     */   {
/* 172 */     return this.keystorefile;
/*     */   }
/*     */ 
/*     */   public void setKeystorefile(String keystorefile)
/*     */   {
/* 177 */     this.keystorefile = keystorefile;
/*     */   }
/*     */ 
/*     */   public String getTop_dn()
/*     */   {
/* 182 */     return this.top_dn;
/*     */   }
/*     */ 
/*     */   public void setTop_dn(String topDn)
/*     */   {
/* 187 */     this.top_dn = topDn;
/*     */   }
/*     */ 
/*     */   public String getDef1()
/*     */   {
/* 192 */     return this.def1;
/*     */   }
/*     */ 
/*     */   public void setDef1(String def1)
/*     */   {
/* 197 */     this.def1 = def1;
/*     */   }
/*     */ 
/*     */   public String getDef2()
/*     */   {
/* 202 */     return this.def2;
/*     */   }
/*     */ 
/*     */   public void setDef2(String def2)
/*     */   {
/* 207 */     this.def2 = def2;
/*     */   }
/*     */ 
/*     */   public String getDef3()
/*     */   {
/* 212 */     return this.def3;
/*     */   }
/*     */ 
/*     */   public void setDef3(String def3)
/*     */   {
/* 217 */     this.def3 = def3;
/*     */   }
/*     */ 
/*     */   public String getDef4()
/*     */   {
/* 222 */     return this.def4;
/*     */   }
/*     */ 
/*     */   public void setDef4(String def4)
/*     */   {
/* 227 */     this.def4 = def4;
/*     */   }
/*     */ 
/*     */   public String getDef5()
/*     */   {
/* 232 */     return this.def5;
/*     */   }
/*     */ 
/*     */   public void setDef5(String def5)
/*     */   {
/* 237 */     this.def5 = def5;
/*     */   }
/*     */ 
/*     */   public String getLdap_cfg_code()
/*     */   {
/* 242 */     return this.ldap_cfg_code;
/*     */   }
/*     */ 
/*     */   public void setLdap_cfg_code(String ldapCfgCode)
/*     */   {
/* 247 */     this.ldap_cfg_code = ldapCfgCode;
/*     */   }
/*     */ 
/*     */   public String getLdap_cfg_name()
/*     */   {
/* 252 */     return this.ldap_cfg_name;
/*     */   }
/*     */ 
/*     */   public void setLdap_cfg_name(String ldapCfgName)
/*     */   {
/* 257 */     this.ldap_cfg_name = ldapCfgName;
/*     */   }
/*     */ 
/*     */   public String getUser_pkcorp_rule()
/*     */   {
/* 262 */     return this.user_pkcorp_rule;
/*     */   }
/*     */ 
/*     */   public void setUser_pkcorp_rule(String userPkcorpRule)
/*     */   {
/* 267 */     this.user_pkcorp_rule = userPkcorpRule;
/*     */   }
/*     */ 
/*     */ 
/*     */   public String getSyn_direction()
/*     */   {
/* 282 */     return this.syn_direction;
/*     */   }
/*     */ 
/*     */   public void setSyn_direction(String synDirection)
/*     */   {
/* 287 */     this.syn_direction = synDirection;
/*     */   }
/*     */ 
/*     */   public String getCorpattname_inuser()
/*     */   {
/* 292 */     return this.corpattname_inuser;
/*     */   }
/*     */ 
/*     */   public void setCorpattname_inuser(String corpattnameInuser)
/*     */   {
/* 297 */     this.corpattname_inuser = corpattnameInuser;
/*     */   }
/*     */ 
/*     */   public String getAuthentication()
/*     */   {
/* 302 */     if ((this.authentication == null) || (this.authentication.trim().length() == 0))
/*     */     {
/* 304 */       this.authentication = "simple";
/*     */     }
/* 306 */     return this.authentication;
/*     */   }
/*     */ 
/*     */   public void setAuthentication(String authentication)
/*     */   {
/* 311 */     this.authentication = authentication;
/*     */   }
/*     */ 
/*     */   public String getContext_factory()
/*     */   {
/* 316 */     if (this.context_factory == null)
/*     */     {
/* 318 */       this.context_factory = "com.sun.jndi.ldap.LdapCtxFactory";
/*     */     }
/* 320 */     return this.context_factory;
/*     */   }
/*     */ 
/*     */   public void setContext_factory(String contextFactory)
/*     */   {
/* 325 */     this.context_factory = contextFactory;
/*     */   }
/*     */ 
/*     */   public String getNcsys_att_inuser()
/*     */   {
/* 330 */     return this.ncsys_att_inuser;
/*     */   }
/*     */ 
/*     */   public void setNcsys_att_inuser(String ncsysAttInuser)
/*     */   {
/* 335 */     this.ncsys_att_inuser = ncsysAttInuser;
/*     */   }
/*     */ 
/*     */   public String getNcsys_value_inuser()
/*     */   {
/* 340 */     return this.ncsys_value_inuser;
/*     */   }
/*     */ 
/*     */   public void setNcsys_value_inuser(String ncsysValueInuser)
/*     */   {
/* 345 */     this.ncsys_value_inuser = ncsysValueInuser;
/*     */   }
/*     */ 
/*     */   public String getNcsys_att_name()
/*     */   {
/* 350 */     return this.ncsys_att_name;
/*     */   }
/*     */ 
/*     */   public void setNcsys_att_name(String ncsysAttName)
/*     */   {
/* 355 */     this.ncsys_att_name = ncsysAttName;
/*     */   }
/*     */ 
/*     */   public String getDeptattname_inuser()
/*     */   {
/* 360 */     return this.deptattname_inuser;
/*     */   }
/*     */ 
/*     */   public void setDeptattname_inuser(String deptattnameInuser)
/*     */   {
/* 365 */     this.deptattname_inuser = deptattnameInuser;
/*     */   }
/*     */ 
/*     */   public String getLdap_uid_att_name()
/*     */   {
/* 370 */     return this.ldap_uid_att_name;
/*     */   }
/*     */ 
/*     */   public void setLdap_uid_att_name(String ldapUidAttName)
/*     */   {
/* 375 */     this.ldap_uid_att_name = ldapUidAttName;
/*     */   }
/*     */ 
/*     */   public String getHr_uid_att_name()
/*     */   {
/* 380 */     return this.hr_uid_att_name;
/*     */   }
/*     */ 
/*     */   public void setHr_uid_att_name(String hrUidAttName)
/*     */   {
/* 385 */     this.hr_uid_att_name = hrUidAttName;
/*     */   }
/*     */ 
/*     */   public Integer getLdap_port()
/*     */   {
/* 390 */     return this.ldap_port;
/*     */   }
/*     */ 
/*     */   public void setLdap_port(Integer ldap_port)
/*     */   {
/* 395 */     this.ldap_port = ldap_port;
/*     */   }
/*     */ 
/*     */   public String getSendmailto()
/*     */   {
/* 400 */     return this.sendmailto;
/*     */   }
/*     */ 
/*     */   public void setSendmailto(String sendmailto)
/*     */   {
/* 405 */     this.sendmailto = sendmailto;
/*     */   }
/*     */ 
/*     */ 
/*     */   public String getNc_obj_type_att()
/*     */   {
/* 433 */     if ((this.nc_obj_type_att == null) || (this.nc_obj_type_att.trim().length() == 0))
/*     */     {
/* 435 */       this.nc_obj_type_att = "ncObjType1";
/*     */     }
/* 437 */     return this.nc_obj_type_att;
/*     */   }
/*     */ 
/*     */   public String getNc_obj_pk_att()
/*     */   {
/* 443 */     if ((this.nc_obj_pk_att == null) || (this.nc_obj_pk_att.trim().length() == 0))
/*     */     {
/* 445 */       this.nc_obj_pk_att = "ncObjPk1";
/*     */     }
/* 447 */     return this.nc_obj_pk_att;
/*     */   }
/*     */ 
/*     */   public void setNc_obj_type_att(String nc_obj_type_att)
/*     */   {
/* 452 */     this.nc_obj_type_att = nc_obj_type_att;
/*     */   }
/*     */ 
/*     */   public void setNc_obj_pk_att(String nc_obj_pk_att)
/*     */   {
/* 457 */     this.nc_obj_pk_att = nc_obj_pk_att;
/*     */   }
/*     */ 
/*     */   public String getDefault_domain()
/*     */   {
/* 462 */     return this.default_domain;
/*     */   }
/*     */ 
/*     */   public void setDefault_domain(String default_domain)
/*     */   {
/* 467 */     this.default_domain = default_domain;
/*     */   }
/*     */ 
/*     */   public String getUsersearchscope()
/*     */   {
/* 472 */     return this.usersearchscope;
/*     */   }
/*     */ 
/*     */   public void setUsersearchscope(String usersearchscope)
/*     */   {
/* 477 */     this.usersearchscope = usersearchscope;
/*     */   }
/*     */ }

/* Location:           E:\WORK\iuap\DOC\天禾\syn2ad\syn2ad\classes\
 * Qualified Name:     nc.csyn.vo.ldapcfg.LdapCfgH
 * JD-Core Version:    0.6.2
 */