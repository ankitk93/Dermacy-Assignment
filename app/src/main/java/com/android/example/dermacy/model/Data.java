package com.android.example.dermacy.model;

import java.util.List;

/**
 * Created by techie93 on 1/18/2017.
 */

public class Data {

    //weather icon url
    private final static String ICON_URL = "http://openweathermap.org/img/w/";

    static class Weather {
        String description;
        String icon;
    }

    static class Main {
        float temp;
    }

    static class Coordinates{
        double lon , lat;
    }

    List<Weather> weather;

    Main main;

    Coordinates coord;

    public String name;
    public String visibility ;

    // A method that converts temperature from Kelvin degrees to Celsius
    public String getTemperatureInCelsius() {
        float temp = main.temp - 273.15f;
        return String.format("%.2f", temp);
    }

    // the icon
    public String getIconAddress() {
        return ICON_URL + weather.get(0).icon + ".png";
    }

    public String getDescription() {
        if (weather != null && weather.size() > 0)
            return weather.get(0).description;
        return null;
    }

    public double getLatitude(){
        return coord.lat;
    }

    public double getLongitude(){

        return coord.lon;
    }
}
