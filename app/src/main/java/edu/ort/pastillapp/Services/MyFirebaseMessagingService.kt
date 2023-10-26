package edu.ort.pastillapp

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import edu.ort.pastillapp.Activities.InitActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {
            val title = remoteMessage.notification?.title
            val body = remoteMessage.notification?.body
            val tipo = remoteMessage.data["tipo"]
            val reminderLogId = remoteMessage.data["id"]

            Log.d(TAG, "Título: $title")
            Log.d(TAG, "Cuerpo: $body")
            Log.d(TAG, "Tipo: $tipo")
            Log.d(TAG, "ID del ReminderLog: $reminderLogId")

            mostrarNotificacion(title, body, tipo, reminderLogId)
        }
    }

    override fun onNewToken(token: String) {
        // Este método se llama cuando se genera un nuevo token o se actualiza el token
        // Aquí puedes obtener y manejar el token
        Log.d(TAG, "Token actualizado: $token")

        // Puedes hacer lo que necesites con el token, como enviarlo a tu servidor
    }

    private fun mostrarNotificacion(
        title: String?,
        body: String?,
        tipo: String?,
        reminderLogId: String?
    ) {
        val channelId = "canal_de_notificacion_ppal"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel Name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        val notificationId = System.currentTimeMillis().toInt()

        val resultIntent = when (tipo) {
            "alarm" -> {
                // Configurar la acción para abrir el fragmento de alarma y pasar el ID
                Intent(this, InitActivity::class.java)
                    .apply { putExtra("reminderLogId", reminderLogId) }
            }

            "emergencycontact" -> {
                // Configurar la acción para abrir el fragmento de contacto de emergencia
                Intent(this, InitActivity::class.java)
            }

            else -> {
                // Configurar la acción predeterminada (puedes ajustarla según tus necesidades)
                Intent(this, InitActivity::class.java)
            }
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            resultIntent,
            PendingIntent.FLAG_IMMUTABLE
            //    PendingIntent.FLAG_UPDATE_CURRENT
        )
        notificationBuilder.setContentIntent(pendingIntent)
        notificationBuilder.setAutoCancel(true)
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "MyFirebaseMessagingService"
    }
}