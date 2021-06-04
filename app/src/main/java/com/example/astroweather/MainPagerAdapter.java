package com.example.astroweather;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MainPagerAdapter extends FragmentStateAdapter {


    public MainPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0)
            return new WeatherFragment();
        else if (position == 1)
            return new TemperatureFragment();
        else if (position == 2)
            return new SunFragment();
        return new MoonFragment();
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
