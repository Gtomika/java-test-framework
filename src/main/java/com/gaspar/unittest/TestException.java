package com.gaspar.unittest;

/**
 * Ez a kivetel akkor dobodik amikor a teszteles valamilyen ok miatt nem tud elindulni.
 * Megszakado teszteles eseten az {@link TestInterruptException} van hasznalva.
 */
public class TestException extends RuntimeException {

	public TestException(String message) {
		super(message);
	}
	
}
