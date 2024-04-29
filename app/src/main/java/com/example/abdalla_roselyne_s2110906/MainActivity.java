package com.example.abdalla_roselyne_s2110906;

//
// Name                Roselyne Abdalla Osundwa
// Student ID           s2110906
// Programme of Study   Computing
//

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import androidx.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WeatherDataAdapter adapter;
    private WeatherService weatherService;

    private static final int LOCATION_PICKER_REQUEST_CODE = 1; // Request code for LocationPickerActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isInternetConnected()) {
            setContentView(R.layout.no_internet);

            Button retryButton = findViewById(R.id.retryButton);
            retryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isInternetConnected()) {
                        // If internet is connected, reload the main layout
                        recreate();
                    } else {
                        // Show a message or handle the case where internet is still not available
                    }
                }
            });

        } else {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setContentView(R.layout.main_landscape);
            } else {
                setContentView(R.layout.activity_main);
            }

            // Apply the saved theme preference at startup
            SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
            boolean isDarkModeEnabled = sharedPreferences.getBoolean("isDarkModeEnabled", false);
            if (isDarkModeEnabled) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            Button getStartedButton = findViewById(R.id.getStartedButton);
            getStartedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, LocationPickerActivity.class);
                    startActivityForResult(intent, LOCATION_PICKER_REQUEST_CODE); // Start LocationPickerActivity for a result
                }
            });

            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new WeatherDataAdapter(new ArrayList<WeatherData>());
            recyclerView.setAdapter(adapter);
        }
    }

    private boolean isInternetConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }

    private void applyTheme(boolean isDarkModeEnabled) {
        if (isDarkModeEnabled) {
            // Apply dark theme
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            // Apply light theme
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_PICKER_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Corrected to use getParcelableArrayListExtra
            List<WeatherData> weatherDataList = data.getParcelableArrayListExtra("weatherDataList");
            if (weatherDataList != null) {
                adapter.setWeatherDataList(weatherDataList);
            } else {
                // Handle the case where no weather data is returned
                // For example, show a message to the user
            }
        } else {
            // Handle the case where the activity result is not as expected
            // For example, show a message to the user
        }
    }

}
