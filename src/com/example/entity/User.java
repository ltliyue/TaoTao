package com.example.entity;

public class User {
	public String id;
	public String tinyurl;
	public String name;
	public String headurl;
	
	public User(){
		
	}
	
	public User(String id, String tinyurl, String name, String headurl) {
		super();
		this.id = id;
		this.tinyurl = tinyurl;
		this.name = name;
		this.headurl = headurl;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTinyurl() {
		return tinyurl;
	}

	public void setTinyurl(String tinyurl) {
		this.tinyurl = tinyurl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHeadurl() {
		return headurl;
	}

	public void setHeadurl(String headurl) {
		this.headurl = headurl;
	}
	 
}

