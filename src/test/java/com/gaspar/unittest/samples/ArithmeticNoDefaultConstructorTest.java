package com.gaspar.unittest.samples;

import com.gaspar.unittest.annotations.*;

/**
 *  Ugyanaz, mint az {@link ArithmeticTest}, de itt a default konstruktor nem letezik/nem elerheto, 
 *  ezert a teszt framework kivetelt dob ra.
 */
@TestCase
public class ArithmeticNoDefaultConstructorTest {

	//nincs default konstruktor
	public ArithmeticNoDefaultConstructorTest(@SuppressWarnings("unused") int i) {}
	
	//ez is kivetelt valt ki
	//private ArithmeticNoDefaultConstructorTest() {}
	
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
