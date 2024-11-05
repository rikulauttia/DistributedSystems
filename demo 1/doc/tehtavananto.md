# Tehtävänanto DEMO1

## Oppimistavoiteet
Tämän viikon demotehtävien oppimistavoitteita ovat muistinvirkistys rajapintaluokista, säikeiden luonti, käynnistys sekä odotus, työtehtävien jako, rinnakkaisuuden ja samanaikaisuuden käsite, sekä tehtävien erottaminen tekijästä (vrt. tehtäväoliot ja säieoliot).

## Yleiset ohjeet
Demokerran tehtävät tulisi tehdä niille varattuihin kansioihin (hakemistopuussa src/main alla olevat kansiot): eli siis tämän viikon ensimmäinen harjoitus tulisi tehdä kansioon "assignment1" ja toinen harjoitus kansioon "assignment2" jne. Tämän viikon tehtävät rakentuvat toistensa päälle, jolloin seuraavan tehtävän App-luokan pohjana tulisi käyttää edellisen tehtävän ratkaisua. Kopioi siis aina edellisen tehtävän App-luokan `main`-metodi seuraavan tehtävän `main`-metodiin jatkaaksesi töitä. Tällä tavoin demonstraatioissa on helppo esittää eristetysti ratkaisu tiettyyn tehtävään.

## Tehtäväpohjan rakenne

Tehtävän kontekstina toimii eräänlainen simulaatio oppilaiden palauttaminen töiden tarkistamisesta. Tarkistusprosessi toimii yleensä siten, että opiskelijat palauttavat kasan palautuksia, jotka on tarpeen arvioida arvosana-asteikolla 0-5. Tehtäviä tarkistaa n kappaletta tarkastajia, joille työt täytyy jollakin tavalla *allokoida*. Toisin sanoen, kuka tarkistajista tarkistaa minkäkin tehtävän. Tehtävä pyörii *palautusten*, *tarkistustehtävien* sekä *tarkistajien* käsitteiden ympärillä.

Tehtäväpohjan pakkauksessa  `fi.tech.utu.common` on luokkia, jotka kuvaavat näitä käsitteitä. Ne ovat yhteisiä kaikkien tehtävien kesken. Osaan `common`-pakkauksen luokkia ei tule tehdä muutoksia (nämä on merkattu), mutta osaan taas täytyy. Muutokset, joita `common`-pakkauksen luokkiin tehdään, eivät ikinä korvaa aiemmassa tehtävässä tehtyjä muutoksia, vaan pelkästään lisäävät toiminnallisuuksia, jonka vuoksi niitä ei tarvitse kopioida tehtäväkohtaisiin kansioihin.

### Submission
Tehtäväpalautusta kuvaava luokka. Tehtäväpohjassa yksittäistä palautusta kuvaa luokan `Submission` oliot. Tässä yksinkertaisessa simulaatiossa olioissa ei oikeasti ole mitään "tehtäväpalautusta", vaan tehtäväpalautuksella on muutamia työn kannalta oleellisia attribuutteja:

- `String submitter` sisältää palautuksen tehneen opiskelijan nimen
- `int grade` sisältää arvosanan, joka palautukselle annetaan (oletuksena, ennen arviointia 0)
- `int difficulty` kuvastaa sitä, kunka *luova* ratkaisu opiskelijalla on. Toisin sanoen, kuinka hankalasti tarkistaja pääsee tehtäväpalautuksesta jyvälle ja kuinka kauan aikaa tehtävän tarkastaminen oikeastaan vie

Itsestäänselvien "gettereiden" lisäksi Submission-luokalla on metodi `grade(int newGrade)`, jonka avulla palautukselle voi asettaa arvosanan. On huomionarvoista, että **Submission-luokan oliot ovat mutatoimattomia (immutable)**, eli arvosanaa annettaessa alkuperäistä `Submission`-luokan oliota ei mutatoida, vaan luodaan uusi `Submission`-luokan olio, joka on arvosanaa lukuun ottamatta identtinen alkuperäisen kanssa. Tämä uusi olio palautetaan kutsujalle.

Tätä luokkaa ei tule muokata.

### SubmissionGenerator
Koska käytössämme ei ole mitään oikeaa datasettiä opiskelijoiden palautuksista ja niiden haastavuudesta tarkistajille, luodaan tätä varten testidataa `SubmissionGenerator`-luokan avulla. `SubmissionGenerator`-luokassa on käytännössä yksi julkinen staattinen metodi, jota käyttämällä saadaan lista testipalautuksia luotua (lista `Submission`-olioita). Testipalautuksille generoidaan satunnaisesti jokin palauttajan nimi.

`public static List<Submission> generateSubmissions(int amount, int difficulty, Strategy strategy)` luo palautuksia sille annettujen argumenttien pohjalta. Argumentit esitetään alla

- `int amount`: Kuinka monta testipalautusta luodaan
- `int difficulty`: Kuinka haastavia palautuksia luodaan (ts. kauanko palautusten tarkastamiseen tulee menemään aikaa). Lopulliseen yksittäisen palautuksen vaikeusasteeseen vaikuttaa niin `difficulty`-arvo kuin `strategy`-enumkin.
- `enum Strategy strategy`: Millä strategialla tarkastuksien lopullinen vaikeus määritetään.
	- `STATIC`: Kaikki luotavat palautukset saavat `difficulty`-argumentin mukaisen tarkistusvaikeusarvon
	- `LINEAR`: Luotavien palautusten vaikeusaste kasvaa lineaarisesti nollasta `difficulty`-argumentin määrittämään arvoon
	- `UNFAIR`: Luotavista palautuksista yksi on 10-kertaisesti toisia vaikeampi, muut ovat difficulty-arvon mukaan staattisia
	- `RANDOM`: Luotavien palautusten vaikeusaste on satunnainen väliltä [0, `difficulty`).

Tätä luokkaa ei tule muokata.

### GradingTask
`GradingTask`-luokan oliot kuvaavat tehtäväpalautusten tarkistustyötä ja sisältävät tarkistuksen tekemiseen tarvittavat metodit:

- `public Submission grade(Submission s)`: Arvioi metodille annetun palautuksen kutsumalla palautuksen grade-metodia annettavalla arvosanalla. On yleisesti tiedossa, että arviointiprosessiin menevä aika koostuu palautuksen vaikeudesta riippuvasta vetkuttelusta ja arvosanan arpomisesta nopalla, joten se toimii myös tämän simulaation arviointiprosessin algoritmina. Vakavammin puhuttaessa, `Thread.sleep()` on ns. simuloitua kuormaa, joka yrittää kuvastaa arviointiin menevää aikaa. Oikeissa ongelmissa algoritmin suoritusaikaan vaikuttaisi niin algoritmin CPU- kuin I/O-käyttökin. Satunnaisluvun arpominen ei vain yksinään ole erityisen raskas operaatio tietokoneelle, jonka vuoksi `Thread.sleep`iä on käytetty tässä.

Tätä luokkaa tarvitsee muokata joissakin tehtävissä

### TaskAllocator
Luokkaa tarvitsee muokata joissakin tehtävissä. Tästä tarkemmat ohjeet myöhemmin.



## Tehtävät

### Lähtökohta ja päämäärä
Lähtökohtana tehtäväpohjassa on tilanne, jossa annettu `main`-metodi (`App1`-luokassa) yksinkertaisesti luo kasan tehtäväpalautuksia annettujen parametrien mukaisesti ja tarkastuttaa tehtävät `GradingTask`-luokan `gradeAll`-metodilla ja lopulta tulostaa tiedot arvioiduista tehtävistä. Tämän lisäksi `main`-metodissa mitataan arviointiin käytetty aika ja tulostetaan tämä ohjelman suorituksen päätteeksi. Kaikki arviointi tapahtuu annetussa pohjassa yhdessä säikeessä, peräkkäin. Päämääränä tällä demokerralla on askel askeleelta muuttaa tämä yksisäikeinen sovellus monisäikeistetyksi ohjelmaksi, joka pystyy tarkistamaan tehtäviä samanaikaisesti ja rinnakkain. Huomioi, että tämä askel askeleelta -lähestymistapa tarkoittaa, että useassa tilanteessa tehtävät riippuvat edellisistä tehtävistä ja tehtävät tulee tämän vuoksi tehdä järjestyksessä, ellei toisin ilmoiteta.

### Tehtävä 1 - Tehtävien määrittely
Kuten oppimateriaalista on toivon mukaan opittu, Javassa säieabstraktiona toimivat luokan `Thread` oliot. Toisin sanoen `Thread`-luokan tarjoamin metodein on mahdollista luoda uusi säie, jossa Java-koodia ajetaan. Se, mitä koodia ajetaan, riippuu puolestaan siitä, mikä *tehtävä* säieoliolle annetaan tehtäväksi. Tämä tehtävä on mahdollista määrittää kahdella tavalla: joko *perimällä* `Thread`-luokka ja ylikirjoittamalla tämän `run`-metodi tai määrittämällä oma tehtäväluokka, joka *implementoi* `Runnable`-rajapinnan. Yleisesti ottaen näistä `Runnable`-rajapinnan käyttö on suositeltavaa, koska tämä ei sido työtehtävää osaksi `Thread`-luokkaa.

Koska tavoitteenamme on säikeistää palautusten arviointi, ensimmäinen askel onkin siis muokata **common**-pakkauksen `GradingTask`-luokkaa implementoimaan `Runnable`-rajapinta (välittämättä vielä säikeistyksestä sen enempää). Muokkaa `GradingTask`-luokkaa siten, että `Runnable`-rajapinnan vaatiman `run`-metodin kutsuminen `App1`-luokan `main`-metodista tarkistuttaa listan palautuksia. Huomaa, että `Runnable`-rajapinnan vaatiman `run`-metodin signatuuri on `public void run()`, joka tarkoittaa, että `run`-metodi ei ota mitään parametreja vastaan, eikä myöskään palauta mitään (voit myös miettiä, miksi näin on). Arvioitavat palautukset täytyy siis välittää `GradingTask`:ille/ilta jollakin muulla tavoin.

Tehtävä 1 on ratkaistu todennäköisesti oikein silloin, kun `main`-metodissa ei viitata suoraan `gradeAll` tai `grade` -metodeihin, mutta arvioidun listan tulostaminen `main`-metodista käsin on edelleen mahdollista. **Älä** hoida arvioitujen palautusten tulostusta suoraan `GradingTask`-luokasta käsin.

### Tehtävä 2 - Ulkoistettua arviointia
`Runnable`-rajapinnan implementointi ja `run`-metodin kutsuminen ei yksistään riitä koodin ajamiseksi erillisessä säikeessä -- `Runnable`-rajapintahan vain määrittää, että luokassa tulee olla metodi `public void run()` ja kyseisen metodin kutsuminen ei eroa mitenkään minkä tahansa muun metodin kutsumisesta. Toisin sanoen, mitään tutusta poikkeavaa mustaa magiaa ei ole vielä tapahtunut.

Jotta `run`-metodissa määritelty metodi ajettaisiin erillisessä säikeessä, täytyy käyttää hyväksi `Thread`-luokan tarjoamia toiminnallisuuksia. Selvitä, miten `Thread`-luokka voi suorittaa `Runnable`-tyyppisiä olioita erillisessä säikeessä ja toteuta tarvittavat toimenpiteet, jotta saat `GradingTask`in arviointiprosessin ajettua erillisessä säikeessä.

Huom! Oikein tehtynä ohjelma saattaa heittää poikkeuksia (kuten NullPointerException) tai palauttaa keskeneräisiä palautuslistoja. Tämä on maistiainen ns. jaeutun tilan aiheuttamista ongelmista, joita käsitellään tarkemmin seuraavan kerran demoissa. Tämä kyseinen ongelma ratkotaan tosin jo seuraavassa tehtävässä. Voit kuitenkin yrittää miettiä syytä tähän hetken aikaa itse, ennen siirtymistä seuraavaan tehtävään.

### Tehtävä 3 - Wait a sec
Kuten edellisessä tehtävässä todettiin, saattaa säikeistetty versio ohjelmasta heittää poikkeuksia ja muutenkin voi toimia virheellisesti. Poikkeuksen aiheuttaa se, että kun tarkistustehtäväsäie käynnistetään, jatkaa main-metodi suoritustaan samanaikaisesti (tai rinnakkain). Main-metodi siis antaa tarkastajasäikeelle tehtäväksi tarkistaa listan palautuksia, "polkaisee" säikeen käyntiin ja saman tien alkaa kaivella palautettuja töitä. Kun töitä tai koko valmiiden töiden listaa ei ole saatavilla, kaatuu ohjelma poikkeukseen.

Muuta main-metodia siten, että pääsäie ei yritä kajota arvioituhin palautuksiin ennen kuin arvioijasäie on saattanut arvioinnin päätökseen. Eli toisin sanoen, laita pääsäie odottamaan arviointisäikeen valmistumista. Huom! Vältä ns. *busy waiting* -tapaa.

Kun olet saanut tehtävän valmiiksi, vertaile suoritusaikaa alkuperäiseen yksisäikeiseen toteutukseen - ajat lienevät melko lailla samoissa kokoluokissa. Miksi näin on? Säikeistyksen yksi päätarkoituksistahan oli rinnakkaisuuden avulla parantaa suoritusaikoja? Spoilerit jälleen seuraavan tehtävän alussa.

### Tehtävä 4 - Kaverille kanssa
Ohjelman kompleksisuus on kasvanut: käytetään säikeitä sekä vältellään jaettujen resurssien aiheuttamia ongelmia, mutta suoritusajat eivät kuitenkaan ole parantuneet. Mikäli prosessin elinkaarta tarkastellaan, huomataan että pääsäie käytännössä ulkoistaa tarkistuksen erilliselle säikeelle, mutta tarkistuksen ajan pääsäie lähinnä nukkuu. Toisin sanoen vain yksi säie tekee kerrallansa töitä, eikä tarkistus ole nopeutunut pätkääkään. Mikäli tarkistusta halutaan nopeuttaa, tulee tarkistussäikeiden määrää lisätä ja tarkistustehtävät jakaa näiden kesken jollakin tavalla.

Tässä tehtävässä olisi tarkoitus toteuttaa yksinkertainen tehtävänjakoalgoritmi. Pakkauksessa `common` on luokka nimeltään `TaskAllocator`, jonka sisässä on runko metodille `public static List<GradingTask> sloppyAllocator(List<Submission> submissions)`. Toteuta kyseinen metodi siten, että annettaessa lista palautuksia (List<Submission> submissions), palauttaa metodi listassa kaksi `GradingTask`-oliota, joilla kummallakin on noin puolet tarkastettavista tehtävistä.

Itse tehtäväkohtaisessa `main`-metodissa, päivitä metodia siten, että allokoijalta saadut **kaikki** kaksi tehtävää jaetaan erillisille säikeille, **kaikki** säikeet käynnistetään ja **kaikkia** säikeitä odotetaan valmistumaan. Säikeiden valmistuttua, kerää **kaikki** arvioidut palautukset **kaikista** tehtävistä. Vaikka toistaiseksi allokointimetodi tuottaa vain kaksi `GradingTask`-oliota (ja täten tarvitaan vain kaksi säiettä), toteuta em. main-metodin toimet siten, että et nojaa missään kohtaa siihen, että tehtäviä tai säikeitä on kaksi, vaan käytä perustana esimerkiksi allokointimetodilta saamasi tehtävälistan (`List<GradingTask>`) kokoa.

### Tehtävä 5 - Joukkovoimaa
Aiemman tehtävän allokointimetodi oltiin kovakoodattu jakamaan palautuslista aina kahteen osaan. Toteuta `TaskAllocator`-luokan sisään metodi `public static List<GradingTask> allocate(List<Submission> submissions, int taskCount)`, siten, että se pystyy jakamaan annetut palautukset `taskCount`-muuttujan määrittämään määrään osia. Eli esimerkiksi mikäli `submissions`-listassa on 20 palautusta ja `taskCount` olisi 4, tulisi metodin palauttaa listassa 4 `GradingTask`-oliota, jolla jokaisella on 5 palausta tarkistettavanaan. Vaihda tehtäväkohtainen `main`-metodi käyttämään uutta allokointimetodia ja tarkastele, miten tehtävämäärä vaikuttaa suoritusaikaan. Saatat huomata, että suoritusaika pienenee melko lailla, vaikka säikeitä käytettäisiin enemmän kuin koneessasi on prosessoriytimiä. Pohdi, mitä syitä tälle voisi olla.

Vinkki: Mikäli et keksi, miten allokointimetodin voisi toteuttaa, etsi tietoa esimerkiksi [Round-robin item allocation](https://en.wikipedia.org/wiki/Round-robin_item_allocation) -hakusanalla. Vaikka osa selityksistä voi vaikuttaa hankalilta, algoritmi on käytännössä jo ala-asteelta tuttu ryhmittäytymistapa, jossa jaettaessa oppilaat esimerkiksi neljään ryhmään, jokainen huutaa vuorollansa 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, ... jne. Tässä kohtaa pitää vain korvata oppilaat tehtäväpalautusolioilla.

### Tehtävä 6 - Dynaamiset kaverit

Aiemmissa tehtävissä työnjako on ollut staattista. Tämä tarkoittaa, että palautustyöt on jaettu ennakkoon määrätyn kokoisiin työtehtäviin. Esimerkiksi 20 palautusta on saatettu jakaa neljän tarkistustehtävän kesken siten, että jokainen tehtävä sisältää viisi palautuksentarkistusta. Tämä toimii hyvin, mikäli kaikki tehtävät ovat yhtä haastavia. Mutta entä mikäli tehtävien koko on tuntematon tai esimerkiksi jollekin säikeelle sattuu tulemaan useampia vaativampia tehtäviä? Tällöin vaativat tehtävät saanut säie painii töiden kanssa yksin muiden säikeiden ollessa jo kauan kuolleita. Mikäli työtehtävät voitaisiin jakaa säikeille sen mukaan kuin säikeet saavat aiemman työtehtävän valmiiksi, saataisiin hyötysuhdetta parannettua. Tällaista mallia kutsutaan dynaamiseksi työnjaoksi.

Luennolla sivuttiin Javan `ExecutorService`-rajapintaa. Kyseisen rajapinnan implementoivat luokat mahdollistavat dynaamisen työnjaon implementoinnin suhteellisen vaivattomasti. Lyhyesti idea ExecutorServicessä on, että rajapinnan olioiden `execute` metodille voidaan antaa `Runnable`-olioita ja ExecutorService hoitaa näiden suorittamisen tietyllä tavalla. Se, millä tavalla Runnable-oliot ajetaan, riippuu ExecuterServicen implementoivasta luokasta. Esimerkiksi FixedThreadPool-implementaatiolla on mahdollista määrittää samanaikaisten säikeiden määrä. Kun oliolle lähetetään `execute`-metodin avulla `Runnable`-olioita, odottavat oliot jonossa, kunnes uusi säie vapautuu ja tällöin tehtävä suoritetaan. `Executors`-luokka sisältää tehdasmetodeja erilaisten `ExecutorService`-olioiden luontiin.

Toteuta tehtävientarkistaja `FixedThreadPool`:ia käyttäen. Dynaamista työnjakoa käyttäessä töitä ei kannata jakaa liian isoissa könteissä. Esimerkiksi 1-2 työtä per työtehtävä voi olla riittävä määrä. Huomaa, että `ExecutorService`:n API-dokumentaatiosivulla on esimerkkejä ExecutorSericen valmistumisen odottamista varten (saattaa tulla tarpeeseen).

- ExecutorService-rajapinta: <https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ExecutorService.html>
- Executors-luokka, joka sisältää tehdasmetodit erityyppisten ExecutorService-olioiden luontiin: <https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/Executors.html>

**Vinkki**: Tehtävä 6:en voi suorittaa ilman, että tehtävän 4 tai 5 alloikoijia on toteutettu. Tällöin "allokointi" voidaan toteuttaa siten, että jokaista `Submission`-oliota kohden luodaan yksi `GradingTask`-olio. Tällaisen allokoijan teko pitäisi olla yksinkertaista. Mikäli tehtävä 5 on toteutettu, voit käyttää sitä allokointiin suoraan.


