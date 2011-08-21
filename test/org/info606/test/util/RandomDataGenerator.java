package org.info606.test.util;

import java.util.List;
import java.util.Random;

import org.info606.util.io.FileIO;

/**
 * @author Greg Vannoni
 * @class CS 575
 *        Purpose:
 *        Notes:
 */
public class RandomDataGenerator {

    private static final String FIRST_NAME_FILE    = "test/data/firstnames.txt";
    private static final String LAST_NAME_FILE     = "test/data/lastnames.txt";
    private static final String COURSE_TITLE_FILE  = "test/data/course_titles.txt";
    private static final String EMAIL_ADDRESS_FILE = "test/data/email_address_suffix.txt";
    private static final Random ra                 = new Random();

    public static String getRandomFirstname() {
        List<String> names = FileIO.getFileContentsAsList(FIRST_NAME_FILE);
        return names.get(ra.nextInt(names.size()));
    }

    public static String getRandomLastname() {
        List<String> names = FileIO.getFileContentsAsList(LAST_NAME_FILE);
        return names.get(ra.nextInt(names.size()));
    }

    public static String getRandomEmailAddressSuffix() {
        List<String> names = FileIO.getFileContentsAsList(EMAIL_ADDRESS_FILE);
        return names.get(ra.nextInt(names.size()));
    }

    public static String getRandomCourseTitle() {
        List<String> names = FileIO.getFileContentsAsList(COURSE_TITLE_FILE);
        return names.get(ra.nextInt(names.size()));
    }

    public static int getRandomNumberWithList(int[] ints) {
        return ints[ra.nextInt(ints.length)];
    }

    public static int getRandomNumberWithRange(int min, int max) {
        int[] ints = new int[max - min];
        int counter = 0;
        for (int i = min; i <= max; i++) {
            ints[counter] = i;
            counter++;
        }

        return ints[ra.nextInt(ints.length)];
    }

    public static String getRandomCoursePrefix() {
        String[] prefixes = {"INFO", "CSC", "MATH"};

        return prefixes[ra.nextInt(prefixes.length)];
    }

    public static String getRandomLocation() {
        String[] prefixes = {"SMITH", "CHAVEZ", "WAREN", "KELLER"};
        String building = prefixes[ra.nextInt(prefixes.length)];
        int number = getRandomNumberWithRange(100, 400);
        return building + " " + number;
    }

    public static String zeroPad(String s, int length) {
        while (s.length() < length) {
            s = "0" + s;
        }

        return s;
    }

    public static String getRandomPhoneNumber() {
        int numberLength = 10;

        String phoneNumber = "";
        for (int i = 0; i < numberLength; i++) {
            int number = ra.nextInt(8) + 1;

            if (i == 3 || i == 6) {
                phoneNumber += "-";
            }

            phoneNumber += number;
        }

        return phoneNumber;
    }
}