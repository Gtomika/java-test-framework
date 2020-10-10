package com.gaspar.unittest.samples;

import com.gaspar.unittest.annotations.AssertFalse;
import com.gaspar.unittest.annotations.AssertThrows;
import com.gaspar.unittest.annotations.AssertTrue;
import com.gaspar.unittest.annotations.Test;
import com.gaspar.unittest.annotations.Before;
import com.gaspar.unittest.annotations.TestCase;

/** Ugyanaz mint az ArithmeticTest, de minden teszt metodus el van rontva valamivel, ami warningokat eredmenyez. */
@TestCase
public class ArithmeticWithWarningsTest {

	//before és paraméter miatt 2 warning
	@SuppressWarnings("unused")
	@Test
	@Before
	public boolean testAdd(int dummyParam) {
		return add(2,2) == 4;
	}
	
	//dupla assert és helytelen visszatérési érték miatt 2 warning
	@SuppressWarnings("unused")
	@Test
	@AssertFalse
	@AssertTrue
	public Object testAddFalse() {
		boolean b = add(6,6) == 10;
		return new Object();
	}
	
	//dupla assert, 1 warning
	@Test
	@AssertFalse
	@AssertThrows(exception = ArithmeticException.class)
	public boolean divideByZero() {
		divide(5,0);
		return false;
	}
	
	public int add(int n1, int n2) {
		return n1 + n2;
	}
	
	public int divide(int n1, int n2) {
		return n1 / n2;
	}
}
