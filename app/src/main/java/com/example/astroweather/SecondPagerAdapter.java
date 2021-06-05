package com.example.astroweather;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SecondPagerAdapter extends FragmentStateAdapter {
    public SecondPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0)
            return new WeatherTempFragment();
        return new SumMoonFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
