package nc.vo.kms.entity;

import java.io.Serializable;

/**
 * 同步部门到知识库系统中机构
 * @author xiongbo
 *
 */
public class DeptInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 组织主键->知识库系统  编号（sys_office.id）
	 */
	private String pk_dept;
	
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
	private String pk_fatherorg;
	
	/**
	 * 启用状态->知识库系统  父级编号（sys_office.useable）
	 */
	private int enablestate;
	/**
	 * 删除标记->知识库系统  父级编号（sys_office.del_flag）
	 */
	private String dr;

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

	public String getPk_dept() {
		return pk_dept;
	}

	public void setPk_dept(String pk_dept) {
		this.pk_dept = pk_dept;
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

	public String getPk_fatherorg() {
		return pk_fatherorg;
	}

	public void setPk_fatherorg(String pk_fatherorg) {
		this.pk_fatherorg = pk_fatherorg;
	}

}
