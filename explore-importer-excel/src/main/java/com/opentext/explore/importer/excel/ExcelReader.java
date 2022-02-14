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
package com.opentext.explore.importer.excel;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.opentext.explore.importer.excel.fieldhandlers.IFieldHandler;
import com.opentext.explore.importer.excel.pojo.Field;
import com.opentext.explore.importer.excel.pojo.FieldHandler;
import com.opentext.explore.importer.excel.pojo.TextData;
import com.opentext.explore.importer.excel.pojo.TextDataImporterMapping;
import com.opentext.explore.util.DateUtil;

/**
 * Read excel files in Java using a very simple yet powerful open source library
 * called Apache POI.
 * 
 * @see https://www.callicoder.com/java-read-excel-file-apache-poi/
 * @author Joaquín Garzón
 */
public class ExcelReader implements ITextDataReader, ISolrFields {

	protected static final Logger log = LogManager.getLogger(ExcelReader.class);

	@Override
	public List<TextData> read(String filePath, TextDataImporterMapping config, String contentType) {
		if (filePath != null) {
			return read(new File(filePath), config, contentType);
		} else {
			log.warn("File path was null");
			return null;
		}
	}

	@Override
	public List<TextData> read(File file, TextDataImporterMapping config, String contentType) {
		List<TextData> textDataList = new LinkedList<TextData>();
		TextData txtData = null;

		List<Field> fields = config.getFields();
		Field field = null;

		String cellValue = null;

		try {
			// Creating a Workbook from an Excel file (.xls or .xlsx)
			Workbook workbook = WorkbookFactory.create(file);

			// Create a DataFormatter to format and get each cell's value as String
			DataFormatter dataFormatter = new DataFormatter();

			// Getting the Sheet at index zero
			Sheet sheet = workbook.getSheetAt(0);

			// Use a for-each loop to iterate over the rows and columns
			log.debug("Iterating over Rows and Columns using for-each loop");
			boolean firstRow = true;
			boolean allCellValuesInRowAreEmpty = true;
			int column = 0;

			for (Row row : sheet) {
				if (firstRow) {
					log.debug("Skipping first row (header)");
					firstRow = false;
					continue;
				}

				column = 0;
				txtData = new TextData();
				txtData.setType(contentType);
				allCellValuesInRowAreEmpty = true;

				for (Cell cell : row) {
					field = fields.get(column);

					if (field.getSkip()) {
						log.info("Excel field " + field.getExcelName() + " skipped");
					} else {
						cellValue = dataFormatter.formatCellValue(cell);

						if (cellValue != null && cellValue.compareTo("") != 0) {
							allCellValuesInRowAreEmpty = false;
						}

						switch (field.getSolrName()) {
						case SOLR_FIELD_REFERENCE_ID:
							txtData.setReferenceId(cellValue);
							break;
						case SOLR_FIELD_INTERACTION_ID:
							txtData.setInteractionId(cellValue);
							break;
						case SOLR_FIELD_TITLE:
							txtData.setTitle(cellValue);
							break;
						case SOLR_FIELD_AUTHOR_NAME:
							txtData.setAuthorName(cellValue);
							break;
						case SOLR_FIELD_GROUP_HIERARCHY:
							txtData.setGroupHierarchy(cellValue);
							break;							
						case SOLR_FIELD_ID:
							txtData.setId(cellValue);
							break;
						case SOLR_FIELD_TYPE:
							txtData.setType(cellValue);
							break;
						case SOLR_FIELD_PUBLISHED_DATE:
							if (cellValue != null && cellValue.compareTo("") != 0) {
								txtData.setPublishedDate(DateUtil.strToDate(cellValue, field.getFormat()));
							}
							break;
						case SOLR_FIELD_DATE_TIME:
							if (cellValue != null && cellValue.compareTo("") != 0) {
								txtData.setDateTime(DateUtil.strToDate(cellValue, field.getFormat()));
							}
							break;
						case SOLR_FIELD_CONTENT:
							txtData.setContent(cellValue);
							break;
						default:
							txtData.addField(field.getSolrName(), cellValue);
						}
					}

					column++;
				}

				if (allCellValuesInRowAreEmpty == false) {
					// Apply Field Handlers to Text Data (Excel or CSV row)
					List<FieldHandler> fieldHandlers = config.getFieldHandlers();
					if (fieldHandlers != null) {
						for (FieldHandler fieldHandler : fieldHandlers) {
							IFieldHandler handler = instanciateFieldHandler(fieldHandler.getJavaClass());
							txtData = handler.handle(txtData, fieldHandler.getInputSolrNames(),
									fieldHandler.getOutputSolrNames());
						}
					}

					textDataList.add(txtData);
				}
			}

			// Closing the workbook
			workbook.close();
		} catch (EncryptedDocumentException | IOException e) {
			log.error("Error reading excel file. ", e);
		} catch (ParseException e) {
			log.error("Error formating date. ", e);
		}

		return textDataList;
	}

	protected IFieldHandler instanciateFieldHandler(String handlerName) {
		IFieldHandler iFieldHandler = null;

		if (handlerName != null) {
			try {
				Class<?> tClass = Class.forName(handlerName);
				iFieldHandler = (IFieldHandler) tClass.getDeclaredConstructor().newInstance();
			} catch (ClassNotFoundException e) {
				log.error("Transformer class not found: " + e.getLocalizedMessage());
			} catch (NoSuchMethodException e) {
				log.error("Not 'transform' method in Transformer class: " + e.getLocalizedMessage());
			} catch (SecurityException e) {
				log.error("Invalid Transformer (1): " + e.getLocalizedMessage());
			} catch (InstantiationException e) {
				log.error("Invalid Transformer (2): " + e.getLocalizedMessage());
			} catch (IllegalAccessException e) {
				log.error("Invalid Transformer (3): " + e.getLocalizedMessage());
			} catch (IllegalArgumentException e) {
				log.error("Invalid Transformer (4): " + e.getLocalizedMessage());
			} catch (InvocationTargetException e) {
				log.error("Invalid Transformer (5): " + e.getLocalizedMessage());
			}
		}

		return iFieldHandler;
	}
}
