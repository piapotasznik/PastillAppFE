package edu.ort.pastillapp.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.ort.pastillapp.Helpers.SharedPref
import edu.ort.pastillapp.Helpers.UserSingleton
import edu.ort.pastillapp.Models.ApiContactEmergencyList
import edu.ort.pastillapp.Models.EmergencyRequestData
import edu.ort.pastillapp.Services.ActivityServiceApiBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactRequestsViewModel: ViewModel() {

    private val email: String = SharedPref.read(SharedPref.EMAIL, UserSingleton.currentUserEmail.toString())
    val emergencyContacts = MutableLiveData<MutableList<EmergencyRequestData>?>()


    fun updateEmergencyRequestList() {
        val userService = ActivityServiceApiBuilder.create()
        val call = userService.getEmergencyRequests(email)

        call.enqueue(object : Callback<ApiContactEmergencyList> {
            override fun onResponse(
                call: Call<ApiContactEmergencyList>,
                response: Response<ApiContactEmergencyList>
            ) {
                val emergencyRequestResponse = response.body()
                val emergencyRequests =
                    emergencyRequestResponse?.emergencyRequestList?.toMutableList()
                Log.d(
                    "contactRequests",
                    "Tamaño de la lista de solicitudes: ${emergencyRequests?.size ?: 0}"
                )

                if (!emergencyRequests.isNullOrEmpty()) {
                    emergencyContacts.postValue(emergencyRequests)
                }
                //                else {
//                    val textViewNoRequests = v.findViewById<TextView>(R.id.textViewNoRequests)
//                    textViewNoRequests.visibility = View.VISIBLE
//                    Log.d("contactRequests", "Llegó a 'No hay solicitudes pendientes'")
//                }
            }

            override fun onFailure(call: Call<ApiContactEmergencyList>, t: Throwable) {
                // Maneja errores de comunicación aquí
            }
        })
    }
}
