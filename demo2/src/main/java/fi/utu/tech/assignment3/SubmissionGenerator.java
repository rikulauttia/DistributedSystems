package fi.utu.tech.assignment3;

/**
 * This file will require NO changes
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class for generating list of submissions of variying difficulty
 */
public class SubmissionGenerator {

    public static enum Strategy {STATIC, RANDOM, LINEAR, UNFAIR};
    private static NameGenerator n = new NameGenerator();
    private static Random rnd = new Random();

    /**
     * Generates a list of submissions based on given arguments.
     * @param amount The amount of submissions to be generated
     * @param difficulty The difficulty of the submissions to-be-generated. Ie. The time it takes to grade the submission.
     * @param strategy Determines how final difficulty is calculated for individual submissions.
     * @return
     */
    public static List<Submission> generateSubmissions(int amount, int difficulty, Strategy strategy) {
        List<Submission> submissions = new ArrayList<>();
        for (int i=0; i<amount; i++) {
            int time = getTimeForStrategy(strategy, difficulty, i, amount);
            submissions.add(new Submission(n.nextName(), time));
        }
        return submissions;
    }

    private static int getTimeForStrategy(Strategy strategy, int difficulty, int currentSubmission, int submissionCount) {
        switch (strategy) {
            case LINEAR:
                return (currentSubmission * difficulty) / (submissionCount-1);
            case RANDOM:
                return rnd.nextInt(difficulty);
            case UNFAIR:
                return currentSubmission == 0 ? 10*difficulty : difficulty;
            case STATIC:
                return difficulty;
            default:
                return difficulty;
        }
    }

    
}
/**
 * Generates Finnish-sounding names. No need to understand
 */
class NameGenerator {

    final static char[] consonants = {'h','j','k','l','m','n','p','r','s','t','v'};
    final static char[] centralvowels = {'e','i'};
    final static char[] backvowels = {'a','o','u'};
    final static char[] frontvowels = {'ä','ö','y'};


    final private Random rnd = new Random();

    public char getConsonant() {
        return consonants[rnd.nextInt(consonants.length)];
    }

    public char getVowel(boolean preferBack) {
        boolean pickCentral = rnd.nextBoolean();
        char[] vowels;
        if (pickCentral) {
            vowels = centralvowels;
        } else {
            vowels = preferBack ? backvowels : frontvowels;
        }
        return vowels[rnd.nextInt(vowels.length)];
    }

    public String nextName() {
        boolean preferBack = rnd.nextBoolean();
        char doubles = getVowel(preferBack);
        String firstName = String.format("%c%c%c%c%c", Character.toUpperCase(getConsonant()), doubles, doubles, getConsonant(), getVowel(preferBack));
        preferBack = rnd.nextBoolean();
        String lastName = String.format("%c%c%c%cnen", Character.toUpperCase(getConsonant()), getVowel(preferBack), getConsonant(), getVowel(preferBack));
        return String.format("%s %s", firstName, lastName);
    };
    
}
