package com.example.astroweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String value;
    private EditText latitude;
    private EditText longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

        Button button = findViewById(R.id.show);
        Button cancel = findViewById(R.id.cancel);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latitude = findViewById(R.id.latitudeNumber);
                longitude =findViewById(R.id.longitudeNumber);

                if(latitude.getText().toString().equals("") || longitude.getText().toString().equals("")){
                    Toast.makeText(getBaseContext(), "Wrong date", Toast.LENGTH_SHORT).show();
                    return;
                }

                Float width = Float.parseFloat(latitude.getText().toString());
                if(width>80 || width<-80){
                    Toast.makeText(getBaseContext(), "Wrong latitude", Toast.LENGTH_SHORT).show();
                    return;
                }

                Float height = Float.parseFloat(longitude.getText().toString());
                if(height>180 || height<-180){
                    Toast.makeText(getBaseContext(), "Wrong longitude", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(getBaseContext(), "Everything ok", Toast.LENGTH_SHORT).show();

                Intent astroWeather = new Intent(MainActivity.this, AstroWeather.class);
                astroWeather.putExtra("latitude", latitude.getText().toString());
                astroWeather.putExtra("longitude", longitude.getText().toString());
                astroWeather.putExtra("refreshTime", value);
                startActivity(astroWeather);

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String la = getIntent().getStringExtra("latitude");
                String lo = getIntent().getStringExtra("longitude");
                String time = getIntent().getStringExtra("refreshTime");
                Intent astroWeather = new Intent(MainActivity.this, AstroWeather.class);
                astroWeather.putExtra("latitude", la);
                astroWeather.putExtra("longitude", lo);
                astroWeather.putExtra("refreshTime", time);
                startActivity(astroWeather);

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        value = text;
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}