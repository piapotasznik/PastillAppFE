package edu.ort.pastillapp
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

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
        }
    }

    override fun onNewToken(token: String) {
        // Este método se llama cuando se genera un nuevo token o se actualiza el token
        // Aquí puedes obtener y manejar el token
        Log.d(TAG, "Token actualizado: $token")

        // Puedes hacer lo que necesites con el token, como enviarlo a tu servidor
    }

    companion object {
        private const val TAG = "MyFirebaseMessagingService"
    }
}