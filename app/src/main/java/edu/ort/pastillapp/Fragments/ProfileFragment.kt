package edu.ort.pastillapp.Fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import edu.ort.pastillapp.Activities.InitActivity
import edu.ort.pastillapp.Helpers.SharedPref
import edu.ort.pastillapp.Helpers.UserSingleton
import edu.ort.pastillapp.Models.ApiContactEmergencyRequest
import edu.ort.pastillapp.Models.ApiUserResponse
import edu.ort.pastillapp.Models.User
import edu.ort.pastillapp.R
import edu.ort.pastillapp.Services.ActivityServiceApiBuilder
import edu.ort.pastillapp.Services.TokenService
import edu.ort.pastillapp.Services.UserService
import edu.ort.pastillapp.ViewModels.ProfileViewModel
import edu.ort.pastillapp.databinding.FragmentProfileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.core.content.ContextCompat
import android.graphics.Color
import android.content.res.ColorStateList
import android.view.MotionEvent
import android.widget.Button

class ProfileFragment : Fragment() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var tokenService: TokenService
    private lateinit var auth: FirebaseAuth

    private var _binding: FragmentProfileBinding? = null
    private var userService: UserService? = null
    private var TAG = "ProfileFragment"
    private var profileName: EditText? = null
    private var profileSurname: EditText? = null
    private var profileEmail: EditText? = null
    private var errorMsg: TextView? = null
    private var userCreatedInformation: ApiUserResponse? = null
    private var email: String? = null
    private var contEmer: EditText? = null

    //agrego
    private lateinit var saveContactBtn: Button

    //
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

        databaseReference = FirebaseDatabase.getInstance().reference

        // Inicializar tokenService
        tokenService = ActivityServiceApiBuilder.createToken()

        val buttonContacts = binding.ContactsBtn
        buttonContacts.setOnClickListener {
            // Navegar al otro fragmento
            val action = ProfileFragmentDirections.actionNavigationProfileToContactRequests2()
            findNavController().navigate(action)
        }

        userService = ActivityServiceApiBuilder.create()

        auth = Firebase.auth

        email = SharedPref.read(SharedPref.EMAIL, UserSingleton.currentUserEmail.toString())
        profileName = binding.myProfileName
        profileSurname = binding.myProfileLastName
        profileEmail = binding.myProfileEmail
        profileEmail?.isEnabled = false
        errorMsg = binding.errorMsg
        errorMsg?.isVisible = false
        contEmer = binding.myProfileEmergencyContact
        saveContactBtn = binding.saveContactBtn

        val buttonUpdateInformation = binding.saveBtn
        buttonUpdateInformation.setOnClickListener {
            updateUserInformation()
        }

        val buttonUpdateContact = binding.saveContactBtn
        buttonUpdateContact.setOnClickListener {
                saveEmergencyContact()
        }

        val buttonDeleteContact = binding.deteleContactBtn
        buttonDeleteContact.setOnClickListener(){
            deleteContact()
        }


        binding.txtSignOut.setOnClickListener {
            if (auth.currentUser != null) {
                val token = SharedPref.read(SharedPref.TOKEN, "")
                Log.e("token", token)
                deleteToken(token)
                SharedPref.delete()
                auth.signOut()
                startActivity(Intent(context, InitActivity::class.java))
                Toast.makeText(context, "Sesión cerrada exitosamente", Toast.LENGTH_SHORT).show()
            }
        }

        this.setUserInformation()


        return root
    }

    fun showToastLong(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUserInformation() {
        clearErrorMessages()
        email?.let {
            this.userService?.getUserEmail(it)?.enqueue(object : Callback<ApiUserResponse> {
                override fun onResponse(
                    call: Call<ApiUserResponse>,
                    response: Response<ApiUserResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            userCreatedInformation = response.body()
                            profileName?.setText(userCreatedInformation?.name)
                            profileSurname?.setText(userCreatedInformation?.lastName)
                            profileEmail?.setText(userCreatedInformation?.email)
                            contEmer?.setText(userCreatedInformation?.emergencyUser)

                            // AGREGO
                            // Verifica el valor de myProfileEmergencyContact para habilitar/deshabilitar el botón
                            val isEmergencyContactNotNull = userCreatedInformation?.emergencyUser != null
                            saveContactBtn.isEnabled = !isEmergencyContactNotNull
                            saveContactBtn.backgroundTintList = ColorStateList.valueOf(
                                if (isEmergencyContactNotNull) Color.GRAY
                                else ContextCompat.getColor(requireContext(), R.color.pink)
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<ApiUserResponse>, t: Throwable) {
                    // Manejar errores de red o solicitud
                    Toast.makeText(requireContext(), "Error de red", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }


    private fun updateUserInformation() {
        val userName = profileName?.text.toString()
        val userLastName = profileSurname?.text.toString()

        // Restablecer los mensajes de error y ocultar el mensaje de advertencia
        profileName?.error = null
        profileSurname?.error = null
        errorMsg?.visibility = View.INVISIBLE

        if (userName.isEmpty()) {
            profileName?.error = "Campo obligatorio"
            errorMsg?.visibility = View.VISIBLE
            errorMsg?.text = "Todos los campos deben ser completados"
        }

        if (userLastName.isEmpty()) {
            profileSurname?.error = "Campo obligatorio"
            errorMsg?.visibility = View.VISIBLE
            errorMsg?.text = "Todos los campos deben ser completados"
        }

        if (userName.length > 50) {
            profileName?.error = "El nombre no debe superar los 50 caracteres"
            errorMsg?.visibility = View.VISIBLE
            errorMsg?.text = "El nombre no debe superar los 50 caracteres"
        }

        if (userLastName.length > 50) {
            profileSurname?.error = "El apellido no debe superar los 50 caracteres"
            errorMsg?.visibility = View.VISIBLE
            errorMsg?.text = "El apellido no debe superar los 50 caracteres"
        }

        // Si no hay errores de validación, actualiza la información del usuario
        if (userName.isNotEmpty() && userLastName.isNotEmpty() &&
            userName.length <= 50 && userLastName.length <= 50) {
            // Realiza la actualización del usuario aquí
            val userInformation = this.userCreatedInformation?.let {
                val userUpdated = User(
                    name = userName,
                    lastName = userLastName,
                    email = it.email
                )
                it.userId?.let { it1 ->
                    this.userService?.updateUser(it1, userUpdated)
                        ?.enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                Toast.makeText(
                                    requireContext(),
                                    "Información de usuario actualizada con éxito",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                Toast.makeText(requireContext(), "Error de red", Toast.LENGTH_SHORT).show()
                            }
                        })
                }
            }
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun saveEmergencyContact() {
        val emailContactoEmergencia = binding.myProfileEmergencyContact.text.toString()

        if (emailContactoEmergencia.isEmpty()) {
            showMessage(requireContext(), "El campo de correo de contacto de emergencia no puede estar vacío")
            return
        }

        // Creo una instancia de Retrofit
        val userService = ActivityServiceApiBuilder.create()

        // Cheqeo en servidor si tiene el correo
        userService.getUserEmail(emailContactoEmergencia)
            .enqueue(object : Callback<ApiUserResponse> {
                override fun onResponse(
                    call: Call<ApiUserResponse>,
                    response: Response<ApiUserResponse>
                ) {
                    if (response.isSuccessful) {
                        // El correo existe en la base de datos
                        val user = response.body()
                        if (user != null) {
                            val name = user.name
                            val lastName = user.lastName

                            if (email == emailContactoEmergencia) {
                                showMessage(
                                    requireContext(),
                                    "No puedes agregar tu propio correo como contacto de emergencia"
                                )
                            } else
                                if (emailContactoEmergencia == userCreatedInformation?.emergencyUser) {
                                    showMessage(
                                        requireContext(),
                                        "El correo ya está agendado como contacto de emergencia"
                                    )
                                } else {
                                    val alertDialog = AlertDialog.Builder(requireContext())
                                    alertDialog.setTitle("Confirmación")
                                    alertDialog.setMessage("¿Desea agregar a $name $lastName como contacto de referencia?")

                                    alertDialog.setPositiveButton("Sí") { _, _ ->
                                        val contactRequest = email?.let {
                                            ApiContactEmergencyRequest(
                                                userMail = it,
                                                contactEmergencyMail = emailContactoEmergencia,
                                            )
                                        }
                                        val call = contactRequest?.let {
                                            userService.contactEmergency(
                                                it
                                            )
                                        }
                                        call?.enqueue(object : Callback<Void> {
                                            override fun onResponse(
                                                call: Call<Void>,
                                                response: Response<Void>
                                            ) {
                                                if (response.isSuccessful) {
                                                    showMessage(
                                                        requireContext(),
                                                        "Notificación enviada con éxito"
                                                    )
                                                } else {
                                                    showMessage(
                                                        requireContext(),
                                                        "Error al enviar la notificación"
                                                    )
                                                }
                                            }

                                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                                // Ocurrió un error en la solicitud (por ejemplo, problemas de red)
                                                showMessage(
                                                    requireContext(),
                                                    "Error al enviar la notificación"
                                                )
                                            }
                                        })
                                    }

                                    alertDialog.setNegativeButton("No") { _, _ ->
                                        // El usuario seleccionó "No", no hacemos ninguna acción adicional.
                                    }

                                    alertDialog.show()
                                }
                        } else {
                            // El correo no existe en la base de datos
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

    private fun deleteContact(){
        val emailToDelete = contEmer?.text.toString()

        if (emailToDelete.isEmpty()) {
            showMessage(requireContext(), "No tienes un contacto de emergencia para eliminar.")
        } else {
            val alertDialog = AlertDialog.Builder(requireContext())
            alertDialog.setTitle("Confirmación")
            alertDialog.setMessage("¿Desea eliminar a $emailToDelete como contacto de emergencia?")
            alertDialog.setPositiveButton("Sí") { _, _ ->
                email?.let {
                    this.userService?.deleteEmergencyContact(it)
                        ?.enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if (response.isSuccessful) {
                                        showMessage(requireContext(), "Contacto de emergencia eliminado con éxito.")
                                    contEmer?.text = null
                                    setUserInformation()
                                } else {
                                    showMessage(
                                        requireContext(),
                                        "Error al eliminar el contacto de emergencia. Inténtalo de nuevo."
                                    )
                                }
                            }
                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                showMessage(
                                    requireContext(),
                                    "Error al eliminar el contacto de emergencia. Inténtalo de nuevo."
                                )
                            }
                        })
                }
            }

            alertDialog.setNegativeButton("No") { _, _ ->
                // El usuario seleccionó "No", no hacemos ninguna acción adicional.
            }

            alertDialog.show()
        }
    }
    fun showMessage(context: Context, mensaje: String) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(mensaje)
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = builder.create()
        dialog.show()
    }

    private fun deleteToken(token: String) {
        val tokenService = ActivityServiceApiBuilder.createToken()
        tokenService.deleteToken(token).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "Token eliminado con éxito")
                } else {
                    Log.e(TAG, "Error al eliminar el token")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e(TAG, "Error de red", t)
            }
        })
    }

    private fun clearErrorMessages() {
        profileName?.error = null
        profileSurname?.error = null
        errorMsg?.visibility = View.INVISIBLE
    }
}