package com.cabbagegod.cabbagescape.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtil {
    public static void saveScreenshot(){
        NativeImage image = ScreenshotRecorder.takeScreenshot(MinecraftClient.getInstance().getFramebuffer());
        try {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss");;
            String currentDate = dateFormat.format(LocalDateTime.now());

            String screenshotDir = FileUtil.directoryPath + "screenshots/";
            FileUtil.CreateNewDirecotryIfNotExists(screenshotDir);
            image.writeTo(new File(screenshotDir + currentDate + ".png"));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
