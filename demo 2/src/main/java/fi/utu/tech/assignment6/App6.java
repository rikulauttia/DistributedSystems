package fi.utu.tech.assignment6;

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
