package edu.ort.pastillapp.Services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import edu.ort.pastillapp.Activities.InitActivity
import edu.ort.pastillapp.R

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {
            val title = remoteMessage.notification?.title
            val body = remoteMessage.notification?.body
            val type = remoteMessage.data["tipo"]

            Log.d(TAG, "Título: $title")
            Log.d(TAG, "Cuerpo: $body")
            Log.d(TAG, "Tipo: $type")

            showNotification(title, body, type)
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Token actualizado: $token")
    }

    private fun showNotification(title: String?, body: String?, type: String?) {
        val channelId = "canal_de_notificacion_ppal"
        val notificationBuilder = buildNotification(title, body, channelId, type)
        showNotification(notificationBuilder)
    }

    private fun buildNotification(title: String?, body: String?, channelId: String, type: String?): NotificationCompat.Builder {

        val soundUri = Uri.parse("android.resource://${packageName}/${R.raw.sound}")

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(soundUri)

        val resultIntent = if (type == "POP") {
            Intent(this, InitActivity::class.java)
        } else {
            Intent(this, InitActivity::class.java)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            resultIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        notificationBuilder.setContentIntent(pendingIntent)
        notificationBuilder.setAutoCancel(true)

        return notificationBuilder
    }

    private fun showNotification(notificationBuilder: NotificationCompat.Builder) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notificationId = System.currentTimeMillis().toInt()
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "canal_de_notificacion_ppal"
            if (notificationManager.getNotificationChannel(channelId) == null) {
                val name = "Nombre del Canal"
                val descriptionText = "Descripción del Canal"
                val importance = NotificationManager.IMPORTANCE_HIGH
                val mChannel = NotificationChannel(channelId, name, importance)
                mChannel.description = descriptionText
                mChannel.enableLights(true)
                mChannel.enableVibration(true)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    mChannel.setAllowBubbles(true)
                }
                notificationManager.createNotificationChannel(mChannel)
            }
        }
    }

    companion object {
        private const val TAG = "MyFirebaseMessagingService"
    }
}