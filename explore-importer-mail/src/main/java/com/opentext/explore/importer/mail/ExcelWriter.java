package com.opentext.explore.importer.mail;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriter {
	private static String[] columns = {"From", "TO", "CC", "Subject", "Body", "Sent Date"};

	private static final Logger log = LogManager.getLogger(ExcelWriter.class);

	/**
	 * 
	 * @param outputFileName
	 * @param messages
	 * @return
	 * @see https://www.callicoder.com/java-write-excel-file-apache-poi/
	 */
	public boolean write(String outputFileName, List<MimeMessage> messages) {
		boolean created = true;

		// Create a Workbook
		Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

		/* CreationHelper helps us create instances of various things like DataFormat, 
           Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way */
		CreationHelper createHelper = workbook.getCreationHelper();

		// Create a Sheet
		Sheet sheet = workbook.createSheet("e-mail");

		// Create a Font for styling header cells
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.BLUE.getIndex());

		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);

		// Create a Row
		Row headerRow = sheet.createRow(0);

		// Create cells
		for(int i = 0; i < columns.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(columns[i]);
			cell.setCellStyle(headerCellStyle);
		}

		// Create Cell Style for formatting Date
		CellStyle dateCellStyle = workbook.createCellStyle();
		dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"));

		// Create Other rows and cells with employees data
        int rowNum = 1;
        StringBuilder to = new StringBuilder();
        StringBuilder cc = new StringBuilder();
        
        try {
			for(MimeMessage message: messages) {
	            Row row = sheet.createRow(rowNum++);
	
	            //"From", "TO", "CC", "Subject", "Body", "Date"
	
	            row.createCell(0).setCellValue(((InternetAddress) message.getFrom()[0]).getAddress()); //FROM
	
	            for(Address address: message.getAllRecipients()) {
	            	switch (address.getType()) {
					case "TO":
						to.append(((InternetAddress) address).getAddress()).append(" ");
						break;
					case "CC":
						cc.append(((InternetAddress) address).getAddress()).append(" ");
						break;
					}
	            }
	            row.createCell(1).setCellValue(to.toString()); //TO
	            
	            row.createCell(2).setCellValue(cc.toString()); //CC
	            
	            row.createCell(3).setCellValue(message.getSubject()); //Subject
	            
	            row.createCell(4).setCellValue(message.getContent().toString()); //Body
	
	            Cell dateOfBirthCell = row.createCell(5);
	            dateOfBirthCell.setCellValue(message.getSentDate()); //Date
	            dateOfBirthCell.setCellStyle(dateCellStyle);
	            
	            to.delete(0, to.length());
	            cc.delete(0, cc.length());
	        }
        }
        catch (MessagingException | IOException e) {
			log.error(e.getMessage());
		}

		// Resize all columns to fit the content size
		for(int i = 0; i < columns.length; i++) {
			sheet.autoSizeColumn(i);
		}

		// Write the output to a file
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(outputFileName);
			
			workbook.write(fileOut);
			fileOut.close();
			
			// Closing the workbook
			workbook.close();			
		} catch (IOException e) {
			log.error(e.getMessage());
			created = false;
		}

		return created;
	}
}
