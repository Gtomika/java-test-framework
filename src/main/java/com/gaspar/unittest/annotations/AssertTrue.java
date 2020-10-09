package com.gaspar.unittest.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/** Ha egy teszt metodus ezzel van jelolve akkor attol igaz visszateresi erteket varunk. */
@Retention(RUNTIME)
@Target(METHOD)
public @interface AssertTrue {}
