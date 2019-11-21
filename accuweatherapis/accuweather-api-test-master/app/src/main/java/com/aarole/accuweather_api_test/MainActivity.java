package com.aarole.accuweather_api_test;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.AsynchronousChannel;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;

public class MainActivity extends AppCompatActivity {

    public static final String apikey = "qk3qYePb4UNXvq0r6xqEkpk47S6FKYlh";
    public String a, b, key;
    public String weather;
    public String tempI, tempM;
    String temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView cityTxt = findViewById(R.id.txtCity);
        final TextView tempTxt = findViewById(R.id.txtTemp);
        final TextView weatherTxt = findViewById(R.id.txtWeather);
        final TextView keyTxt = findViewById(R.id.txtKey);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        final String city = (String) bundle.get("city");

        final String temp1 = "https://dataservice.accuweather.com/locations/v1/cities/search?apikey=" + apikey + "&q=" + city;

        cityTxt.setText(city);

        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, Void> task1 = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try{
                    key = getLocationKey(temp1);
                    System.out.println(key);
                }
                catch(IOException e){
                    e.printStackTrace();
                }
                catch (JSONException ex){
                    //key = "JSONException";
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                keyTxt.setText(key);
                temp = "https://dataservice.accuweather.com/currentconditions/v1/" + key + "?apikey=" + apikey;

                @SuppressLint("StaticFieldLeak")
                AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        try{
                            setWeather(getWeatherText(temp));
                            tempI = getImperialTemperature(temp);
                            System.out.println(getImperialTemperature(temp));
                            System.out.println(weather);
                        }
                        catch(IOException e){
                            e.printStackTrace();
                        }
                        catch (JSONException ex){
                            //key = "JSONException";
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        weatherTxt.setText(weather);
                        tempTxt.setText(tempI);
                    }
                };
                task.execute();
            }
        };
        task1.execute();

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                finish();
            }
        });
    }

    public JSONArray getJSONArrayFromURL(String urlString) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );
        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();

        String jsonString = sb.toString();
        System.out.println("JSON: " + jsonString);

        return new JSONArray(jsonString);
    }

    public String getLocationKey(String url) throws IOException, JSONException{
        String key = getJSONArrayFromURL(url).getJSONObject(0).getString("Key");
        return key;
    }

    public String getWeatherText(String url) throws IOException, JSONException{

        String weather1 = getJSONArrayFromURL(url).getJSONObject(0).getString("WeatherText");

        return weather1;
    }

    public String getImperialTemperature(String url) throws IOException, JSONException{
        Double temp2 = getJSONArrayFromURL(url).getJSONObject(0).getJSONObject("Temperature").getJSONObject("Imperial").getDouble("Value");
        return Double.toString(temp2);
    }

    public String getMetricTemperature(String url) throws IOException, JSONException{
        Double temp2 = getJSONArrayFromURL(url).getJSONObject(0).getJSONObject("Temperature").getJSONObject("Metric").getDouble("Value");
        return Double.toString(temp2);
    }

    public void setWeather(String weatherTemp){
        this.weather = weatherTemp;
    }
}
