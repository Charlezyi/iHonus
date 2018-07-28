package com.example.joeytayyiqin.orbital;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Admin {

    private static final String UID = "aNBqSYzuYtMe34EoM9HPnPOueA23";

    public static void main(String[] args ) throws IOException, ExecutionException, InterruptedException{

        FileInputStream serviceAccount =
                new FileInputStream("./ServiceAccountKey.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://userauthentication2-d2875.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);

        Map<String, Object> additionalClaims = new HashMap<String, Object>();
        additionalClaims.put("AdminAccount", true);
        String customToken = FirebaseAuth.getInstance().createCustomTokenAsync(UID, additionalClaims).get();
        System.out.println(customToken);
    }
}
