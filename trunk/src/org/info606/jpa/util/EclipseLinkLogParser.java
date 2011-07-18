package org.info606.jpa.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.info606.util.io.FileIO;

public class EclipseLinkLogParser {

    private String filename;

    public EclipseLinkLogParser(String filename) {
        this.filename = filename;
    }

    public void match(String regularExpression) {
        Pattern pattern = Pattern.compile(regularExpression, Pattern.MULTILINE);

        String fileStr = FileIO.getFileContentsAsString(filename, false);
        Matcher matcher = pattern.matcher(fileStr);

        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                String group = matcher.group(i);

                double seconds = 0;
                try {
                    seconds = Long.parseLong(group) / ((1E9) * 1.0);
                } catch (NumberFormatException fe) {
                }

                if (seconds > 0)
                    group += " (" + seconds + " sec)";

                System.out.println(group);
            }
        }

    }
}
