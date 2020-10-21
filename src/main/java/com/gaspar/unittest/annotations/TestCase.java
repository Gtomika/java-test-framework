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
	
	/** 
	 * Idokorlat az osztaly tesztelesere (ezredmasodperc).
	 * @throws TestException Ha nem pozitiv szam.
	 */
	public long timeLimit() default NO_TIME_LIMIT;
	
	/** 
	 * Elfogadott hibaarany. Ha ez X, akkor az osztaly tesztelese meg akkor 
	 * is sikeres lesz, ha a tesztek X szazaleka nem sikeres. 0 es 1 kozott kell lennie.
	 * @throws TestException Ha nem 0 es 1 kozotti.
	 */
	public double errorTolerance() default 0;
	
}
