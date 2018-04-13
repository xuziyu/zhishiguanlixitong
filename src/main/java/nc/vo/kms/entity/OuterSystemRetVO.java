package nc.vo.kms.entity;

import java.io.Serializable;
import java.util.List;

public class OuterSystemRetVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 业务数据(json)
	 */
	private String data;
	
	/**
	 * 调用是否成功
	 */
	private boolean success;
	
	/**
	 * 第三方业务系统数据的状态，如存在于否等  考虑枚举类型
	 */
	private String dataState;
	
	/**
	 * 第三方服务里的异常信息
	 */
	private String errorMsg;
	
	
	private String maxTs;

	public String getMaxTs() {
		return maxTs;
	}

	public void setMaxTs(String maxTs) {
		this.maxTs = maxTs;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getDataState() {
		return dataState;
	}

	public void setDataState(String dataState) {
		this.dataState = dataState;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
