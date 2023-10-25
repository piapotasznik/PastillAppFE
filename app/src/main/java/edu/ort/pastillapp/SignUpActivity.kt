package edu.ort.pastillapp

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.ort.pastillapp.databinding.ActivitySignUpBinding
import edu.ort.pastillapp.models.User
import edu.ort.pastillapp.models.UserRepository

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
        return when {
            TextUtils.isEmpty(name) || TextUtils.getTrimmedLength(name) > 50 -> {
                binding?.txtName?.error = "Ingresar nombre"
                false
            }

            TextUtils.isEmpty(lastName) || TextUtils.getTrimmedLength(lastName) > 50 -> {
                binding?.txtLastName?.error = "Ingresar apellido"
                false
            }

            TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding?.txtEmail?.error = "Ingresar mail valido"
                false
            }

            TextUtils.getTrimmedLength(email) > 50 -> {
                binding?.txtEmail?.error = "Ingresar mail valido"
                false
            }

            TextUtils.getTrimmedLength(password) < 6 -> {
                binding?.txtPass?.error = "Ingresar contraseña de al menos 6 caracteres"
                false
            }

            TextUtils.isEmpty(password) -> {
                binding?.txtPass?.error = "Ingresar contraseña"
                false
            }

            TextUtils.isEmpty(password2) -> {
                binding?.txtPass2?.error = "Repetir contraseña"
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