#Unit Teszt keretrendszer

##Vállalati információs rendszerek kötelező program

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