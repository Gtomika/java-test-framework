package com.gaspar.unittest.samples;

import com.gaspar.unittest.annotations.After;
import com.gaspar.unittest.annotations.Before;
import com.gaspar.unittest.annotations.BeforeClass;
import com.gaspar.unittest.annotations.Test;
import com.gaspar.unittest.annotations.TestCase;

/**
 * Ugyanaz, mint a {@link StringTest}, de itt van idokorlat, amit 
 * szandekosan tullepunk, ami miatt a teszteredmeny FAIL lesz.
 */
@TestCase(timeLimit = 10) //10 ezred mp.
public class StringWithTimeLimitTest {

	private static String testString1 = null, testString2 = null;
	private static int testCounter = 0;
	
	public StringWithTimeLimitTest() {}
	
	@BeforeClass
	public static void initTestStrings() {
		testCounter = 0;
		testString1 = "Hello world!";
		testString2 = "My test framework";
	}
	
	//before 2-vel noveli, after 1-el csokkenti a szamlalot. Igy az elvart eredmeny 
	//a szamlalora annyi ahany tesztet vegzunk es latszik hogy mindketto annotacio mukodik
	@Before
	public void increaseCounter() {
		testCounter += 2;
	}
	
	@After
	public void decreaseCounter() {
		testCounter--;
	}
	
	@Test
	public boolean testCharCounter() {
		try {
			Thread.sleep(12); //12 ezred mp. varakozas
		} catch (Exception e) {}
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
