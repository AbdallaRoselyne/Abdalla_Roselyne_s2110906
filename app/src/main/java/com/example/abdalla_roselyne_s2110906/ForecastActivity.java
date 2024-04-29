package com.example.abdalla_roselyne_s2110906;

//
// Name                Roselyne Abdalla Osundwa
// Student ID           s2110906
// Programme of Study   Computing
//

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ForecastActivity extends AppCompatActivity {

    private ExecutorService executorService;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        boolean isDarkModeEnabled = sharedPreferences.getBoolean("isDarkModeEnabled", false);
        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Check the current orientation and set the appropriate layout
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_landscape_forecast);
        } else {
            setContentView(R.layout.activity_forecast);
        }


        ImageView settingsIcon = findViewById(R.id.settingsIcon);

        // Set a click listener on the settings icon
        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the SettingsActivity when the settings icon is clicked
                Intent intent = new Intent(ForecastActivity.this, settings.class);
                startActivity(intent);
            }
        });


        String locationName = getIntent().getStringExtra("locationName");
        String locationId = getIntent().getStringExtra("locationId");
        Log.d("ForecastActivity", "Location ID: " + locationId);

        TextView locationDisplayTextView = findViewById(R.id.locationDisplay);
        locationDisplayTextView.setText("" + locationName);

        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ForecastFetcher fetcher = new ForecastFetcher();
                    if (locationId != null) {
                        InputStream inputStream = fetcher.fetchForecastDataAsStream(locationId);
                        List<ForecastData> forecastDataList = fetcher.parseForecastData(inputStream);
                        inputStream.close(); // Close the stream after use

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (forecastDataList != null && !forecastDataList.isEmpty()) {
                                    displayForecastData(forecastDataList);
                                } else {
                                    Log.e("ForecastActivity", "No forecast data available");
                                }
                            }
                        });
                    } else {
                        Log.e("ForecastActivity", "Location ID is null, cannot fetch forecast data");
                    }
                } catch (Exception e) {
                    Log.e("ForecastActivity", "Error fetching or parsing forecast data", e);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown(); // Shut down the ExecutorService
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Check the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_landscape_forecast);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_forecast);
        }
    }

    private void displayForecastData(List<ForecastData> forecastDataList) {
    // Today's forecast
        ForecastData todayForecast = forecastDataList.get(0);
        TextView todayTitle = findViewById(R.id.todayTitle);
        TextView todayMaxTemperature = findViewById(R.id.todayMaxTemperature);
        TextView todayMinTemperature = findViewById(R.id.todayMinTemperature);
        TextView todayWind = findViewById(R.id.todayWind);
        TextView todayForecastDescription = findViewById(R.id.todayDescription); // New TextView for the forecast description

        todayTitle.setText("Today");
        todayMaxTemperature.setText("Max: " + todayForecast.getMaxTemperature() + "°C");
        todayMinTemperature.setText("Min: " + todayForecast.getMinTemperature() + "°C");
        todayWind.setText("Wind: " + todayForecast.getWindDirection() + ", " + todayForecast.getWindSpeed());
        todayForecastDescription.setText(todayForecast.getForecastDescription()); // Set the forecast description

        // Tomorrow's forecast
        ForecastData tomorrowForecast = forecastDataList.get(1); // Get the forecast data for tomorrow

        TextView tomorrowTitle = findViewById(R.id.tomorrowTitle);
        TextView tomorrowMaxTemperature = findViewById(R.id.tomorrowMaxTemperature);
        TextView tomorrowMinTemperature = findViewById(R.id.tomorrowMinTemperature);
        TextView tomorrowWind = findViewById(R.id.tomorrowWind);
        TextView tomorrowForecastDescription = findViewById(R.id.tomorrowDescription);

        // Set the day for tomorrow
        tomorrowTitle.setText(tomorrowForecast.getForecastDate()); // This will display the specific day for tomorrow

        // Set the other forecast details
        tomorrowMaxTemperature.setText("Max: " + tomorrowForecast.getMaxTemperature() + "°C");
        tomorrowMinTemperature.setText("Min: " + tomorrowForecast.getMinTemperature() + "°C");
        tomorrowWind.setText("Wind: " + tomorrowForecast.getWindDirection() + ", " + tomorrowForecast.getWindSpeed());
        tomorrowForecastDescription.setText(tomorrowForecast.getForecastDescription());

        // Assuming this code is inside an Activity or Fragment where the TextViews are present
        ForecastData theDayAfterForecast = forecastDataList.get(2); // Get the forecast data for "The Day After Tomorrow"

        TextView theDayAfterTitle = findViewById(R.id.thedayafterTitle);
        TextView theDayAfterMaxTemperature = findViewById(R.id.thedayafterMaxTemperature);
        TextView theDayAfterMinTemperature = findViewById(R.id.thedayafterMinTemperature);
        TextView theDayAfterWind = findViewById(R.id.thedayafterWind);
        TextView theDayAfterForecastDescription = findViewById(R.id.thedayafterDescription); // TextView for the forecast description

        theDayAfterTitle.setText(theDayAfterForecast.getForecastDate()); // This will display the specific day for tomorrow

        theDayAfterMaxTemperature.setText("Max: " + theDayAfterForecast.getMaxTemperature() + "°C");
        theDayAfterMinTemperature.setText("Min: " + theDayAfterForecast.getMinTemperature() + "°C");
        theDayAfterWind.setText("Wind: " + theDayAfterForecast.getWindDirection() + ", " + theDayAfterForecast.getWindSpeed());
        theDayAfterForecastDescription.setText(theDayAfterForecast.getForecastDescription());
    }
}