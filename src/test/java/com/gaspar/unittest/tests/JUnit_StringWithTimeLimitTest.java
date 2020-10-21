package com.gaspar.unittest.tests;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import com.gaspar.unittest.TestRunner;
import com.gaspar.unittest.results.ResultStatus;
import com.gaspar.unittest.results.TestResult;
import com.gaspar.unittest.samples.StringWithTimeLimitTest;

public class JUnit_StringWithTimeLimitTest {

	private static TestResult result;
	
	@BeforeClass
	public static void performTest() {
		try {
			result = TestRunner.testClass(StringWithTimeLimitTest.class);
			System.out.println(result);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//azt varjuk, hogy a teszteles FAIL eredmenyt ad, mert kifutunk az idobol
	@Test
	public void testStatus() {
		assertTrue(result.getStatus() == ResultStatus.FAIL);
	}
	
}
