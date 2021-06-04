package com.example.astroweather;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;


public class WeatherFragment extends Fragment {
    private static final Handler handler = new Handler();
    private TextView weather, description, visibility, windSpeed, windDeg;
    private ImageView imageView;
    private SharedViewModel sharedViewModel;
    private View view;
    private final String API_KEY = "d9a1946fc8a52ae928c8165a706be683";
    private RequestQueue requestQueue;

    private String baseURL = "https://api.openweathermap.org/data/2.5/weather?";

    protected String refreshTime, longitude, latitude, units;

    protected final Runnable updateData = new Runnable() {
        @Override
        public void run() {
            Long refresT = Long.parseLong(refreshTime.substring(0, refreshTime.length()-4));

            if(refreshTime.substring(refreshTime.length()-3, refreshTime.length()).equals("min"))
                refresT *= 1_000_000;
            else
                refresT *= 1_000;

            //todo: get data from api
            String url = baseURL +"lat="+longitude+"&lon="+latitude+"&appid="+API_KEY+"&units="+units;

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray weatherJsonArray = response.getJSONArray("weather");
                        JSONObject weatherJsonObject = weatherJsonArray.getJSONObject(0);
                        JSONObject windJsonObject = response.getJSONObject("wind");

                        String url = "http://openweathermap.org/img/wn/"+weatherJsonObject.getString("icon")+"@2x.png";
//                        url = "https://picsum.photos/600";
                        Glide
                                .with(getContext())
                                .load(url)
                                .override(50,50)
                                .into(imageView);


                        weather.setText("Current weather: "+weatherJsonObject.get("main"));
                        description.setText("Description: "+weatherJsonObject.getString("description"));
                        visibility.setText("Visibility: "+response.getString("visibility"));
                        windSpeed.setText("Wind speed: "+windJsonObject.getString("speed"));
                        windDeg.setText("Wind degree: "+windJsonObject.getString("deg"));

                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Something went wrong during parse JSON", Toast.LENGTH_SHORT).show();
                    }
                }
            }, error -> {
                Toast.makeText(getContext(), "City not found", Toast.LENGTH_SHORT).show();
            });

            requestQueue.add(request);

            handler.postDelayed(this, refresT);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_weather, container, false);

        weather = view.findViewById(R.id.weather);
        description = view.findViewById(R.id.description);
        visibility = view.findViewById(R.id.visibility);
        windSpeed = view.findViewById(R.id.windSpeed);
        windDeg = view.findViewById(R.id.windDeg);

        imageView = view.findViewById(R.id.imageView);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        refreshTime = sharedViewModel.getRefreshTime();
        longitude = sharedViewModel.getLongitude();
        latitude = sharedViewModel.getLatitude();
        units = sharedViewModel.getUnits();

        requestQueue = Volley.newRequestQueue(getContext());

        handler.post(updateData);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateData);
    }
}