package com.gaspar.unittest;

import com.gaspar.unittest.results.InterruptCause;

/**
 * Ez a kivetel dobodik, ha egy sikeresen elindult teszt megszakad.
 */
public class TestInterruptException extends Exception {

	private InterruptCause cause;
	
	public TestInterruptException(InterruptCause cause) {
		super(cause.toString());
		this.cause = cause;
	}
	
	public InterruptCause getInterruptCause() {
		return cause;
	}
}
