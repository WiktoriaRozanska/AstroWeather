package com.example.astroweather;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> city = new MutableLiveData<>("Radom");
    private final MutableLiveData<String> longitude = new MutableLiveData<String>("51.4025");
    private final MutableLiveData<String> latitude = new MutableLiveData<String>("21.1471");
    private final MutableLiveData<String> refreshTime = new MutableLiveData<String>("15 min");
    private final MutableLiveData<String> units = new MutableLiveData<>("standard");
    private final MutableLiveData<ArrayList<String>> arrayOfFavouritePlaces = new MutableLiveData<>(new ArrayList<>());

    public void setLongitude(String longitude) {
        this.longitude.setValue(longitude);
    }

    public void setLatitude(String latitude) {
        this.latitude.setValue(latitude);
    }

    public void setRefreshTime(String refreshTime) {
        this.refreshTime.setValue(refreshTime);
    }

    public void setCity(String city) { this.city.setValue(city); }

    public void setUnits(String units) { this.units.setValue(units); }

    public void setArrayOfFavouritePlaces(ArrayList<String> array) { this.arrayOfFavouritePlaces.setValue(array); }

    public String getLatitude() {
        return latitude.getValue();
    }

    public String getLongitude() {
        return longitude.getValue();
    }

    public String getRefreshTime() {
        return refreshTime.getValue();
    }

    public String getCity() { return city.getValue(); }

    public String getUnits() { return units.getValue(); }

    public ArrayList<String> getPlaces() { return arrayOfFavouritePlaces.getValue();}
}
