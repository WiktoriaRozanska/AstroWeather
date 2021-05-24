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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.time.ZonedDateTime;


public class MoonFragment extends Fragment {
    private static final Handler handler = new Handler();
    private View view;
    private TextView textView, moonRise, moonSet, newMoon, fullMoon, phaseOfMonth, dayOfMonth;
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

            AstroCalculator.MoonInfo moonInfo = astroCalculator.getMoonInfo();

            moonRise.setText("Moon rise: "+String.format("%d:%02d", moonInfo.getMoonrise().getHour(), moonInfo.getMoonrise().getMinute()));
            moonSet.setText("Moon set: "+String.format("%d:%02d", moonInfo.getMoonset().getHour(), moonInfo.getMoonset().getMinute()));
            newMoon.setText("New moon: "+String.format("%02d-%02d-%d", moonInfo.getNextNewMoon().getDay(), moonInfo.getNextNewMoon().getMonth(), moonInfo.getNextNewMoon().getYear()));
            fullMoon.setText("Full moon: "+String.format("%02d-%02d-%d",moonInfo.getNextFullMoon().getDay(), moonInfo.getNextFullMoon().getMonth(), moonInfo.getNextFullMoon().getYear()));
            phaseOfMonth.setText("Phase of the month: "+String.format("%.02f%%", moonInfo.getIllumination()));
            dayOfMonth.setText("Day of the synodic month:"+String.format("%d", (int) moonInfo.getAge()));

            handler.postDelayed(this, refreshT);

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_moon, container, false);
        textView = view.findViewById(R.id.textView);
        moonRise = view.findViewById(R.id.moonRise);
        moonSet = view.findViewById(R.id.moonSet);
        newMoon = view.findViewById(R.id.newMoon);
        fullMoon = view.findViewById(R.id.fullMoon);
        phaseOfMonth = view.findViewById(R.id.phaseOfMonth);
        dayOfMonth = view.findViewById(R.id.dayOfMonth);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        refreshTime = sharedViewModel.getRefreshTime();
        longitude = sharedViewModel.getLongitude();
        latitude = sharedViewModel.getLatitude();

//        textView.setText("NO i sie uruchomilem mooooon!-> "+refreshTime+" -. "+longitude+" -> "+latitude);

        handler.post(updateData);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateData);
    }

}