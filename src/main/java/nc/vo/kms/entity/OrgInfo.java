package nc.vo.kms.entity;

import java.io.Serializable;

/**
 * ͬ��ҵ��Ԫ��֪ʶ��ϵͳ�л���
 * @author xiongbo
 *
 */
public class OrgInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ��֯����->֪ʶ��ϵͳ  ��ţ�sys_office.id��
	 */
	private String pk_org;
	
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
	private String pk_fatherorg;
	
	/**
	 * ����״̬->֪ʶ��ϵͳ  ������ţ�sys_office.useable��
	 */
	private int enablestate;
	/**
	 * ɾ�����->֪ʶ��ϵͳ  ������ţ�sys_office.del_flag��
	 */
	private String dr;

	public String getPk_org() {
		return pk_org;
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

	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
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