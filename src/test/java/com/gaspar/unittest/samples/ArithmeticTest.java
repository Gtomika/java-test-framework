package com.gaspar.unittest.samples;

import com.gaspar.unittest.annotations.*;

/**
 *  Egyszeru muveleti fuggvenyeket tesztel a SAJAT teszt framework-kel (NEM JUnit).
 *  A JUnit_ArithmeticTest osztalyban (test package) van tesztelve, hogy a framework jol mukodik-e.
 */
@TestCase
public class ArithmeticTest {

	@Test
	public boolean testAdd() {
		return add(2,2) == 4;
	}
	
	@Test
	@AssertFalse
	public boolean testAddFalse() {
		return add(6,6) == 10;
	}
	
	@Test
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
