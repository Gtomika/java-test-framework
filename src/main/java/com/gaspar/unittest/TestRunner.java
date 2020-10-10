package com.gaspar.unittest;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.gaspar.unittest.annotations.TestCase;
import com.gaspar.unittest.results.MethodTestResult;
import com.gaspar.unittest.results.TestResult;

/**
 * Tesztek futtatasat vegzo osztaly. A teszteles a statikus {@link #testClass(Class)} es {@link #testClasses(Class...)} 
 * metodusokkal tortenik.
 * @author Gaspar Tamas
 */
public class TestRunner {
	
	/** A tesztelt osztaly */
	private final Class<?> clazz;
	/** Listazza az osztaly teszteleshez kapcsolodo metodusait. */
	private final MethodCollector methodCollector;
	/** A teszt eredmenyeit, warningokat gyujt ossze. */
	private TestResult.Builder resultBuilder = new TestResult.Builder();
	
	/** Privat konstruktor. */
	private TestRunner(Class<?> clazz) throws TestException {
		this.clazz = clazz;
		if(!clazz.isAnnotationPresent(TestCase.class)) { //annotacio ellenorzes
			throw new TestException("Missing @TestCase on class " + clazz.getSimpleName());
		}
		try {
			clazz.getConstructor(); //deafult konstruktor elkerese
		} catch (NoSuchMethodException | SecurityException e) {
			//vagy nem letezik vagy nem hivhato meg
			throw new TestException("Default constructor not found or cannot be called on class " + clazz.getSimpleName());
		}
		this.methodCollector = new MethodCollector(clazz, resultBuilder); //metodus listazas itt megtortenik
	}
	
	/**
	 * Lefuttatja az adott osztaly teszt metodusait.
	 * @param clazz Az adott osztaly.
	 * @throws TestException Ha az adott osztaly nem annotalt TestCase-el, vagy nincs default konstruktora.
	 */
	public static TestResult testClass(Class<?> clazz) throws TestException {
		try {
			return new TestRunner(clazz).doClassTest();
		} catch (IllegalAccessException | InstantiationException e) {
			throw new TestException(e.getClass().getSimpleName() + " during test class instantiation: " + e.getMessage());
		}
	}
	
	/**
	 * Lefuttatja az osszes adott osztaly teszt metodusat, minden osztalyt sajat szalon.
	 * @param classes Az adott osztalyok.
	 * @throws TestException Ha barmelyik adott osztaly nem annotalt TestCase-el, vagy nincs default konstruktora.
	 */
	public static List<TestResult> testClasses(Class<?>...classes) throws TestException {
		int threadNum = Math.min(classes.length, Runtime.getRuntime().availableProcessors());
		
		final ExecutorService executor = Executors.newFixedThreadPool(threadNum);
		List<Callable<TestResult>> tasks = new ArrayList<>(); //hatter feladatok elkeszitese
		for(Class<?> clazz: classes) {
			Callable<TestResult> task = () -> new TestRunner(clazz).doClassTest();
			tasks.add(task);
		}
		try {
			List<Future<TestResult>> futures = executor.invokeAll(tasks); //feladatok beadasa
			executor.shutdown();
			executor.awaitTermination(1, TimeUnit.HOURS); //feladatok befejezesenek megvarasa
			
			List<TestResult> results = new ArrayList<>(); //eredmenyek elkerese
			for(Future<TestResult> future: futures) {
				results.add(future.get());
			}
			return results;
		} catch (InterruptedException | ExecutionException e) {
			throw new TestException(e.getClass().getSimpleName() + " while running tests on multiple threads: " + e.getMessage());
		} 
		
	}
	
	/** 
	 * Elvegzi a tesztelest a metodus listazo altal megadott metodusokkal.
	 * @return A teszt eredmenyt tartalmazo objektum.
	 */
	private TestResult doClassTest() throws IllegalAccessException, InstantiationException, TestException {
		resultBuilder.withClassName(clazz.getName());
		final Object testInstance = clazz.newInstance(); //default konstruktorral peldanyositas
		//tesztmetodusok iteralasa
		for(TestMethod testMethod: methodCollector.getTestMethods()) {
			MethodTestResult methodResult = doMethodTest(testMethod, testInstance);
			resultBuilder.addResult(methodResult); //eredmeny elmentese 
		}
		return resultBuilder.build();
	}
	
	/**
	 * Meghivja a teszt metodust es osszeallitja az eredmeny objktumot.
	 * @param testMethod A tesztelendo metodus.
	 * @param testInstance A teszt osztaly peldanya.
	 * @return A teszt eredmenye.
	 */
	private MethodTestResult doMethodTest(final TestMethod testMethod, final Object testInstance) throws IllegalAccessException {
		MethodTestResult testResult = null;
		try {
			boolean boolResult = (boolean)testMethod.getMethod().invoke(testInstance);
			//a teszt lefutott es boolean eredmenyt adott
			if(testMethod.isExceptionAsserted()) { //de: kivetel volt az elvart
				testResult = new MethodTestResult(testMethod.getMethod().getName(), testMethod.getAssertedException().getSimpleName(), String.valueOf(boolResult));
			} else { //boolean volt az elvart
				testResult = new MethodTestResult(testMethod.getMethod().getName(), String.valueOf(testMethod.getAssertValue()), String.valueOf(boolResult));
			}
		} catch(InvocationTargetException e) { //ha az "invoke"-kal hivott metodus kiveteld do akkor az invoke InvocationTargetException-t dob
			Class<? extends Throwable> actualExc = e.getCause().getClass(); //eredeti kivetel
			Class<? extends Exception> assertedExc = testMethod.getAssertedException();
			if(testMethod.isExceptionAsserted() && actualExc.equals(assertedExc)) { 
				//ez a kivetel volt az elvart eredmeny
				testResult = new MethodTestResult(testMethod.getMethod().getName(), assertedExc.getSimpleName(), assertedExc.getSimpleName());
			} else {
				//nem ez a kivetel volt az elvart eredmeny
				if(testMethod.isExceptionAsserted()) { //egy masik kivetel volt az elvart
					testResult = new MethodTestResult(testMethod.getMethod().getName(), assertedExc.getSimpleName(), actualExc.getSimpleName());
				} else { //boolean volt az elvart
					testResult = new MethodTestResult(testMethod.getMethod().getName(), String.valueOf(testMethod.getAssertValue()), actualExc.getSimpleName());
				}
			}
		}
		return testResult;
	}
}
