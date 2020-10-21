package com.gaspar.unittest;

import com.gaspar.unittest.results.InterruptCause;
import com.gaspar.unittest.results.ResultStatus;

/**
 * Ez a kivetel dobodik, ha egy sikeresen elindult teszt megszakad. Ezzel kivulrol nem 
 * kell foglalkozni, az eredmenyben ugy jelenik meg, hogy a statusza {@link ResultStatus#INTERRUPTED} lesz.
 */
public class TestInterruptException extends Exception {

	/** A megszakadas oka. */
	private InterruptCause cause;
	
	public TestInterruptException(InterruptCause cause) {
		super(cause.toString());
		this.cause = cause;
	}
	
	public InterruptCause getInterruptCause() {
		return cause;
	}
}
