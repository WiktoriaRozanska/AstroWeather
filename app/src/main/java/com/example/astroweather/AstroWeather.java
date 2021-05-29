package com.example.astroweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AstroWeather extends AppCompatActivity {

    protected ViewPager2 viewPager2;
    protected MainPagerAdapter pagerAdapter;
    protected TextView time, position;
    private String latitude, longitude;
    private String refreshTime;
    private Button settings;

    private SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");
        refreshTime = getIntent().getStringExtra("refreshTime");
        Toast.makeText(getBaseContext(), latitude+" "+longitude+" "+refreshTime, Toast.LENGTH_SHORT).show();
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        if(latitude!=null && longitude!=null && refreshTime!=null) {
            sharedViewModel.setLatitude(latitude);
            sharedViewModel.setLongitude(longitude);
            sharedViewModel.setRefreshTime(refreshTime);
        }

        setContentView(R.layout.activity_astro_weather);

        position = findViewById(R.id.pos);
        longitude = sharedViewModel.getLongitude();
        latitude = sharedViewModel.getLatitude();
        position.setText("Latitude: "+latitude+", longitude: "+longitude);

        int orientation = getResources().getConfiguration().orientation;
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);

        time = findViewById(R.id.timeView);
        Thread timer = null;
        Runnable runnable = new CurrentTimeRunner();
        timer= new Thread(runnable);
        timer.start();

//        settings = findViewById(R.id.settings);
//        settings.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent mainActivity = new Intent(AstroWeather.this, MainActivity.class);
//                mainActivity.putExtra("latitude", latitude);
//                mainActivity.putExtra("longitude", longitude);
//                mainActivity.putExtra("refreshTime", refreshTime);
//                startActivity(mainActivity);
//            }
//        });

        if(orientation == Configuration.ORIENTATION_PORTRAIT && tabletSize == false) {
            viewPager2 = findViewById(R.id.viewPager);
            pagerAdapter = new MainPagerAdapter(this);
            viewPager2.setAdapter(pagerAdapter);
        }
        else if (tabletSize == false){
            setFragments(new SunFragment(), new MoonFragment());
        }
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
                Toast.makeText(this, "FavoritePlace", Toast.LENGTH_LONG).show();
                return true;
            }
            case R.id.refreshButton:{
                Toast.makeText(this, "Refresh", Toast.LENGTH_LONG).show();
                return true;
            }
            case R.id.settings:{
                Intent mainActivity = new Intent(AstroWeather.this, MainActivity.class);
                mainActivity.putExtra("latitude", latitude);
                mainActivity.putExtra("longitude", longitude);
                mainActivity.putExtra("refreshTime", refreshTime);
                startActivity(mainActivity);
                return true;
            }
            default: return super.onOptionsItemSelected(item);
        }
    }
}