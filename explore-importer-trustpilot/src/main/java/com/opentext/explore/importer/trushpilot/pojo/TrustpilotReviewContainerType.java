package com.opentext.explore.importer.trushpilot.pojo;

public enum TrustpilotReviewContainerType {
	LOCAL_BUSINESS("LocalBusiness"),
	DATASET("Dataset"),
	BREADCRUMB_LIST("BreadcrumbList");
	
	public final String label;
	
	private TrustpilotReviewContainerType(String label) {
        this.label = label;
    }	
}
