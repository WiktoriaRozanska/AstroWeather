package com.example.astroweather;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class TemperatureFragment extends Fragment {

    private static final Handler handler = new Handler();
    private TextView temp, feels_like, temp_min, temp_max, pressure, humidity;
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

            if(isNetworkAvailable()!=true)
            {
                try {
                    JSONObject tempJsonObject  = readJson(getContext(), "weather.json").getJSONObject("main");
                    temp.setText("Temp: "+tempJsonObject.getString("temp"));
                    feels_like.setText("Feels like: "+tempJsonObject.getString("feels_like"));
                    temp_min.setText("Temp min: "+tempJsonObject.getString("temp_min"));
                    temp_max.setText("Temp max: "+tempJsonObject.getString("temp_max"));
                    pressure.setText("Pressure: "+tempJsonObject.getString("pressure"));

                    Toast.makeText(getContext(), "Connect to Internet to have correct data", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            String url = baseURL +"lat="+longitude+"&lon="+latitude+"&appid="+API_KEY+"&units="+units;

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject tempJsonObject = response.getJSONObject("main");

                        writeToFile(getContext(), "weather.json", response.toString());

                        temp.setText("Temp: "+tempJsonObject.getString("temp"));
                        feels_like.setText("Feels like: "+tempJsonObject.getString("feels_like"));
                        temp_min.setText("Temp min: "+tempJsonObject.getString("temp_min"));
                        temp_max.setText("Temp max: "+tempJsonObject.getString("temp_max"));
                        pressure.setText("Pressure: "+tempJsonObject.getString("pressure"));
                        humidity.setText("Humminidty: "+tempJsonObject.getString("humidity"));
                    } catch (JSONException e) {
//                        Toast.makeText(getContext(), "Something went wrong during parse JSON", Toast.LENGTH_SHORT).show();
                    }
                }
            }, error -> {
//                Toast.makeText(getContext(), "City not found", Toast.LENGTH_SHORT).show();
            });

            requestQueue.add(request);

            handler.postDelayed(this, refresT);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_temperature, container, false);

        temp = view.findViewById(R.id.temp);
        feels_like = view.findViewById(R.id.feels_like);
        temp_min = view.findViewById(R.id.temp_min);
        temp_max = view.findViewById(R.id.temp_max);
        pressure = view.findViewById(R.id.pressure);
        humidity = view.findViewById(R.id.humidity);

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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void writeToFile(Context context, String fileName, String str){
        try{
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(str.getBytes(),0,str.length());
            fos.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static JSONObject readJson(Context context, String fileName) {
        JSONObject reader = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(context.openFileInput(fileName)));) {
            String line;
            String txt = "";
            while ((line = br.readLine()) != null) {
                txt+=line;
            }
            reader = new JSONObject(txt);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return reader;
    }

}