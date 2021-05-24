package com.example.astroweather;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.time.ZonedDateTime;

public class SunFragment extends Fragment {
    private static final Handler handler = new Handler();
    private View view;
    private TextView textView, sunRise, sunRiseAzimuth, sunSet, sunsetAzimuth, dusk, dawn;

    private SharedViewModel sharedViewModel;
    protected String refreshTime, longitude, latitude;

    protected final Runnable updateData = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void run() {

            Long refreshT = Long.parseLong(refreshTime.substring(0, refreshTime.length()-4));
            if(refreshTime.substring(refreshTime.length()-3, refreshTime.length()).equals("min"))
                refreshT *= 1_000_000;
            else
                refreshT *= 1_000;

            Double width = Double.parseDouble(longitude);
            Double height = Double.parseDouble(latitude);

            ZonedDateTime zonedDateTime = ZonedDateTime.now();
            AstroCalculator astroCalculator = new AstroCalculator(new AstroDateTime(zonedDateTime.getYear(), zonedDateTime.getMonthValue(), zonedDateTime.getDayOfMonth(), zonedDateTime.getHour(), zonedDateTime.getMinute(), zonedDateTime.getSecond(), zonedDateTime.getOffset().getTotalSeconds()%(3600), false),
                    new AstroCalculator.Location(height, width));

            AstroCalculator.SunInfo sunInfo = astroCalculator.getSunInfo();

            sunRise.setText("Sunrise: "+String.format("%d:%02d", sunInfo.getSunrise().getHour(), sunInfo.getSunrise().getMinute()));
            sunRiseAzimuth.setText("Sunrise azimuth: "+String.format("%.04f", sunInfo.getAzimuthRise()));
            sunSet.setText("Sunset: "+String.format("%d:%02d",sunInfo.getSunset().getHour(), sunInfo.getSunset().getMinute(), sunInfo.getSunset().getSecond()));
            sunsetAzimuth.setText("Sunset azimuth: "+String.format("%.04f",sunInfo.getAzimuthSet()));
            dusk.setText("Dusk: "+String.format("%d:%02d", sunInfo.getTwilightEvening().getHour(), sunInfo.getTwilightEvening().getMinute()));
            dawn.setText("Dawn: "+String.format("%d:%02d", sunInfo.getTwilightMorning().getHour(), sunInfo.getTwilightMorning().getMinute()));

            handler.postDelayed(this, refreshT);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_sun, container, false);
        textView = view.findViewById(R.id.textView);
        sunRise = view.findViewById(R.id.sunRise);
        sunRiseAzimuth = view.findViewById(R.id.sunRiseAzimuth);
        sunSet = view.findViewById(R.id.sunSet);
        sunsetAzimuth = view.findViewById(R.id.sunsetAzimuth);
        dusk = view.findViewById(R.id.dusk);
        dawn = view.findViewById(R.id.dawn);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        refreshTime = sharedViewModel.getRefreshTime();
        longitude = sharedViewModel.getLongitude();
        latitude = sharedViewModel.getLatitude();
        System.out.println("SUNFRAGMENT: "+longitude+ " "+latitude+" "+refreshTime);

//        textView.setText("NO i sie wstalo slonce!");
        handler.post(updateData);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateData);
    }
}