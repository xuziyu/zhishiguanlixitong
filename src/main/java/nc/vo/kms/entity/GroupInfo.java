package nc.vo.kms.entity;

import java.io.Serializable;

/**
 * ͬ�����ŵ�֪ʶ��ϵͳ�л���
 * @author xiongbo
 *
 */
public class GroupInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ��֯����->֪ʶ��ϵͳ  ��ţ�sys_office.id��
	 */
	private String pk_group;
	
	/**
	 * ��֯����->֪ʶ��ϵͳ ���루sys_office.code��
	 */
	private String code;
	
	/**
	 * ��֯����->֪ʶ��ϵͳ ���ƣ�sys_office.name��
	 */
	private String name;
	
	/**
	 * �ϼ���֯->֪ʶ��ϵͳ  ������ţ�sys_office.parent_id��
	 */
	private String pk_fathergroup;
	
	/**
	 * ����״̬->֪ʶ��ϵͳ  ������ţ�sys_office.useable��
	 */
	private int enablestate;
	/**
	 * ɾ�����->֪ʶ��ϵͳ  ������ţ�sys_office.del_flag��
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
