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
public @interface TestCase {}
