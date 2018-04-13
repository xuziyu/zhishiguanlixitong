package nc.vo.kms.entity;

import java.io.Serializable;

/**
 * ͬ��ҵ��Ԫ��֪ʶ��ϵͳ�л���
 * @author xiongbo
 *
 */
public class NCOffice implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ��Ӧ֪ʶ��ϵͳ��ţ�sys_office.id��
	 */
	private String id;
	
	/**
	 * ��Ӧ֪ʶ��ϵͳ ���루sys_office.code��
	 */
	private String code;
	
	/**
	 * ��Ӧ֪ʶ��ϵͳ ���ƣ�sys_office.name��
	 */
	private String name;
	
	/**
	 * ��Ӧ֪ʶ��ϵͳ  ������ţ�sys_office.parent_id��
	 */
	private String parent_id;
	
	/**
	 * ��Ӧ֪ʶ��ϵͳ  �Ƿ����ã�sys_office.useable��
	 */
	private String useable;
	/**
	 * ��Ӧ֪ʶ��ϵͳ  ɾ����ʶ��sys_office.del_flag��
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
