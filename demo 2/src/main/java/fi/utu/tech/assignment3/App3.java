package fi.utu.tech.assignment3;

import java.util.ArrayList;
import java.util.List;

import fi.utu.tech.assignment3.SubmissionGenerator.Strategy;


public class App3 {

    public static void main(String[] args) {
        // Luodaan yhteinen lista, johon automaattitarkastajat lisäilevät tarkistettuja tehtäviä
        // ja josta studyRegistrar niitä lukee
        List<Submission> gradedSubmissions = new ArrayList<Submission>(30);
        
        // Luodaan 50 automaattitarkistajaa, jolle jokaiselle annetaan 20 tehtävää tarkistettavaksi
        // Jokainen automaattitarkastaja saa viittauksen samaan gradedSubmissions-listaan, johon tarkistetut palautukset lisätään
        List<AutomaticGrader> autograders = new ArrayList<>();
        for (int i=0; i<50; i++) {
            var ungradedSubmissions = SubmissionGenerator.generateSubmissions(20, 2000, Strategy.RANDOM);
            autograders.add(new AutomaticGrader(ungradedSubmissions, gradedSubmissions));
        }

        // Luodaan lista kuvaamaan "opintorekisteriä"
        List<StudyRecord> studyRegistry = new ArrayList<>();
        // Luodaan opintorekisteriin suorituksia kirjaava säieolio ja annetaan sille viittaus arvioituihin töihin, sekä "opintorekisteriin"
        // kerrotaan myös, minkä kurssin alle merkinnät pitäisi tehdä
        StudyRegistrar studyRegistrar = new StudyRegistrar(gradedSubmissions, studyRegistry, "DTEK0095");
        
        // Käynnistetään opintorekisterin kirjaaja
        studyRegistrar.start();

        // Käynnistetään palautusten arvioijat
        for (var grader : autograders) {
            grader.start();
        }

        // Odotetaan, että kaikki automaattitarkistajat ovat saaneet työt tarkistettua
        for (var grader : autograders) {
            try {
                grader.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Informoidaan kirjuria, että uusia palautuksia ei pitäisi enää tulla
        // Huomaa, että jossain tapauksissa kirjuri ei välttämättä kerkeä käsittelemään
        // kaikkia jonoon lisättyjä töitä ennen interruptia.
        // Tätä ongelmaa ei tarvitse kuitenkaan työssä ratkaista, mutta voit ottaa sen
        // vapaaehtoiseksi lisätehtäväksi
        studyRegistrar.interrupt();
    }
}
