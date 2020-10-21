package com.gaspar.unittest.samples;

import com.gaspar.unittest.TestRunner;
import com.gaspar.unittest.annotations.After;
import com.gaspar.unittest.annotations.AfterClass;
import com.gaspar.unittest.annotations.AssertFalse;
import com.gaspar.unittest.annotations.AssertThrows;
import com.gaspar.unittest.annotations.Before;
import com.gaspar.unittest.annotations.BeforeClass;
import com.gaspar.unittest.annotations.Skip;
import com.gaspar.unittest.annotations.Test;
import com.gaspar.unittest.annotations.TestCase;
import com.gaspar.unittest.results.TestResult;

/** Nagyjabol minden funkciot bemutato peldakod, ez nincs tesztelve, hanem a README-ben lathato. */
@TestCase(timeLimit = 15, errorTolerance = 0.4) //15 ezred mp limit, 40% hibas lehet.
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
	@AssertFalse //ez sikertelen lesz, a 0.4-es hiba arany miatt az osztalyteszt sikeres lesz
	public boolean testAdd2() {
		System.out.println("Teszt add 2");
		return add(10,0) == 10;
	}
	
	@Test
	@AssertThrows(exception = NullPointerException.class)
	public boolean testAdd3() {
		System.out.println("Teszt add 3");
		throw new NullPointerException();
	}
	
	@Test
	@Skip //ez nem fog futni
	public boolean testAdd4() {
		System.out.println("Teszt add 4");
		return add(3,3) == 6;
	}
	
	public static void main(String[] args) {
		TestResult res = TestRunner.testClass(AllFunctionsSample.class);
		System.out.println(res);
	}
}