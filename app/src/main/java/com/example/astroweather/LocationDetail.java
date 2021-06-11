package com.example.astroweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LocationDetail extends AppCompatActivity {

    DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail);

        databaseHelper = FavoritePlaces.databaseHelper;

        Button show = findViewById(R.id.show);
        Button delete = findViewById(R.id.delete);

        TextView city = findViewById(R.id.city);
        TextView lon = findViewById(R.id.lon);
        TextView lat = findViewById(R.id.lat);

        city.setText("City: "+getIntent().getStringExtra("city"));
        lon.setText("Lon: "+getIntent().getStringExtra("longitude"));
        lat.setText("Lat: "+getIntent().getStringExtra("latitude"));


        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent details = new Intent(LocationDetail.this, AstroWeather.class);
                Bundle bundle = new Bundle();
                String city = getIntent().getStringExtra("city");
                String lon = getIntent().getStringExtra("longitude");
                String lat = getIntent().getStringExtra("latitude");

                bundle.putString("city", city);
                bundle.putString("longitude", lon);
                bundle.putString("latitude", lat);
                details.putExtras(bundle);

                startActivity(details);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favoritePlaces = new Intent(LocationDetail.this, FavoritePlaces.class);
                String id  = getIntent().getStringExtra("id");
                String city = getIntent().getStringExtra("city");
                boolean res = databaseHelper.deleteData(id);
//                Toast.makeText(getBaseContext(), "REult = "+res, Toast.LENGTH_LONG).show();
                startActivity(favoritePlaces);
            }
        });
    }
}