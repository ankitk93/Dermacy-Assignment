package com.android.example.dermacy.fragment;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.example.dermacy.R;
import com.android.example.dermacy.adapter.WeatherAdapter;
import com.android.example.dermacy.model.Data;
import com.android.example.dermacy.utils.AppConstants;
import com.android.example.dermacy.utils.InternetReciever;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by techie93 on 1/18/2017.
 */

public class MapFragment extends Fragment {



    public MapFragment(){}

    Data data;
    GoogleMap mGoogleMap;
    InternetReciever reciever = new InternetReciever();

    // base url for different city weather
    private final static String BASE_URL = AppConstants.WEATHER_API_BASE_URL;

    private String getDataAddress(String city) {
        return BASE_URL + "?q=" + city + "&APPID=023148822cc2ea34ef926103a9dede29";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mapfragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        boolean isInternetConnected = isOnline(getContext());
        if (isInternetConnected==true){
            for (String c : AppConstants.cities)
                getForcast(c);
        }else{
            Toast.makeText(getContext(),"Please connect to internet",Toast.LENGTH_LONG).show();
        }


        SupportMapFragment supportMapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        if (supportMapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            supportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.map, supportMapFragment).commit();
        }

        if (supportMapFragment !=  null){
            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mGoogleMap = googleMap;
                    mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    //setUpMarkers();
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
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

                    data = new Gson().fromJson(dataInput, Data.class);
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

                    // setting up the markers on the map based on latitude and longitude
                    LatLng positions = new LatLng(result.getLatitude(),result.getLongitude());
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positions, 5));
                    mGoogleMap.addMarker(new MarkerOptions().position(positions)
                            .title(result.name)
                            .snippet("Temp :" +result.getTemperatureInCelsius()+ "°C")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                }
                super.onPostExecute(result);
            }
        }.execute(cities);
    }

    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in air plan mode it will be null
        return (netInfo != null && netInfo.isConnected());

    }
}
