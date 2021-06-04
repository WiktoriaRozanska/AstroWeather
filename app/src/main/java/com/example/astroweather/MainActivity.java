package com.example.astroweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String value;
    private String units;
    private EditText latitude;
    private EditText longitude;
    private EditText city;
    private SharedViewModel sharedViewModel;
    private final String API_KEY = "d9a1946fc8a52ae928c8165a706be683";
    private String baseURL = "https://api.openweathermap.org/data/2.5/weather?";
    private RequestQueue requestQueue;
    private AtomicBoolean flag = new AtomicBoolean(true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

        Spinner spinner1 = findViewById(R.id.spinnerForUnits);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.units, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        spinner1.setOnItemSelectedListener(this);

        requestQueue = Volley.newRequestQueue(this);

        Button button = findViewById(R.id.show);
        Button cancel = findViewById(R.id.cancel);

        String latitude2 = getIntent().getStringExtra("latitude");
        String longitude2 = getIntent().getStringExtra("longitude");
        String city2 = getIntent().getStringExtra("city");
        latitude = findViewById(R.id.latitudeNumber);
        longitude =findViewById(R.id.longitudeNumber);
        city = findViewById(R.id.editCityName);

        latitude.setText(latitude2);
        longitude.setText(longitude2);
        city.setText(city2);

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
                astroWeather.putExtra("city", city.getText().toString());
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
                String city = getIntent().getStringExtra("city");
                Intent astroWeather = new Intent(MainActivity.this, AstroWeather.class);
                astroWeather.putExtra("latitude", la);
                astroWeather.putExtra("longitude", lo);
                astroWeather.putExtra("refreshTime", time);
                astroWeather.putExtra("city", city);
                astroWeather.putExtra("units", units);
                startActivity(astroWeather);

            }
        });

        city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()!=0 && flag.get()){
                    findCityByName(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        latitude.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()!=0 && flag.get()){
                    findCityByLat(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        longitude.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()!=0 && flag.get()){
                    findCityByLon(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void findCityByLat(String lat) {
        String url = baseURL +"lat="+lat+"&lon="+longitude.getText().toString()+"&appid="+API_KEY;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    flag.set(false);
                    JSONObject jsonObject = response.getJSONObject("coord");
                    String lat = jsonObject.getString("lat");
                    String name = response.getString("name");

                    city.setText(name);
                    latitude.setText(lat);
                    flag.set(true);

                } catch (JSONException e) {
                    flag.set(true);
                    Toast.makeText(getBaseContext(), "Something went wrong during parse JSON", Toast.LENGTH_SHORT).show();
                }
            }
        }, error -> {
            flag.set(true);
            Toast.makeText(getBaseContext(), "City not found", Toast.LENGTH_SHORT).show();
        });

        requestQueue.add(request);
    }

    private void findCityByLon(String lon) {
        String url = baseURL +"lon="+lon+"&lat="+latitude.getText().toString()+"&appid="+API_KEY;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    flag.set(false);
                    JSONObject jsonObject = response.getJSONObject("coord");
                    String lon = jsonObject.getString("lon");
                    String name = response.getString("name");

                    city.setText(name);
                    longitude.setText(lon);
                    flag.set(true);

                } catch (JSONException e) {
                    Toast.makeText(getBaseContext(), "Something went wrong during parse JSON", Toast.LENGTH_SHORT).show();
                    flag.set(true);
                }
            }
        }, error -> {
            flag.set(true);
            Toast.makeText(getBaseContext(), "City not found", Toast.LENGTH_SHORT).show();
        });

        requestQueue.add(request);
    }

    private void findCityByName(String name){
        String url = baseURL +"q="+name+"&appid="+API_KEY;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    flag.set(false);
                    JSONObject jsonObject = response.getJSONObject("coord");
                    String lon = jsonObject.getString("lon");
                    String lat = jsonObject.getString("lat");

                    latitude.setText(lat);
                    longitude.setText(lon);
                    flag.set(true);

                } catch (JSONException e) {
                    flag.set(true);
                    Toast.makeText(getBaseContext(), "Something went wrong during parse JSON", Toast.LENGTH_SHORT).show();
                }
            }
        }, error -> {
            flag.set(true);
            Toast.makeText(getBaseContext(), "City not found", Toast.LENGTH_SHORT).show();
        });

        requestQueue.add(request);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        if(text.equals("standard") || text.equals("metric") || text.equals("imperial")){
            units = text;
            Toast.makeText(parent.getContext(), "UNITS-> "+text, Toast.LENGTH_SHORT).show();
        }
        else {
            value = text;
            Toast.makeText(parent.getContext(), "RT->"+text, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}