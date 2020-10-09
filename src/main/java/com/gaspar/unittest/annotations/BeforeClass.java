package com.gaspar.unittest.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/** Az ezzel jelolt metodusok az osztaly tesztelese elott egyszer lefutnak. */
@Retention(RUNTIME)
@Target(METHOD)
public @interface BeforeClass {

}
