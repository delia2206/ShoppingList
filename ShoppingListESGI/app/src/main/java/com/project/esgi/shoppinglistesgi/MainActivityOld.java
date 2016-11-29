package com.project.esgi.shoppinglistesgi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivityOld extends AppCompatActivity {

    private static final String TAG = "Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "___BEFORE CALL");
        try{
            Log.i(TAG, "___SETTING URL");
            URL url = new URL("http://appspaces.fr/esgi/shopping_list/account/login.php?email=the&password=t");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            //
            Log.i(TAG, conn.toString());
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(TAG, "The response is: " + response);
            InputStream is = conn.getInputStream();

            //code ici

            is.close();
            conn.disconnect();
        } catch (Exception e) {
             new Exception("problem");
        }



    }
}
