package nc.vo.kms.entity;

import java.io.Serializable;

/**
 * 同步业务单元到知识库系统中机构
 * @author xiongbo
 *
 */
public class NCOffice implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 对应知识库系统编号（sys_office.id）
	 */
	private String id;
	
	/**
	 * 对应知识库系统 编码（sys_office.code）
	 */
	private String code;
	
	/**
	 * 对应知识库系统 名称（sys_office.name）
	 */
	private String name;
	
	/**
	 * 对应知识库系统  父级编号（sys_office.parent_id）
	 */
	private String parent_id;
	
	/**
	 * 对应知识库系统  是否启用（sys_office.useable）
	 */
	private String useable;
	/**
	 * 对应知识库系统  删除标识（sys_office.del_flag）
	 */
	private String del_flag;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}
	public String getUseable() {
		return useable;
	}
	public void setUseable(String useable) {
		this.useable = useable;
	}
	public String getDel_flag() {
		return del_flag;
	}
	public void setDel_flag(String del_flag) {
		this.del_flag = del_flag;
	}

}
