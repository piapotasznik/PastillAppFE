package edu.ort.pastillapp.Helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationUtils {
    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Configura el canal de notificación predeterminado
            val defaultChannel = NotificationChannel(
                "ppal",
                "canal_de_notificacion",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            defaultChannel.description = "Descripción del Canal Predeterminado"
            notificationManager.createNotificationChannel(defaultChannel)

            // Puedes agregar más canales de notificación si es necesario aquí
        }
    }
}