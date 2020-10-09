package com.gaspar.unittest.results;

import java.util.ArrayList;
import java.util.List;

/** 
 * Egy osztaly tesztelesenek az eredmenyet tartalmazza.
 * Szep megjelenites biztositva van a {@link #toString()} metodussal.
 */
public class TestResult {

	/** A tesztelt osztaly neve */
	private final String className;
	/** Az osztaly tesztelese soran talalt hibak (szöveges formaban). */
	private final List<String> errors;
	/** A teszt metodusok futtatasanak eredmenyei. */
	private final List<MethodTestResult> methodResults;
	/** A sikeres, sikertelen és hibasan futo tesztek szama. */
	private int successfulTestCount, failedTestCount, interruptedTestCount;
	
	/** Privat konstruktor, helyette builder-t kell hasznalni. */
	private TestResult(String className, List<String> errors, List<MethodTestResult> methodResults) {
		this.className = className;
		this.errors = errors;
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
		sb.append("Result of testing for class " + className + ":\n");
		sb.append("There were a total of " + methodResults.size() + " tests run,\n");
		sb.append("out of which " + successfulTestCount + " succeded, " + failedTestCount + " failed and " + interruptedTestCount + " were unexpectedly interrupted.\n");
		sb.append("Detailed reports for each test method:\n");
		for(MethodTestResult mts: methodResults) {
			sb.append("- " + mts.toString() + "\n");
		}
		return sb.toString();
	}
	
	public MethodTestResult getMethodResultByName(String methodName) throws NoSuchMethodException {
		for(MethodTestResult result: methodResults) {
			if(result.getMethodName().equals(methodName)) return result;
		}
		throw new NoSuchMethodException(methodName);
	}
	
	public String getClassName() {
		return className;
	}

	public List<String> getErrors() {
		return errors;
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

	/** TestResult objektumok keszitesehez. */
	public static class Builder {
		
		private final List<String> errors = new ArrayList<>();
		private String className;
		private final List<MethodTestResult> methodResults = new ArrayList<>();
		
		public void withClassName(String className) {
			this.className = className;
		}
		
		public void addResult(MethodTestResult methodResult) {
			methodResults.add(methodResult);
		}
		
		public void addError(String error) {
			errors.add(error);
		}
		
		/** Aktualis allapotbol elkesziti az objektumot. */
		public TestResult build() {
			return new TestResult(className, errors, methodResults);
		}
	}
}
