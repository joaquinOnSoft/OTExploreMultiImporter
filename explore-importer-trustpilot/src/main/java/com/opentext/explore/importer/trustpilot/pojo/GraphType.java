/*
 *   (C) Copyright 2022 OpenText and others.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *   Contributors:
 *     Joaquín Garzón - initial implementation
 *
 */

package com.opentext.explore.importer.trustpilot.pojo;

public enum GraphType {
	ORGANIZATION("Organization"),
	IMAGE_OBJECT("ImageObject"),
	WEBSITE("WebSite"),
	WEBPAGE("WebPage"),
	BREADCRUMB_LIST("BreadcrumbList"),
	LOCAL_BUSINESS("LocalBusiness"),	
	REVIEW("Review");
	
	
	private final String label;
	
	private GraphType(String label) {
        this.label = label;
    }

	public String getLabel() {
		return label;
	}	
	
	public static GraphType getGraphTypeFromString(String typeName) {
		GraphType type = null;
		
		if(typeName != null) {
			if(typeName.compareToIgnoreCase(ORGANIZATION.label) == 0) {
				type = ORGANIZATION;
			}
			else if(typeName.compareToIgnoreCase(IMAGE_OBJECT.label) == 0) {
				type = IMAGE_OBJECT;
			}						
			else if(typeName.compareToIgnoreCase(WEBSITE.label) == 0) {
				type = WEBSITE;
			}
			else if(typeName.compareToIgnoreCase(WEBPAGE.label) == 0) {
				type = WEBPAGE;
			}			
			else if(typeName.compareToIgnoreCase(BREADCRUMB_LIST.label) == 0) {
				type = BREADCRUMB_LIST;
			}				
			else if(typeName.compareToIgnoreCase(LOCAL_BUSINESS.label) == 0) {
				type = LOCAL_BUSINESS;
			}			
			else if(typeName.compareToIgnoreCase(REVIEW.label) == 0) {
				type = REVIEW;
			}		
		}
		
		return type;
	}
}
