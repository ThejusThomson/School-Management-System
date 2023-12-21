package edu.neu.csye6200.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static List<String> readFile(String filePath) {
        List<String> csvLines = new ArrayList<>();
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            while ((line = br.readLine()) != null) {
                csvLines.add(line);
            }
        } catch (IOException e) {
            System.out.println("IOException : " + e);
        } catch (Exception e) {
            System.out.println("Exception : " + e);
        }
        return csvLines;
    }

    public static void writeFile(String output, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(output);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("IOException : " + e);
        } catch (Exception e) {
            System.out.println("Exception : " + e);
        }

    }
}
