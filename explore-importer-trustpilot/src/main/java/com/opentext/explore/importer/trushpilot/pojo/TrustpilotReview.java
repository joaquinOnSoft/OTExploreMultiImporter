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
package com.opentext.explore.importer.trushpilot.pojo;

import java.text.ParseException;
import java.util.Date;

import com.opentext.explore.util.DateUtil;

public class TrustpilotReview {

	/** Consumer name */
	private String consumerName = null;
	/** Rated 1 out of 5 stars */
	private int rate = 0;
	/** Review title */
	private String title = null;
	/** Review text */
	private String text = null;
	/** Review date */
	private Date datetime;

	public TrustpilotReview() {
		
	}		
	
	public TrustpilotReview(String consumerName, int rate, String title, String text, Date datetime) {
		this.consumerName = consumerName;
		this.rate = rate;
		this.title = title;
		this.text = text;
		this.datetime = datetime;
	}

	public TrustpilotReview(String consumerName, int rate, String title, String text, String datetime) throws ParseException {
		this(consumerName, rate, title, text, DateUtil.utcToDate(datetime));
	}	
	
	public String getConsumerName() {
		return consumerName;
	}

	public void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public void setDatetime(String utcDatetime) throws ParseException{
		this.datetime = DateUtil.utcToDate(utcDatetime);
	}		
}
