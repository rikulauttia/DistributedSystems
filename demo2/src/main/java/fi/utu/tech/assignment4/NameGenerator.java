package fi.utu.tech.assignment4;

import java.util.Random;

/**
 * Generates Finnish-sounding names. No need to understand nor modify
 */
public class NameGenerator {

    final static char[] consonants = {'h','j','k','l','m','n','p','r','s','t','v'};
    final static char[] centralvowels = {'e','i'};
    final static char[] backvowels = {'a','o','u'};
    final static char[] frontvowels = {'ä','ö','y'};


    final static private Random rnd = new Random();

    private static char getConsonant() {
        return consonants[rnd.nextInt(consonants.length)];
    }

    private static char getVowel(boolean preferBack) {
        boolean pickCentral = rnd.nextBoolean();
        char[] vowels;
        if (pickCentral) {
            vowels = centralvowels;
        } else {
            vowels = preferBack ? backvowels : frontvowels;
        }
        return vowels[rnd.nextInt(vowels.length)];
    }

    public static String nextName() {
        boolean preferBack = rnd.nextBoolean();
        char doubles = getVowel(preferBack);
        String firstName = String.format("%c%c%c%c%c", Character.toUpperCase(getConsonant()), doubles, doubles, getConsonant(), getVowel(preferBack));
        preferBack = rnd.nextBoolean();
        String lastName = String.format("%c%c%c%cnen", Character.toUpperCase(getConsonant()), getVowel(preferBack), getConsonant(), getVowel(preferBack));
        return String.format("%s %s", firstName, lastName);
    };
    
}