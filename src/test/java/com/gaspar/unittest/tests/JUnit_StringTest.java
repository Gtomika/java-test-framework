package com.gaspar.unittest.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import com.gaspar.unittest.TestRunner;
import com.gaspar.unittest.results.ResultStatus;
import com.gaspar.unittest.results.TestResult;
import com.gaspar.unittest.samples.StringTest;

public class JUnit_StringTest {

	/* A SAJAT teszt framework teszt eredmenye az eredeti osztalyon. */
	private static TestResult testResult;
	
	/* Elvegzi a tesztet a SAJAT framework-kel az eredeti osztalyon. */
	@BeforeClass
	public static void performTest() {
		testResult = TestRunner.testClass(StringTest.class);
		//ezeket futtatva lehet latni a formazott eredmenyt
		//System.out.println(testResult.toString());
	}
	
	@Test
	public void testCounter() {
		assertEquals(2, StringTest.getTestCounter());
	}
	
	@Test
	public void testResult1() {
		assertTrue(testResult.getStatus() == ResultStatus.SUCCESS);
	}
	
	@Test
	public void testResult2() {
		assertEquals(2, testResult.getTestCount());
	}
	
	@Test
	public void testWarnings() {
		assertEquals(testResult.getWarnings().size(), 0);
	}
}
