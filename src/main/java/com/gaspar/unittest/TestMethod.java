package com.gaspar.unittest;

import java.lang.reflect.Method;

/** Egy tesztmetodust reprezental. */
public class TestMethod {

	/** A futtatando metodus. */
	private final Method method;
	/** A teszt metodustol elvart boolean ertek. */
	private final boolean assertedValue;
	/** A teszt metodustol elvárt throw ertek. null, ha throw-t nem varunk. */
	private final Class<? extends Exception> assertedException;
	
	/** Boolean elvart ertek konstruktor. */
	TestMethod(Method method, boolean assertedValue) {
		this.method = method;
		this.assertedValue = assertedValue;
		assertedException = null;
	}
	
	TestMethod(Method method, Class<? extends Exception> assertedException) {
		this.method = method;
		this.assertedException = assertedException;
		assertedValue = false; //nincs jelentosege
	}

	boolean isExceptionAsserted() {
		return assertedException != null;
	}
	
	public Method getMethod() {
		return method;
	}

	public boolean getAssertValue() {
		return assertedValue;
	}

	public Class<? extends Exception> getAssertedException() {
		return assertedException;
	}
}
