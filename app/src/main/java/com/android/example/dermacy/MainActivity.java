package com.android.example.dermacy;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.android.example.dermacy.adapter.WeatherAdapter;
import com.android.example.dermacy.fragment.MapFragment;
import com.android.example.dermacy.fragment.WeatherFragmnet;
import com.android.example.dermacy.model.Data;
import com.android.example.dermacy.utils.AppConstants;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TabLayout mTabLayout;
    ViewPager mViewPager;

    // base url for different city weather
    private final static String BASE_URL = AppConstants.WEATHER_API_BASE_URL;

    // The following array contains the names of the cities we are interested in
    private String[] cities = new String[] { "Tokyo", "London", "Moscow",
            "Ottawa", "Madrid", "Lisboa", "Zurich" , "NewYork" , "Patna" ,
            "Delhi" , "Bengaluru" , "Arizona" , "Goa" , "Mangalore" , "Germany" ,
            "Manila" , "Hyderabad" , "Chandigarh" , "Lucknow" , "Gurgaon" , "Gangtok" };

    // Reference to the Adapter object. WeatherAdapter is a custom class,
    // defined in a separate file
    private WeatherAdapter adapter;

    private String getDataAddress(String city) {
        return BASE_URL + "?q=" + city + "&APPID=023148822cc2ea34ef926103a9dede29";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //adapter instantiation and linked to list view
        adapter = new WeatherAdapter(this);

        /*for (String c : cities)
            getForcast(c);*/

        mTabLayout = (TabLayout)findViewById(R.id.tabs);
        mViewPager = (ViewPager)findViewById(R.id.viewpager);

        setupViewPager(mViewPager);

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
        mTabLayout.setTabTextColors(getResources().getColor(R.color.white),getResources().getColor(R.color.white));

        /*mMapsButton = (Button)findViewById(R.id.btn_maps);
        mMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapsIntent = new Intent(MainActivity.this,Maps.class);
                startActivity(mapsIntent);
            }
        });*/
    }

    private void setupViewPager(final ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new WeatherFragmnet(), "Cities Weather");
        adapter.addFragment(new MapFragment(), "Maps");
        viewPager.setAdapter(adapter);
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter{

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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
