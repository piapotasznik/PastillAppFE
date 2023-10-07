package edu.ort.pastillapp.ui.profile
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import edu.ort.pastillapp.databinding.FragmentProfileBinding
import edu.ort.pastillapp.models.ApiUserResponse
import edu.ort.pastillapp.models.Token
import edu.ort.pastillapp.services.ActivityServiceApiBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var databaseReference: DatabaseReference

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

//        val textView: TextView = binding.textDashboard
//        dashboardViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        val buttonSaveEmergencyContact = binding.saveBtn
        buttonSaveEmergencyContact.setOnClickListener {
            saveEmergencyContact()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun saveEmergencyContact() {
        val emailContactoEmergencia = binding.myProfileEmergencyContact.text.toString()

        // Creo una instancia de Retrofit
        val userService = ActivityServiceApiBuilder.create()

        // Cheqeo en servidor si tiene el correo
        userService.getUserEmail(emailContactoEmergencia).enqueue(object : Callback<ApiUserResponse> {
            override fun onResponse(call: Call<ApiUserResponse>, response: Response<ApiUserResponse>) {
                if (response.isSuccessful) {
                    // El correo existe en la base de datos
                    val user = response.body()
                    if (user != null) {
                        val name = user.name
                        val lastName = user.lastName
                        val email = user.email

                        val alertDialog = AlertDialog.Builder(requireContext())
                        alertDialog.setTitle("Confirmación")
                        alertDialog.setMessage("¿Desea agregar a $name $lastName como contacto de referencia?")

                        alertDialog.setPositiveButton("Sí") { _, _ ->

                            // Obtengo el token del dispositivo del contacto de emergencia
                            val tokenContactoEmergencia = "TOKEN_DEL_CONTACTO_DE_EMERGENCIA"

                            val notificationData = mapOf(
                                "title" to "Solicitud de contacto de emergencia",
                                "body" to "El usuario ${user.name} ${user.lastName} desea agregarte como contacto de emergencia."
                            )
                            // 3. Crea un mensaje FCM con los datos de la notificación
                            val solicitudMessage  = RemoteMessage.Builder(tokenContactoEmergencia)
                                .setData(notificationData)
                                .build()

                            // 4. Intenta enviar el mensaje FCM
                            try {
                                FirebaseMessaging.getInstance().send(solicitudMessage )
                                Toast.makeText(requireContext(), "Petición enviada", Toast.LENGTH_SHORT).show()
                                // La notificación se envió correctamente
                                // Aquí puedes realizar las acciones adicionales, como actualizar la base de datos y notificar al usuario


                            } catch (e: Exception) {
                                // Hubo un error al enviar la notificación
                                Toast.makeText(requireContext(), "Error al enviar la solicitud", Toast.LENGTH_SHORT).show()
                            }
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
                Toast.makeText(requireContext(), "Error de red", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun sendTokenToServer(token: String, userEmail: String) {
        // Llama a la función para enviar el token al backend utilizando Retrofit u otra biblioteca que prefieras.
        // Aquí debes hacer la solicitud HTTP al servidor para enviar el token y el correo electrónico.

        val userService = ActivityServiceApiBuilder.create()

        val tokenData = Token(deviceToken = token, userEmail = userEmail)
        val request = userService.sendTokenToServer(tokenData)

        request.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Token enviado al servidor", Toast.LENGTH_SHORT).show()
                } else {
                    // Hubo un error al enviar el token al servidor
                    Toast.makeText(requireContext(), "Error al enviar el token al servidor", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "Error de red al enviar el token", Toast.LENGTH_SHORT).show()
            }
        })
    }


}