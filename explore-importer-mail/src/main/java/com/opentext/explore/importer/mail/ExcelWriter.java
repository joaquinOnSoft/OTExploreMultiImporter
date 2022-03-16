package com.opentext.explore.importer.mail;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

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

				row.createCell(4).setCellValue( getTextFromMessage(message) ); //Body

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

	/**
	 * Read text inside body of mail using javax.mail
	 * @param message
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 * @see https://stackoverflow.com/questions/11240368/how-to-read-text-inside-body-of-mail-using-javax-mail/55328918
	 */
	private String getTextFromMessage(MimeMessage message) throws MessagingException, IOException {
	    String result = "";
	    if (message.isMimeType("text/plain")) {
	        result = message.getContent().toString();
	    } else if (message.isMimeType("multipart/*")) {
	        MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
	        result = getTextFromMimeMultipart(mimeMultipart);
	    }
	    return result;
	}

	private String getTextFromMimeMultipart(
	        MimeMultipart mimeMultipart)  throws MessagingException, IOException{
	    String result = "";
	    int count = mimeMultipart.getCount();
	    for (int i = 0; i < count; i++) {
	        BodyPart bodyPart = mimeMultipart.getBodyPart(i);
	        if (bodyPart.isMimeType("text/plain")) {
	            result = result + "\n" + bodyPart.getContent();
	            break; // without break same text appears twice in my tests
	        } else if (bodyPart.isMimeType("text/html")) {
	            String html = (String) bodyPart.getContent();
	            result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
	        } else if (bodyPart.getContent() instanceof MimeMultipart){
	            result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
	        }
	    }
	    return result;
	}
}
