package com.example.astroweather;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FavoritePlaces extends AppCompatActivity {
    ListView listOfFavoritePlaces;
    private ArrayList<String> arrayOfPlaces = new ArrayList<>(0);
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_places);

        databaseHelper = new DatabaseHelper(this);

        getDataFromDbToArray();

        listOfFavoritePlaces = findViewById(R.id.listOfPlaces);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayOfPlaces);

        listOfFavoritePlaces.setAdapter(arrayAdapter);

        listOfFavoritePlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                load mainActivity (AstroWeatherActivity) witch chosen data
                Toast.makeText(FavoritePlaces.this, "clicked id: "+position+" "+arrayOfPlaces.get(position), Toast.LENGTH_SHORT).show();
                Intent favoritePlacesActivity = new Intent(FavoritePlaces.this, AstroWeather.class);
                Bundle bundle = new Bundle();
                String[] tab = arrayOfPlaces.get(position).split("[(),]");
                bundle.putString("city", tab[0]);
                bundle.putString("longitude", tab[2]);
                bundle.putString("latitude", tab[1]);
                favoritePlacesActivity.putExtras(bundle);

                startActivity(favoritePlacesActivity);
            }
        });
    }

    private void getDataFromDbToArray() {
        Cursor data = databaseHelper.getData();

        while (data.moveToNext())
            arrayOfPlaces.add(data.getString(1)+"("+data.getString(2)+","+data.getString(3)+")");
    }
}
