package com.dev.emed.models;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dev.emed.patient.ReminderBroadcast;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class MedicineReminder extends ReminderBroadcast {
    private static final String TAG = "MedicineReminder";
    private ArrayList<PrescriptionObject> medObj;
    private AlarmManager alarmManager;
    private DataSnapshot reminderTime;
    private ArrayList<PendingIntent> intentList;
    Context context;
    int intentId = 0;
    View view;

    public MedicineReminder(ArrayList<PrescriptionObject> medObj, DataSnapshot reminderTime, Context context, View view) {
        this.medObj = new ArrayList<>(medObj);
        this.intentList = new ArrayList<>();
        this.reminderTime = reminderTime;
        this.context = context;
        this.view = view;
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Medicine Notifications";
            String description = "Channel for eMed Notification";

            NotificationChannel channel = new NotificationChannel("notifyMedicine", name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void setReminder() {
        Log.d(TAG, "onDataChange: " + medObj);
        Log.d(TAG, "onDataChange: " + reminderTime.getValue());

        intentList = new ArrayList<>();

        int bHr = (int) ((long) reminderTime.child("breakfast").child("hour").getValue()),
                bMin = (int) ((long) reminderTime.child("breakfast").child("min").getValue()),
                lHr = (int) ((long) reminderTime.child("lunch").child("hour").getValue()),
                lMin = (int) ((long) reminderTime.child("lunch").child("min").getValue()),
                dHr = (int) ((long) reminderTime.child("dinner").child("hour").getValue()),
                dMin = (int) ((long) reminderTime.child("dinner").child("min").getValue());


        for (PrescriptionObject item : medObj) {
            Calendar startDate = Calendar.getInstance();

            if (startDate.get(Calendar.HOUR_OF_DAY) > bHr || (startDate.get(Calendar.HOUR_OF_DAY) == bHr && startDate.get(Calendar.MINUTE) >= bMin))
                startDate.add(Calendar.DAY_OF_YEAR, 1);

            //  Make up Message for notification
            String message = "Please have " + item.medDose + " of " + item.medName;

            switch (item.medTime) {
                case "OD":     //  Once a Day          -M
                    Calendar rem = (Calendar) startDate.clone();
                    rem.set(Calendar.HOUR_OF_DAY, bHr);
                    rem.set(Calendar.MINUTE, bMin);

                    if (item.medFood.equals("AC")) {
                        rem.add(Calendar.MINUTE, -30);
                        message += " before you have your Food";
                    } else {
                        rem.add(Calendar.MINUTE, 15);
                        message += " if you'e had your Food";
                    }
                    setNotification(item, message, rem);
                    break;
                case "BiD":    //  Twice a Day         -M, N
                    Calendar rem1 = (Calendar) startDate.clone();
                    Calendar rem2 = (Calendar) startDate.clone();

                    rem1.set(Calendar.HOUR_OF_DAY, bHr);
                    rem1.set(Calendar.MINUTE, bMin);

                    rem2.set(Calendar.HOUR_OF_DAY, dHr);
                    rem2.set(Calendar.MINUTE, dMin);

                    if (item.medFood.equals("AC")) {
                        rem1.add(Calendar.MINUTE, -30);
                        rem2.add(Calendar.MINUTE, -30);
                        message += " before you have your Food";
                    } else {
                        rem1.add(Calendar.MINUTE, 15);
                        rem2.add(Calendar.MINUTE, 15);
                        message += " if you'e had your Food";
                    }
                    setNotification(item, message, rem1);
                    setNotification(item, message, rem2);
                    break;
                case "TiD":    //  Thrice a Day        -M, A, N
                    Calendar rem3 = (Calendar) startDate.clone();
                    Calendar rem4 = (Calendar) startDate.clone();
                    Calendar rem5 = (Calendar) startDate.clone();

                    rem3.set(Calendar.HOUR_OF_DAY, bHr);
                    rem3.set(Calendar.MINUTE, bMin);

                    rem4.set(Calendar.HOUR_OF_DAY, lHr);
                    rem4.set(Calendar.MINUTE, lMin);

                    rem5.set(Calendar.HOUR_OF_DAY, dHr);
                    rem5.set(Calendar.MINUTE, dMin);

                    if (item.medFood.equals("AC")) {
                        rem3.add(Calendar.MINUTE, -30);
                        rem4.add(Calendar.MINUTE, -30);
                        rem5.add(Calendar.MINUTE, -30);
                        message += " before you have your Food";
                    } else {
                        rem3.add(Calendar.MINUTE, 15);
                        rem4.add(Calendar.MINUTE, 15);
                        rem5.add(Calendar.MINUTE, 15);
                        message += " if you'e had your Food";
                    }
                    setNotification(item, message, rem3);
                    setNotification(item, message, rem4);
                    setNotification(item, message, rem5);
                    break;
                case "QiD":    //  Four times a day    -First time then 6 hours increasing
                    Toast.makeText(context, item.medName+" will be set with 6 Hours Gap \nand not based on Food Time", Toast.LENGTH_LONG).show();
                    Calendar qRem = (Calendar) startDate.clone();

                    if (item.medFood.equals("AC"))
                        message += " before you have your Food";
                     else
                        message += " if you'e had your Food";

                    int limit = Integer.parseInt(item.medDur);
                    for(int it = 0; it<(limit*4); it++) {
                        Intent nIntent = new Intent(context, ReminderBroadcast.class);
                        nIntent.putExtra("id", "" + intentId);
                        nIntent.putExtra("message", message);
                        nIntent.putExtra("date", qRem.getTime().toString());
                        PendingIntent nPendingIntent = PendingIntent.getBroadcast(context, intentId, nIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        intentList.add(nPendingIntent);
                        alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

                        assert alarmManager != null;
                        alarmManager.set(AlarmManager.RTC_WAKEUP,
                                qRem.getTime().getTime(),
                                nPendingIntent);

                        Log.d(TAG, "onCheckedChanged: Reminder set for " + item.medName + " at " + qRem.getTime() + " - " + qRem.get(Calendar.DATE));

                        qRem.add(Calendar.HOUR, 6);
                        intentId++;
                    }
                    break;
            }
            Snackbar snackbar = Snackbar.make(view, "Reminders Set" ,Snackbar.LENGTH_LONG);
            snackbar.show();
            Log.d(TAG, "onCheckedChanged: MedName: " + item.medName + " Duration: " + item.medDur + "Days-" + item.medTime);
            Log.d(TAG, "onCheckedChanged: " + intentId + " Reminders set for " + item.medName + " for " + item.medDur + " Days");
        }
    }

    public void removeReminder() {
        if (!intentList.isEmpty()) {
            int i = 1;
            for (PendingIntent item : intentList) {
                alarmManager.cancel(item);
                i++;
            }
            Log.d(TAG, "removeReminder: "+i+" Remainders Unset");
        } else
            Log.d(TAG, "onCheckedChanged: Pending List Empty!");
    }

    private void setNotification(@NonNull PrescriptionObject item, String message, Calendar rem) {
        int limit = Integer.parseInt(item.medDur);
        for (int it = 0; it < limit; it++) {

            Intent nIntent = new Intent(context, ReminderBroadcast.class);
            nIntent.putExtra("id", "" + intentId);
            nIntent.putExtra("message", message);
            nIntent.putExtra("date", rem.getTime().toString());
            PendingIntent nPendingIntent = PendingIntent.getBroadcast(context, intentId, nIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            intentList.add(nPendingIntent);
            alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

            assert alarmManager != null;
            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    rem.getTime().getTime(),
                    nPendingIntent);

            Log.d(TAG, "onCheckedChanged: Reminder set for " + item.medName + " at " + rem.getTime() + " - " + rem.get(Calendar.DATE));

            rem.add(Calendar.DAY_OF_YEAR, 1);
            intentId++;
        }
    }
}
