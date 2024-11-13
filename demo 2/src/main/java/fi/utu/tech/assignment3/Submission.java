package fi.utu.tech.assignment3;

/*
 * This file will require NO changes
 */

/**
 * Represents a Submission
 */
public class Submission {
    private final int grade;
    private final int difficulty;
    private final String submittedBy;

    /**
     * 
     * @param submitter The name of the original submitter
     * @param grade The given grade for the submission
     * @param difficulty How long it takes relatively to grade this submission
     * @throws IllegalArgumentException If the grade is out of range !(0-5)
     */
    public Submission(String submitter, int grade, int difficulty) throws IllegalArgumentException {
        this.submittedBy = submitter;
        this.difficulty = difficulty;
        if (grade >= 0 && grade <= 5) {
            this.grade = grade;
        } else {
            throw new IllegalArgumentException("Grade must be between 0 and 5");
        }
    }

    public Submission(String submitter, int difficulty) throws IllegalArgumentException {
        this(submitter, 0, difficulty);
    }

    /**
     * 
     * @return The name of the person who submitted this submission
     */
    public String getSubmittedBy() {
        return this.submittedBy;
    }


    /**
     * Not all submissions are equal in clarity.
     * A bit more *creative* solutions might take longer
     * to grade.
     * 
     * @return The difficulty level/time that it takes to grade this submission
     */
    public int getDifficulty() {
        return this.difficulty;
    }

    /**
     * 
     * @return The grade given for this submission
     */
    public int getGrade() {
        return this.grade;
    }

    /**
     * 
     * @param newGrade The grade to be given
     * @return A new submission object with the given grade
     */
    public Submission grade(int newGrade) {
        return new Submission(this.getSubmittedBy(), newGrade, difficulty);
    }

    public String toString() {
        return String.format("%s: %d (Difficulty: %d)", submittedBy, grade, difficulty);
    }
    
}
