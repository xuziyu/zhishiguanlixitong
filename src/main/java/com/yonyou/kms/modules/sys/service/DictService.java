/**
 * 
 */
package com.yonyou.kms.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yonyou.kms.common.service.CrudService;
import com.yonyou.kms.common.utils.CacheUtils;
import com.yonyou.kms.modules.sys.dao.DictDao;
import com.yonyou.kms.modules.sys.entity.Dict;
import com.yonyou.kms.modules.sys.utils.DictUtils;

/**
 * 字典Service
 * @author hotsum
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class DictService extends CrudService<DictDao, Dict> {
	
	/**
	 * 查询字段类型列表
	 * @return
	 */
	public List<String> findTypeList(){
		return dao.findTypeList(new Dict());
	}

	@Override
	@Transactional(readOnly = false)
	public String save(Dict dict) {
		super.save(dict);
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
		return "";
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(Dict dict) {
		super.delete(dict);
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}

}
