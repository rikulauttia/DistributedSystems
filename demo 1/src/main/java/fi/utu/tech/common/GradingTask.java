package fi.utu.tech.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * You need to modify this file
 */

public class GradingTask implements Runnable {
    private final List<Submission> submissions;
    private List<Submission> gradedSubmissions;
    private final Random rnd = new Random();

    // Konstruktori, joka ottaa arvioitavien palautusten listan
    public GradingTask(List<Submission> submissions) {
        this.submissions = submissions;
        this.gradedSubmissions = new ArrayList<>();
    }

    /**
     * Suorittaa arvioinnin kaikille palautuksille
     */
    @Override
    public void run() {
        gradedSubmissions = gradeAll(submissions);
    }

    /**
     * Palauttaa arvioidut palautukset
     */
    public List<Submission> getGradedSubmissions() {
        return gradedSubmissions;
    }

    /**
     * Grades all given submissions. Does not mutate the given objects
     * @param submissions List of submissions to be graded
     * @return List of graded submissions (new objects)
     */
    public List<Submission> gradeAll(List<Submission> submissions) {
        List<Submission> graded = new ArrayList<>();
        for (var s : submissions) {
            graded.add(grade(s));
        }
        return graded;
    }

    /**
     * Grades the given submission
     * @param s Ungraded submission to be graded
     * @return New submission object with a given grade
     */
    public Submission grade(Submission s) {
        try {
            Thread.sleep(s.getDifficulty());
        } catch (InterruptedException e) {
            System.err.println("Who dared to interrupt my sleep?!");
        }
        return s.grade(rnd.nextInt(6));
    }
}
