package com.cabbagegod.cabbagescape.client;

import com.cabbagegod.cabbagescape.data.Settings;
import com.cabbagegod.cabbagescape.github.Release;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class VersionChecker {
    private static boolean showUpdate = false;
    private static String currentVersion;

    public static String getCurrentVersion(){
        return currentVersion;
    }

    public static boolean shouldShowUpdate(){
        return showUpdate;
    }

    public static void disableUpdateMessage(){
        showUpdate = false;
        CabbageScapeClient.settings.lastVersion = currentVersion;
        CabbageScapeClient.saveSettings();
    }

    void init(){

    }

    public static void Verify() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.github.com/repos/cabbagegod/cabbagescape/releases"))
                    .headers("per_page", "1")
                    .GET()
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            Gson gson = new Gson();
            Release[] releases = gson.fromJson(response.body(), Release[].class);

            currentVersion = releases[0].tag_name;
            //Show update message if the last saved version in settings and current versions are different to the one on Github
            if(!CabbageScapeClient.settings.lastVersion.equals(currentVersion) && !releases[0].prerelease){
                if(CabbageScapeClient.version.equals(currentVersion)){
                    CabbageScapeClient.settings.lastVersion = currentVersion;
                    CabbageScapeClient.saveSettings();
                    return;
                }
                showUpdate = true;
            }
        } catch (URISyntaxException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
