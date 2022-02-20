package com.opentext.explore.importer.trushpilot.pojo;

public enum TrustpilotReviewContainerType {
	LOCAL_BUSINESS("LocalBusiness"),
	DATASET("Dataset"),
	BREADCRUMB_LIST("BreadcrumbList");
	
	private final String label;
	
	private TrustpilotReviewContainerType(String label) {
        this.label = label;
    }

	public String getLabel() {
		return label;
	}	
	
	public static TrustpilotReviewContainerType getContainerTypeFromString(String typeName) {
		TrustpilotReviewContainerType type = null;
		
		if(typeName != null) {
			if(typeName.compareToIgnoreCase(LOCAL_BUSINESS.label) == 0) {
				type = LOCAL_BUSINESS;
			}
			else if(typeName.compareToIgnoreCase(DATASET.label) == 0) {
				type = DATASET;
			}
			else if(typeName.compareToIgnoreCase(BREADCRUMB_LIST.label) == 0) {
				type = BREADCRUMB_LIST;
			}			
		}
		
		return type;
	}
}
