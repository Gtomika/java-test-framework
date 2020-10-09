package com.gaspar.unittest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gaspar.unittest.annotations.After;
import com.gaspar.unittest.annotations.AfterClass;
import com.gaspar.unittest.annotations.AssertFalse;
import com.gaspar.unittest.annotations.AssertThrows;
import com.gaspar.unittest.annotations.AssertTrue;
import com.gaspar.unittest.annotations.Before;
import com.gaspar.unittest.annotations.BeforeClass;
import com.gaspar.unittest.annotations.Skip;
import com.gaspar.unittest.annotations.Test;

/**
 * Listakba szedi a kapott osztaly metodusait, az alapjan, hogy milyen annotacioik vannak.
 * @author Gaspar Tamas
 */
class MethodCollector {
	/** Az osszes osztalybeli metodus. */
	private final List<Method> methods;
	/** A futtatando teszt metodusok. */
	private final List<TestMethod> testMethods = new ArrayList<>();
	/** A minden teszt elott futtatando metodusok. */
	private final List<Method> beforeEachMethods = new ArrayList<>();
	/** Az osztaly tesztelese elott egyszer futtatando metodusok. */
	private final List<Method> beforeAllMethods = new ArrayList<>();
	/** A minden teszt utan futtatando metodusok. */
	private final List<Method> afterEachMethods = new ArrayList<>();
	/** Az osztaly tesztelese utan egyszer futtatando metodusok. */
	private final List<Method> afterAllMethods = new ArrayList<>();
	
	/** A listazas soran talalt problemak. */
	
	
	MethodCollector(Class<?> clazz) {
		methods = Arrays.asList(clazz.getDeclaredMethods());
		listMethods(); //azonnal listaz
	}
	
	/** Feltolti a metodus listakat. */
	private void listMethods() {
		for(Method method: methods) {
			if(method.isAnnotationPresent(Skip.class)) { //skip-es ignoralasa
				continue;
			} else if(validTestMethod(method)) { //ervenyes teszmetodus
				//elvart ertek beolvasasa
				if(hasAnyAnnotation(method, AssertFalse.class, AssertTrue.class, AssertThrows.class)) { //van elvart ertek annotacio
					
					if(method.isAnnotationPresent(AssertFalse.class)) {
						testMethods.add(new TestMethod(method, false));
					} else if(method.isAnnotationPresent(AssertTrue.class)) {
						testMethods.add(new TestMethod(method, true));
					} else { //csak throw lehet
						AssertThrows throwAnnotation = (AssertThrows)method.getAnnotation(AssertThrows.class);
						testMethods.add(new TestMethod(method, throwAnnotation.exception()));
					}
					
				} else { //nincs elvart ertek annotacio, true lesz
					testMethods.add(new TestMethod(method, true));
				}
				
			} //TODO: before, after, ... metodusok osszegyujtese
		}
	}
	
	/**
	 *  Megnezi, hogy a kapott metodus érvényes tesztmetodus-e.
	 *  Érvényes, ha:
	 *  	- van rajta Test annotáció
	 *  	- nincs rajta elvárt visszatérési érték jelző (ekkor true lesz), vagy pontosan egy van rajta (AssertTrue, AssertFalse, AssertThrow).
	 *  	- nincs rajta egyéb annotáció, pl Before, Skip.
	 *  	- nincs parametere
	 *  	- boolean a visszateresi erteke (meg akkor is, ha kivetelt varunk el tole)
	 */
	private boolean validTestMethod(final Method method) {
		if(!method.isAnnotationPresent(Test.class)) return false; //@Test nelkul biztos nem jo
		//van @Test, elvart ertek keresese
		if(!correctAssertValue(method)) return false; //elvart ertek nem megfeleloen jelezve
		//van, Test, elvart ertek megfelelo, mas nem lehet
		if(hasAnyAnnotation(method, Before.class, After.class, BeforeClass.class, AfterClass.class)) return false;
		//annotaciok szempontjabol ez jo, de parameterek es visszateresi ertek szempontjabol nem biztos
		if(method.getParameterCount() > 0) return false;
		if(!method.getReturnType().equals(Boolean.TYPE)) return false;
		return true;
	}
	
	/**
	 *  Megnezi hogy a metóduson helyesen van-e elhelyzve elvárt érték jelző.
	 *  Jó, ha nincs rajta elvárt visszatérési érték jelző (ekkor true lesz), vagy pontosan egy van rajta (AssertTrue, AssertFalse, AssertThrow). 
	 */
	private static boolean correctAssertValue(final Method method) {
		int count = 0;
		List<Class<? extends Annotation>> assertAnnotations = Arrays.asList(AssertTrue.class, AssertFalse.class, AssertThrows.class);
		for(Class<? extends Annotation> annotation: assertAnnotations) {
			if(method.isAnnotationPresent(annotation)) count++;
		}
		return count <= 1;
	}
	
	/** Megnezi, hogy a kapott metodus jelolve van-e barmelyik kapott annotacioval. */
	@SafeVarargs
	private static boolean hasAnyAnnotation(final Method method, Class<? extends Annotation>... annotations) {
		for(Class<? extends Annotation> annotation: annotations) {
			if(method.isAnnotationPresent(annotation)) return true;
		}
		return false;
	}

	public List<TestMethod> getTestMethods() {
		return testMethods;
	}

	public List<Method> getBeforeEachMethods() {
		return beforeEachMethods;
	}

	public List<Method> getBeforeAllMethods() {
		return beforeAllMethods;
	}

	public List<Method> getAfterEachMethods() {
		return afterEachMethods;
	}

	public List<Method> getAfterAllMethods() {
		return afterAllMethods;
	}	
}
