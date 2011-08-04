package org.info606.util.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FileIO {
    public static String getFileContentsAsString(String filename, boolean newline) {

        File f = new File(filename);
        StringBuffer result = new StringBuffer((int)f.length());
        try {
            BufferedReader reader = new BufferedReader(new FileReader(f));

            String line = null;

            while ((line = reader.readLine()) != null) {
                result.append(line);
                if (newline) {
                    result.append("\n");
                }
            }

            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    public static List<String> getFileContentsAsList(String filename) {

        String contents = getFileContentsAsString(filename, true);

        String[] arrList = contents.split("\n");

        return Arrays.asList(arrList);
    }

    public static void writeListToFile(File file, List<String> list, String delimiter, boolean append) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, append));
            for (String s : list) {
                bw.write(s);
                bw.write(delimiter);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
