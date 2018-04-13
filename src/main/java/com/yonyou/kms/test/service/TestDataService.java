/**
 * 
 */
package com.yonyou.kms.test.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yonyou.kms.common.persistence.Page;
import com.yonyou.kms.common.service.CrudService;
import com.yonyou.kms.test.entity.TestData;
import com.yonyou.kms.test.dao.TestDataDao;

/**
 * 单表生成Service
 * @author hotsum
 * @version 2015-04-06
 */
@Service
@Transactional(readOnly = true)
public class TestDataService extends CrudService<TestDataDao, TestData> {

	@Override
	public TestData get(String id) {
		return super.get(id);
	}
	
	@Override
	public List<TestData> findList(TestData testData) {
		return super.findList(testData);
	}
	
	@Override
	public Page<TestData> findPage(Page<TestData> page, TestData testData) {
		return super.findPage(page, testData);
	}
	
	@Override
	@Transactional(readOnly = false)
	public String save(TestData testData) {
		super.save(testData);
		return "";
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(TestData testData) {
		super.delete(testData);
	}
	
}