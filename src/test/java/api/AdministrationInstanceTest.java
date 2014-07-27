package api;

import junit.framework.Assert;

import org.joda.time.LocalDate;
import org.junit.Test;

public class AdministrationInstanceTest {
	private static final String ID = "ID";

	@Test
	public void testParseDateTime() {
		String testString = "2013-01-31";
		LocalDate testJoda = new LocalDate(2013, 1, 31);
		Assert.assertEquals(new AdministrationInstance(ID, testString),
				new AdministrationInstance(ID, testJoda));
	}
	

	@Test(expected=IllegalArgumentException.class)
	public void testParseDateTimeFail() {
		String testString = "20130131";
		LocalDate testJoda = new LocalDate(2013, 1, 31);
		Assert.assertEquals(new AdministrationInstance(ID, testString),
				new AdministrationInstance(ID, testJoda));
	}
}
