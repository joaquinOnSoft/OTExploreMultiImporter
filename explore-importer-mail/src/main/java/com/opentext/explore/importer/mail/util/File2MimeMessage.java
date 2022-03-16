package com.opentext.explore.importer.mail.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class File2MimeMessage {
	private Properties props;
	
	private static final Logger log = LogManager.getLogger(File2MimeMessage.class);
		
	public File2MimeMessage(Properties props) {
		this.props = props;
	}
	
	public File2MimeMessage() {
        props = System.getProperties();
        props.put("mail.host", "smtp.dummydomain.com");
        props.put("mail.transport.protocol", "smtp");
	}
	
	/**
	 * 
	 * @param emlFile
	 * @return
	 * @see https://www.daniweb.com/programming/software-development/threads/209663/how-to-read-an-eml-file
	 * @see https://www.rgagnon.com/javadetails/java-0458.html
	 */
	public MimeMessage convert(File emlFile) {
		MimeMessage message = null;
		
        Session mailSession = Session.getDefaultInstance(props, null);        
        try {
        	InputStream source = new FileInputStream(emlFile);
			message = new MimeMessage(mailSession, source);
		} catch (MessagingException | FileNotFoundException e) {
			log.error("Error getting MimeMessage from file: " + e.getMessage());
		}	
        
        return message;
	}
}
