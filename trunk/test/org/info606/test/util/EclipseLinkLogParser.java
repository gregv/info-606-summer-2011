package org.info606.test.util;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.info606.util.io.FileIO;

public class EclipseLinkLogParser {

    private static final String CLASS_NAME = EclipseLinkLogParser.class.getName();
    private static Logger       logger     = Logger.getLogger(CLASS_NAME);

    private String              filename;

    public EclipseLinkLogParser(String filename) {
        this.filename = filename;
    }

    public void saveAllInsertPerformance(File file, String entityName) {
        logger.entering(CLASS_NAME, "saveAllInsertPerformance");
        FileIO.writeListToFile(file, addNumbersToList(getAllInsertPerformanceForEntityAsList(entityName)), ",", false);
        logger.entering(CLASS_NAME, "saveAllInsertPerformance");
    }

    public void saveAllSelectPerformance(File file, String entityName) {
        logger.entering(CLASS_NAME, "saveAllSelectPerformance");
        FileIO.writeListToFile(file, addNumbersToList(getAllSelectPerformanceForEntityAsList(entityName)), ",", false);
        logger.exiting(CLASS_NAME, "saveAllSelectPerformance");
    }

    private List<String> addNumbersToList(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            list.set(i, i + 1 + ", " + list.get(i));
        }

        return list;
    }

    public double averageListOfStringsAsIntegers(List<String> in) {
        int sum = 0;
        for (String myInt : in) {
            sum += Integer.parseInt(myInt);
        }
        return sum / (1.0 * in.size());
    }

    public List<Integer> convertStringListToIntegerList(List<String> strList) {
        List<Integer> intList = new LinkedList<Integer>();
        for (String myInt : strList) {
            intList.add(Integer.parseInt(myInt));
        }
        return intList;
    }

    public List<String> getAllInsertPerformanceForEntityAsList(String entityName) {
        logger.entering(CLASS_NAME, "getAllSelectPerformanceForEntityAsList", entityName);

        List<String> insertList = new LinkedList<String>();
        insertList.addAll(Arrays.asList(getAllInsertPerformanceForEntity(entityName).split("\n")));

        logger.exiting(CLASS_NAME, "getAllSelectPerformanceForEntityAsList", insertList.size());
        return insertList;
    }

    public List<String> getAllSelectPerformanceForEntityAsList(String entityName) {
        logger.entering(CLASS_NAME, "getAllSelectPerformanceForEntityAsList", entityName);

        List<String> selectList = new LinkedList<String>();
        selectList.addAll(Arrays.asList(getAllSelectPerformanceForEntity(entityName).split("\n")));

        logger.exiting(CLASS_NAME, "getAllSelectPerformanceForEntityAsList", selectList.size());
        return selectList;
    }

    public String getAllSelectPerformanceForEntity(String entityName) {
        logger.entering(CLASS_NAME, "getAllSelectPerformanceForEntity", entityName);
        String regex = "ReadAllQuery\\(referenceClass=" + entityName + ".*?total time=(\\d{1,30})";
        logger.exiting(CLASS_NAME, "getAllSelectPerformanceForEntity", regex);
        return match(regex);
    }

    public String getAllInsertPerformanceForEntity(String entityName) {
        String regex = "InsertObjectQuery.*?total time=(\\d{1,30})";
        return match(regex);
    }

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