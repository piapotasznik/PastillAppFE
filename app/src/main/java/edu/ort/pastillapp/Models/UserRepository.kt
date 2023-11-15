package edu.ort.pastillapp.Models

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessaging
import edu.ort.pastillapp.Services.ActivityServiceApiBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(private val context: Context) {
    private val userService = ActivityServiceApiBuilder.create()

    fun createUser(newUser: User, onSuccess: () -> Unit, onError: () -> Unit) {
        userService.createUser(newUser).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    onSuccess.invoke()
                    Log.d("UserRepository", "Usuario creado con éxito en el backend.")
                } else {
                    onError.invoke()
                    Log.e(
                        "UserRepository",
                        "Error al crear usuario en el backend: ${response.message()}"
                    )
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showErrorToast("Error al comunicarse con el backend: ${t.message}")
            }
        })
    }

    fun sendTokenToServer(userEmail: String, onSuccess: () -> Unit, onError: () -> Unit) {
        // Obtener el token FCM del dispositivo
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result

                // Enviar el token FCM al servidor
                val tokenData = Token(deviceToken = token, userEmail = userEmail)
                Log.d("UserRepository", "Token obtenido: $tokenData")
                userService.sendTokenToServer(tokenData).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            // Token enviado con éxito
                            Log.d("UserRepository", "Token enviado al servidor con éxito.")
                            onSuccess.invoke()
                        } else {
                            // Error al enviar el token
                            Log.e(
                                "UserRepository",
                                "Error al enviar el token al servidor: ${response.message()}"
                            )
                            onError.invoke()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        // Error de red al enviar el token
                        Log.e("UserRepository", "Error de red al enviar el token: ${t.message}")
                        onError.invoke()
                    }
                })
            } else {
                // Error al obtener el token FCM
                Log.e("UserRepository", "Error al obtener el token FCM: ${task.exception}")
                onError.invoke()
            }
        }
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}