#Unit Teszt keretrendszer

Vállalati információs rendszerek kötelező program.

##A feladat:
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

##Tesztek működése:
Minden @Test-tel annotált metódusnak **boolean** visszatérési értékűnek kell lennie (még akkor is, 
ha kivételt várunk tőle). Azt, hogy milyen értéket várunk a teszttől az @AssertTrue, @AssertFalse 
és @AssertThrow(exception) annotációkkal lehet jelezni (nem kell, alapértelmezetten true az elvárt). 
Tesztmetódusnak nem lehet paramétere.

A teszt sikeres lesz, ha az elvárt boolean értéket adja vissza, vagy elvárt kivétel esetén ténylegesen 
dobja a kivételt.

##Before, After működése
MÉG NINCSENEK IMPLEMENTÁLVA.
~~Ezek az annotációk a feladat leírása szerint működnek.~~

##Tesztek eredménye:
Az eredmény egy TestResult objektumban jön vissza, ami toString-el formázottan jeleníthető meg, 
vagy el lehet kérni külön külön a dolgokat, pl az egyes metódusok eredményeit.

##Hibás teszt osztályok, teszt metódusok.
Ha a teszt osztály hibás (nincs @TestCase, nincs default konstruktor, stb...) akkor TestException 
dobódik. Ha egy metódus nem megfelelő teszt metódus (pl van paramétere vagy nem bool a visszatérési 
értéke) akkor az ignorálva lesz.

#Példakódok, tesztek
Példakódok és a framework tesztelése JUnit-tal az *src/test/java* mappában találhatóak.