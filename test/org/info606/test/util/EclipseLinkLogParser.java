package org.info606.test.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.info606.util.io.FileIO;

public class EclipseLinkLogParser {

    private String filename;

    public EclipseLinkLogParser(String filename) {
        this.filename = filename;
    }

    public void saveAllInsertPerformance(File file, String entityName) {
        ArrayList<String> selectList = new ArrayList<String>(3000);
        selectList.addAll(Arrays.asList(getAllInsertPerformanceForEntity(entityName).split("\n")));
        FileIO.writeListToFile(file, selectList, ",", false);
    }

    public void saveAllSelectPerformance(File file, String entityName) {
        ArrayList<String> selectList = new ArrayList<String>(3000);
        selectList.addAll(Arrays.asList(getAllSelectPerformanceForEntity(entityName).split("\n")));
        FileIO.writeListToFile(file, selectList, ",", false);
    }

    public String getAllSelectPerformanceForEntity(String entityName) {
        String regex = "ReadAllQuery\\(referenceClass=" + entityName + ".*?total time=(\\d{1,30})";
        return match(regex);
    }

    public String getAllInsertPerformanceForEntity(String entityName) {
        String regex = "InsertObjectQuery.*?total time=(\\d{1,30})";
        return match(regex);
    }

    public String match(String regularExpression) {
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

        return str.toString();
    }
}
