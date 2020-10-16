package com.gaspar.unittest.tests;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import com.gaspar.unittest.TestRunner;
import com.gaspar.unittest.results.TestResult;
import com.gaspar.unittest.samples.ArithmeticWithWarningsTest;

/** JUnit-tal teszteli az ArithmeticWithWarningsTest osztály tesztelését */
public class JUnit_ArithmeticWithWarningsTest {
	
	/** A SAJAT teszt framework teszt eredmenye az eredeti osztalyon. */
	private static TestResult testResult;
	
	/** Elvegzi a tesztet a SAJAT framework-kel az eredeti osztalyon. */
	@BeforeClass
	public static void performTest() {
		testResult = TestRunner.testClass(ArithmeticWithWarningsTest.class);
		//ezeket futtatva lehet latni a formazott eredmenyt
		//System.out.println(testResult.toString());
		//testResult.printWarnings();
	}
	
	@Test
	public void testWarningsNumber() {
		assertTrue(testResult.getWarnings().size() == 11);
	}
	
	@Test
	public void testValidMethodCount() {
		assertTrue(testResult.getTestCount() == 0);
	}
}
