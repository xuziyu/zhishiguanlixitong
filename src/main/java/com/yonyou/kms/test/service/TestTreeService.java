/**
 * 
 */
package com.yonyou.kms.test.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yonyou.kms.common.service.TreeService;
import com.yonyou.kms.test.entity.TestTree;
import com.yonyou.kms.test.dao.TestTreeDao;

/**
 * 树结构生成Service
 * @author hotsum
 * @version 2015-04-06
 */
@Service
@Transactional(readOnly = true)
public class TestTreeService extends TreeService<TestTreeDao, TestTree> {

	@Override
	public TestTree get(String id) {
		return super.get(id);
	}
	
	@Override
	public List<TestTree> findList(TestTree testTree) {
		if (org.apache.commons.lang3.StringUtils.isNotBlank(testTree.getParentIds())){
			testTree.setParentIds(","+testTree.getParentIds()+",");
		}
		return super.findList(testTree);
	}
	
	@Override
	@Transactional(readOnly = false)
	public String save(TestTree testTree) {
		super.save(testTree);
		return "";
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(TestTree testTree) {
		super.delete(testTree);
	}
	
}