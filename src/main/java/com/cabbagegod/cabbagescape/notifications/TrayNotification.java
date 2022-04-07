package com.cabbagegod.cabbagescape.notifications;

import java.awt.*;

//Wasn't able to get this working in MC :(
//For some reason minecraft seems to think it's running headless on my Windows machine
public class TrayNotification {
    public static void main(String[] args) throws AWTException {
        if (SystemTray.isSupported()) {
            TrayNotification td = new TrayNotification();
            td.displayTray();
        } else {
            System.err.println("System tray not supported!");
        }
    }

    public void displayTray() throws AWTException {
        //Obtain only one instance of the SystemTray object
        SystemTray tray = SystemTray.getSystemTray();

        //If the icon is a file
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        //Alternative (if the icon is on the classpath):
        //Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));

        TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        //Set tooltip text for the tray icon
        trayIcon.setToolTip("System tray icon demo");
        tray.add(trayIcon);

        trayIcon.displayMessage("Hello, World", "notification demo", TrayIcon.MessageType.INFO);
    }
}
