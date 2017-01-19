package com.android.example.dermacy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.example.dermacy.R;
import com.android.example.dermacy.model.Data;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by techie93 on 1/18/2017.
 */

public class WeatherAdapter extends BaseAdapter {

    // each of the Data contain information about city
    private ArrayList<Data> cities = new ArrayList<Data>();
    private Context context;

    private static class ViewHolder{
        ImageView mIconImageView;
        TextView mCityName,mDescription,mTempreature,mLongitude,mLatitude;
    }

    public WeatherAdapter(Context context){

        this.context = context;
    }

    public void addData(Data data){
        cities.add(data);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {

        return cities.size();
    }

    @Override
    public Object getItem(int position) {

        return cities.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder mViewHolder = null;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.single_city,null);

            mViewHolder = new ViewHolder();
            mViewHolder.mIconImageView = (ImageView)view.findViewById(R.id.img);
            mViewHolder.mCityName =(TextView)view.findViewById(R.id.city);
            mViewHolder.mDescription =(TextView)view.findViewById(R.id.description);
            mViewHolder.mTempreature =(TextView)view.findViewById(R.id.temperature);
            mViewHolder.mLongitude =(TextView)view.findViewById(R.id.longitude);
            mViewHolder.mLatitude =(TextView)view.findViewById(R.id.latitude);
            view.setTag(mViewHolder);
        }else {
            mViewHolder = (ViewHolder)view.getTag();
        }

        // We retrieve the object in a specific position
        Data resource = (Data)getItem(position);

        // Picasso library downloads the icon and set it as ImageView source
        Picasso.with(context).load(resource.getIconAddress()).into(mViewHolder.mIconImageView);

        // Other TextViews are filled with Data object informations
        mViewHolder.mDescription.setText(resource.getDescription());
        mViewHolder.mCityName.setText(resource.name);
        mViewHolder.mTempreature.setText(resource.getTemperatureInCelsius() + "°C");
        mViewHolder.mLongitude.setText("Long : " + String.valueOf(resource.getLongitude()) + ", ");
        mViewHolder.mLatitude.setText("Lat : " + String.valueOf(resource.getLatitude()));
        return view;
    }
}
