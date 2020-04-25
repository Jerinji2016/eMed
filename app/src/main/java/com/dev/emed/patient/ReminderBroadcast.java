package com.dev.emed.patient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.dev.emed.R;

public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        int id = Integer.parseInt(intent.getStringExtra("id"));
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context, "notifyMedicine")
                .setSmallIcon(R.drawable.ic_menu_prescription)
                .setContentTitle("Time to have your Medicine")
                .setContentText("Have 1 glass of water now or you die")
                .setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManagerCompat nManager = NotificationManagerCompat.from(context);

        nManager.notify(id, nBuilder.build());
    }
}
