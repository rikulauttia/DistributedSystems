package fi.utu.tech.assignment6;


//Tässä tehtävässä useat säikeet voivat käyttää samaa Hub-oliota ja muokata valojen tilaa.
// Tehty tarvittavat muutokset, jotta Hub- ja Light-luokat ovat säieturvallisia.
// Pääasiassa synkronoimme Hub-luokan valojen hallintatoimintoja ja Light-luokan tilaoperaatioita, jotta ne eivät häiritse
// toistensa toimintaa.

public class App6 {

    public static void main(String[] args) {
        // Kuinka monta lamppua luodaan alkutilanteeseen
        int initialLights = 40;
        // Kuinka monta kaukosäädintä luodaan ja käynnistetään
        int remoteAmount = 5;
        System.out.println("Press Ctrl+C to terminate application");
        Hub hub = new Hub();
        for (int i=0; i<initialLights; i++) {
            hub.addLight();
        }
        for (int i=0; i<remoteAmount; i++) {
            Remote r = new Remote(hub);
            // Kaukosäätimet tapetaan kun yhtäkään ei-demonisäiettä ei ole ajossa
            r.setDaemon(true);
            r.start();
        }
        // Keskitintä voidaan ajaa yhtä hyvin pääsäikeessä,
        // koska sillä ole tässä kohtaa muutakaan tekemistä
        hub.run();
    }
    
}
