package com.gaspar.unittest.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.gaspar.unittest.TestException;

/**
 * A teszt osztalyokat jelolo annotacio. Ha ez nincs az tesztelendo 
 * osztalyon, akkor {@link TestException} fog dobodni.
 * @author Gaspar Tamas
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface TestCase {
	
	/** Konstans annak jelzesere, hogy nincs idokorlat. Ezt nem kell megadni, ez az alapertelmezett. */
	public static final long NO_TIME_LIMIT = -1;
	
	/** Idokorlat az osztaly tesztelesere (ezredmasodperc). */
	public long timeLimit() default NO_TIME_LIMIT;
	
}
