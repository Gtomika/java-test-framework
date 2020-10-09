package com.gaspar.unittest.tests;

import org.junit.Test;

import com.gaspar.unittest.TestException;
import com.gaspar.unittest.TestRunner;
import com.gaspar.unittest.samples.ArithmeticNoDefaultConstructorTest;

public class JUnit_ArithmeticNoDefaultConstructorTest {

	@Test(expected = TestException.class)
	public void testInvalidClass() {
		TestRunner.testClass(ArithmeticNoDefaultConstructorTest.class);
	}
	
}
