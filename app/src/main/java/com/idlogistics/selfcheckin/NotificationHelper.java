package com.idlogistics.selfcheckin;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.core.app.NotificationCompat;

public class NotificationHelper {

    private static final String CHANNEL_ID = "default_channel_id";
    private static final String CHANNEL_NAME = "Notificações Padrão";

    public static void showTemporaryNotification(
            final Context context,
            String title,
            String message,
            final int notificationId,
            long durationMillis
    ) {
        final NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Cria o canal de notificação (Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setBypassDnd(true);
            notificationManager.createNotificationChannel(channel);
        }

        // Cria a notificação
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        // Exibe a notificação
        notificationManager.notify(notificationId, notification);

        // Agenda a remoção após o tempo especificado
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                notificationManager.cancel(notificationId);
            }
        }, durationMillis);
    }

    // Versão com tempo padrão (5 segundos)
    public static void showTemporaryNotification(
            Context context,
            String title,
            String message,
            int notificationId
    ) {
        showTemporaryNotification(context, title, message, notificationId, 5000L);
    }
}

