## Unit Teszt keretrendszer

Vállalati információs rendszerek kötelező program.

## A feladat:
**VIR1:** Egyszerű unit test futtató keretrendszer annotációk segítségével. Valósítsunk meg egy a 
JUnit-hoz hasonló funkcionalitást, azaz olyan annotációkat, amelyek segítségével unit testek hajthatók 
végre. A minimálisan támogatandó annotációk a következők: @TestCase, @BeforeClass, @AfterClass, @Before, 
@After, @Test, @Skip. Valósítsuk meg ezen annotációk saját változatait, és implementáljunk egy TestRunner 
meghajtó osztályt, amely kap egy Class objektumot (ami @TestCase-zel van annotálva), és végrehajtja a 
benne definiált teszteket a következőképpen: reflection segítségével végigmegy az összes @Test-tel 
annotált metódusán, és végrehajtja azt, ám ha van @Before és/vagy @After annotációval ellátott metódus, 
azt előtte/utána közvetlenül meghívja. Ha vannak @BeforeClass/@AfterClass annotációval ellátott metódusok, 
akkor azokat egyetlen egyszer a tesztek futtatása előtt/után végrehajtja. Ha egy metódus annotálva van 
a @Test annotációval, de egy @Skip is rá van téve, akkor azt a tesztet mégsem kell végrehajtani.

## Tesztek működése:
Minden @Test-tel annotált metódusnak **boolean** visszatérési értékűnek kell lennie (még akkor is, 
ha kivételt várunk tőle). Azt, hogy milyen értéket várunk a teszttől az @AssertTrue, @AssertFalse 
és @AssertThrow(exception) annotációkkal lehet jelezni (nem kell, alapértelmezetten true az elvárt). 
Tesztmetódusnak nem lehet paramétere.

A teszt sikeres lesz, ha az elvárt boolean értéket adja vissza, vagy elvárt kivétel esetén ténylegesen 
dobja a kivételt.

## Tesztek eredménye:
Az eredmény egy *TestResult* objektumban jön vissza, ami *toString*-el formázottan jeleníthető meg, 
vagy el lehet kérni külön külön a dolgokat, pl az egyes metódusok eredményeit.

## Hibás teszt osztályok, teszt metódusok.
Ha a teszt osztály hibás (nincs @TestCase, nincs default konstruktor, stb...) akkor *TestException* 
dobódik. Ha egy metódus nem megfelelő teszt metódus (pl van paramétere, *static*, vagy nem bool a visszatérési 
értéke) egy warning fog készülni és a metódus ignorálva lesz.

A warningok a *TestResult* objektumból kérhetőek le vagy írathatóak ki.

## Before(Class), After(Class) működése
Ezek a feladatkiírás szerint működnek. Mindegyiknek *public*-nak kell lenne, az *@AfterClass* és 
*@BeforeClass* metódusoknak *static*-nak is kell lenniük. Se paraméterük, se visszatérési értékük 
nem lehet. Ha ezek nem teljesülnek, akkor a hibás szintaxisú tesztekhez hasonlóan ezekhez is warningok 
készülnek és a metódusok ignorálva lesznek.

Ha egy ilyen metódus híváskor kivételt dob, akkor a tesztelés megszakad, INTERRUPTED eredménnyel.

## Extrák
Extra funkciók, amik a feladatban nincsenek benne, de implementálva vannak:
- Időkorlát állítása az *@TestCase* annotációval. Az ezt túllépő teszt FAIL eredményű lesz. Opcionális, alapértelmezetten nincs korlát.
- Hibaarány állítása: megadható a *@TestCase* esetén egy 0 és 1 közötti hibaarány. Pl, ha ez 0.05, akkor ha a tesztek kevesebb mint 5% lesz rossz, az osztály teszt még sikeres lesz. Opcionális, alapértelmezetten ez 0, vagyis minden teszt sikere kell.

## Példakódok, tesztek
Példakódok és a framework tesztelése JUnit-tal az [src/test/java](http://vir.inf.u-szeged.hu:8181/Gtomika/UnitTest/tree/master/src/test/java/com/gaspar/unittest) mappában találhatóak.

Itt egy egyszerű példa ami bemutat nagyjából minden funkciót:

```java
@TestCase(timeLimit = 15, errorTolerance = 0.4) //15 ezred mp limit, 40% hibas lehet.
public class AllFunctionsSample {

	//tesztelendo metodus
	public int add(int n1, int n2) {
		return n1 + n2;
	}
	
	//teszt elott es utan futtatando metodusok
	@BeforeClass
	public static void printBeginTesting() {
		System.out.println("Kezdodik a teszteles!");
	}
	
	@AfterClass
	public static void printEndTesting() {
		System.out.println("Befejezodott a teszteles!");
	}
	
	@Before
	public void printBeginTestCase() {
		System.out.println("Kezdodik egy metodusteszt!");
	}
	
	@After
	public void printEndTestCase() {
		System.out.println("Befejezodott egy metodusteszt!");
	}
	
	//tenyleges tesztek
	@Test
	public boolean testAdd1() {
		System.out.println("Teszt add 1");
		return add(2,3) == 5;
	}
	
	@Test
	@AssertFalse //ez sikertelen lesz, a 0.4-es hiba arany miatt az osztalyteszt sikeres lesz
	public boolean testAdd2() {
		System.out.println("Teszt add 2");
		return add(10,0) == 10;
	}
	
	@Test
	@AssertThrows(exception = NullPointerException.class)
	public boolean testAdd3() {
		System.out.println("Teszt add 3");
		throw new NullPointerException();
	}
	
	@Test
	@Skip //ez nem fog futni
	public boolean testAdd4() {
		System.out.println("Teszt add 4");
		return add(3,3) == 6;
	}
}
```

Futtatása így történik:

```java
public static void main(String[] args) {
	TestResult result = TestRunner.testClass(AllFunctionsSample.class);
	System.out.println(result);
}
```

A megjelenő output:

```
Kezdodik a teszteles!
Kezdodik egy metodusteszt!
Teszt add 2
Befejezodott egy metodusteszt!
Kezdodik egy metodusteszt!
Teszt add 1
Befejezodott egy metodusteszt!
Kezdodik egy metodusteszt!
Teszt add 3
Befejezodott egy metodusteszt!
Befejezodott a teszteles!
--------------------------------------------------------------------------------------
Result of testing for class com.gaspar.unittest.samples.AllFunctionsSample. Status: SUCCESS
Testing took 4 milliseconds, time limit was 15.
Sucess ratio was 66%, which is allowed by the 40% error tolerance.
There were a total of 3 tests run,
out of which 2 succeded, 1 failed and 0 were unexpectedly interrupted.

Detailed reports for each test method:
- Method: testAdd2, result: FAIL
Expected result: false, actual result: true
- Method: testAdd1, result: SUCCESS
Expected result: true, actual result: true
- Method: testAdd3, result: SUCCESS
Expected result: NullPointerException, actual result: NullPointerException

```

## Dokumentáció
Javadoc segítségével készült, a [doc](http://vir.inf.u-szeged.hu:8181/Gtomika/UnitTest/tree/master/doc) mappában.