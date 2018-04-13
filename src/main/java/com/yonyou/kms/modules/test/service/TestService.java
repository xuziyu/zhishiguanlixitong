/**
 * 
 */
package com.yonyou.kms.modules.test.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yonyou.kms.common.service.CrudService;
import com.yonyou.kms.modules.test.entity.Test;
import com.yonyou.kms.modules.test.dao.TestDao;

/**
 * 测试Service
 * @author hotsum
 * @version 2013-10-17
 */
@Service
@Transactional(readOnly = true)
public class TestService extends CrudService<TestDao, Test> {

}
