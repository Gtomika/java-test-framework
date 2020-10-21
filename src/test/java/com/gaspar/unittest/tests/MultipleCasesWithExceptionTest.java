package com.gaspar.unittest.tests;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.gaspar.unittest.TestRunner;
import com.gaspar.unittest.results.ResultStatus;
import com.gaspar.unittest.results.TestResult;
import com.gaspar.unittest.samples.ArithmeticTest;
import com.gaspar.unittest.samples.StringTest;
import com.gaspar.unittest.samples.StringWithExceptionTest;

public class MultipleCasesWithExceptionTest {
	
	private static List<TestResult> results;
	
	@BeforeClass
	public static void doTests() {
		results = TestRunner.testClasses(ArithmeticTest.class, StringTest.class, StringWithExceptionTest.class);
		results.forEach(System.out::println);
	}
	
	@Test
	public void testResults() {
		assertTrue(results.get(0).getStatus() == ResultStatus.SUCCESS &&
				   results.get(1).getStatus() == ResultStatus.SUCCESS &&
				   results.get(2).getStatus() == ResultStatus.INTERRUPTED);
	}
}
