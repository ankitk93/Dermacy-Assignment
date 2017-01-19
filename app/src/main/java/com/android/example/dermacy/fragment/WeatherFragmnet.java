package com.android.example.dermacy.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.example.dermacy.R;
import com.android.example.dermacy.adapter.WeatherAdapter;
import com.android.example.dermacy.model.Data;
import com.android.example.dermacy.utils.AppConstants;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by techie93 on 1/19/2017.
 */

public class WeatherFragmnet extends Fragment {

    public WeatherFragmnet(){}

    ListView mListView;
    WeatherAdapter adapter;

    // base url for different city weather
    private final static String BASE_URL = AppConstants.WEATHER_API_BASE_URL;

    //Network network = new Network();


    private String getDataAddress(String city) {
        return BASE_URL + "?q=" + city + "&APPID=023148822cc2ea34ef926103a9dede29";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new WeatherAdapter(getActivity());

        mListView = (ListView)view.findViewById(R.id.city_weather_list);
        mListView.setAdapter(adapter);

        for (String c : AppConstants.cities)
            getForcast(c);
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
