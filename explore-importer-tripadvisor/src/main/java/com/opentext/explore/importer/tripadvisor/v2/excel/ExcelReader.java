/*
 *   (C) Copyright 2021 OpenText and others.
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
package com.opentext.explore.importer.tripadvisor.v2.excel;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.opentext.explore.importer.tripadvisor.pojo.TAFacility;
import com.opentext.explore.importer.tripadvisor.pojo.TAReview;

/**
 * Read excel files in Java using a very simple yet powerful open source library
 * called Apache POI.
 * 
 * @see https://www.callicoder.com/java-read-excel-file-apache-poi/
 * @author Joaquín Garzón
 */
public class ExcelReader {

	private static final String ID = "id";
	private static final String AUTHOR = "author";
	private static final String LOCATION = "location";
	private static final String TITLE = "title";
	private static final String CONTENT = "content";
	private static final String RATING = "rating";
	private static final String CREATION_DATE = "creationDate";
	private static final String LANGUAGE = "language";
	private static final String TTAG = "ttag";
	private static final String FACILITY_NAME = "facilityName";
	private static final String FACILITY_ADDRESS = "facilityAddress";
	private static final String FACILITY_PHONE = "facilityPhone";
	private static final String FACILITY_WEB = "facilityWeb";

	protected static final Logger log = LogManager.getLogger(ExcelReader.class);

	public List<TAReview> read(File file) {
		List<TAReview> reviews = new LinkedList<TAReview>();
		List<String> headers = new LinkedList<String>();
		TAReview review = null;
		TAFacility facility = null;


		try {
			// Creating a Workbook from an Excel file (.xls or .xlsx)
			Workbook workbook = WorkbookFactory.create(file);

			// Getting the Sheet at index zero
			Sheet sheet = workbook.getSheetAt(0);

			// Use a for-each loop to iterate over the rows and columns
			log.debug("Iterating over Rows and Columns using for-each loop");
			boolean firstRow = true;
			int column = 0;

			for (Row row : sheet) {
				if (firstRow) {
					log.debug("Skipping first row (header)");
					firstRow = false;

					for (Cell cell : row) {
						headers.add(cell.getStringCellValue());
					}
					continue;
				}

				column = 0;
				review = new TAReview();
				facility = new TAFacility();

				for (Cell cell : row) {
					switch (headers.get(column)) {
					case ID:
						review.setId(cell.getStringCellValue());
						break;
					case AUTHOR:
						review.setAuthor(cell.getStringCellValue());
						break;
					case LOCATION:
						review.setLocation(cell.getStringCellValue());
						break;
					case TITLE:
						review.setTitle(cell.getStringCellValue());
						break;
					case CONTENT:
						review.setContent(cell.getStringCellValue());
						break;
					case RATING:
						review.setRating(cell.getStringCellValue());
						break;
					case CREATION_DATE:						
						review.setCreationDate(cell.getDateCellValue());
						break;
					case LANGUAGE:
						review.setLanguage(cell.getStringCellValue());
						break;
					case TTAG:
						review.setTtag(cell.getStringCellValue());
						break;
					case FACILITY_NAME:
						facility.setName(cell.getStringCellValue());
						break;
					case FACILITY_ADDRESS:
						facility.setAddress(cell.getStringCellValue());
						break;
					case FACILITY_PHONE:
						facility.setPhone(cell.getStringCellValue());
						break;
					case FACILITY_WEB:
						facility.setWeb(cell.getStringCellValue());						
						break;
					}
					column++;
				}
				
				review.setFacility(facility);
				reviews.add(review);				
			}
			// Closing the workbook
			workbook.close();
		} catch (EncryptedDocumentException | IOException e) {
			log.error("Error reading excel file. ", e);
		}

		return reviews;
	}

}
