package edu.ort.pastillapp.ui.profile
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.compose.ui.text.input.TextFieldValue
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
//import edu.ort.pastillapp.NotificationUtils
import edu.ort.pastillapp.databinding.FragmentProfileBinding
import edu.ort.pastillapp.models.ApiTokenResponse
import edu.ort.pastillapp.models.ApiUserResponse
import edu.ort.pastillapp.models.User
import edu.ort.pastillapp.services.ActivityServiceApiBuilder
import edu.ort.pastillapp.services.TokenService
import edu.ort.pastillapp.services.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var tokenService: TokenService

    private var _binding: FragmentProfileBinding? = null
    private var userService: UserService? = null
    private var TAG = "ProfileFragment"
    private var profileName: EditText? = null
    private var profileSurname: EditText? = null
    private var profileEmail: EditText? = null
    private var userCreatedInformation: ApiUserResponse? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inicializo la referencia a Firebase
        databaseReference = FirebaseDatabase.getInstance().reference

        // Inicializar tokenService
        tokenService = ActivityServiceApiBuilder.createToken()

//        val textView: TextView = binding.textDashboard
//        dashboardViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        this.userService = ActivityServiceApiBuilder.create()

        val buttonUpdateInformation = binding.saveBtn
        buttonUpdateInformation.setOnClickListener {
            updateUserInformation()
        }

        profileName = binding.myProfileName
        profileSurname = binding.myProfileLastName
        profileEmail = binding.myProfileEmail

        this.setUserInformation()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUserInformation() {
        this.userService?.getUserId(1)?.enqueue(object: Callback<ApiUserResponse> {
            override fun onResponse(call: Call<ApiUserResponse>, response: Response<ApiUserResponse>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        userCreatedInformation = response.body()
                        profileName?.setText(userCreatedInformation?.name)
                        profileSurname?.setText(userCreatedInformation?.lastName)
                        profileEmail?.setText(userCreatedInformation?.email)
                        profileEmail?.isEnabled = false
                    }
                }
            }

            override fun onFailure(call: Call<ApiUserResponse>, t: Throwable) {
                // Manejar errores de red o solicitud
                Toast.makeText(requireContext(), "Error de red", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUserInformation() {
        val name = profileName?.text.toString()
        val surname = profileSurname?.text.toString()

        if (name?.isNotEmpty() == true && surname?.isNotEmpty() == true) {
            val userEmail = this.userCreatedInformation?.email?.let {
                val userUpdated = User(name = name,
                    lastName = surname,
                    email = it
                )
                this.userService?.updateUser(1, userUpdated)?.enqueue(object: Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        Toast.makeText(requireContext(), "Informacion de usuario actualizada con exito", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(requireContext(), "Error de red", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
    private fun saveEmergencyContact() {
        val emailContactoEmergencia = binding.myProfileEmergencyContact.text.toString()

        // Cheqeo en servidor si tiene el correo
        this.userService?.getUserEmail(emailContactoEmergencia)?.enqueue(object : Callback<ApiUserResponse> {
            override fun onResponse(call: Call<ApiUserResponse>, response: Response<ApiUserResponse>) {
                if (response.isSuccessful) {
                    // El correo existe en la base de datos
                    val user = response.body()
                    if (user != null) {
                        val name = user.name
                        val lastName = user.lastName

                        val alertDialog = AlertDialog.Builder(requireContext())
                        alertDialog.setTitle("Confirmación")
                        alertDialog.setMessage("¿Desea agregar a $name $lastName como contacto de referencia?")

                        alertDialog.setPositiveButton("Sí") { _, _ ->
                            obtenerTokenPorEmail(emailContactoEmergencia)
                        }

                        alertDialog.setNegativeButton("No") { _, _ ->
                            // El usuario seleccionó "No", no hacemos ninguna acción adicional.
                        }

                        alertDialog.show()
                    } else {
                        // El correo no existe en la base de datos o hubo un error en la solicitud
                        Toast.makeText(
                            requireContext(),
                            "La persona no fue encontrada en la base de datos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<ApiUserResponse>, t: Throwable) {
                // Manejar errores de red o solicitud
                Log.e("SaveEmergencyContact", "Error de red", t)
                Toast.makeText(requireContext(), "Error de red", Toast.LENGTH_SHORT).show()
            }
        })
    }

private fun obtenerTokenPorEmail(emailContactoEmergencia: String) {
    val call = tokenService.getUserEmail(emailContactoEmergencia)

    call.enqueue(object : Callback<ApiTokenResponse> {
        override fun onResponse(call: Call<ApiTokenResponse>, response: Response<ApiTokenResponse>) {
            if (response.isSuccessful) {
                val apiTokenResponse = response.body()
                if (apiTokenResponse != null) {
                    val token = apiTokenResponse.deviceToken // Accede directamente a la propiedad 'token'
                    if (token != null) {
                        // Aquí puedes utilizar el objeto 'token'
                        // Por ejemplo, imprimir su valor
                        println("Token de $emailContactoEmergencia: $token")
                        enviarNotificacionPush(token)

                    } else {
                        println("NO HAY TOKEN")// Manejar el caso en el que no se encontró ningún token en ApiUserResponse
                    }
                } else {
                    // Manejar el caso en el que ApiUserResponse es nulo
                }
            } else {
                // Manejar errores de la respuesta, por ejemplo, error 404 si el usuario no existe
            }
        }

        override fun onFailure(call: Call<ApiTokenResponse>, t: Throwable) {
            // Manejar errores de conexión u otros errores
        }
    })
}
private fun enviarNotificacionPush(token: String) {
    // Construir los datos de la notificación para el usuario
//    NotificationUtils.createNotificationChannels(requireContext())
    Log.d(TAG, "Enviando notificación push...")
    Log.d(TAG, token)
    val title = "Solicitud de contacto de emergencia"
    val body = "¿Aceptar la solicitud para agregar a pepe como contacto de emergencia?"

    // Crear un mapa de datos que contendrá los detalles de la notificación

    val notificationData = mapOf(
        "title" to "Solicitud de contacto de emergencia",
        "body" to "¿Aceptar la solicitud para agregar a pepe como contacto de emergencia?"
    )

    val notificationMessage = RemoteMessage.Builder(token)
        .setData(notificationData)
        .build()

    try {
        FirebaseMessaging.getInstance().send(notificationMessage)
        // Resto de tu código
    } catch (e: Exception) {
        // Manejar el error
    }

    // Crear un mensaje FCM con los datos de la notificación
    /*val notificationMessage = RemoteMessage.Builder(token)
        .setData(notificationData)
        .build()

    // Intenta enviar el mensaje FCM
    try {
        FirebaseMessaging.getInstance().send(notificationMessage)

        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Notificación Enviada")
        alertDialog.setMessage("La notificación se ha enviado correctamente.")
        alertDialog.setPositiveButton("Aceptar") { _, _ ->
            // Aquí puedes realizar acciones adicionales si el usuario acepta la notificación enviada.
        }
        alertDialog.show()

        // Aquí puedes realizar las acciones adicionales, como actualizar la base de datos y notificar al usuario
    } catch (e: Exception) {
        // Hubo un error al enviar la notificación
        Log.e("Notificación", "Error al enviar la notificación: ${e.message}", e)
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Error")
        alertDialog.setMessage("Hubo un error al enviar la notificación.")
        Log.e(TAG, "Error al enviar la notificación: ${e.message}", e)
        alertDialog.setPositiveButton("Aceptar") { _, _ ->
            // Aquí puedes realizar acciones adicionales si el usuario acepta la notificación enviada.
        }
        alertDialog.show()

    }
}*/
}
}