package fi.utu.tech.assignment3;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class StudyRegistrar extends Thread {

    private BlockingQueue<Submission> submissionQueue;
    private List<StudyRecord> finalGrades;
    private String courseCode;

    public StudyRegistrar(BlockingQueue<Submission> gradedSubmissions, List<StudyRecord> finalGrades, String courseCode) {
        super("RegistrarThread");
        this.submissionQueue = gradedSubmissions;
        this.finalGrades = finalGrades;
        this.courseCode = courseCode;
    }

    public void run() {
        try {
            while (true) {
                var s = submissionQueue.take();
                addToStudyRegistery(s.getGrade(), s.getSubmittedBy());
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupt received, no new submissions should be coming");
        }
    }

    void addToStudyRegistery(int grade, String studentName) {
        System.out.printf("Adding grade %d for %s on %s%n", grade, studentName, this.courseCode);
        finalGrades.add(new StudyRecord(studentName, this.courseCode, grade));
    }
}
