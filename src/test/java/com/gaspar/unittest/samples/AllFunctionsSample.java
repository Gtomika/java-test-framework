package com.gaspar.unittest.samples;

import com.gaspar.unittest.annotations.After;
import com.gaspar.unittest.annotations.AfterClass;
import com.gaspar.unittest.annotations.AssertFalse;
import com.gaspar.unittest.annotations.AssertThrows;
import com.gaspar.unittest.annotations.Before;
import com.gaspar.unittest.annotations.BeforeClass;
import com.gaspar.unittest.annotations.Skip;
import com.gaspar.unittest.annotations.Test;
import com.gaspar.unittest.annotations.TestCase;

@TestCase(timeLimit = 15) //15 ezred mp.
public class AllFunctionsSample {

	//tesztelendo metodus
	public int add(int n1, int n2) {
		return n1 + n2;
	}
	
	//teszt elott es utan futtatando metodusok
	@BeforeClass
	public static void printBeginTesting() {
		System.out.println("Kezdodik a teszteles!");
	}
	
	@AfterClass
	public static void printEndTesting() {
		System.out.println("Befejezodott a teszteles!");
	}
	
	@Before
	public void printBeginTestCase() {
		System.out.println("Kezdodik egy metodusteszt!");
	}
	
	@After
	public void printEndTestCase() {
		System.out.println("Befejezodott egy metodusteszt!");
	}
	
	//tenyleges tesztek
	@Test
	public boolean testAdd1() {
		System.out.println("Teszt add 1");
		return add(2,3) == 5;
	}
	
	@Test
	@AssertFalse
	public boolean testAdd2() {
		System.out.println("Teszt add 2");
		return add(10,0) == 11;
	}
	
	@Test
	@AssertThrows(exception = NullPointerException.class)
	public boolean testAdd3() {
		System.out.println("Teszt add 3");
		throw new NullPointerException();
	}
	
	@Test
	@Skip
	public boolean testAdd4() {
		System.out.println("Teszt add 4");
		return add(3,3) == 6;
	}
}