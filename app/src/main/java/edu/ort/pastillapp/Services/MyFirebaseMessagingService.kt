package edu.ort.pastillapp.Services
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import edu.ort.pastillapp.R

class MyFirebaseMessagingService: FirebaseMessagingService() {
 override fun onMessageReceived(remoteMessage: RemoteMessage) {
     // Aquí puedes manejar las notificaciones cuando se reciban
     if (remoteMessage.notification != null) {
         val title = remoteMessage.notification!!.title
         val body = remoteMessage.notification!!.body

         // Puedes mostrar la notificación o realizar otras acciones aquí
         // Por ejemplo, mostrar un log con el título y el cuerpo de la notificación
         Log.d(TAG, "Título: $title")
         Log.d(TAG, "Cuerpo: $body")

         mostrarDialogoDeNotificacion(title, body)
     }
 }

    override fun onNewToken(token: String) {
        // Este método se llama cuando se genera un nuevo token o se actualiza el token
        // Aquí puedes obtener y manejar el token
        Log.d(TAG, "Token actualizado: $token")

        // Puedes hacer lo que necesites con el token, como enviarlo a tu servidor
    }

    private fun mostrarDialogoDeNotificacion(title: String?, body: String?) {
        val channelId = "canal_de_notificacion_ppal"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Si deseas, puedes crear un canal de notificación para Android Oreo y versiones posteriores
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel Name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        val notificationId = System.currentTimeMillis().toInt()

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "MyFirebaseMessagingService"
    }

}