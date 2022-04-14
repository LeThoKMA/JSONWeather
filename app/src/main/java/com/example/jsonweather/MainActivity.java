package com.example.jsonweather;



import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.jsonweather.api.ApiService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    public static final String QUERY = "https://www.metaweather.com/api/location/search/?query=";
    public static final String QUERYID = "https://www.metaweather.com/api/location/";
    Button btnGetID,btnWeatherbyID,btnWeatherbyName;
EditText txtIF;
ListView listView;
ArrayList<ModalWeather> arrayListWeathers=new ArrayList<>();
AdapterWeather adapterWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControl();
        addEvent();
    }

    private void addEvent() {
        btnWeatherbyID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String request= QUERYID +txtIF.getText().toString();
                WeatherTask task=new WeatherTask();
                task.execute(request);


            }
        });
        btnGetID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIDbyNameCity();
            }
        });
        btnWeatherbyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIDByName();
            }});

    }

    private void getIDbyNameCity() {

        ApiService.apiService.callApi(txtIF.getText().toString()).enqueue(new Callback<List<ModalCityQuery>>() {
            @Override
            public void onResponse(Call<List<ModalCityQuery>> call, Response<List<ModalCityQuery>> response) {
                List<ModalCityQuery> list=response.body();
                ModalCityQuery mCQ=list.get(0);
                txtIF.setText(mCQ.getWoeid());
            }

            @Override
            public void onFailure(Call<List<ModalCityQuery>> call, Throwable t) {
                Toast.makeText(MainActivity.this,"ERROR",Toast.LENGTH_LONG).show();

            }
        });



    }

    private void addControl() {
        btnGetID=findViewById(R.id.btnid);
        btnWeatherbyID=findViewById(R.id.btn_wea_id);
        btnWeatherbyName=findViewById(R.id.btn_wea_name);
        txtIF=findViewById(R.id.editText);
        listView=findViewById(R.id.listview);
        adapterWeather=new AdapterWeather(MainActivity.this,R.layout.item,arrayListWeathers);
        listView.setAdapter(adapterWeather);

    }
    class WeatherTask extends AsyncTask<String,Void, ArrayList<ModalWeather>>
    {

        @Override
        protected ArrayList<ModalWeather> doInBackground(String... strings) {
            String s=strings[0];
            ArrayList<ModalWeather> arrayList=new ArrayList<>();
            try {
                URL url = new URL(s);
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("content-type","application/json");
                connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36");
                connection.setRequestProperty("Accept","*/*");
                connection.setRequestProperty("Server","Google Frontend");

                InputStream is=connection.getInputStream();
                InputStreamReader irs=new InputStreamReader(is);
                BufferedReader br=new BufferedReader(irs);
                StringBuilder builder=new StringBuilder();
                String line=br.readLine();
                while(line!=null)
                {
                    builder.append(line);
                    line= br.readLine();
                }
                    JSONObject jsonObject=new JSONObject(builder.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("consolidated_weather");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        ModalWeather modalWeather = new ModalWeather();
                        modalWeather.setId(object.getString("id"));
                        modalWeather.setWeather_state_name(object.getString("weather_state_name"));
                        modalWeather.setWeather_state_abbr(object.getString("weather_state_abbr"));
                        modalWeather.setWind_direction_compass(object.getString("wind_direction_compass"));
                        modalWeather.setCreated(object.getString("created"));
                        modalWeather.setApplicable_date(object.getString("applicable_date"));
                        modalWeather.setMin_temp(object.getString("min_temp"));
                        modalWeather.setMax_temp(object.getString("max_temp"));
                        modalWeather.setThe_temp(object.getString("the_temp"));
                        modalWeather.setWind_speed(object.getString("wind_speed"));
                        modalWeather.setWind_direction(object.getString("wind_direction"));
                        modalWeather.setAir_pressure(object.getString("air_pressure"));
                        modalWeather.setHumidity(object.getString("humidity"));
                        modalWeather.setVisibility(object.getString("visibility"));
                        modalWeather.setPredictability(object.getString("predictability"));
                        arrayList.add(modalWeather);
                    }
            }
            catch (Exception e)
            {
                Log.e("Error",e.toString());
            }
          return arrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<ModalWeather> strings) {
            super.onPostExecute(strings);
            arrayListWeathers.addAll(strings);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayListWeathers.clear();


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
    private void getIDByName() {

        ApiService.apiService.callApi(txtIF.getText().toString()).enqueue(new Callback<List<ModalCityQuery>>() {
            @Override
            public void onResponse(Call<List<ModalCityQuery>> call, Response<List<ModalCityQuery>> response) {
                List<ModalCityQuery> list = response.body();
                ModalCityQuery mCQ = list.get(0);
                WeatherTask weatherTask = new WeatherTask();
                weatherTask.execute(QUERYID + mCQ.getWoeid());
            }

            @Override
            public void onFailure(Call<List<ModalCityQuery>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_LONG).show();
            }
        });
    }
    }
