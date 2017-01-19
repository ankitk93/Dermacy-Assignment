package com.android.example.dermacy.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.android.example.dermacy.adapter.WeatherAdapter;
import com.android.example.dermacy.model.Data;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by techie93 on 1/18/2017.
 */

public class Network {

    Context context;
    WeatherAdapter adapter = new WeatherAdapter(context);

    // base url for different city weather
    private final static String BASE_URL = AppConstants.WEATHER_API_BASE_URL;

    private String getDataAddress(String city) {
        return BASE_URL + "?q=" + city + "&APPID=023148822cc2ea34ef926103a9dede29";
    }

    public void getForcast(String cities){

        new AsyncTask<String , Void , Data>(){

            @Override
            protected Data doInBackground(String... params) {

                String selectedCities = params[0];

                URL url;
                try{
                    url = new URL(getDataAddress(selectedCities));

                    // After connection, url provides the stream to the remote
                    // data. Reader object can be used to read them
                    Reader dataInput = new InputStreamReader(url.openStream());

                    Data data = new Gson().fromJson(dataInput, Data.class);
                    return data;
                }catch (MalformedURLException ex){
                    ex.printStackTrace();
                }catch(IOException ex){
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Data result) {

                if (result != null){
                    adapter.addData(result);
                }
                super.onPostExecute(result);
            }
        }.execute(cities);
    }
}
