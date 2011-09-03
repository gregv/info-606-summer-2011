package org.info606.test.util;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.info606.util.io.FileIO;

/**
 * @class INFO 606
 *        Purpose: Provide utility methods to extract metadata from the EclipseLink log
 *        Notes:
 */
public class EclipseLinkLogParser {

    private static final String CLASS_NAME = EclipseLinkLogParser.class.getName();
    private static Logger       logger     = Logger.getLogger(CLASS_NAME);

    private String              filename;

    /**
     * @param filename - the eclipselink filename to parse
     */
    public EclipseLinkLogParser(String filename) {
        this.filename = filename;
    }

    /**
     * Method: saveAllInsertPerformance<br/>
     * Save all insert performance
     * @param file The filename you want to save as
     * @param entityName The name of the entity you want statistics for
     */
    public void saveAllInsertPerformance(File file, String entityName) {
        logger.entering(CLASS_NAME, "saveAllInsertPerformance");
        FileIO.writeListToFile(file, addNumbersToList(getAllInsertPerformanceForEntityAsList(entityName)), ",", false);
        logger.entering(CLASS_NAME, "saveAllInsertPerformance");
    }

    /**
     * Method: saveAllSelectPerformance<br/>
     * @param file The filename you want to save as
     * @param entityName The name of the entity you want statistics for
     */
    public void saveAllSelectPerformance(File file, String entityName) {
        logger.entering(CLASS_NAME, "saveAllSelectPerformance");
        FileIO.writeListToFile(file, addNumbersToList(getAllSelectPerformanceForEntityAsList(entityName)), ",", false);
        logger.exiting(CLASS_NAME, "saveAllSelectPerformance");
    }

    /**
     * Method: addNumbersToList<br/>
     * Add a numbering scheme to a list
     * @param list
     * @return
     */
    private List<String> addNumbersToList(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            list.set(i, i + 1 + ", " + list.get(i));
        }

        return list;
    }

    /**
     * Method: averageListOfStringsAsIntegers<br/>
     * Get the average of a list of integers
     * @param in
     * @return
     */
    public double averageListOfStringsAsIntegers(List<String> in) {
        int sum = 0;
        for (String myInt : in) {
            sum += Integer.parseInt(myInt);
        }
        return sum / (1.0 * in.size());
    }

    /**
     * Method: convertStringListToIntegerList<br/>
     * Maintaining order, convert a list of strings to a list of integers
     * @param strList
     * @return
     */
    public List<Integer> convertStringListToIntegerList(List<String> strList) {
        List<Integer> intList = new LinkedList<Integer>();
        for (String myInt : strList) {
            intList.add(Integer.parseInt(myInt));
        }
        return intList;
    }

    /**
     * Method: getAllInsertPerformanceForEntityAsList<br/>
     * Parse through the EclipseLink log to get performance for inserts, return a list of performance values
     * @param entityName
     * @return
     */
    public List<String> getAllInsertPerformanceForEntityAsList(String entityName) {
        logger.entering(CLASS_NAME, "getAllSelectPerformanceForEntityAsList", entityName);

        List<String> insertList = new LinkedList<String>();
        insertList.addAll(Arrays.asList(getAllInsertPerformanceForEntity(entityName).split("\n")));

        logger.exiting(CLASS_NAME, "getAllSelectPerformanceForEntityAsList", insertList.size());
        return insertList;
    }

    /**
     * Method: getAllSelectPerformanceForEntityAsList<br/>
     * Parse through the EclipseLink log to get performance for selects, return a list of performance values
     * @param entityName
     * @return
     */
    public List<String> getAllSelectPerformanceForEntityAsList(String entityName) {
        logger.entering(CLASS_NAME, "getAllSelectPerformanceForEntityAsList", entityName);

        List<String> selectList = new LinkedList<String>();
        selectList.addAll(Arrays.asList(getAllSelectPerformanceForEntity(entityName).split("\n")));

        logger.exiting(CLASS_NAME, "getAllSelectPerformanceForEntityAsList", selectList.size());
        return selectList;
    }

    /**
     * Method: getAllSelectPerformanceForEntity<br/>
     * Parse through the EclipseLink log to get performance for selects
     * @param entityName
     * @return
     */
    public String getAllSelectPerformanceForEntity(String entityName) {
        logger.entering(CLASS_NAME, "getAllSelectPerformanceForEntity", entityName);
        String regex = "ReadAllQuery\\(referenceClass=" + entityName + ".*?total time=(\\d{1,30})";
        logger.exiting(CLASS_NAME, "getAllSelectPerformanceForEntity", regex);
        return match(regex);
    }

    /**
     * Method: getAllInsertPerformanceForEntity<br/>
     * Parse through the EclipseLink log to get all performance for a given entity
     * @param entityName
     * @return
     */
    public String getAllInsertPerformanceForEntity(String entityName) {
        String regex = "InsertObjectQuery.*?total time=(\\d{1,30})";
        return match(regex);
    }

    /**
     * Method: match<br/>
     * Do regular expression searching on the eclipselink filename
     * @param regularExpression
     * @return
     */
    public String match(String regularExpression) {
        logger.entering(CLASS_NAME, "match", regularExpression);
        Pattern pattern = Pattern.compile(regularExpression, Pattern.MULTILINE);

        String fileStr = FileIO.getFileContentsAsString(filename, false);
        Matcher matcher = pattern.matcher(fileStr);

        StringBuffer str = new StringBuffer(1000);
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                String group = matcher.group(i);

                double seconds = 0;
                try {
                    seconds = Long.parseLong(group) / ((1E9) * 1.0);
                } catch (NumberFormatException fe) {
                }

                // if (seconds > 0)
                // group += " (" + seconds + " sec)";

                str.append(group + "\n");
            }
        }

        String result = str.toString();
        logger.entering(CLASS_NAME, "match", result);
        return result;
    }
}
