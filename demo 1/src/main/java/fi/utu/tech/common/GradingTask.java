package fi.utu.tech.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * You need to modify this file
 */

public class GradingTask implements Runnable {

    private Random rnd = new Random();
    private List<Submission> submissions;
    private List<Submission> gradedSubmissions;

    /**
     * Contructor that takes a list of submissions to be graded
     * @param submissions List of submissions to be graded
     */

     public GradingTask(List<Submission> submissions) {
        this.submissions = submissions;
        this.gradedSubmissions = new ArrayList<>();
     }

     /**
      * Runnable's run method for grading all submissions in the list.
      */

      @Override
      public void run() {
        this.gradedSubmissions = gradeAll(submissions);
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

    /**
     * Method to retrieve the graded submissions after grading
     * @return List of graded submissions
     */

     public List<Submission> getGradedSubmissions() {
        return gradedSubmissions;
     }
}