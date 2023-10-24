package edu.ort.pastillapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.ort.pastillapp.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private var binding: ActivitySignUpBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.txtSignIn?.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
        }
    }
}