package com.gaspar.unittest.tests;

import org.junit.Test;

import com.gaspar.unittest.TestException;
import com.gaspar.unittest.TestRunner;
import com.gaspar.unittest.samples.ArithmeticTest;
import com.gaspar.unittest.samples.StringTest;
import com.gaspar.unittest.samples.StringWithExceptionTest;

/**
 * JUnit-tal tesztel egy olyan esetet, ahol tobb osztaly teszteleset vegezzuk sajat 
 * framework-kel, tobb szalon ES az egyik osztaly kivetelt dob.
 * @author Gaspar Tamas
 */
public class MultipleCasesWithExceptionTest {
	
	@Test(expected = TestException.class)
	public void attemptMultipleTests() {
		TestRunner.testClasses(ArithmeticTest.class, StringTest.class, StringWithExceptionTest.class);
	}
}
