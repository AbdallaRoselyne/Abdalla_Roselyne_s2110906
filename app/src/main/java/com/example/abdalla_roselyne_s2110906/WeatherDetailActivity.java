package com.example.abdalla_roselyne_s2110906;

//
// Name                Roselyne Abdalla Osundwa
// Student ID           s2110906
// Programme of Study   Computing
//

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class WeatherDetailActivity extends AppCompatActivity {
    private String locationName;
    private WeatherData weatherData;
    private List<String> locationNamesList; // List of location names
    private int currentLocationIndex = 0; // Index of the current location in the list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        boolean isDarkModeEnabled = sharedPreferences.getBoolean("isDarkModeEnabled", false);
        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Determine the current orientation
        int orientation = getResources().getConfiguration().orientation;

        // Set the content view based on the orientation
        if (orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_landscape_weather_detail);
        } else {
            setContentView(R.layout.activity_weather_detail);
        }

        ImageView settingsIcon = findViewById(R.id.settingsIcon);

        // Set a click listener on the settings icon
        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the SettingsActivity when the settings icon is clicked
                Intent intent = new Intent(WeatherDetailActivity.this, settings.class);
                startActivity(intent);
            }
        });

        locationNamesList = new ArrayList<>(WeatherService.locationNames.values());

        // The rest of your onCreate method remains the same
        TabLayout weatherOptionsTabLayout = findViewById(R.id.weatherOptionsTabLayout);
        TabLayout.Tab tabForecast = weatherOptionsTabLayout.getTabAt(0);

        weatherData = getIntent().getParcelableExtra("weatherData");
        Log.d("WeatherDetailActivity", "Location ID: " + weatherData.getLocationId());

        locationName = getIntent().getStringExtra("locationName");

        // Populate UI with weather data
        TextView locationTextView = findViewById(R.id.locationTextView);
        TextView dateTextView = findViewById(R.id.dateTextView);
        TextView temperatureTextView = findViewById(R.id.temperatureTextView);
        TextView windTextView = findViewById(R.id.windTextView);
        TextView humidityTextView = findViewById(R.id.humidityTextView);
        TextView pressureTextView = findViewById(R.id.pressureTextView);

        locationTextView.setText(locationName);
        dateTextView.setText(weatherData.getDate());
        temperatureTextView.setText("Temp: " + String.format("%.2f", weatherData.getTemperature()) + "°C");
        windTextView.setText("Wind: " + weatherData.getWindSpeed());
        humidityTextView.setText("Humidity: " + weatherData.getHumidity());
        pressureTextView.setText("Pressure: " + weatherData.getPressure());

        Log.d("WeatherDetailActivity", "Location Name: " + locationName);

        // Set an onClickListener to the "Weather Forecast" tab item
        tabForecast.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch ForecastActivity with location data
                Intent intent = new Intent(WeatherDetailActivity.this, ForecastActivity.class);
                // Pass both the locationName and locationId
                intent.putExtra("locationName", locationName);
                intent.putExtra("locationId", weatherData.getLocationId()); // Use the locationId from the WeatherData object
                startActivity(intent);
            }
        });


        // Set onClickListeners for the next and previous buttons
        findViewById(R.id.previousButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Decrement the current location index, looping back to the end if necessary
                currentLocationIndex = (currentLocationIndex - 1 + locationNamesList.size()) % locationNamesList.size();
                updateWeatherDetails();
            }
        });

        findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increment the current location index, looping back to the start if necessary
                currentLocationIndex = (currentLocationIndex + 1) % locationNamesList.size();
                updateWeatherDetails();
            }
        });
    }

    private void updateWeatherDetails() {
        locationName = locationNamesList.get(currentLocationIndex);
        String locationId = WeatherService.getLocationId(locationName);
        if (locationId != null) {
            new Thread(() -> {
                WeatherService weatherService = new WeatherService();
                weatherService.fetchWeatherData(locationId, new WeatherService.WeatherDataCallback() {
                    @Override
                    public void onSuccess(List<WeatherData> weatherDataList) {
                        if (!weatherDataList.isEmpty()) {
                            weatherData = weatherDataList.get(0);
                            runOnUiThread(() -> {
                                // Update UI with new weather data
                                TextView locationTextView = findViewById(R.id.locationTextView);
                                TextView dateTextView = findViewById(R.id.dateTextView);
                                TextView temperatureTextView = findViewById(R.id.temperatureTextView);
                                TextView windTextView = findViewById(R.id.windTextView);
                                TextView humidityTextView = findViewById(R.id.humidityTextView);
                                TextView pressureTextView = findViewById(R.id.pressureTextView);

                                locationTextView.setText(locationName);
                                dateTextView.setText(weatherData.getDate());
                                temperatureTextView.setText("Temp: " + String.format("%.2f", weatherData.getTemperature()) + "°C");
                                windTextView.setText("Wind: " + weatherData.getWindSpeed());
                                humidityTextView.setText("Humidity: " + weatherData.getHumidity());
                                pressureTextView.setText("Pressure: " + weatherData.getPressure());
                            });
                        } else {
                            runOnUiThread(() -> {
                                // Show message indicating no data found
                                Toast.makeText(WeatherDetailActivity.this, "No data found for this location.", Toast.LENGTH_SHORT).show();
                            });
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        runOnUiThread(() -> {
                            // Show error message
                            Toast.makeText(WeatherDetailActivity.this, "Error fetching weather data.", Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            }).start();
        } else {
            runOnUiThread(() -> {
                // Show message indicating invalid location
                Toast.makeText(WeatherDetailActivity.this, "Invalid location.", Toast.LENGTH_SHORT).show();
            });
        }

        // Update the intent for the "Weather Forecast" tab item to include the new location ID
        TabLayout weatherOptionsTabLayout = findViewById(R.id.weatherOptionsTabLayout);
        TabLayout.Tab tabForecast = weatherOptionsTabLayout.getTabAt(0);
        tabForecast.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch ForecastActivity with the updated location data
                Intent intent = new Intent(WeatherDetailActivity.this, ForecastActivity.class);
                // Pass both the locationName and locationId
                intent.putExtra("locationName", locationName);
                intent.putExtra("locationId", locationId); // Use the updated locationId
                startActivity(intent);
            }
        });
    }
}