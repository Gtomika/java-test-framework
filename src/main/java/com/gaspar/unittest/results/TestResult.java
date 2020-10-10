package com.gaspar.unittest.results;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/** 
 * Egy osztaly tesztelesenek az eredmenyet tartalmazza.
 * Szep megjelenites biztositva van a {@link #toString()} metodussal.
 */
public class TestResult {

	/** A tesztelt osztaly neve */
	private final String className;
	/** Az osztaly tesztelese soran talalt problemak (szöveges formaban). */
	private final List<String> warnings;
	/** A teszt metodusok futtatasanak eredmenyei. */
	private final List<MethodTestResult> methodResults;
	/** A sikeres, sikertelen és hibasan futo tesztek szama. */
	private int successfulTestCount, failedTestCount, interruptedTestCount;
	
	/** Privat konstruktor, helyette builder-t kell hasznalni. */
	private TestResult(String className, List<String> warnings, List<MethodTestResult> methodResults) {
		this.className = className;
		this.warnings = warnings;
		this.methodResults = methodResults;
		for(MethodTestResult methodResult: this.methodResults) { //eredmeny tipusok szamlalasa
			switch(methodResult.getStatus()) {
			case SUCCESS:
				successfulTestCount++;
				break;
			case FAIL:
				failedTestCount++;
				break;
			case INTERRUPTED:
				interruptedTestCount++;
				break;
			}
		}
	}
	
	/** Szoveges formaban visszaadja az osztaly tesztelesenek eredmenyet. */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("--------------------------------------------------------------------------------------\n");
		sb.append("Result of testing for class " + className + ":\n");
		if(methodResults.size() > 0) {
			sb.append("There were a total of " + methodResults.size() + " tests run,\n");
			sb.append("out of which " + successfulTestCount + " succeded, " + failedTestCount + " failed and " + interruptedTestCount + " were unexpectedly interrupted.\n");
		} else {
			sb.append("WARNING: this class was marked with @TestCase, but no valid test methods were found!");
		}
		if(warnings.size() > 0) {
			sb.append("\nWARNING: " + warnings.size() + " problems were found during testing, use getWarnings or printWarnings to see them.\n");
		}
		if(methodResults.size() > 0) {
			sb.append("\nDetailed reports for each test method:\n");
			for(MethodTestResult mts: methodResults) {
				sb.append("- " + mts.toString() + "\n");
			}
		}
		return sb.toString();
	}
	
	/** 
	 * Metodusnev alapjan elkeri az adott metodus teszt eredmenyet.
	 * @throws NoSuchMethodException he nincs ilyen nevu metodus.
	 */
	public MethodTestResult getMethodResultByName(String methodName) throws NoSuchMethodException {
		for(MethodTestResult result: methodResults) {
			if(result.getMethodName().equals(methodName)) return result;
		}
		throw new NoSuchMethodException(methodName);
	}
	
	/** Kiirja a megadott stream-re a warningokat. */
	public void printWarnings(final PrintStream printStream) {
		printStream.println(warnings.size() + " warnings were found while testing class " + className + ".");
		for(String warning: warnings) {
			printStream.println(warning);
		}
	}
	
	/** Kiirja konzol-ra a warningokat. */
	public void printWarnings() {
		printWarnings(System.out);
	}
	
	public String getClassName() {
		return className;
	}

	public List<String> getWarnings() {
		return warnings;
	}

	public List<MethodTestResult> getMethodResults() {
		return methodResults;
	}

	public int getSuccessfulTestCount() {
		return successfulTestCount;
	}

	public int getFailedTestCount() {
		return failedTestCount;
	}

	public int getInterruptedTestCount() {
		return interruptedTestCount;
	}
	
	public int getTestCount() {
		return methodResults.size();
	}

	/** TestResult objektumok keszitesehez. */
	public static class Builder {
		
		private final List<String> warnings = new ArrayList<>();
		private String className;
		private final List<MethodTestResult> methodResults = new ArrayList<>();
		
		public void withClassName(String className) {
			this.className = className;
		}
		
		public void addResult(MethodTestResult methodResult) {
			methodResults.add(methodResult);
		}
		
		public void addWarning(String warning) {
			warnings.add(warning);
		}
		
		/** Aktualis allapotbol elkesziti az objektumot. */
		public TestResult build() {
			return new TestResult(className, warnings, methodResults);
		}
	}
}
