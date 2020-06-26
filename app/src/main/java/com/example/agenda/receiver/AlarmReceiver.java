package com.example.agenda.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.agenda.service.NotificationService;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentService = new Intent( context , NotificationService.class);

        intentService.putExtra("titulo" , intent.getStringExtra("titulo"));

        intentService.putExtra("fecha" , intent.getLongExtra("fecha" , -1));

        context.startService(intentService);
    }
}
