package edu.ort.pastillapp.Activities

import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import edu.ort.pastillapp.Helpers.SharedPref
import edu.ort.pastillapp.databinding.ActivityInitBinding


class InitActivity : AppCompatActivity() {

    private var binding: ActivityInitBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInitBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        checkAndRequestNotificationPermissions();
        SharedPref.init(applicationContext);

        binding?.btnLogIn?.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
        }

        binding?.btnSignUp?.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // User is signed in
            val i = Intent(this@InitActivity, HomeScreenActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            i.putExtra("user", user)
            startActivity(i)
        } else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out")
        }
    }

    private fun checkAndRequestNotificationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NotificationManager::class.java)
            val channel = notificationManager.getNotificationChannel("canal_de_notificacion_ppal")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationManager = getSystemService(NotificationManager::class.java)
                val channel =
                    notificationManager.getNotificationChannel("canal_de_notificacion_ppal")

                if (channel != null && channel.importance == NotificationManager.IMPORTANCE_NONE) {
                    // Los permisos de notificación están deshabilitados, solicitar al usuario que los habilite
                    val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                    intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.id)
                    startActivity(intent)
                }
            }
        }
    }
}