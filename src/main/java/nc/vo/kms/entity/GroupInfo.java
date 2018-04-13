package nc.vo.kms.entity;

import java.io.Serializable;

/**
 * 同步集团到知识库系统中机构
 * @author xiongbo
 *
 */
public class GroupInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 组织主键->知识库系统  编号（sys_office.id）
	 */
	private String pk_group;
	
	/**
	 * 组织编码->知识库系统 编码（sys_office.code）
	 */
	private String code;
	
	/**
	 * 组织编码->知识库系统 名称（sys_office.name）
	 */
	private String name;
	
	/**
	 * 上级组织->知识库系统  父级编号（sys_office.parent_id）
	 */
	private String pk_fathergroup;
	
	/**
	 * 启用状态->知识库系统  父级编号（sys_office.useable）
	 */
	private int enablestate;
	/**
	 * 删除标记->知识库系统  父级编号（sys_office.del_flag）
	 */
	private String dr;

	public String getPk_group() {
		return pk_group;
	}

	public int getEnablestate() {
		return enablestate;
	}

	public void setEnablestate(int enablestate) {
		this.enablestate = enablestate;
	}

	public String getDr() {
		return dr;
	}

	public void setDr(String dr) {
		this.dr = dr;
	}

	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPk_fathergroup() {
		return pk_fathergroup;
	}

	public void setPk_fathergroup(String pk_fathergroup) {
		this.pk_fathergroup = pk_fathergroup;
	}
}
