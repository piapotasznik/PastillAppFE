package edu.ort.pastillapp.Activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.ort.pastillapp.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity: BaseActivity() {

    private var binding: ActivityForgotPasswordBinding? = null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnForgotPasword?.setOnClickListener {
            resetPassword()
        }
    }

    private fun resetPassword() {
        val email = binding?.txtEmail?.text.toString()
        if (validate(email)) {
            showProgressBar()
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        hideProgressBar()
                        showToast(this, "Email enviado para resetear contraseña")
                        startActivity(Intent(this, InitActivity::class.java))
                        finish()
                    } else {
                        hideProgressBar()
                        showToast(this, "Error en el envio de mail para resetear contraseña")
                    }
                }
        }
    }

    private fun validate(email: String): Boolean {
        return when {
            TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding?.txtEmail?.error = "Ingresar un email valido"
                false
            }

            else -> { true }
        }
    }
}