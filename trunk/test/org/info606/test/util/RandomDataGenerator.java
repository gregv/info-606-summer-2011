package org.info606.test.util;

import generated.TermType;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.info606.util.io.FileIO;

/**
 * @class INFO 606
 *        Purpose: Return randomly generated data
 *        Notes:
 */
public class RandomDataGenerator {

    private static final String FIRST_NAME_FILE       = "test/data/firstnames.txt";
    private static final String LAST_NAME_FILE        = "test/data/lastnames.txt";
    private static final String COURSE_TITLE_FILE     = "test/data/course_titles.txt";
    private static final String EMAIL_ADDRESS_FILE    = "test/data/email_address_suffix.txt";
    private static final String ACADEMIC_PROGRAM_FILE = "test/data/academic_program.txt";

    private static final Random ra                    = new Random();

    /**
     * Method: getRandomFirstname<br/>
     * @return
     */
    public static String getRandomFirstname() {
        List<String> names = FileIO.getFileContentsAsList(FIRST_NAME_FILE);
        return names.get(ra.nextInt(names.size()));
    }

    /**
     * Method: getRandomLastname<br/>
     * @return
     */
    public static String getRandomLastname() {
        List<String> names = FileIO.getFileContentsAsList(LAST_NAME_FILE);
        return names.get(ra.nextInt(names.size()));
    }

    /**
     * Method: getRandomEmailAddressSuffix<br/>
     * @return
     */
    public static String getRandomEmailAddressSuffix() {
        List<String> names = FileIO.getFileContentsAsList(EMAIL_ADDRESS_FILE);
        return names.get(ra.nextInt(names.size()));
    }

    /**
     * Method: getRandomCourseTitle<br/>
     * @return
     */
    public static String getRandomCourseTitle() {
        List<String> names = FileIO.getFileContentsAsList(COURSE_TITLE_FILE);
        return names.get(ra.nextInt(names.size()));
    }

    /**
     * Method: getRandomNumberWithList<br/>
     * @param ints
     * @return
     */
    public static int getRandomNumberWithList(int[] ints) {
        return ints[ra.nextInt(ints.length)];
    }

    /**
     * Method: getRandomIntegerWithRange<br/>
     * @param min
     * @param max
     * @return
     */
    public static int getRandomIntegerWithRange(int min, int max) {
        int[] ints = new int[max - min + 1];
        int counter = 0;
        for (int i = min; i <= max; i++) {
            ints[counter] = i;
            counter++;
        }

        return ints[ra.nextInt(ints.length)];
    }

    /**
     * Method: getRandomDoubleWithRange<br/>
     * @param min
     * @param max
     * @param increment
     * @return
     */
    public static double getRandomDoubleWithRange(int min, int max, double increment) {

        List<Double> doubles = new ArrayList<Double>(max);

        // This fixes an issue with some numbers that have many decimal places
        DecimalFormat df = new DecimalFormat("#.##");

        double currNum = min;
        while (currNum <= max) {
            currNum = new Double(df.format(currNum));
            doubles.add(currNum);
            currNum += increment;
        }

        return doubles.get(ra.nextInt(doubles.size()));
    }

    /**
     * Method: getRandomCoursePrefix<br/>
     * @return
     */
    public static String getRandomCoursePrefix() {
        String[] prefixes = {"INFO", "CSC", "MATH", "ENGL", "PYS", "ECE", "CHEM", "SPAN", "GER"};

        return prefixes[ra.nextInt(prefixes.length)];
    }

    /**
     * Method: getRandomLocation<br/>
     * @return
     */
    public static String getRandomLocation() {
        String[] prefixes = {"SMITH", "CHAVEZ", "WAREN", "KELLER"};
        String building = prefixes[ra.nextInt(prefixes.length)];
        int number = getRandomIntegerWithRange(100, 400);
        return building + " " + number;
    }

    /**
     * Method: getRandomTerm<br/>
     * @return
     */
    public static TermType getRandomTerm() {
        TermType[] prefixes = {TermType.FALL, TermType.WINTER, TermType.SPRING, TermType.SUMMER};
        return prefixes[ra.nextInt(prefixes.length)];
    }

    /**
     * Method: getRandomProgram<br/>
     * @return
     */
    public static String getRandomProgram() {
        List<String> names = FileIO.getFileContentsAsList(ACADEMIC_PROGRAM_FILE);
        return names.get(ra.nextInt(names.size()));
    }

    /**
     * Method: zeroPad<br/>
     * @param s
     * @param length
     * @return
     */
    public static String zeroPad(String s, int length) {
        while (s.length() < length) {
            s = "0" + s;
        }

        return s;
    }

    /**
     * Method: getRandomPhoneNumber<br/>
     * @return
     */
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
