package com.example.astroweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AstroWeather extends AppCompatActivity {

    protected ViewPager2 viewPager2;
    protected MainPagerAdapter pagerAdapter;
    protected TextView time, position;
    private String latitude, longitude;
    private String refreshTime;
    private String city;
    private String units;
    private Button settings;
    private ArrayList<String> arrayOfPlaces = new ArrayList<>();
    DatabaseHelper mDataBaseHelper;

    private SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_astro_weather);

        loadDataFromIntent();

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        mDataBaseHelper = new DatabaseHelper(this);

        if(latitude!=null && longitude!=null && city!=null) {
            setDataToSharedViewModel();
        }

        if(refreshTime!=null){
            sharedViewModel.setRefreshTime(refreshTime);
        }

        position = findViewById(R.id.pos);

        getDataFromSharedViewModel();

        position.setText(city+" ("+latitude+", "+longitude+")");

        int orientation = getResources().getConfiguration().orientation;
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);

        time = findViewById(R.id.timeView);
        Thread timer = null;
        Runnable runnable = new CurrentTimeRunner();
        timer= new Thread(runnable);
        timer.start();

        if(orientation == Configuration.ORIENTATION_PORTRAIT && tabletSize == false) {
            viewPager2 = findViewById(R.id.viewPager);
            pagerAdapter = new MainPagerAdapter(this);
            viewPager2.setAdapter(pagerAdapter);
        }
        else if(orientation == Configuration.ORIENTATION_LANDSCAPE && tabletSize == false){

        }
        else if (tabletSize == false){
            setFragments(new SunFragment(), new MoonFragment());
        }
    }

    private void getDataFromSharedViewModel() {
        longitude = sharedViewModel.getLongitude();
        latitude = sharedViewModel.getLatitude();
        city = sharedViewModel.getCity();
        units = sharedViewModel.getUnits();
    }

    private void setDataToSharedViewModel() {
        sharedViewModel.setLatitude(latitude);
        sharedViewModel.setLongitude(longitude);
        sharedViewModel.setCity(city);
        sharedViewModel.setUnits(units);
    }

    private void loadDataFromIntent() {
        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");
        refreshTime = getIntent().getStringExtra("refreshTime");
        city = getIntent().getStringExtra("city");
        units = getIntent().getStringExtra("units");
    }

    private void setFragments(SunFragment sunFragment, MoonFragment moonFragment) {
        FragmentManager fragmentManager =  getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameSunLayout, sunFragment);
        fragmentTransaction.replace(R.id.frameMoonLayout, moonFragment);
        fragmentTransaction.commit();
    }

    public void displayCurrentTime() {
        runOnUiThread(new Runnable() {
            public void run() {
                try{
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
                    String dateTime = simpleDateFormat.format(calendar.getTime());
                    time.setText(dateTime);
                }catch (Exception e) {}
            }
        });
    }

    class CurrentTimeRunner implements Runnable{

        @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    displayCurrentTime();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
       inflater.inflate(R.menu.uppermenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.favorite_place:{
                Intent favoritePlacesActivity = new Intent(AstroWeather.this, FavoritePlaces.class);
                startActivity(favoritePlacesActivity);
                return true;
            }
            case R.id.refreshButton:{
                Toast.makeText(this, "Refresh", Toast.LENGTH_LONG).show();
                return true;
            }
            case R.id.starButton:{
                arrayOfPlaces = sharedViewModel.getPlaces();
                arrayOfPlaces.add(city + " ("+latitude+","+longitude+")");
                sharedViewModel.setArrayOfFavouritePlaces(arrayOfPlaces);
                addDataToDB(city, latitude, longitude);

                Toast.makeText(this, "Add to my favourite", Toast.LENGTH_LONG).show();
                return true;
            }
            case R.id.settings:{
                Intent mainActivity = new Intent(AstroWeather.this, MainActivity.class);
                mainActivity.putExtra("latitude", latitude);
                mainActivity.putExtra("longitude", longitude);
                mainActivity.putExtra("refreshTime", refreshTime);
                mainActivity.putExtra("city", city);
                startActivity(mainActivity);
                return true;
            }
            default: return super.onOptionsItemSelected(item);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void addDataToDB(String city, String latitude, String longitude){
        boolean res = mDataBaseHelper.addData(city, latitude, longitude);

        if(res==false){
            toastMsg("add data to DB finish with false");
        }
        else
            toastMsg("add data to DB finish with true");
    }

    private void toastMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}