package com.gaspar.unittest.tests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.gaspar.unittest.TestRunner;
import com.gaspar.unittest.results.ResultStatus;
import com.gaspar.unittest.samples.StringWithExceptionTest;

public class JUnit_StringWithExceptionTest {

	//azt varjuk, hogy a teszteles INTERRUPTED lesz, mert az @BeforeClass kivetelt dob
	@Test
	public void attemptTest() {
		assertTrue(TestRunner.testClass(StringWithExceptionTest.class).getStatus() == ResultStatus.INTERRUPTED);
	}
	
}
