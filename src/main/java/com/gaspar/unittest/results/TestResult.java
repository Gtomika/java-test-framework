package com.gaspar.unittest.results;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.gaspar.unittest.annotations.TestCase;

/** 
 * Egy osztaly tesztelesenek az eredmenyet tartalmazza.
 * Szep megjelenites biztositva van a {@link #toString()} metodussal.
 * @author Gaspar Tamas
 */
public class TestResult {

	/**
	 *  Megmondja hogy sikeres-e ez az osztaly teszt. Pontosan akkor {@link ResultStatus#SUCCESS}, ha: 
	 *  - A kapott hiba tolerancianak ({@link #errorTolerance}) megfelelo szamu teszmetodus tesztelese sikeres ES
	 *  - A teszteles idoben befejezodik, ha volt megadva idokorlat.
	 *  Ha nem sikeres akkor {@link ResultStatus#FAIL} vagy {@link ResultStatus#INTERRUPTED}.
	 *  {@link ResultStatus#INTERRUPTED} eseten {@link #interruptCause}-ban van a megszakitas oka.
	 */
	private ResultStatus status;
	/** A tesztelt osztaly neve */
	private final String className;
	/**
	 *  Az osztaly tesztelese soran talalt problemak (szöveges formaban).
	 *  Formazottan kiirathato a {@link #printWarnings()} es {@link #printWarnings(PrintStream)} metodusokkal.
	 */
	private final List<String> warnings;
	/** A teszt metodusok futtatasanak eredmenyei. */
	private final List<MethodTestResult> methodResults;
	/** A sikeres tesztek szama. */
	private int successfulTestCount;
	/** A nem sikeres tesztek szama. */
	private int failedTestCount;
	/** A megszakadt tesztek szama. */
	private int interruptedTestCount;
	/** A megadott idokorlat. Ha nem volt megadva, akkor a limit {@link TestCase#NO_TIME_LIMIT}. */
	private final long testTimeLimit;
	/** A teszteles tenyleges ideje. */
	private final long testTime;
	/**
	 * Ha megszakadt a teszt ({@link #status} == {@link ResultStatus#INTERRUPTED}), akkor ez tartalmazza az 
	 * okot. Egyebkent null.
	 */
	private final InterruptCause interruptCause;
	/**
	 * Elfogadott hibaarany. Ha ez X, akkor az osztaly tesztelese meg akkor 
	 * is sikeres lesz, ha a tesztek X szazaleka nem sikeres.
	 */
	private final double errorTolerance;
	/** A sikeres tesztek aranya. */
	private final double successRatio;
	
	/**
	 *  Privat konstruktor, helyette builder-t kell hasznalni!
	 *  @see Builder
	 */
	private TestResult(String className, List<String> warnings, List<MethodTestResult> methodResults,
			long testTimeLimit, long testTime, InterruptCause interruptCause, double errorTolerance) {
		this.className = className;
		this.warnings = warnings;
		this.methodResults = methodResults;
		this.testTimeLimit = testTimeLimit;
		this.testTime = testTime;
		this.interruptCause = interruptCause;
		this.errorTolerance = errorTolerance;
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
		this.successRatio = Double.valueOf(successfulTestCount) / getTestCount();
		//sikeresseg eldontese
		if(this.interruptCause == null) { //nem volt megszakadas
			
			if((1 - this.successRatio) <= this.errorTolerance) { //megfelelo szamu tesztmetodus sikeres
				
				if(this.testTimeLimit != TestCase.NO_TIME_LIMIT) { //volt idokorlat
					status = this.testTime <= this.testTimeLimit ? ResultStatus.SUCCESS : ResultStatus.FAIL;
				} else { //nem volt idokorlat
					status = ResultStatus.SUCCESS;
				}
			} else { //nem eleg metodus lett sikeres
				status = ResultStatus.FAIL;
			}
		} else { //megszakadt
			status = ResultStatus.INTERRUPTED;
		}
	}
	
	/** Szoveges formaban visszaadja az osztaly tesztelesenek eredmenyet. */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("--------------------------------------------------------------------------------------\n");
		sb.append("Result of testing for class " + className + ". Status: " + ResultStatus.asString(status) + "\n");
		if(status != ResultStatus.INTERRUPTED) { //a teszt lefutott
			//ido mutatasa
			sb.append("Testing took " + testTime + " milliseconds, ");
			if(testTimeLimit == TestCase.NO_TIME_LIMIT) {
				sb.append("there was no time limit.\n");
			} else {
				sb.append("time limit was " + testTimeLimit + ".\n");
			}
			//hiba arany mutatasa
			sb.append("Sucess ratio was " + percent(successRatio) + "%, ");
			if((1 - successRatio) > errorTolerance) {
				sb.append("but only " + percent(errorTolerance) + "% error tolerance was allowed!\n");
			} else {
				sb.append("which is allowed by the " + percent(errorTolerance) + "% error tolerance.\n");
			}
			
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
		} else { //a teszt megszakadt
			sb.append(interruptCause.toString() + "\n");
		}
		return sb.toString();
	}
	
	/** Szazalekra konvertalo segedmetodus. */
	private int percent(double d) {
		return (int)(100*d);
	}
	
	/** 
	 * Metodusnev alapjan elkeri az adott metodus teszt eredmenyet.
	 * @param methodName A kert metodus neve.
	 * @return Az eredmeny, ami egy {@link MethodTestResult}} objektum.
	 * @throws NoSuchMethodException he nincs ilyen nevu metodus.
	 */
	public MethodTestResult getMethodResultByName(String methodName) throws NoSuchMethodException {
		for(MethodTestResult result: methodResults) {
			if(result.getMethodName().equals(methodName)) return result;
		}
		throw new NoSuchMethodException(methodName);
	}
	
	/**
	 *  Kiirja a warningokat.
	 *  @param printStream Az a {@link PrintStream} amire irodik az eredmeny.
	 */
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

	public ResultStatus getStatus() {
		return status;
	}
	
	public long getTestTimeLimit() {
		return testTimeLimit;
	}

	public long getTestTime() {
		return testTime;
	}
	
	public double getErrorTolerance() {
		return errorTolerance;
	}

	public double getSuccessRatio() {
		return successRatio;
	}

	/**
	 * Builder {@link TestResult} objektumok keszitesehez. Adattagok nincsenek dokumentalva, mert azok 
	 * ugyanazok mint a {@link TestResult} eseten. 
	 * @see TestResult 
	 */
	public static class Builder {
		
		private final List<String> warnings = new ArrayList<>();
		private String className;
		private final List<MethodTestResult> methodResults = new ArrayList<>();
		private long timeLimit = TestCase.NO_TIME_LIMIT, testTime;
		private InterruptCause interruptCause = null;
		private double errorTolerance = 0;
		
		public void withClassName(String className) {
			this.className = className;
		}
		
		public void withTestTime(long testTime) {
			this.testTime = testTime;
		}
		
		public void withTimeLimit(long timeLimit) {
			this.timeLimit = timeLimit;
		}
		
		public void withInterrupt(InterruptCause interruptCause) {
			this.interruptCause = interruptCause;
		}
		
		public void withErrorTolerance(double errorTolerance) {
			this.errorTolerance  = errorTolerance;
		}
		
		public void addResult(MethodTestResult methodResult) {
			methodResults.add(methodResult);
		}
		
		public void addWarning(String warning) {
			warnings.add(warning);
		}
		
		/**
		 *  Aktualis allapotbol elkesziti a {@link TestResult} objektumot.
		 *  @return A teszt eredmenye.
		 */
		public TestResult build() {
			return new TestResult(className, warnings, methodResults, timeLimit, testTime, interruptCause, errorTolerance);
		}
	}
}
