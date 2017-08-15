package com.win.test.annotation;

@EnableCreate(name = "objectA")
public class ObjectA {

	@SetVal("wangzhao")
	private String name;
	@SetVal("China")
	private String address;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		System.out.println("setName");
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	@Override
	public String toString() {
		return "[ name: " + name + ", address: " + address + " ]";
	}

}
