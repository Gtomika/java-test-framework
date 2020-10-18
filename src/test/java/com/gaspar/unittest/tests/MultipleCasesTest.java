package com.gaspar.unittest.tests;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.gaspar.unittest.TestRunner;
import com.gaspar.unittest.results.ResultStatus;
import com.gaspar.unittest.results.TestResult;
import com.gaspar.unittest.samples.ArithmeticTest;
import com.gaspar.unittest.samples.ArithmeticWithWarningsTest;
import com.gaspar.unittest.samples.StringTest;

/**
 * JUnit-tal tesztel egy olyan esetet, ahol tobb osztaly teszteleset vegezzuk sajat 
 * framework-kel, tobb szalon.
 * @author Gaspar Tamas
 */
public class MultipleCasesTest {

	//tesztek eredmenye sajat framework-el.
	private static List<TestResult> results;
	
	@BeforeClass
	public static void doTests() {
		results = TestRunner.testClasses(ArithmeticTest.class, ArithmeticWithWarningsTest.class, StringTest.class);
		//ezt futtatva lehet latni formazott eredmenyeket
		results.forEach(System.out::println);
	}
	
	@Test
	public void testAllSuccess() {
		assertTrue(results.stream().allMatch(res -> res.getStatus() == ResultStatus.SUCCESS));
	}
	
	@Test
	public void testArithmeticWarnings() {
		assertTrue(results.get(1).getWarnings().size() == 11);
	}
	
	@Test
	public void testArithmeticWarningValidMethodCount() {
		assertTrue(results.get(1).getTestCount() == 0);
	}
}
