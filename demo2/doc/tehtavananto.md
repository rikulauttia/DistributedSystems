# Tehtävänanto DEMO2

## Oppimistavoitteet
Säieturvallisuus, kriittinen alue, synkronointi (mutexit ja monitorit), säiesignalointi, kilpailutilanteet ja lukkiumatilanteet.

## Yleiset ohjeet
Demokerran tehtävät tulisi tehdä niille varattuihin kansioihin (hakemistopuussa src/main alla olevat kansiot): eli siis tämän viikon ensimmäinen harjoitus tulisi tehdä kansioon "assignment1" ja toinen harjoitus kansioon "assignment2" jne. **Tämän viikon tehtävät ovat suoritettavissa toisistaan riippumattomasti**, eli tehtävien tekojärjestyksellä ei ole merkitystä, mutta ne ovat joko haastavampia tai laajempia loppua kohden.

## Tehtävät

### Tehtävä 1 - Laskin
Ensimmäisessä tehtävässä on luotu valmiiksi kolme luokkaa: `App1`, `Count`, ja `Counter`. Kuten luokkien nimestä voi päätellä, tässä tehtävässä lasketaan lukumääriä. `main`-metodissa luodaan muuttujan `threadCount` verran `Counter`-säikeitä, jotka suoritetaan samanaikaisesti, sekä yksi `Count`-olio, joka pitää sisällänsä lukuarvon (alussa 0). `Counter`-säieolioiden elämäntehtävä on yksinkertainen: lue `Count`-olion nykyinen arvo ja lisää siihen yksi, kuole onnellisena. Mikäli ala-asteen matematiikka on hallussa, voi päätellä, että kun lukuarvoon 0 lisätään luku 1, 100 kertaa, pitäisi `Count`-olion lukuarvon olla lopuksi 100. Näin ei kuitenkaan näytä olevan. Jollain tietokoneilla ja joinakin kertoina lopullinen lukuarvo saattaa pitää paikkansa, mutta tämä ei ole mitenkään varmaa. Muokkaa ohjelmaa siten, että eri säikeistä ajettaessakin loppusumma tulee olemaan 100. 

Vinkki: Lopullinen lukuarvo tulostusta varten luetaan pääsäikeestä käsin, jolloin on tarpeen varmistaa, että myös pääsäie saa ajantasaisimman arvon. Eri säikeiden tekemät muutokset eivät välttämättä välity muille säikeille, ellei muuttujia varusteta tietyllä avainsanalla tai pääsyä arvoon kääritä joka kohdassa synkronointilohkoon...

Huom! Boksin ulkopuolelta ajatteleville tiedoksi: Ratkaisu, jossa jokainen säie asettaa arvoksi 100 ei ole hyväksyttävä ratkaisu.

### Tehtävä 2 - Lista
Tehtävässä 2 on melko lailla sama rakenne kuin tehtävässä 1. Erona on, että säikeistä (oletus 100 kpl) jokainen haluaa lisäillä samaan listaan luvun 123 muuttujan `addCount` verran (oletuksena 1000) ja täten lopuksi listassa tulisi `100*1000 = 100 000`. Näin ei kuitenkaan ole. Ratkaise ongelma jälleen siten, että samanaikaisista säikeistä riippumatta listaan tulee oikea määrä lukuja.

### Tehtävä 3 - Roboassarit
On vuosi 2100 ja assarit on korvattu automaattitarkastajilla, joita kuvaa ohjelmassamme `AutomaticGrader`-luokan säieoliot. Näille automaattitarkastajille voidaan antaa lista tarkistettavia tehtäviä (`Submission`) ja ne tarkistavat annetut tehtävät automaattisesti käyttäen samaa algoritmia kuin viime viikon demoissakin käytettiin (arvosana arvotaan). Kun tarkistus on suoritettu, lisää automaattitarkastaja arvioidun palautuksen yhteiseen `gradedSubmissions` -listaan, joka on siis **jaettu kaikkien muidenkin automaattitarkastajasäieolioiden kesken**.

`StudyRegistrar`-luokan säieolion tehtävä on puolestaan seurata automaattitarkastajien täyttämää `gradedSubmissions`-listaa uusien tarkistettujen palautusten varalta. Kun uusi tarkistettu palautus löytyy listalta, lisää `StudyRegistrar` merkinnän "opintorekisteriin" ja poistaa kyseisen palautuksen `gradedSubmissions`-listalta, kyseisen palautuksen ollessa käsitelty.

Kyseessä on siis eräänlainen tuottaja -- kuluttaja -ongelma, jossa n kappaletta arvioijia tuottaa arviointeja ja 1 kappale StudyRegistrar-säieolioita kuluttaa näitä arvioituja töitä. Olio `gradedSubmissions` toimii puskurina näiden välillä; jokaisella oliolla/säikeellä on viittaus tähän listaan.

```java
List<Submission> gradedSubmissions = new ArrayList<Submission>(30);
```

Tämä ei tietenkään toimi, sillä ensinnäkään `ArrayList` ei ole säieturvallinen ja toiseksi listan kokoa ei olla rajoitettu (ArrayListin parametri on vain *aloituskoko*), jolloin suurella määrällä automaattitarkastajia, lista täyttyy koko ajan nopeammin, mitä kirjuri ehtii opintorekisteriin kirjaamaan, kunnes tietokoneesta lopulta loppuu muisti. Tarkastajat eivät saisi siis täyttää puskuria äärettömästi. Ja vastavuoroisesti kirjurikaan ei saisi kaatua tyhjää tietorakennetta raapiessa.

Tehtäväsi on selvittää, minkälainen tietorakenne sopisi em. puskuriksi paremmin, vaihtaa  `gradedSubmission` käyttämään kyseistä tietorakennetta ja muokata tarvittavat osat ohjelmasta toimimaan uuden tietorakenteen kanssa. Tietorakenteen pitäisi olla säieturvallinen ja kokorajoitettu (ts. automaattitarkistajat jäävät odottamaan, mikäli tietorakenne on "täynnä"). Sinun ei tarvitse itse toteuttaa kyseistä tietorakennetta, **eikä** käyttää matalan tason säiemekanismeja (esim. wait ja notify).


Vinkkejä:
 - Sivulta <https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/package-summary.html> voisi löytyä sopivia tietorakenteita...
 - Myös tuottaja--kuluttaja -esimerkistä voisi ottaa oppia: <https://gitlab.utu.fi/tech/education/distributed-systems/ProducerConsumer/>


### Tehtävä 4 - Kaikkien janoisten sankari
Erinäisissä opiskelijatapahtumissa on usein tarjolla mitä erilaisimpia booleja, joiden sisältö vaihtelee boolinvalmistajan taidon sekä kaapista löytyvien raaka-aineiden mukaan. Tehtävässämme on nyt yksi boolivastaava, joka tekee booleja sitä mukaa kun opiskelijat niitä juovat. Opiskelijat ovat tehtävässämme hyvin janoisia, tai boolit hyvin pieniä koska yksi opiskelija juo aina yhden kokonaisen boolin kerrallaan. Boolivastaavan täytyy valmistaa uusi booli heti kun edellinen on nautittu loppuun sekä *ilmoittaa* boolin valmistumisesta: vuoroansa *odottavia* opiskelijoita kun ei kannata pitää odotuksessa. Boolikulhomme on tässä tehtävässä jaettu resurssi: emme haluaisi, että siitä hörpitään usean opiskelijan toimesta samanaikaisesti kun boolivastaava valmistaa siihen seuraavaa taidonnäytettään. Tämän vuoksi eri tahojen pääsy kulhoon tapahtuu vain synkronoitujen metodien kautta, mikä ratkaisee samanaikaisuuden ongelman.

Ongelmana on nyt vain se, että opiskelijat kärkkyvät kerta toisensa jälkeen kulhoa, joka saattaa olla joko täynnä boolivastaavan parhainta tai huonossa tapauksessa typösen tyhjä. Boolivastaava puolestaan ei tunnu ymmärtävän, että boolikulhossa ei saa olla vanhaa juomaa uutta satsia kaataessa -- liekö boolivastaava maistanut omia tuotoksiaan? Mikäli booliastia on tyhjä, pettyy opiskelija ja poistuu juhlapaikalta yritettyään juoda tyhjästä kulhosta. Jos taas boolivastaava kaataa täyteen kulhoon juomaa, tulvii astia syntynyttä toisen asteen boolia.

Muokkaa boolikulhon synkronoituja metodeita siten, että juomista yrittävä opiskelija odottaa kunnes booliastiassa on juomaa ja boolivastaava puolestaan ei täytä kulhoa yli äyräittensä. Ratkaisussa tulisi käyttää **vartiointilohkoja** sekä **säiesignalointia**.

### Tehtävä 5 - Progressio toiseen potenssiin
Valtio on päättänyt tasoittaa tuloeroja määräämällä lain, jossa tilisiirtoja voidaan tehdä ainoastaan tilanteessa, jossa lähdetilille jää yli 0 euroa ja kohdetilillä on siirron jälkeen maksimissaan 1000 euroa. Koska siirtoja tehdään useita samanaikaisesti samoille tileillekin, täytyy molemmat tilit (lähde ja kohde) lukita tilisiirron ajaksi. Muutenhan "katetarkistuksen" ja oikean tilisiirron välissä voisi joku toinen tilisiirtotapahtuma tehdä oman siirtonsa ja tilisiirto voisi olla laiton.

`BankTransfer`-luokan oliot vastaavat yksittäisistä tilisiirtotapahtumista. On tämän luokan vastuulla lukita molemmat tilit (joita kuvaavat luokan `Account` oliot), varmistaa tilisiirron "laillisuus" ja lopulta tehdä tilisiirto. Nykyisessä implementaatiossa on tosin jokin bugi, sillä vaikka tilisiirtoja pitäisi tapahtua jatkuvalla syötöllä, muutaman samanaikaisen tilisiirron alettua, yksikään tilisiirto ei enää mene läpi.

a. Millä nimellä tätä tilannetta kutsutaan ja miksi tilisiirrot tässä ohjelmassa hyytyvät (selosta vaikka esimerkin avulla)?
b. Pystytkö korjaamaan `BankTransfer`-luokan siten, että tilisiirrot eivät jämähdä?

Yritä keksiä tehtävään (b) ratkaisu, mutta suoritukseksi riittää, että olet tehnyt kohdan (a) ja pohtinut kohtaa (b), vaikket olisikaan löytänyt täydellistä ratkaisua.


### Tehtävä 6 - Lamppukeskitin
Tässä tehtävässä simuloidaan eräänlaista älyvalaisimien keskitinlaitetta. Tehtäväpohjassa on valmiiksi annettuna kaikki tähän tarvittavat luokat, jotka esitellään seuraavaksi:

#### Light
Kuvaa yksittäistä älyvalaisinta, jonka voi kytkeä päälle tai pois joko käskemällä suoraan tai käyttämällä toggle-metodia, joka tekee jomman kumman riippuen valaisimen nykytilasta. Valaisimelta voi myös kysyä, onko se päällä.

#### Hub
Itse valaisinkeskitin, johon kaikki valaisimet ovat liitettyinä. Kaikkia valaisimia käytetään tämän luokan kautta kutsumalla sopivaa metodia valaisimen id-numeron kanssa (tai kohdistamalla operaatio kaikkiin valaisimiin).

#### Remote
Säikeessä ajettavat Remote-oliot simuloivat kaukosäätimiä, joilla valaisimia voi ohjata. Kaukosäätimiä voi olla useita yhtä keskitintä kohden ja ne saattavat samanaikaisesti ohjata samaa keskitintä ja samoja lamppuja. Kaukosäätimet pääsevät ohjaamaan keskitintä siten, että kaukosäätimille annetaan viittaus keskitinolioon.

Tehtäväsi on tutkia annettua ohjelmaa ja tunnistaa ns. kriittiset alueet, joissa useat säikeet koskevat samanaikaisesti samoihin muistirakenteihin ja yrittää suojata näitä samanaikaisuuden aiheuttamilta ongelmilta käyttämällä tarvittaessa säieturvallisia rakenteita ja synkronointilohkoja.
