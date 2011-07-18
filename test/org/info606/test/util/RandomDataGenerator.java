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

    private static final String FIRST_NAME_FILE = "test/data/firstnames.txt";
    private static final String LAST_NAME_FILE  = "test/data/lastnames.txt";
    private static final Random ra              = new Random();

    public static String getRandomFirstname() {
        List<String> names = FileIO.getFileContentsAsList(FIRST_NAME_FILE);
        return names.get(ra.nextInt(names.size()));
    }

    public static String getRandomLastname() {
        List<String> names = FileIO.getFileContentsAsList(LAST_NAME_FILE);
        return names.get(ra.nextInt(names.size()));
    }
}
