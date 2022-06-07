package com.cabbagegod.cabbagescape.util;

import java.io.File;

public class FileUtil {
    public static String directoryPath = System.getProperty("user.dir") + "/cabbagescape/";

    public static void CreateDefaultDirectoryIfNotExists(){
        File directory = new File(directoryPath);
        if(!directory.exists()){
            directory.mkdir();
        }
    }

    public static void CreateNewDirecotryIfNotExists(String dir){
        File directory = new File(dir);
        if(!directory.exists()){
            directory.mkdir();
        }
    }
}
