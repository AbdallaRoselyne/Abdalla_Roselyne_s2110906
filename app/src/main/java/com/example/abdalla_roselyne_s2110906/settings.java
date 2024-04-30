package com.example.abdalla_roselyne_s2110906;

//
// Name                Roselyne Abdalla Osundwa
// Student ID           s2110906
// Programme of Study   Computing
//

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.app.AppCompatDelegate;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.widget.Toast;
import java.util.Calendar;

public class settings extends AppCompatActivity {

    private SwitchCompat darkModeSwitch;
    private Button exitButton;
    private TimePicker timePicker;
    private Button setUpdateTimeButton;
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
        timePicker = findViewById(R.id.timePicker);
        setUpdateTimeButton = findViewById(R.id.setUpdateTimeButton);

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

        // Set up the TimePicker and Button
        setUpdateTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();
                // Save the selected time
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("updateTime1Hour", hour);
                editor.putInt("updateTime1Minute", minute);
                editor.apply();

                // Schedule the alarm for the selected time
                scheduleDataUpdateAlarm(hour, minute);
            }
        });

        // Schedule alarms for data updates
        scheduleDataUpdateAlarms();
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

    private void scheduleDataUpdateAlarms() {
        // Retrieve the selected update times from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        int hour1 = sharedPreferences.getInt("updateTime1Hour", 8); // Default to 08:00
        int minute1 = sharedPreferences.getInt("updateTime1Minute", 0);
        int hour2 = sharedPreferences.getInt("updateTime2Hour", 20); // Default to 20:00
        int minute2 = sharedPreferences.getInt("updateTime2Minute", 0);

        // Schedule alarms for the selected times
        scheduleDataUpdateAlarm(hour1, minute1);
        scheduleDataUpdateAlarm(hour2, minute2);
    }

    private void scheduleDataUpdateAlarm(int hour, int minute) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, DataUpdateReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        // Set the alarm to repeat daily at the specified time
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // Schedule the alarm
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }
}
