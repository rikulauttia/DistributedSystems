package fi.utu.tech.assignment3;

import java.util.List;

public class StudyRegistrar extends Thread {

    private List<Submission> submissionQueue;
    private List<StudyRecord> finalGrades;
    private String courseCode;

    public StudyRegistrar(List<Submission> gradedSubmissions, List<StudyRecord> finalGrades, String courseCode) {
        super("RegistrarThread");
        submissionQueue = gradedSubmissions;
        this.finalGrades = finalGrades;
        this.courseCode = courseCode;
    }

    public void run() {
        try {
            while (true) {
                if (interrupted())
                    throw new InterruptedException();
                var s = submissionQueue.remove(0);
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
