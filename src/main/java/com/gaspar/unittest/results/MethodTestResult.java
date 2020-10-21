package com.gaspar.unittest.results;

/** 
 * Egy metodus teszelesenek eredmenye, ami a {@link TestResult} objektumtol kerheto el.
 * Szep megjelenites biztositva van a {@link #toString()} metodussal.
 */
public class MethodTestResult { 
	
	/** A tesztelt metodus neve. */
	private final String methodName;
	/** A teszt elvart eredmenye (szoveges formaban). Ez lehet true, false, vagy az elvart kivetel neve. */
	private final String expectedResult;
	/** A teszt tenyleges eredmenye (szoveges formaban). Ez lehet true, false, vagy a kapott kivetel neve. */
	private final String actualResult;
	/** A teszt sikeressege. */
	private final ResultStatus status;
	
	public MethodTestResult(String methodName, String expectedResult, String actualResult) {
		this.methodName = methodName;
		this.expectedResult = expectedResult;
		this.actualResult = actualResult;
		//a teszt eredmeny kiertekelese
		if(expectedResult.equals(actualResult)) {
			status = ResultStatus.SUCCESS;
		} else { //kudarc
			//nem vart kivetel kovetkezett be
			if((expectedResult.equals("true")||expectedResult.equals("false")) && (!actualResult.equals("true")&&!actualResult.equals("false"))) {
				status = ResultStatus.INTERRUPTED;
			} else { //helytelen boolean eredmeny
				status = ResultStatus.FAIL;
			}
		}
	}

	/** Eredmeny szoveges formaba alakitasa. */	
	@Override
	public String toString() {
		String s = "Method: " + methodName + ", result: " + ResultStatus.asString(status) + "\n";
		s += "Expected result: " + expectedResult + ", actual result: " + actualResult;
		return s;
	} 

	public String getMethodName() {
		return methodName;
	}

	public String getExpectedResult() {
		return expectedResult;
	}

	public String getActualResult() {
		return actualResult;
	}

	public ResultStatus getStatus() {
		return status;
	}
}
