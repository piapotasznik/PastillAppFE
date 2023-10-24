package edu.ort.pastillapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.ort.pastillapp.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {

    private var binding: ActivityForgotPasswordBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}