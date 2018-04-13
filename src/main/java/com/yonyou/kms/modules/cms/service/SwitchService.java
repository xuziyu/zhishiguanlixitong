/**
 * 
 */
package com.yonyou.kms.modules.cms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yonyou.kms.common.mapper.JsonMapper;
import com.yonyou.kms.common.service.CrudService;
import com.yonyou.kms.modules.cms.dao.SwitchDao;
import com.yonyou.kms.modules.cms.entity.Switch;

/**
 * 文章Service
 * 
 * @author hotsum
 * @version 2013-05-15
 */
@Service
@Transactional(readOnly = true)
public class SwitchService extends CrudService<SwitchDao, Switch> {
	@Autowired
	private SwitchDao switchDao;

	/**
	 * 
	 * @return 全部的vo
	 */
	public List<Switch> getAll() {
		List<Switch> frontswitch = new ArrayList<Switch>();
		if (switchDao.getAll() != null) {
			frontswitch = switchDao.getAll();
		}
		return frontswitch;
	};
	/**
	 * 
	 * @return delFlag为0(启用状态的全部vo)
	 */
	public List<Switch> getAllBydelFlag() {
		List<Switch> frontswitch = new ArrayList<Switch>();
		if (switchDao.getAllBydelFlag() != null) {
			frontswitch = switchDao.getAllBydelFlag();
		}
		return frontswitch;
	};
	/**
	 * 
	 * @param id
	 * @return 根据id得到switch vo
	 */
	public Switch getByID(String id) {
		Switch switch_only = new Switch();
		if (dao.get(id) != null) {
			switch_only = dao.get(id);
		}
		return switch_only;
	}

	/**
	 * 更新操作
	 * 
	 * @param switch_only
	 */
	public void update(Switch switchOnly) {
		try{
			switchOnly.preUpdate();
			switchDao.update(switchOnly);
		}catch(Exception e){
			e.printStackTrace();
		}


	}
}