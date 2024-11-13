package fi.utu.tech.assignment4;

//valmistaBooli-metodissa: Käytetään while-silmukkaa tarkistamaan, onko boolia jo valmistettu (booliValmis).
// Jos booli on valmis, boolivastaava jää odottamaan (wait()), kunnes opiskelija tyhjentää kulhon.

// juoBooli-metodissa: Opiskelija tarkistaa, onko boolia valmiina kulhossa.
// Jos boolia ei ole (!booliValmis), opiskelija jää odottamaan (wait()), kunnes boolivastaava täyttää kulhon uudella boolilla.

//Kun booli valmistuu valmistaBooli-metodissa, kutsutaan notifyAll(), mikä herättää odottavat opiskelijasäikeet.
//Kun opiskelija juo boolin, juoBooli-metodi kutsuu notifyAll(), herättäen boolivastaavan, jotta uusi booli voidaan valmistaa.



public class App4 {

    public static void main(String[] args) {
        Boolikulho kulho = new Boolikulho();

        // Käynnistetään säikeet
        new Boolivastaava(kulho).start();
        for (int i=0; i<Boolivastaava.booliReseptit.length; i++) {
            new Opiskelija(kulho).start();
        }
    }

}


class Boolikulho {

    private String booli;
    private boolean booliValmis = false;

    public synchronized void valmistaBooli(String boolinNimi) {
        // Boolivastaava odottaa, jos booli on jo valmis eikä sitä ole vielä juotu
        while (booliValmis) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Boolivastaava keskeytettiin boolia valmistaessa");
            }
        }
        // Boolivastaava kaataa uuden boolin kulhoon
        this.booli = boolinNimi;
        booliValmis = true;
        System.out.println("Booli valmis: " + booli);
        // Ilmoitetaan opiskelijoille, että booli on valmis
        notifyAll();
    }

    public synchronized void juoBooli(String juoja) {
        while (!booliValmis) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(juoja + " keskeytettiin boolia juodessa");
            }
        }

        System.out.println(juoja + " nautti boolin " + booli);
        booliValmis = false; // Kulho tyhjennetään
        // Ilmoitetaan boolivastaavalle, että kulho on nyt tyhjä ja uusi booli voidaan valmistaa
        notifyAll();
    }

}

// Alla oleviin luokkiin ei tule tai tarvitse koskea

class Opiskelija extends Thread {

    private Boolikulho kulho;
    private String nimi;

    public Opiskelija(Boolikulho kulho) {
        this.kulho = kulho;
        this.nimi = NameGenerator.nextName();
    }

    @Override
    public void run() {
        kulho.juoBooli(this.nimi);
    }

}

class Boolivastaava extends Thread {

    private Boolikulho kulho;
    public static String[] booliReseptit = { "Marjabooli", "Simabooli", "Mehukattibooli", "Boolibooli", "Gambinabooli" };


    public Boolivastaava(Boolikulho kulho) {
        this.kulho = kulho;
    }

    @Override
    public void run() {
        for (String boolinNimi : booliReseptit) {
            kulho.valmistaBooli(boolinNimi);
        }
    };
}

