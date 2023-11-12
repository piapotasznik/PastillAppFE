package edu.ort.pastillapp.Activities

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import edu.ort.pastillapp.Helpers.SharedPref
import edu.ort.pastillapp.Helpers.UserSingleton
import edu.ort.pastillapp.Models.UserRepository
import edu.ort.pastillapp.databinding.ActivityLogInBinding

class LogInActivity: BaseActivity() {

    private var binding: ActivityLogInBinding? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        auth = Firebase.auth

        binding?.txtForgotPassword?.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
            finish()
        }

        binding?.txtSignUp?.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

        binding?.btnLogIn?.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val email = binding?.txtEmail?.text.toString()
        val password = binding?.txtPass?.text.toString()

        if (validateForm(email, password)) {
            showProgressBar()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser

                        // Obtengo el token FCM del dispositivo.
                        if (user != null && user.isEmailVerified) {
                            Log.d(
                                ContentValues.TAG,
                                "Usuario autenticado y correo electrónico verificado."
                            )
                            UserSingleton.currentUser = user
                            UserSingleton.currentUserEmail = email
                            SharedPref.write(SharedPref.EMAIL, email)
                            // Obtengo el token FCM del dispositivo.
                            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val token = task.result
                                    SharedPref.write(SharedPref.TOKEN, token.toString())
                                    // Llama a la función para enviar el token al backend
                                    UserRepository(this).sendTokenToServer(email,
                                        onSuccess = {
                                            // Manejar éxito
                                            Log.d(
                                                "LoginFragment",
                                                "Token enviado con éxito al servidor"
                                            )
                                        },
                                        onError = {
                                            // Manejar error
                                            Log.e(
                                                "LoginFragment",
                                                "Error al enviar el token al servidor"
                                            )
                                        }
                                    )
                                } else {
                                    // Manejar el error al obtener el token
                                    Log.e(
                                        ContentValues.TAG,
                                        "Error al obtener el token FCM: ${task.exception}"
                                    )
                                }
                            }

                            val intent = Intent(this, HomeScreenActivity::class.java)
                            intent.putExtra(
                                "user",
                                user
                            )
                            startActivity(intent)
                            finish()
                            hideProgressBar()
                        } else {
                            // El correo electrónico no está verificado, mostrar un mensaje o realizar alguna acción
                            hideProgressBar()
                            showToast(
                                this,
                                "Por favor, verifica tu dirección de correo electrónico antes de iniciar sesión."
                            )
                        }
                    } else {
                        hideProgressBar()
                        showToast(this, "No se pudo iniciar sesion, intente de nuevo mas tarde")
                    }
                }
        }
    }

    private fun validateForm(email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding?.txtEmail?.error = "Ingresar mail valido"
                false
            }

            TextUtils.isEmpty(password) -> {
                binding?.txtPass?.error = "Ingresar contraseña"
                false
            }

            else -> {
                true
            }
        }
    }
}