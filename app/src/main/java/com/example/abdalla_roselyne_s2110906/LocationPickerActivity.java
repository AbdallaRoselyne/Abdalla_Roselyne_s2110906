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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.TooltipCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationPickerActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private HashMap<String, Integer> locationMap;

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

        // Check the current orientation
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // If the orientation is landscape, set the landscape layout
            setContentView(R.layout.activity_landscape_location_picker);
        } else {
            // Otherwise, set the portrait layout
            setContentView(R.layout.activity_location_picker);
        }

        ImageView settingsIcon = findViewById(R.id.settingsIcon);

        // Set a click listener on the settings icon
        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the SettingsActivity when the settings icon is clicked
                Intent intent = new Intent(LocationPickerActivity.this, settings.class);
                startActivity(intent);
            }
        });

        // Set a tooltip for the settings icon
        TooltipCompat.setTooltipText(settingsIcon, "Settings");
        // Initialize the location map
        locationMap = new HashMap<>();
        locationMap.put("Glasgow", 2648579);
        locationMap.put("London", 2643743);
        locationMap.put("New York", 5128581);
        locationMap.put("Oman", 287286);
        locationMap.put("Mauritius", 934154);
        locationMap.put("Bangladesh", 1185241);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        EditText searchBar = findViewById(R.id.searchBar);
        searchBar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                performSearch(searchBar.getText().toString().trim());
                return true;
            }
            return false;
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Example LatLng coordinates for each city
        Map<String, LatLng> cityCoordinates = new HashMap<>();
        cityCoordinates.put("Glasgow", new LatLng(55.8642, -4.2518));
        cityCoordinates.put("London", new LatLng(51.5074, -0.1278));
        cityCoordinates.put("New York", new LatLng(40.7128, -74.0060));
        cityCoordinates.put("Oman", new LatLng(21.5022, 55.9670));
        cityCoordinates.put("Mauritius", new LatLng(-20.2855, 57.4742));
        cityCoordinates.put("Bangladesh", new LatLng(23.6850, 90.3563));

        // Add a marker for each city in the locationMap
        for (String city : locationMap.keySet()) {
            LatLng location = cityCoordinates.get(city);
            if (location != null) {
                mMap.addMarker(new MarkerOptions().position(location).title(city));
            }
        }

        // Set an OnMarkerClickListener on the GoogleMap instance
        mMap.setOnMarkerClickListener(marker -> {
            // Update the search bar with the title of the clicked marker
            EditText searchBar = findViewById(R.id.searchBar);
            searchBar.setText(marker.getTitle());
            return false; // Return false to indicate that we have not consumed the event and that we wish
            // for the default behavior to occur (which is for the camera to move such that the
            // marker is centered and for the marker's info window to open, if it has one).
        });

        // Move the camera to a default location
        LatLng defaultLocation = new LatLng(0, 0); // Placeholder for actual coordinates
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 2));
    }


    private void performSearch(String query) {
        if (query.isEmpty()) {
            return;
        }

        Integer locationId = locationMap.get(query);
        if (locationId != null) {
            new Thread(() -> {
                WeatherService weatherService = new WeatherService();
                weatherService.fetchWeatherData(String.valueOf(locationId), new WeatherService.WeatherDataCallback() {
                    @Override
                    public void onSuccess(List<WeatherData> weatherDataList) {
                        if (!weatherDataList.isEmpty()) {
                            WeatherData selectedWeatherData = weatherDataList.get(0);
                            // Ensure the locationId is set in the WeatherData object
                            selectedWeatherData.setLocationId(String.valueOf(locationId));
                            String locationName = locationMap.entrySet().stream()
                                    .filter(entry -> entry.getValue().equals(locationId))
                                    .map(Map.Entry::getKey)
                                    .findFirst()
                                    .orElse("Unknown");
                            Intent intent = new Intent(LocationPickerActivity.this, WeatherDetailActivity.class);
                            intent.putExtra("weatherData", selectedWeatherData);
                            intent.putExtra("locationName", locationName);
                            startActivity(intent);
                            Log.d("LocationPickerActivity", "Location ID: " + locationId);
                        } else {
                            runOnUiThread(() -> {
                                // Show message indicating no data found
                                Toast.makeText(LocationPickerActivity.this, "No data found for this location.", Toast.LENGTH_SHORT).show();
                            });
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        runOnUiThread(() -> {
                            // Show error message
                            Toast.makeText(LocationPickerActivity.this, "Error fetching weather data.", Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            }).start();
        } else {
            runOnUiThread(() -> {
                // Show message indicating invalid query
                Toast.makeText(LocationPickerActivity.this, "Location not found. Type available location", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
