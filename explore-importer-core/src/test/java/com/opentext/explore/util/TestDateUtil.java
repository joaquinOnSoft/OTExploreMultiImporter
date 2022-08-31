package com.opentext.explore.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.junit.Test;

public class TestDateUtil{
	
	private int subStringToInt(String str, int init, int end) {
		return Integer.parseInt(str.substring(init, end));
	}
	
	@Test
	public void testNowToUTC() {
		Calendar now = GregorianCalendar.getInstance();
		
		String dateStr = DateUtil.nowToUTC();
		
		assertNotNull(dateStr);
		assertEquals(now.get(Calendar.YEAR), subStringToInt(dateStr, 0, 4));
		assertEquals(now.get(Calendar.MONTH) + 1, subStringToInt(dateStr, 5, 7));
		assertEquals(now.get(Calendar.DAY_OF_MONTH), subStringToInt(dateStr, 8, 10));		
	}

	@Test
	public void testUtcToDate() {
		final String utcRef = "2020-05-21T16:30:52Z";
		
		try {
			Date utc = DateUtil.utcToDate(utcRef);
			assertEquals(utcRef, DateUtil.dateToUTC(utc));
		} catch (ParseException e) {
			fail(e.getMessage());
		}
	}	
	
	@Test
	public void testDateToUTC() {
		Calendar now = GregorianCalendar.getInstance();
		String nowStr = DateUtil.dateToUTC(now.getTime());
		
		assertNotNull(nowStr);
		assertEquals(now.get(Calendar.YEAR), subStringToInt(nowStr, 0, 4));
		assertEquals(now.get(Calendar.MONTH) + 1, subStringToInt(nowStr, 5, 7));
		assertEquals(now.get(Calendar.DAY_OF_MONTH), subStringToInt(nowStr, 8, 10));		
	}
	
	@Test
	public void testGetDateOneWeekAgo() {
		Calendar now = GregorianCalendar.getInstance();
		now.roll(Calendar.DAY_OF_YEAR, -7);
		
		String oneWeekAgo = DateUtil.getDateOneWeekAgo();
		
		assertNotNull(oneWeekAgo);
		assertEquals(now.get(Calendar.YEAR), subStringToInt(oneWeekAgo, 0, 4));
		assertEquals(now.get(Calendar.MONTH) + 1, subStringToInt(oneWeekAgo, 5, 7));
		assertEquals(now.get(Calendar.DAY_OF_MONTH), subStringToInt(oneWeekAgo, 8, 10));	
	}
	
	@Test
	public void testStrToDate() {
		final String strDate = "01/12/2020";
		final String format = "dd/MM/yyyy";
		
		try {
			Date date = DateUtil.strToDate(strDate, format);
			assertEquals("Tue Dec 01 00:00:00 GMT 2020", date.toString().replace("CET", "GMT").replace("CST", "GMT"));
		} catch (ParseException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testStrWithLocaleToDate() {
		final String strDate = "June 22, 2022";
		final String format = "MMM d, yyyy";
		
		try {
			Date date = DateUtil.strToDate(strDate, Locale.ENGLISH, format);
			assertEquals("Wed Jun 22 00:00:00 CEST 2022", date.toString());
		} catch (ParseException e) {
			fail(e.getMessage());
		}
	}		
	
	@Test
	public void testStrEngToDate() {
		final String strDate = "June 22, 2022";
		final String format = "MMM d, yyyy";
		
		try {
			Date date = DateUtil.strEngToDate(strDate, format);
			assertEquals("Wed Jun 22 00:00:00 CEST 2022", date.toString());
		} catch (ParseException e) {
			fail(e.getMessage());
		}
	}		
}
