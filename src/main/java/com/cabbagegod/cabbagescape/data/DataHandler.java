package com.cabbagegod.cabbagescape.data;

import com.cabbagegod.cabbagescape.util.FileUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DataHandler {
    public static void WriteStringToFile(String data, String fileName){
        FileUtil.CreateDefaultDirectoryIfNotExists();
        String directoryPath = FileUtil.directoryPath;

        try {
            FileWriter myWriter = new FileWriter(directoryPath + fileName + ".txt");
            myWriter.write(data);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static String ReadStringFromFile(String fileName){
        FileUtil.CreateDefaultDirectoryIfNotExists();
        String directoryPath = FileUtil.directoryPath;

        String text = "";
        try {
            text = Files.readString(Paths.get(directoryPath + fileName + ".txt"));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return text;
    }
}
