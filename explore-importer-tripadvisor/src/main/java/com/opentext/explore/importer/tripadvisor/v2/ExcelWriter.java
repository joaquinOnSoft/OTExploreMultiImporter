/*
 *   (C) Copyright 2019 OpenText and others.
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
package com.opentext.explore.importer.tripadvisor.v2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.opentext.explore.importer.tripadvisor.pojo.TAReview;

/**
 * SEE: https://www.callicoder.com/java-write-excel-file-apache-poi/
 * 
 * @author Joaquín Garzón
 */
public class ExcelWriter {

	public void write(String tag, List<TAReview> reviews, String outputFileName) throws IOException  {
	
		if(reviews != null && reviews.size() > 0) {
			String[] columns = reviews.get(0).getColumnsNames();
			
			// Create a Workbook
			// Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating
			// `.xls` file
			Workbook workbook = new HSSFWorkbook(); // new XSSFWorkbook() for generating `.xlsx` file
	
			/*
			 * CreationHelper helps us create instances of various things like DataFormat,
			 * Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way
			 */
			// CreationHelper createHelper = workbook.getCreationHelper();
	
			// Create a Sheet
			Sheet sheet = workbook.createSheet("Sheet1");
	
			// Create a Row
			Row headerRow = sheet.createRow(0);
	
			// Create cells
			for (int i = 0; i < columns.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(columns[i]);
				// cell.setCellStyle(headerCellStyle);
			}
	
			// Create cell style to date cells
			CellStyle cellStyle = workbook.createCellStyle();
			CreationHelper createHelper = workbook.getCreationHelper();
			cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy hh:mm:ss"));
	
			// Create Other rows and cells with employees data
			int rowNum = 1;
			int maxNumCol = columns.length;
			int col = 0;
			String columnName = null;
			
			Row row = null;
			Cell cell = null;
			String value = null;
			for (TAReview review : reviews) {
				review.setTtag(tag);
				
				row = sheet.createRow(rowNum++);
	
				for (int nCol = 0; nCol < maxNumCol; nCol++) {
					cell = row.createCell(col);
	
					value = null;
					columnName = columns[nCol];
					switch (columnName) {
					case "id":
						value = review.getId();
					case "author":
						value = review.getAuthor();
						break;
					case "location":
						value = review.getLocation();
						break;
					case "title":
						value = review.getTitle();
						break;
					case "content":
						value = review.getContent();
						break;
					case "rating":
						value = Integer.toString(review.getRating());
						break;
					case "creationDate":
						cell.setCellValue(review.getCreationDate());
						cell.setCellStyle(cellStyle);
						value = null; //Avoid value assignment after the "case"							
						break;
					case "language":
						value = review.getLanguage();
						break;			
					case "ttag":
						value = review.getTtag();
						break;						
					case "facilityName":
						value = review.getFacility().getName();
						break;
					case "facilityAddress":
						value = review.getFacility().getAddress();
						break;
					case "facilityPhone":
						value = review.getFacility().getPhone();
						break;
					case "facilityWeb":
						value = review.getFacility().getWeb();
						break;		
					}
	
					if (value != null) { 
						cell.setCellValue(value);	
					}				
					
					col++;
				}
	
				col = 0;
			}
	
			// Resize all columns to fit the content size
			for (int i = 0; i < columns.length; i++) {
				sheet.autoSizeColumn(i);
			}
	
			// Write the output to a file
			FileOutputStream fileOut = new FileOutputStream(outputFileName);
			workbook.write(fileOut);
			fileOut.close();
	
			// Closing the workbook
			workbook.close();
		}
	}
}
