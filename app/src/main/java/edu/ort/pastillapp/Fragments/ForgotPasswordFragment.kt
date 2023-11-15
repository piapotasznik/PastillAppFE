package edu.ort.pastillapp.Fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.ort.pastillapp.R
import edu.ort.pastillapp.Services.ActivityServiceApiBuilder
import edu.ort.pastillapp.databinding.FragmentForgotPasswordBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.IOException

class ForgotPasswordFragment : BaseFragment() {

    private lateinit var binding: FragmentForgotPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)

        binding?.btnForgotPasword?.setOnClickListener {
            resetPassword()
        }
        return binding.root
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
                            showToast("Email enviado para restablecer contraseña")
                            findNavController().navigate(R.id.action_forgotPasswordFragment_to_initFragment)
                        } else {
                            showToast("Error en el envío de correo para restablecer contraseña")
                        }
                    }
                } else {
                    // Manejar caso donde el correo electrónico no existe
                    showToast("El correo electrónico no está registrado")
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