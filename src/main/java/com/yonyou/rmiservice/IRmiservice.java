package com.yonyou.rmiservice;

import java.util.List;

import com.yonyou.rmiservice.entity.RmiEntity;

public interface IRmiservice {
	public void test();
	public List<RmiEntity> getCategory();
	public String submitKnowledge(String data);
}
