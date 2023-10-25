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
import edu.ort.pastillapp.Helpers.UserSingleton
import edu.ort.pastillapp.databinding.ActivityLogInBinding
import edu.ort.pastillapp.Models.UserRepository

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
                        Log.d(ContentValues.TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        UserSingleton.currentUser = user

                        // Actualiza la variable global con el correo y el ID del usuario
                        UserSingleton.currentUser = user
                        UserSingleton.currentUserEmail = email

                        // En la primera actividad, al crear el Intent
                        val intent = Intent(this, HomeScreenActivity::class.java)
                        intent.putExtra(
                            "user",
                            user
                        )
                        startActivity(intent)

                        // Obtengo el token FCM del dispositivo.
                        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val token = task.result

                                // Llama a la función para enviar el token al backend
                                UserRepository(this).sendTokenToServer(email,
                                    onSuccess = {
                                        // Manejar éxito
                                        Log.d("LoginFragment", "Token enviado con éxito al servidor")

                                        // Continúa con la navegación u otras acciones necesarias después del inicio de sesión
                                        val intent = Intent(this, HomeScreenActivity::class.java)
                                        intent.putExtra("user", user)
                                        startActivity(intent)
                                    },
                                    onError = {
                                        // Manejar error
                                        Log.e("LoginFragment", "Error al enviar el token al servidor")
                                    }
                                )
                            } else {
                                // Manejar el error al obtener el token
                                Log.e(
                                    ContentValues.TAG, "Error al obtener el token FCM: ${task.exception}"
                                )
                            }
                        }
                        finish()
                        hideProgressBar()
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