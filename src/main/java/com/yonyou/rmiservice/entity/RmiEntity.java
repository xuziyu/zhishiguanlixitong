package com.yonyou.rmiservice.entity;

import java.io.Serializable;

public class RmiEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String id;
	
	private String name;
	
	private String content;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public RmiEntity(String id, String name, String content) {
		this.id = id;
		this.name = name;
		this.content = content;
	}

	public RmiEntity() {
		
	}
		

}
