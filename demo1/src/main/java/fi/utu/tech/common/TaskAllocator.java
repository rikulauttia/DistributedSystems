package fi.utu.tech.common;

import java.util.ArrayList;
import java.util.List;

/**
 * You need to modify this file
 */

public class TaskAllocator {

    /**
     * Allocator that creates list of two (2) GradingTask objects with each having half of the given submissions
     * @param submissions The submissions to be allocated
     * @return The two GradingTask objects in a list, each having half of the submissions
     */
    public static List<GradingTask> sloppyAllocator(List<Submission> submissions) {
        // Jaetaan submissions lista kahtia
        int mid = submissions.size() / 2;

        List<Submission> firstHalf = submissions.subList(0, mid);
        List<Submission> secondHalf = submissions.subList(mid, submissions.size());

        // Luodaan kaksi gradingtask oliota, ja kummallekkin annetaan puolet palautuksista
        GradingTask gradingTask1 = new GradingTask(new ArrayList<>(firstHalf));
        GradingTask gradingTask2 = new GradingTask(new ArrayList<>(secondHalf));

        // Palautetaan lista, joka sisältää molemmat äsken tehdyt Gradintask oliot
        List<GradingTask> tasks = new ArrayList<>();
        tasks.add(gradingTask1);
        tasks.add(gradingTask2);
        return tasks;
    }

    /**
     * Allocate List of ungraded submissions to tasks
     * @param submissions List of submissions to be graded
     * @param taskCount Amount of tasks to be generated out of the given submissions
     * @return List of GradingTasks allocated with some amount of submissions (depends on the implementation)
     */
    public static List<GradingTask> allocate(List<Submission> submissions, int taskCount) {
        List<GradingTask> tasks = new ArrayList<>();

        // Alustetaan tyhjät listat kullekin tehtävälle
        List<List<Submission>> splitSubmissions = new ArrayList<>();
        for (int i = 0; i < taskCount; i++) {
            splitSubmissions.add(new ArrayList<>());
        }

        // jaetaan palautukset eri listojen kesken vuorotellen
        for (int i = 0; i < submissions.size(); i++) {
            splitSubmissions.get(i % taskCount).add(submissions.get(i));
        }

        // Luodaan GradingTask-oliot kullekin listalle ja lisätään ne tasks-listaan
        for (List<Submission> part : splitSubmissions) {
            tasks.add(new GradingTask(part));
        }
        return tasks;
    }
}