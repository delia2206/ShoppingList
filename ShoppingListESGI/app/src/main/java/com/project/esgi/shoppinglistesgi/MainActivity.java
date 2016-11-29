package com.project.esgi.shoppinglistesgi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main Activity";
    //private static final String URL_TO_HIT = "http://appspaces.fr/esgi/shopping_list/account/login.php?email=the&password=t";
    private static final String URL_TO_HIT = "http://jsonparsing.parseapp.com/jsonData/moviesData.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new JSONTask().execute(URL_TO_HIT);

    }
}
