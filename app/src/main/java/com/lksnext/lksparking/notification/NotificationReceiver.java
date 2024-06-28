package com.lksnext.lksparking.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.lksnext.lksparking.R;
import com.lksnext.lksparking.view.activity.MainActivity;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Broadcast recibido");
        // Obtén los datos de la reserva desde el Intent
        String notificationId = intent.getStringExtra("notification_id");
        String message = intent.getStringExtra("message");

        Log.i(TAG, "ID: " + notificationId);
        Log.i(TAG, "Mensaje: " + message);

        // Crea un PendingIntent para abrir la actividad de la aplicación cuando se toque la notificación
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Crea el NotificationChannel si es necesario (Android 8.0+)
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("reservation_channel", "Reservation Notifications", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        // Construye la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "reservation_channel")
                .setSmallIcon(R.drawable.baseline_circle_notifications_24)
                .setContentTitle("Reserva Próxima")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Muestra la notificación
        notificationManager.notify(1, builder.build());
        Log.i(TAG, "Notificación mostrada");
    }
}
