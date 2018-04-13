/**
 * 
 */
package com.yonyou.kms.modules.cms.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.yonyou.kms.common.config.Global;
import com.yonyou.kms.common.persistence.DataEntity;
import com.yonyou.kms.modules.sys.entity.User;

/**
 * 文章附件实体
 * @author huangmj
 * @version 2015-10-15
 */
public class ArticleAttFile extends DataEntity<ArticleAttFile> {

	private static final long serialVersionUID = 1L;
	private String id;		// 编号
	private String acticleid;	// 文章ID
	private String attfiletime;// 附件时间戳
	private String attfilename;// 附件原始名
	private String attfilesize;// 附件大小
	private String attfiletype;// 附件类型
	//private String att_file_user;// 附件作者
	//private Date   att_file_time;// 上传附件时间
	private String attfilekey;   // 附件服务器存放key值
	private String isnew;
	private String attfile_temp_guid;//唯一标识对应文章
	private String ispostarticle;//0未保存，1文章保存
	//private User user;
	//private Article article;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getActicleid() {
		return acticleid;
	}
	public void setActicleid(String acticleid) {
		this.acticleid = acticleid;
	}
	public String getAttfiletime() {
		return attfiletime;
	}
	public void setAttfiletime(String attfiletime) {
		this.attfiletime = attfiletime;
	}
	public String getAttfilename(){
		return attfilename;
	}
	public void setAttfilename(String attfilename){
		this.attfilename = attfilename;
	}
	public String getAttfilesize(){
		return attfilesize;
	}
	public void setAttfilesize(String attfilesize) {
		this.attfilesize = attfilesize;
	}
	public String getAttfiletype() {
		return attfiletype;
	}
	public void setAttfiletype(String attfiletype) {
		this.attfiletype = attfiletype;
	}
	public String getAttfilekey() {
		return attfilekey;
	}
	public void setAttfilekey(String attfilekey) {
		this.attfilekey = attfilekey;
	}
	public String getIsnew() {
		return isnew;
	}
	public void setIsnew(String isnew) {
		this.isnew = isnew;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getAttfile_temp_guid() {
		return attfile_temp_guid;
	}
	public void setAttfile_temp_guid(String attfile_temp_guid) {
		this.attfile_temp_guid = attfile_temp_guid;
	}
	public String getIspostarticle() {
		return ispostarticle;
	}
	public void setIspostarticle(String ispostarticle) {
		this.ispostarticle = ispostarticle;
	}
	

}