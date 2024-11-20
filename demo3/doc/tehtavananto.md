# Tehtävänanto DEMO3

## Oppimistavoiteet
Tämän viikon demotehtävien oppimistavoitteita ovat palvelinkomponentin sekä siihen yhdistävän asiakasohjelman luominen, eri tietovirtojen tarjoamat abstraktiot, TCP:n "tietovirranomaisen" luonteen ymmärtäminen, alkeellisen sovellusprotokollan rakentaminen, sekä säikeiden käyttö osana verkkosovellusta.

## Yleiset ohjeet
Demokerran tehtävät tulisi tehdä niille varattuihin kansioihin (hakemistopuussa src/main alla olevat kansiot): eli siis tämän viikon ensimmäinen harjoitus tulisi tehdä kansioon "assignment1" ja toinen harjoitus kansioon "assignment2" jne. Tehtävät rakentuvat toistensa päälle, jolloin seuraavan tehtävän pohjana tulisi käyttää edellisen tehtävän ratkaisua. Kopioi siis aina edellisen tehtävän oleelliset luokat seuraavan tehtävän luokiksi jatkaaksesi töitä. Tällä tavoin demonstraatioissa on helppo esittää eristetysti ratkaisu tiettyyn tehtävään.

## Tehtävät

### Tehtävä 1 - Ensin oli palvelin, sitten asiakas
Kirjoita Server-luokkaan TCP-soketteihin pohjautuva palvelinohjelma, joka odottaa asiakkaan yhdistämistä. Aseta palvelin kuuntelemaan yhteydenottopyyntöjä vapaavalintaisessa TCP-portissa, kunhan muistat, että suurin mahdollinen porttinumero on 65535 ja porttinumeroita ei kannata valita numeroista alle 1024, koska näiden käyttö saattaa vaatia pääkäyttäjän oikeuksia käyttöjärjestelmältä.

Toteuta myös Client-luokkaan asiakasohjelma, joka pystyy yhdistämään em. palvelimeen. Voit yhdistäessä käyttää takaisinkytkentä, eli loopback-osoitetta ("localhost", 127.0.0.1).

Palvelimen ei tarvitse tukea tässä kohtaa useita samanaikaisia asiakkaita, eikä asiakkaan ja palvelimen tarvitse vielä tässä vaiheessa lähettää toisilleen viestejä. Riittää, että palvelin odottaa asiakkaan yhteydenottoa ja lopettaa toimintansa kun asiakas on yhdistänyt. Asiakkaan puolelta puolestaan riittää, että asiakas yhdistää palvelimeen.

### Tehtävä 2 - Ensimmäinen yhteys
Jatka kopioimalla tehtävän 1 ratkaisu tehtävän 2 vastaaviin tiedostoihin ja jatka työskentelyä tässä kansiossa. Ole tarkkana, että muokkaat oikean tehtävän tiedostoja, sillä luokat ovat samannimisiä eri paketeissa!

Tässä tehtävässä tarkoituksesi olisi välittää ensimmäinen viesti asiakkaalta palvelimelle. Toteuta toiminnallisuus, jossa asiakkaan yhdistettyä palvelimeen, asiakas lähettää merkkijonon "Hello" (tai muun vastaavan) palvelimelle ja palvelin tulostaa vastaanotetun viestin sisällön terminaaliin. Toteuta toiminnallisuus käyttäen suoraan Socket-olioilta saatavia Input- ja OutputStream-oliota **ilman**, että käytät tässä kohtaa vielä muita tietovirtaluokkia apuna (ts. älä käytä BufferedWriteriä ja Readeria tai muita vastaavia kaveruksia).

Vinkkejä tehtävän tekoon:

- String-tyyppinen olio on mahdollista muuntaa tavutauluksi (`byte[]`) ja siitä takaisin käyttäen String-luokan metodeja. API-dokumentaatio: <https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/String.html>.
	- Lisätehtävä: Mieti, miksi joudut muuttamaan String-olion tavutaulukoksi - miten "tavutaulu" eroaa String-oliosta? Eikö muistissa oleva data ole lopulta aina tavuja?

- Tavujen kirjoitus tavuvirtaan: <https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/io/OutputStream.html> (`write(byte[] b)`)
- Tavujen luku tavuvirrasta: <https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/io/InputStream.html>. Voit tässä tehtävässä lukea "kaikki loput" tavut (`readAllBytes`-metodi), vaikka sitä ei yleensä soketeissa käytetä.

HUOM! `readAllBytes` odottaa ns. EOF (End of File) -signaalia luettavalta soketilta. Mikäli tietovirtoja ei suljeta oikein, saattaa ohjelma päättyä virheeseen ennen kuin EOF:iin on sokettia lukiessa törmätty. TL;DR: Käytä joko try-with-resources-rakennetta tiedostovirroissa tai kutsu input- ja output-streamien `close()`-metodia manuaalisesti luvun ja kirjoituksen jälkeen.


### Tehtävä 3 - Asiakaspalvelija
Edellisessä tehtävässä yhdenkin asiakkaan otettua yhteys palvelimeen, palvelin ei enää suostu ottamaan muita asiakkaita vastaan. Tämä johtuu siitä, että `accept`-metodia ei kutsuta kuin kerran ohjelman suorituksen aikana. Kun asiakas sulkee yhteyden, sulkeutuu myös palvelin.

Kopioi tehtävän 2 ratkaisusi tähän tehtävään ja jatka sen pohjalta. Muuta palvelinohjelmaa siten, että palvelin:
1. Suostuu vastaamaan useammalle asiakkaalle kuin yhdelle ohjelman suorituksen aikana (ts. accept-metodia **ei** kutsuttaisi vain kerran)
2. Voisi (ainakin teoriassa) vastata useammalle kuin yhdelle asiakkaalle **samanaikaisesti** - palvelin siis jatkaisi uuden asiakkaan yhdistetyä muiden uusien asiakkaiden yhteydenottojen kuuntelua

Tehtäväpohjassa on nyt yksi luokkatiedosto lisää - `ClientHandler`. Toteuta/siirrä asiakaspalvelutoiminnallisuus tähän luokkaan ja muokkaa `Server`-luokkaa siten, että se ei enää yksinään palvelele asiakkaita, vaan delegoi asiakaspalvelun `ClientHandler`-luokan säikeille.

### Tehtävä 4 - Monologista dialogiksi
Tällä hetkellä keskustelu on melkolailla yksipuolista - asiakas lähettää viestinsä ja lyö luurin palvelimen "asiakaspalvelijan" korvaan. Palvelin lukee viestin heti kun asiakas on katkaissut yhteytensä (ainakin mikäli käytit tehtävässä 2 ja 3 `readAllBytes`-metodia). Seuraavaksi tarkoitus olisi tehdä kommunikaatiosta kaksisuuntaista.

Aloita taas kopioimalla tehtävän 3 ratkaisusi tähän tehtävään ja käytä sitä pohjana ratkaisussa. Mikäli et ole tehnyt tehtävää 3, voit myös toteuttaa tämän tehtävän tehtävän 2 päälle, mutta tehtävä 6 tulee vaatimaan tehtävän 3 suoritusta. Huomioi, että tässä tehtävässä saat, ja on oikeastaan suositeltavaakin, käyttää korkeamman tason tietovirta-abstraktioita, etkä voi enää turvautua `readAllBytes`-metodiin. Tutustu luokkiin `InputStreamReader`, `OutputStreamWriter`, `BufferedReader`, `BufferedWriter` ja `PrintWriter` tehtävää tehdessäsi.

Tehtävän ollessa valmis, yhdistämisen jälkeen palvelimen "asiakaspalvelijoiden" tulisi noudattaa seuraavaa logiikkaa:
- Kun palvelimen asikaspalvelija vastaanottaa asiakkaan lähettämän merkkijonon, tulosta se komentoriville
- Mikäli viesti on merkkijono "Hello", tulee palvelimen lähettää asiakkaalle takaisin merkkijono "Ack".
- Mikäli viesti on puolestaan "quit", pitää palvelimen sulkea yhteys.
- Ei saa olettaa, että viesti "Hello" lähetetään vain kerran. Mikäli sama asiakas lähettää viestin "Hello" 5 kertaa, palvelimen tulee lähettää myös vastaus "Ack" joka kerta takaisin.

Asiakas on puolestaan hieman palvelinta yksinkertaisempi: Yhdistämisen jälkeen asiakas lähettää viestin "Hello" palvelimelle ja jää odottamaan palvelimen vastausta. Kun vastaus saadaan, asiakas tulostaa komentoriville "Varmistus saatu" ja lähettää palvelimelle viestin "quit". Asiakasohjelman suoritus voi tämän jälkeen päättyä.

Vinkkejä:
- Kun lähetät viestejä, ota huomioon, että vastaanottajan täytyy tietää milloin viestisi päättyy. TCP on tietovirta, jossa tavuja kulkee osapuolelta toiselle, eikä vastaanottaja voi tietää, mihin yksi viesti päättyy ja toinen alkaa, ellet tätä ota huomioon. Älä kuitenkaan sulje TCP-yhteyttä joka viestin jälkeen -- ainoastaan quit-komento saa päättää yhteyden
	- Eräs tapa on erotella viestit rivinvaihdolla (PrintWriter ja BufferedReader yhdessä toimivat hyvin, sillä näistä löytyy valmiit metodit "rivi kerrallaan" lukemiselle)
	- Eli esimerkiksi kun lähetetään viesti "Hello" ja rivinvaihto, vastaanottava osapuoli tietää, että rivinvaihdon jälkeen viesti on kokonaisuudessaan saapunut ja se voidaan tulkita

Yleisiä ongelmia ja ratkaisuja:
- Viesti jää makaamaan lähettäjän puskuriin, eikä vastaanottaja saa viestiä: `flush()` pakottaa viestin lähtemään.
- Viestejä vastaanottaessa viestin "komennon" samuus tarkistetaan väärin (vrt. `==` ja `equals`)
- Lähetetty viesti on kirjoitettu eri kokoisilla kirjaimilla kuin komento, johon sitä verrataan (quit vs Quit)

Lisätehtävä: Pohdi, voiko rivivaihdon käyttämisestä "erottajana" tulla ongelmia? Onko rivinvaihto yksiselitteinen eri järjestelmissä? Entä jos lähetettävä viesti itsessään sisältäisi rivivaihdon?

### Tehtävä 5 - Asiakas on aina oikeassa
Kopioi tehtävän 4 ratkaisu tehtävän 5 pohjalle ja aloita tästä. Tehtävässä 5 voidaan unohtaa edellisen tehtävän "Hello" yms. viestit ja aloittaa tämän "sovellusprotokollan" osalta melko puhtaalta pöydältä. Muuta palvelimen asiakaspalvelijoiden koodia siten, että ne tunnistavat viestit, jotka noudattavat muotoa

```
LIGHT;KOMENTO;[ID]<rivinvaihto>
```

jossa hakasulkeet ID ympärillä tarkoittavat, että ID on vapaaehtoinen komennosta riippuen (viestissä ei siis hakasulkeita tule lähettää). Komentovaihtoehdot ovat `ON`, `OFF` ja `QUERY`. Vastaanottaessa rivinvaihdon, palvelin tietää, että viesti on vastaanotettu ja voidaan jäsentää (l. "parsia"). Viestit voisivat näyttää esimerkiksi seuraavilta:

```
LIGHT;ON;3
LIGHT;OFF;4
LIGHT;QUERY
```

Tässä kohtaa tehtävää riittää, että tunnistaessaan komennon, palvelin tulostaa konsoliin esimerkiksi "Kytketään lamppu 5 POIS", "Kyselykomento vastaanotettu" jne.

Asiakkaan rooli on toimia lähinnä testausapuna. Laita asiakas lähettämään eri komentoja (vaikkapa kovakoodaamalla lähetettäviä merkkijonoja) ja testaa siten palvelimen toiminta. Palvelimen ei tämän tehtävän kontekstissa tarvitse olla erityisen vikasietoinen (esim. ei tarvitse pohtia tilanteita, jossa asiakas lähettää täyttä roskaa).

Vinkki:
Voit käyttää apunasi esimerkiksi String-luokan `split`-metodia ja Integer-luokan `parseInt`-metodia. Toki voit hoitaa jäsennyksen muillakin tavoin.


### Tehtävä 6 - Viimeinen sammuttaa valot
Aloita tehtävä 6 kopioimalla tehtävän 5 ratkaisu. Mikäli ohitit tehtävän 3, täytyy sinun säikeistää palvelimesi viimeistään tässä vaiheessa. Tehtävään 5 verrattuna tässä tehtäväpohjassa tulee mukana kaksi jo entuudestaan tuttua luokkaa - `Hub` ja `Light`. Nämä luokat ovat aavistuksen yksinkertaistettuja versioita viime viikon demotehtävien vastaavista luokista. Luokat on tosin tässä kohtaa valmiiksi säieturvallisia, joten säieturvallisuuden suhteen tällä viikolla ei pitäisi olla tarve tehdä mitään.

Tavoitteena on luoda palvelinsovellus, joka ohjaa "valoja" asiakkaiden lähettämäen komentojen perusteella. Kaikki palvelimeen yhdistetyt valot ovat hallittavissa yhden ja saman `Hub`-luokasta luodun olion avulla. Tämän tehtävän mukana tuleva Hub-luokka luo itselleen automaattisesti viisi valoa (id-numeroilla 0-4) ja käynnistää säikeen, joka tulostaa valojen senhetkisen tilan joka 100. millisekunti. 

Tarkoitus on nyt muokata palvelinta siten, että palvelimen käynnistyessä luodaan Hubi, jota kaikki asiakaspalvelijat voivat yhdessä käyttää. Toisin sanoen palvelimen elinkaaren tulisi olla seuraavanlainen:

1. Palvelinsovellus käynnistetään
2. Luodaan uusi `Hub`-olio (tämä luo automaattisesti luotavaan hubiin 5 lamppua ja käynnistää monitorisäikeen)
3. Aletaan odottamaan asiakkaita
4. Asiakkaan yhdistäessä, luodaan uusi asiakaspalvelija, jolle annetaan pääsy aiemmin luotuun hubiin (käytännössä viittaus hub-olioon)
5. Asiakkaan lähettäessä tehtävän 5 mukaisia komentoja, tulee asiakaspalvelijan kutsua vastaavia komentoja jaetusta hubista

Palvelimen ei tarvitse enää tässä kohtaa tulostaa vastaanotettuja komentoja, sillä Hubin monitorisäie tulostaa valaisimien tilan terminaaliin automaattisesti. Tämän tehtävän kontekstissa ei ole tarve käsitellä poikkeuksia esimerkiksi tilanteessa, jossa asiakas lähettää roskaa tai yrittää kontrolloida olematonta lamppua jne.

QUERY-komennon saadessaan, palvelimen tulisi lähettää asiakkaalle viesti, joka on muotoa `ID1:STATUS;ID2:STATUS;...` eli esimerkiksi `1:OFF;2:ON;` jne. Voit käyttää tähän Hubin `getLights()`-metodia apuna.

Asiakkaan rooli on jälleen testauspainotteinen. Voit laittaa asiakkaan säätämään lamppuja miten itse tykkäät.
