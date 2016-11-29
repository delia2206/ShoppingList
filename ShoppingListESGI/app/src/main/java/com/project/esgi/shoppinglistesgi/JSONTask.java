package com.project.esgi.shoppinglistesgi;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.project.esgi.shoppinglistesgi.models.MovieModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by delia on 29/11/2016.
 */
public class JSONTask extends AsyncTask<String,String, List<MovieModel>> {

    private static final String TAG = "Json task";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //dialog.show();
    }

    @Override
    protected List<MovieModel> doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            String finalJson = buffer.toString();

            JSONObject parentObject = new JSONObject(finalJson);
            JSONArray parentArray = parentObject.getJSONArray("movies");

            List<MovieModel> movieModelList = new ArrayList<>();

            Gson gson = new Gson();
            for (int i = 0; i < parentArray.length(); i++) {
                JSONObject finalObject = parentArray.getJSONObject(i);
                /**
                 * below single line of code from Gson saves you from writing the json parsing yourself which is commented below
                 */
                MovieModel movieModel = gson.fromJson(finalObject.toString(), MovieModel.class);
//                    movieModel.setMovie(finalObject.getString("movie"));
//                    movieModel.setYear(finalObject.getInt("year"));
//                    movieModel.setRating((float) finalObject.getDouble("rating"));
//                    movieModel.setDirector(finalObject.getString("director"));
//
//                    movieModel.setDuration(finalObject.getString("duration"));
//                    movieModel.setTagline(finalObject.getString("tagline"));
//                    movieModel.setImage(finalObject.getString("image"));
//                    movieModel.setStory(finalObject.getString("story"));
//
//                    List<MovieModel.Cast> castList = new ArrayList<>();
//                    for(int j=0; j<finalObject.getJSONArray("cast").length(); j++){
//                        MovieModel.Cast cast = new MovieModel.Cast();
//                        cast.setName(finalObject.getJSONArray("cast").getJSONObject(j).getString("name"));
//                        castList.add(cast);
//                    }
//                    movieModel.setCastList(castList);
                // adding the final object in the list
                movieModelList.add(movieModel);
                Log.d(TAG, "Movie Model: " + movieModel);
            }
            return movieModelList;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}