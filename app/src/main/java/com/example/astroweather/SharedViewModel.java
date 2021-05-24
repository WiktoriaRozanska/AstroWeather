package com.example.astroweather;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> longitude = new MutableLiveData<String>("55");
    private final MutableLiveData<String> latitude = new MutableLiveData<String>("20");
    private final MutableLiveData<String> refreshTime = new MutableLiveData<String>("1 sec");

    public void setLongitude(String longitude) {
        this.longitude.setValue(longitude);
    }

    public void setLatitude(String latitude) {
        this.latitude.setValue(latitude);
    }

    public void setRefreshTime(String refreshTime) {
        this.refreshTime.setValue(refreshTime);
    }

    public String getLatitude() {
        return latitude.getValue();
    }

    public String getLongitude() {
        return longitude.getValue();
    }

    public String getRefreshTime() {
        return refreshTime.getValue();
    }
}
