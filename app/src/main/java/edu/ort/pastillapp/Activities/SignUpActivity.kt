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
import edu.ort.pastillapp.Helpers.UserSingleton
import edu.ort.pastillapp.Models.User
import edu.ort.pastillapp.Models.UserRepository
import edu.ort.pastillapp.databinding.ActivitySignUpBinding

class SignUpActivity : BaseActivity() {

    private var binding: ActivitySignUpBinding? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        auth = Firebase.auth

        binding?.txtSignIn?.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
        }

        binding?.btnSignUp?.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val name = binding?.txtName?.text.toString()
        val lastName = binding?.txtLastName?.text.toString()
        val email = binding?.txtEmail?.text.toString()
        val password = binding?.txtPass?.text.toString()
        val password2 = binding?.txtPass2?.text.toString()

        if (validateForm(name, lastName, email, password, password2)) {
            showProgressBar()
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        createUser(name, lastName, email) {
                            hideProgressBar()
                            showToast(this, "Usuario creado exitosamente")
                            startActivity(Intent(this, LogInActivity::class.java))
                            finish()
                        }
                    } else {
                        hideProgressBar()
                        showToast(this, "Usuario no creado, intente de nuevamente")
                    }
                }
        }
    }

    private fun createUser(name: String, lastName: String, email: String, function: () -> Unit) {
        Log.d(ContentValues.TAG, "createUserWithEmail:success")
        val newUser = User(name = name, lastName = lastName, email = email)
        val userRepository = UserRepository(this)
        userRepository.createUser( newUser,
            onSuccess = {
                auth.currentUser?.sendEmailVerification()
                showToast(this, "Email de verificacion enviado!")
                val user = auth.currentUser
                UserSingleton.currentUser = user
                function()
            }, onError = {
                hideProgressBar()
                showToast(this, "Error en el envio de mail")
                function()
            }
        )
    }

    private fun validateForm(name: String, lastName: String, email: String, password: String, password2: String): Boolean {
        val nameRegex = "^[a-zA-Z ]+\$".toRegex()

        return when {
            !nameRegex.matches(name) || TextUtils.getTrimmedLength(name) > 50 -> {
                binding?.txtName?.error = "Ingrese un nombre válido (sin caracteres especiales, números y máximo 50 caracteres)"
                false
            }

            !nameRegex.matches(lastName) || TextUtils.getTrimmedLength(lastName) > 50 -> {
                binding?.txtLastName?.error = "Ingrese un apellido válido (sin caracteres especiales, números y máximo 50 caracteres)"
                false
            }

            TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding?.txtEmail?.error = "Ingrese un correo válido"
                false
            }

            TextUtils.getTrimmedLength(email) > 50 -> {
                binding?.txtEmail?.error = "Ingrese un correo válido (máximo 50 caracteres)"
                false
            }

            TextUtils.getTrimmedLength(password) < 6 -> {
                binding?.txtPass?.error = "Ingrese una contraseña de al menos 6 caracteres"
                false
            }

            TextUtils.isEmpty(password) -> {
                binding?.txtPass?.error = "Ingrese una contraseña"
                false
            }

            TextUtils.isEmpty(password2) -> {
                binding?.txtPass2?.error = "Repita la contraseña"
                false
            }

            !TextUtils.equals(password, password2) -> {
                binding?.txtPass2?.error = "Las contraseñas no coinciden"
                false
            }

            else -> {
                true
            }
        }
    }
}