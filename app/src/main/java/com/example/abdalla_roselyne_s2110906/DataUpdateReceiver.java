package com.example.abdalla_roselyne_s2110906;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class DataUpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Data update triggered", Toast.LENGTH_SHORT).show();
    }
}
