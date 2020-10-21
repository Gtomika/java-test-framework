package com.gaspar.unittest.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/** Az ezzel jelolt metodusok tesztelesre fognak kerulni, amennyiben mas gond nincs veluk. */
@Retention(RUNTIME)
@Target(METHOD)
public @interface Test {}
