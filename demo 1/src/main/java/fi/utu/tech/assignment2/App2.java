package fi.utu.tech.assignment2;

import java.util.List;

// Käytetään tehtässä 1 muokattua GradingTask-oliota
import fi.utu.tech.common.GradingTask;
import fi.utu.tech.common.Submission;
import fi.utu.tech.common.SubmissionGenerator;
import fi.utu.tech.common.SubmissionGenerator.Strategy;

public class App2 {
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

        // Luodaan uusi säie ja annetaan sille tehtäväksi GradingTaskin suorittaminen
        Thread gradingThread = new Thread(gradingTask);

        // Käynnistetään säie
        gradingThread.start();

        // Odotetaan säikeen suorituksen päättymistä
        try {
            gradingThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Submission> gradedSubmissions = gradingTask.getGradedSubmissions();

        // Tulostetaan arvioidut palautukset
        System.out.println("------------ CUT HERE ------------");
        for (var gs : gradedSubmissions) {
            System.out.println(gs);
        }

        // Lasketaan funktion suoritusaika
        System.out.printf("Total time for grading: %d ms%n", System.currentTimeMillis()-startTime);
    }
}