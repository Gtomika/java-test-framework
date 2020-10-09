package com.gaspar.unittest.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/** Az ezzel jelolt metodusok minden teszt elott lefutnak. */
@Retention(RUNTIME)
@Target(METHOD)
public @interface Before {}
