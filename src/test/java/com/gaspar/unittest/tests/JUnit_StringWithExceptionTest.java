package com.gaspar.unittest.tests;

import org.junit.Test;

import com.gaspar.unittest.TestException;
import com.gaspar.unittest.TestRunner;
import com.gaspar.unittest.samples.StringWithExceptionTest;

/**
 * JUnit-tal teszteli a {@link StringWithExceptionTest} osztaly teszteredmenyet.
 * @author Gaspar Tamas
 */
public class JUnit_StringWithExceptionTest {

	//azt varjuk, hogy a teszteles nem fog vegezni (TestException), mert az @BeforeClass NPE-t dob
	@Test(expected = TestException.class)
	public void attemptTest() {
		TestRunner.testClass(StringWithExceptionTest.class);
	}
	
}
