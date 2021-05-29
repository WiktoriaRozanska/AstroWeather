package com.example.astroweather;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FavoritePlaces extends AppCompatActivity {
    ListView listOfFavoritePlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_places);

        listOfFavoritePlaces = findViewById(R.id.listOfPlaces);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Paris");
        arrayList.add("Radom");
        arrayList.add("Lodz");
        arrayList.add("New York");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);

        listOfFavoritePlaces.setAdapter(arrayAdapter);
    }
}
