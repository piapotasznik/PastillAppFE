package edu.ort.pastillapp.Models
import android.content.Context
import android.util.Log
import android.widget.Toast
import edu.ort.pastillapp.Services.ActivityServiceApiBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class ReminderLogRepository(private val context: Context) {
    private val apiService = ActivityServiceApiBuilder.createReminderLogService()

    fun obtenerReminderLog(reminderLogId: String, onSuccess: (ReminderLog) -> Unit, onError: (String) -> Unit) {
        apiService.getReminderLogById(reminderLogId.toInt()).enqueue(object : Callback<ReminderLog> {
            override fun onResponse(call: Call<ReminderLog>, response: Response<ReminderLog>) {
                if (response.isSuccessful) {
                    val reminderLog = response.body()
                    if (reminderLog != null) {
                        onSuccess.invoke(reminderLog)
                        Log.d("ReminderLogRepository", "Recordatorio obtenido con éxito del servidor.")
                    } else {
                        onError.invoke("Respuesta vacía del servidor")
                    }
                } else {
                    onError.invoke("Error al obtener el recordatorio del servidor: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ReminderLog>, t: Throwable) {
                showErrorToast("Error al comunicarse con el servidor: ${t.message}")
                onError.invoke("Error de red: ${t.message}")
            }
        })
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}