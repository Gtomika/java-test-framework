package com.gaspar.unittest.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import com.gaspar.unittest.TestRunner;
import com.gaspar.unittest.results.MethodTestResult;
import com.gaspar.unittest.results.ResultStatus;
import com.gaspar.unittest.results.TestResult;
import com.gaspar.unittest.samples.ArithmeticTest;

public class JUnit_ArithmeticTest {

	/* A SAJAT teszt framework teszt eredmenye az eredeti osztalyon. */
	private static TestResult testResult;
	
	/* Elvegzi a tesztet a SAJAT framework-kel az eredeti osztalyon. */
	@BeforeClass
	public static void performTest() {
		testResult = TestRunner.testClass(ArithmeticTest.class);
		//ezeket futtatva lehet latni a formazott eredmenyt
		//System.out.println(testResult.toString());
	}
	
	@Test
	public void testSuccessCount() {
		assertEquals(testResult.getSuccessfulTestCount(), 3);
	}
	
	@Test
	public void testFailCount() {
		assertEquals(testResult.getFailedTestCount(), 0);
	}
	
	@Test
	public void testInterruptedCount() {
		assertEquals(testResult.getInterruptedTestCount(), 0);
	}
	
	@Test
	public void testAddMethodResult1() {
		try {
			final MethodTestResult methodResult = testResult.getMethodResultByName("testAdd");
			assertTrue(methodResult.getStatus() == ResultStatus.SUCCESS);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAddMethodResult2() {
		try {
			final MethodTestResult methodResult = testResult.getMethodResultByName("testAddFalse");
			assertTrue(methodResult.getStatus() == ResultStatus.SUCCESS);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDivideMethodResult() {
		try {
			final MethodTestResult methodResult = testResult.getMethodResultByName("divideByZero");
			assertTrue(methodResult.getStatus() == ResultStatus.SUCCESS);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
}
