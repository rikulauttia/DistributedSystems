package fi.utu.tech.assignment3;

import java.util.List;

// Käytetään tehtässä 1 muokattua GradingTask-oliota
import fi.utu.tech.common.GradingTask;
import fi.utu.tech.common.Submission;
import fi.utu.tech.common.SubmissionGenerator;
import fi.utu.tech.common.SubmissionGenerator.Strategy;

public class App3 {
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

        //  luodaan uusi GradingTask-olio ja sille annetaan arvioimattomat palautukset (eli ungradedSubmissions-lista).
        GradingTask gradingTask = new GradingTask(ungradedSubmissions);
        // Tässä kutsutaan GradingTask-luokan konstruktoria, joka ottaa vastaan listan Submission-olioita
        // (eli arvioitavat palautukset). Tämä konstruktori tallentaa annetut palautukset GradingTask-olion
        // sisäiseen submissions-muuttujaan, jota GradingTask käyttää arvioinnin suorittamiseen run()-metodissa.
        // Näin run()-metodi voi viitata submissions-muuttujaan suoraan, eikä palautuksia tarvitse välittää run()-metodille parametrina.
        // Runnable-rajapinnan run()-metodilla ei ole parametreja, joten ainoa tapa välittää arvioitavat palautukset
        // run()-metodille on tallentaa ne GradingTask-olion sisäiseen muuttujaan (submissions).

        // Luodaan uusi säie ja annetaan sille tehtäväksi GradingTaskin suorittaminen
        Thread gradingThread = new Thread(gradingTask);

        // Käynnistetään säie
        gradingThread.start();

        // Odotetaan säikeen suorituksen päättymistä
        // join()-metodi pysäyttää kutsuvan säikeen
        // (tässä tapauksessa pääsäikeen) suorittamisen, kunnes gradingThread-säie on suorittanut työnsä loppuun.
        // printStackTrace()-metodi tulostaa tietoa poikkeuksen syystä ja sijainnista ohjelman suorituksessa.
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
