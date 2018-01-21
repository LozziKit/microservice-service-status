package io.lozzikit.servicestatus.server.steps.integration;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;

public class Utils {
    public static int getPortFromURL(String url){
        return Integer.valueOf(url.split("/")[2].split(":")[1]);
    }

    public static boolean ping(OkHttpClient httpClient, String url){

        Request request = new Request.Builder().url(url).build();

        try {
            httpClient.newCall(request).execute();
        } catch (IOException e) {
            return false;
        }

        return true;
    }
}
