package com.example.agenda.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.agenda.R;

public class NotificationService extends IntentService {

    public NotificationService() {
        super("Alarm Service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Context context = getApplicationContext();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        RemoteViews remoteViews = new RemoteViews( getPackageName() , R.layout.custom_notification_task);

        String titulo = intent.getStringExtra("titulo");

        String observaciones = intent.getStringExtra("observaciones");

        remoteViews.setCharSequence(R.id.notification_title , "setText" , titulo);

        remoteViews.setCharSequence(R.id.notification_task , "setText" , observaciones);

        NotificationCompat.Builder builder = new NotificationCompat.Builder( context , context.getString(R.string.channel_id));

        builder
                .setSmallIcon(R.drawable.ic_event_black_24dp)
                .setStyle( new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView( remoteViews )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify( 1 , builder.build());

    }
}
