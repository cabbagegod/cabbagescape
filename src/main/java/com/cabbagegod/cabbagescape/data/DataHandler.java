package com.cabbagegod.cabbagescape.data;

import com.cabbagegod.cabbagescape.util.FileUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

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
            File myObj = new File(directoryPath + fileName + ".txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                text += data;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return text;
    }
}
