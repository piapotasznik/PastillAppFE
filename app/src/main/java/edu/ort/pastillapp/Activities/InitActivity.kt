package edu.ort.pastillapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.ort.pastillapp.databinding.ActivityInitBinding

class InitActivity : AppCompatActivity() {

    private var binding: ActivityInitBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInitBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnLogIn?.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
        }

        binding?.btnSignUp?.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

        val auth = Firebase.auth

//        if (auth.currentUser != null) {
//            val intent = Intent(this, HomeScreenActivity::class.java)
//            intent.putExtra("user", auth.currentUser)
//            startActivity(intent)
//            finish()
//        }
    }
}