package com.example.abdalla_roselyne_s2110906;

//
// Name                Roselyne Abdalla Osundwa
// Student ID           s2110906
// Programme of Study   Computing
//

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.app.AppCompatDelegate;

public class settings extends AppCompatActivity {

    private SwitchCompat darkModeSwitch;
    private Button exitButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.settings_landscape);
        } else {
            setContentView(R.layout.settings);
        }

        darkModeSwitch = findViewById(R.id.darkModeSwitch);
        exitButton = findViewById(R.id.exitButton);

        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        boolean isDarkModeEnabled = sharedPreferences.getBoolean("isDarkModeEnabled", false);
        darkModeSwitch.setChecked(isDarkModeEnabled);

        // Apply the saved theme preference at startup
        applyTheme(isDarkModeEnabled);

        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isDarkModeEnabled", isChecked);
            editor.apply();

            // Apply the theme immediately after the user changes it
            applyTheme(isChecked);
        });


        // Set up the Exit button
        exitButton.setOnClickListener(v -> {
            // Code to handle exit action
            Toast.makeText(this, "Exiting...", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
        });
    }

    private void applyTheme(boolean isDarkModeEnabled) {
        if (isDarkModeEnabled) {
            // Apply dark theme
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            Log.d("SettingsActivity", "Dark Mode Enabled"); // Log when dark mode is enabled
            Toast.makeText(this, "Dark Mode Enabled", Toast.LENGTH_SHORT).show();
        } else {
            // Apply light theme
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Log.d("SettingsActivity", "Dark Mode Disabled"); // Log when dark mode is disabled
            Toast.makeText(this, "Dark Mode Disabled", Toast.LENGTH_SHORT).show();
        }
    }
}
