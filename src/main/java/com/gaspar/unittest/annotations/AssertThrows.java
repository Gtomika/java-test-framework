package com.gaspar.unittest.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/** Az ezzel jelolt tesztmetodus eseten az elvart viselkedes a kivetel dobasa. */
@Retention(RUNTIME)
@Target(METHOD)
public @interface AssertThrows {

	/* Ilyen kivetelt varunk. */
	public Class<? extends Exception> exception();
	
}
