package com.gaspar.unittest.samples;

import com.gaspar.unittest.annotations.Before;
import com.gaspar.unittest.annotations.BeforeClass;
import com.gaspar.unittest.annotations.Test;
import com.gaspar.unittest.annotations.TestCase;

/**
 * Hasonlo a {@link StringTest} osztalyhoz, de itt a teszteles NPE-miatt megszakad.
 * @author Gaspar Tamas
 */
@TestCase
public class StringWithExceptionTest {

	private static String testString1 = null, testString2 = null;
	private static int testCounter = 0;
	
	@BeforeClass
	public static void initTestStrings() {
		//testString2 null!!!!
		testString1 = testString2.concat("valami");
	}
	
	@Before
	public void countTest() {
		testCounter++;
	}
	
	@Test
	public boolean testCharCounter() {
		char c1 = 'l', c2 = 'o';
		int count1 = countChar(testString1, c1);
		int count2 = countChar(testString1, c2);
		return count1 == 3 && count2 == 2;
	}
	
	@Test
	public boolean testCharRemover() {
		char c1 = ' ';
		String removed = removeChar(testString2, c1);
		return removed.equals("Mytestframework");
	}
	
	//karakter szamolo
	public int countChar(String text, char c) {
		int count = 0;
		for(char letter: text.toCharArray()) {
			if(letter == c) count++;
		}
		return count;
	}
	
	//karakter eltavolito
	public String removeChar(String text, char c) {
		StringBuilder sb = new StringBuilder();
		for(char letter: text.toCharArray()) {
			if(letter != c) sb.append(letter);
		}
		return sb.toString();
	}

	public static int getTestCounter() {
		return testCounter;
	}
}
