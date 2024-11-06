package fi.utu.tech.assignment6;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// Käytetään tehtässä 1 muokattua GradingTask-oliota
import fi.utu.tech.common.GradingTask;
// Allokointifunktiot implementoidaan TaskAllocator-luokassa
import fi.utu.tech.common.TaskAllocator;

import fi.utu.tech.common.Submission;
import fi.utu.tech.common.SubmissionGenerator;
import fi.utu.tech.common.SubmissionGenerator.Strategy;

public class App6 {
    public static void main(String[] args) {
        // Generoidaan kasa esimerkkitehtäväpalautuksia
        List<Submission> ungradedSubmissions = SubmissionGenerator.generateSubmissions(21, 200, Strategy.UNFAIR);

        // Kopioi edellisen tehtävän ratkaisu tähän lähtökohdaksi
        // Voit käyttää yllä olevaa riviä palautusten generointiin. Se ei vaikuta ratkaisuun, mutta
        // "epäreilu" strategia tekee yhdestä palautuksesta paljon muita haastavamman tarkistettavan,
        // demonstroiden dynaamisen työnjaon etuja paremmin.

        // Otetaan funktion aloitusaika talteen suoritusajan laskemista varten
        long startTime = System.currentTimeMillis();

        // Tulostetaan tiedot esimerkkipalautuksista ennen arviointia
        for (var ug : ungradedSubmissions) {
            System.out.println(ug);
        }

        // Allokoidaan palautukset useille GradingTask-olioille, esim. 10 säiettä
        int taskCount = 10;
        List<GradingTask> tasks = TaskAllocator.allocate(ungradedSubmissions, taskCount);

        // Luodaan "säiepooli", jossa on neljä säiettä. ExecutorService hallitsee näitä
        // säikeitä automaattisesti, ja ylimääräiset tehtävät odottavat, kunnes joku säikeistä vapautuu.
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Lähetetään jokainen GradingTask-olio suoritettavaksi ExecutorServiceen
        for (GradingTask task : tasks) {
            executor.execute(task);
        }

        // Suljetaan ExecutorService ja odotetaan, että kaikki tehtävät suoritetaan loppuun
        executor.shutdown();
        try {
            // Odotetaan korkeintaan 1 minuuttia, että kaikki tehtävät valmistuvat
            if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                System.err.println("Some tasks did not finish in time.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // Tulostetaan arvioidut palautukset
        System.out.println("------------ CUT HERE ------------");
        for (GradingTask task : tasks) {
            List<Submission> gradedSubmissions = task.getGradedSubmissions();
            for (Submission gs : gradedSubmissions) {
                System.out.println(gs);
            }
        }

        // Lasketaan funktion suoritusaika
        System.out.printf("Total time for grading: %d ms%n", System.currentTimeMillis()-startTime);

    }
}