package com.opentext.explore.importer.tripadvisor.pojo;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class TAJobInfo {
	private String url;
	private HtmlPage page;
	private int pageNumber;
	private TAJobInfoType type;

	public TAJobInfo() {
		type = TAJobInfoType.TERMINATOR;
	}
	
	public TAJobInfo(String url, HtmlPage page, int pageNumber) {
		this.url = url;
		this.page = page;
		this.pageNumber = pageNumber;
		type = TAJobInfoType.URL;
	}

	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public HtmlPage getPage() {
		return page;
	}
	
	public void setPage(HtmlPage page) {
		this.page = page;
	}
	
	public int getPageNumber() {
		return pageNumber;
	}
	
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public TAJobInfoType getType() {
		return type;
	}

	public void setType(TAJobInfoType type) {
		this.type = type;
	}	
	
	
}
