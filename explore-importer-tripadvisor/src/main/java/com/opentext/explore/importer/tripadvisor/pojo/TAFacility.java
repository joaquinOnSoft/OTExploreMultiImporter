package com.opentext.explore.importer.tripadvisor.pojo;

public class TAFacility {
	private String name;
	private String address;
	private String phone;
	private String web;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getWeb() {
		return web;
	}
	
	public void setWeb(String web) {
		this.web = web;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("TAFacility [name=").append(name)
			.append(", address=").append(address)
			.append(", phone=").append(phone)
			.append(", web=").append(web).append("]");
		
		return str.toString();
	}		
}
