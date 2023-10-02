package edu.ort.pastillapp.models
import android.content.Context
import android.util.Log
import android.widget.Toast
import edu.ort.pastillapp.services.ActivityServiceApiBuilder
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
                Log.e("UserRepository", "Error al crear usuario en el backend: ${response.message()}")
            }
       }
        override fun onFailure(call: Call<Void>, t: Throwable) {
            showErrorToast("Error al comunicarse con el backend: ${t.message}")
       }
     })
  }
  private fun showErrorToast(message: String) {
      Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
  }


}