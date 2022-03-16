package com.opentext.explore.importer.mail.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.Test;

import com.opentext.explore.util.FileUtil;

public class TestFile2MimeMessage {
	File2MimeMessage converter = new File2MimeMessage();
	
	@Test
	public void convert() {
		File fileMail = FileUtil.getFileFromResources("mail-sample-01.eml");
		MimeMessage message = converter.convert(fileMail);
		assertNotNull(message);
		
		try {
			assertNotNull(message.getFrom());
			assertEquals("joaquin.opentext@gmail.com", ((InternetAddress) message.getFrom()[0]).getAddress());
		} catch (MessagingException e) {
			fail(e.getMessage());
		}		
	}
}
