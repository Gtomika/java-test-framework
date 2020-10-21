package com.gaspar.unittest.results;

/** 
 * Teszteset megszakadasanak okat tartalmazza.
 * @see TestResult
 */
public class InterruptCause {

	/** Ez a kivetel okozta a megszakadast. */
	private final Throwable exception;
	/** Ebben a metodusban dobodott a kivetel. */
	private final String methodName;
	/** A metodus tulajdonsaga, pl: Before, After, stb... */
	private final String methodProperty;
	
	public InterruptCause(Throwable exception, String methodName, String methodProperty) {
		this.exception = exception;
		this.methodName = methodName;
		this.methodProperty = methodProperty;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Cause of interrupt: " + exception.getClass().getSimpleName() + "\n");
		sb.append("Exception message: " + exception.getMessage() + "\n");
		sb.append("Occurred in " + methodProperty + " method " + methodName + ".");
		return sb.toString();
	}
}
