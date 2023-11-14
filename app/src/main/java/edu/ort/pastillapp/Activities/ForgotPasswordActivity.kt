package edu.ort.pastillapp.Activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.ort.pastillapp.Helpers.SharedPref
import edu.ort.pastillapp.Helpers.UserSingleton
import edu.ort.pastillapp.Models.ApiUserResponse
import edu.ort.pastillapp.Models.ReminderLogToday
import edu.ort.pastillapp.Services.ActivityServiceApiBuilder
import edu.ort.pastillapp.databinding.ActivityForgotPasswordBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

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

            runBlocking {
                val emailExists = checkEmailInDataBase(email)
                hideProgressBar()

                if (emailExists) {
                    auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            showToast(this@ForgotPasswordActivity, "Email enviado para restablecer contraseña")
                            startActivity(Intent(this@ForgotPasswordActivity, InitActivity::class.java))
                            finish()
                        } else {
                            showToast(this@ForgotPasswordActivity, "Error en el envío de correo para restablecer contraseña")
                        }
                    }
                } else {
                    // Manejar caso donde el correo electrónico no existe
                    showToast(this@ForgotPasswordActivity, "El correo electrónico no está registrado")
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

    private suspend fun checkEmailInDataBase(email: String): Boolean = withContext(Dispatchers.IO) {
        val service = ActivityServiceApiBuilder.create()
        try {
            val response = service.getUserEmail(email).execute()
            response.isSuccessful && response.body() != null
        } catch (e: IOException) {
            false
        }
    }







}