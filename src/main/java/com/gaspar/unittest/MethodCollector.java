package com.gaspar.unittest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
import com.gaspar.unittest.results.TestResult;

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
	private final List<Method> beforeMethods = new ArrayList<>();
	/** Az osztaly tesztelese elott egyszer futtatando metodusok. */
	private final List<Method> beforeClassMethods = new ArrayList<>();
	/** A minden teszt utan futtatando metodusok. */
	private final List<Method> afterMethods = new ArrayList<>();
	/** Az osztaly tesztelese utan egyszer futtatando metodusok. */
	private final List<Method> afterClassMethods = new ArrayList<>();
	
	/** A listazas soran talalt problemakat gyujti ossze. */
	private final TestResult.Builder resultBuilder;
	
	/**
	 * Konstruktor.
	 * @param clazz Az osztaly aminek listazni kell a metodusait.
	 * @param resultBuilder Az osztaly teszteredmenyenek buildere.
	 */
	MethodCollector(Class<?> clazz, final TestResult.Builder resultBuilder) {
		methods = Arrays.asList(clazz.getDeclaredMethods());
		this.resultBuilder = resultBuilder;
		listMethods(); //azonnal listaz
	}
	
	/** Feltolti a metodus listakat. Nem kell meghivni, mar a konstruktorban hivodik. */
	private void listMethods() {
		for(Method method: methods) {
			if(method.isAnnotationPresent(Skip.class) || method.getAnnotations().length == 0) { //skip-es és annotacio nelkuli ignoralasa
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
				
			} else if(validBeforeMethod(method)) {
				beforeMethods.add(method);
			} else if(validBeforeClassMethod(method)) {
				beforeClassMethods.add(method);
			} else if(validAfterMethod(method)) {
				afterMethods.add(method);
			} else if(validAfterClassMethod(method)) {
				afterClassMethods.add(method);
			} 
		}
	}
	
	/**
	 *  Megnezi, hogy a kapott metodus érvényes tesztmetodus-e.
	 *  A kapott metoduson biztosan van egy nem Skip annotacio, skip biztosan nincs rajta. Ervenyes, ha:
	 *  <ul>
	 *  	<li> van rajta Test annotáció</li>
	 *  	<li> nincs rajta elvárt visszatérési érték jelző (ekkor true lesz), vagy pontosan egy van rajta (AssertTrue, AssertFalse, AssertThrow).</li>
	 *  	<li> nincs rajta egyéb annotáció, pl Before, Skip.</li>
	 *  	<li> nincs parametere</li>
	 *  	<li> boolean a visszateresi erteke (meg akkor is, ha kivetelt varunk el tole)</li>
	 *  	<li> nem statikus, és public</li>
	 *  </ul>
	 */
	private boolean validTestMethod(final Method method) {
		boolean isValid = true;
		if(!method.isAnnotationPresent(Test.class)) {
			//@Test nelkul biztos nem jo teszt metodus (de ez nem warning, lehetnek nem teszt metodusok is)
			return false; //visszateres, hogy a tobbi warning ne adodjon hozza ehhez a metodushoz
		}
		//van @Test, elvart ertek keresese
		if(!correctAssertValue(method)) { //elvart ertek nem megfeleloen jelezve
			resultBuilder.addWarning(method.getName() + ": this test method has multiple asserted values, so it's ignored.");
			isValid = false; 
		}
		//van, Test, elvart ertek megfelelo, mas nem lehet
		if(hasAnyAnnotation(method, Before.class, After.class, BeforeClass.class, AfterClass.class)) {
			resultBuilder.addWarning(method.getName() + ": this test method is also marked @Before(Class) or @After(Class), so it's ignored.");
			isValid = false;
		}
		//annotaciok szempontjabol ez jo, de parameterek es visszateresi ertek szempontjabol nem biztos
		if(method.getParameterCount() > 0) {
			resultBuilder.addWarning(method.getName() + ": this test method has parameters, which is not allowed, so it's ignored.");
			isValid = false;
		}
		if(!method.getReturnType().equals(Boolean.TYPE)) {
			resultBuilder.addWarning(method.getName() + ": this test method has non-boolean return value, so it's ignored.");
			isValid = false;
		}
		if(Modifier.isStatic(method.getModifiers())) {
			resultBuilder.addWarning(method.getName() + ": this test method is static, which is not allowed.");
			isValid = false;
		}
		if(!Modifier.isPublic(method.getModifiers())) {
			resultBuilder.addWarning(method.getName() + ": this test method is not public, which is not allowed.");
			isValid = false;
		}
		return isValid;
	}
	
	/**
	 * Megnezi, hogy az adott metodus valid Before, minden teszt elott futtatando metodus.
	 * A kapott metoduson biztosan van egy nem Skip annotacio, skip biztosan nincs rajta. Valid, ha:
	 * <ul>
	 * 	<li> Nincs rajta Skip, Test, After, BeforeClass, AfterClass</li>
	 * 	<li> Van rajta Before</li>
	 *  <li> Nincs paramétere, se visszatérési értéke</li>
	 *  <li> Nem statikus és public</li>
	 *  </ul>
	 */
	private boolean validBeforeMethod(final Method method) {
		boolean isValid = true;
		if(!method.isAnnotationPresent(Before.class)) {
			//nem @Before, de ez nem warning
			return false; //visszateres, hogy a tobbi warning ne adodjon hozza ehhez a metodushoz
		}
		if(hasAnyAnnotation(method, BeforeClass.class, AfterClass.class, After.class, Test.class)) {
			resultBuilder.addWarning(method.getName() + ": this @Before method has non-compatible annotations with @Before, so it's ignored.");
		}
		//annotaciok szempontjabol ez jo, de parameterek es visszateresi ertek szempontjabol nem biztos
		if(method.getParameterCount() > 0) {
			resultBuilder.addWarning(method.getName() + ": this @Before method has parameters, which is not allowed, so it's ignored.");
			isValid = false;
		}
		if(!method.getReturnType().equals(Void.TYPE)) {
			resultBuilder.addWarning(method.getName() + ": this @Before method has a return value, so it's ignored.");
			isValid = false;
		}
		if(Modifier.isStatic(method.getModifiers())) {
			resultBuilder.addWarning(method.getName() + ": this @Before method is static, which is not allowed.");
			isValid = false;
		}
		if(!Modifier.isPublic(method.getModifiers())) {
			resultBuilder.addWarning(method.getName() + ": this @Before method is not public, which is not allowed.");
			isValid = false;
		}
		return isValid;
	}
	
	/**
	 * Megnezi, hogy az adott metodus valid BeforeClass, tesztek elott egyszer futtatando metodus.
	 * A kapott metoduson biztosan van egy nem Skip annotacio, skip biztosan nincs rajta. Valid, ha:
	 * <ul>
	 * 	<li> Nincs rajta Skip, Test, After, Before, AfterClass</li>
	 * 	<li> Van rajta BeforeClass</li>
	 *  <li> Nincs paramétere, se visszatérési értéke</li>
	 *  <li> Statikus és public</li>
	 *  </ul>
	 */
	private boolean validBeforeClassMethod(final Method method) {
		boolean isValid = true;
		if(!method.isAnnotationPresent(BeforeClass.class)) {
			//nem @BeforeClass, de ez nem warning
			return false; //visszateres, hogy a tobbi warning ne adodjon hozza ehhez a metodushoz
		}
		if(hasAnyAnnotation(method, Before.class, AfterClass.class, After.class, Test.class)) {
			resultBuilder.addWarning(method.getName() + ": this @BeforeClass method has non-compatible annotations with @BeforeClass, so it's ignored.");
		}
		//annotaciok szempontjabol ez jo, de parameterek es visszateresi ertek szempontjabol nem biztos
		if(method.getParameterCount() > 0) {
			resultBuilder.addWarning(method.getName() + ": this @BeforeClass method has parameters, which is not allowed, so it's ignored.");
			isValid = false;
		}
		if(!method.getReturnType().equals(Void.TYPE)) {
			resultBuilder.addWarning(method.getName() + ": this @BeforeClass method has a return value, so it's ignored.");
			isValid = false;
		}
		if(!Modifier.isStatic(method.getModifiers())) {
			resultBuilder.addWarning(method.getName() + ": this @BeforeClass method is NOT static, which is not allowed.");
			isValid = false;
		}
		if(!Modifier.isPublic(method.getModifiers())) {
			resultBuilder.addWarning(method.getName() + ": this @BeforeClass method is not public, which is not allowed.");
			isValid = false;
		}
		return isValid;
	}
	
	/**
	 * Megnezi, hogy az adott metodus valid After, minden teszt utan futtatando metodus.
	 * A kapott metoduson biztosan van egy nem Skip annotacio, skip biztosan nincs rajta. Valid, ha:
	 * <ul>
	 * 	<li> Nincs rajta Skip, Test, Before, BeforeClass, AfterClass</li>
	 * 	<li> Van rajta After</li>
	 *  <li> Nincs paramétere, se visszatérési értéke</li>
	 *  <li> Nem statikus és public</li>
	 * </ul>
	 */
	private boolean validAfterMethod(final Method method) {
		boolean isValid = true;
		if(!method.isAnnotationPresent(After.class)) {
			//nem @After, de ez nem warning
			return false; //visszateres, hogy a tobbi warning ne adodjon hozza ehhez a metodushoz
		}
		if(hasAnyAnnotation(method, BeforeClass.class, AfterClass.class, Before.class, Test.class)) {
			resultBuilder.addWarning(method.getName() + ": this @After method has non-compatible annotations with @Before, so it's ignored.");
		}
		//annotaciok szempontjabol ez jo, de parameterek es visszateresi ertek szempontjabol nem biztos
		if(method.getParameterCount() > 0) {
			resultBuilder.addWarning(method.getName() + ": this @After method has parameters, which is not allowed, so it's ignored.");
			isValid = false;
		}
		if(!method.getReturnType().equals(Void.TYPE)) {
			resultBuilder.addWarning(method.getName() + ": this @After method has a return value, so it's ignored.");
			isValid = false;
		}
		if(Modifier.isStatic(method.getModifiers())) {
			resultBuilder.addWarning(method.getName() + ": this @After method is static, which is not allowed.");
			isValid = false;
		}
		if(!Modifier.isPublic(method.getModifiers())) {
			resultBuilder.addWarning(method.getName() + ": this @After method is not public, which is not allowed.");
			isValid = false;
		}
		return isValid;
	}
	
	/**
	 * Megnezi, hogy az adott metodus valid AfterClass, tesztek utan egyszer futtatando metodus.
	 * A kapott metoduson biztosan van egy nem Skip annotacio, skip biztosan nincs rajta. Valid, ha:
	 * <ul>
	 * 	<li> Nincs rajta Skip, Test, After, Before, BeforeClass</li>
	 * 	<li> Van rajta AfterClass</li>
	 *  <li> Nincs paramétere, se visszatérési értéke</li>
	 *  <li> Statikus és public</li>
	 *  </ul>
	 */
	private boolean validAfterClassMethod(final Method method) {
		boolean isValid = true;
		if(!method.isAnnotationPresent(AfterClass.class)) {
			//nem @BeforeClass, de ez nem warning
			return false; //visszateres, hogy a tobbi warning ne adodjon hozza ehhez a metodushoz
		}
		if(hasAnyAnnotation(method, Before.class, BeforeClass.class, After.class, Test.class)) {
			resultBuilder.addWarning(method.getName() + ": this @AfterClass method has non-compatible annotations with @BeforeClass, so it's ignored.");
		}
		//annotaciok szempontjabol ez jo, de parameterek es visszateresi ertek szempontjabol nem biztos
		if(method.getParameterCount() > 0) {
			resultBuilder.addWarning(method.getName() + ": this @AfterClass method has parameters, which is not allowed, so it's ignored.");
			isValid = false;
		}
		if(!method.getReturnType().equals(Void.TYPE)) {
			resultBuilder.addWarning(method.getName() + ": this @AfterClass method has a return value, so it's ignored.");
			isValid = false;
		}
		if(!Modifier.isStatic(method.getModifiers())) {
			resultBuilder.addWarning(method.getName() + ": this @AfterClass method is NOT static, which is not allowed.");
			isValid = false;
		}
		if(!Modifier.isPublic(method.getModifiers())) {
			resultBuilder.addWarning(method.getName() + ": this @AfterClass method is not public, which is not allowed.");
			isValid = false;
		}
		return isValid;
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

	public List<Method> getBeforeMethods() {
		return beforeMethods;
	}

	public List<Method> getBeforeClassMethods() {
		return beforeClassMethods;
	}

	public List<Method> getAfterMethods() {
		return afterMethods;
	}

	public List<Method> getAfterClassMethods() {
		return afterClassMethods;
	}
}
