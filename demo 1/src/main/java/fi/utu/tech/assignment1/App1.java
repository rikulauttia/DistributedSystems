package fi.utu.tech.assignment1;

import java.util.List;

import fi.utu.tech.common.GradingTask;
import fi.utu.tech.common.Submission;
import fi.utu.tech.common.SubmissionGenerator;
import fi.utu.tech.common.SubmissionGenerator.Strategy;

public class App1 {
    public static void main( String[] args )
    {
        // Otetaan funktion aloitusaika talteen suoritusajan laskemista varten
        long startTime = System.currentTimeMillis();

        // Generoidaan kasa esimerkkitehtäväpalautuksia
        List<Submission> ungradedSubmissions = SubmissionGenerator.generateSubmissions(21, 200, Strategy.STATIC);

        // Tulostetaan tiedot esimerkkipalautuksista ennen arviointia
        for (var ug : ungradedSubmissions) {
            System.out.println(ug);
        }

        // Luodaan uusi gardintask olio ja annetaan sille arvioimattomat palautukset
        GradingTask gradingTask = new GradingTask(ungradedSubmissions);
        // Luodaan uusi säie ja käynnistetään se
        Thread gradingThread = new Thread(gradingTask);
        gradingThread.start();
        // Odotetaan säikeen suorituksen päättymistä
        try {
            gradingThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Haetaan arvioidut palautukset
        List<Submission> gradedSubmissions = gradingTask.getGradedSubmissions();

        // Annetaan palautukset gradeAll-metodille ja saadaan arvioidut palautukset takaisin
        // List<Submission> gradedSubmissions =  gradingTask.gradeAll(ungradedSubmissions);
        /*
         * TODO: Muokkaa common-pakkauksen GradingTask-luokkaa siten,
         * että alla oleva run()-metodi (ilman argumentteja!) tarkistaa palautukset (ungradedSubmissions).
         * Yllä olevaa gt.gradeAll()-metodia ei tule enää käyttää suoraan
         * tästä main-metodista. Tarkemmat ohjeet tehtävänannossa.
         * Joudut keksimään, miten GradingTaskille voi antaa tehtävät ja miten ne siltä saa noukittua
         */
        // gradingTask.run();

        // Tulostetaan arvioidut palautukset
        System.out.println("------------ CUT HERE ------------");
        for (var gs : gradedSubmissions) {
            System.out.println(gs);
        }

        // Lasketaan funktion suoritusaika
        System.out.printf("Total time for grading: %d ms%n", System.currentTimeMillis()-startTime);
    }
}
