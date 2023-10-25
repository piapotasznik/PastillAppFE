package edu.ort.pastillapp.Fragments
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import edu.ort.pastillapp.Activities.InitActivity
import edu.ort.pastillapp.Helpers.UserSingleton
import edu.ort.pastillapp.databinding.FragmentProfileBinding
import edu.ort.pastillapp.Models.ApiUserResponse
import edu.ort.pastillapp.Models.User
import edu.ort.pastillapp.Services.ActivityServiceApiBuilder
import edu.ort.pastillapp.Services.TokenService
import edu.ort.pastillapp.Services.UserService
import edu.ort.pastillapp.ViewModels.ProfileViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
    private var email: String? = UserSingleton.currentUserEmail

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

        userService = ActivityServiceApiBuilder.create()

        auth = Firebase.auth

        val buttonUpdateInformation = binding.saveBtn
        buttonUpdateInformation.setOnClickListener {
            updateUserInformation()
        }

        binding.txtSignOut.setOnClickListener {
            if (auth.currentUser != null) {
                auth.signOut()
                startActivity(Intent(context, InitActivity::class.java))
                Toast.makeText(context, "Sesion cerrada exitosamente", Toast.LENGTH_SHORT).show()
            }
        }

        profileName = binding.myProfileName
        profileSurname = binding.myProfileLastName
        profileEmail = binding.myProfileEmail
        profileEmail?.isEnabled = false
        errorMsg = binding.errorMsg
        errorMsg?.isVisible = false

        this.setUserInformation()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUserInformation() {
        email?.let {
            this.userService?.getUserEmail(it)?.enqueue(object: Callback<ApiUserResponse> {
                override fun onResponse(call: Call<ApiUserResponse>, response: Response<ApiUserResponse>) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            userCreatedInformation = response.body()
                            profileName?.setText(userCreatedInformation?.name)
                            profileSurname?.setText(userCreatedInformation?.lastName)
                            profileEmail?.setText(userCreatedInformation?.email)
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

        // si el campo email o nombre estan vacios
        if (userName.isEmpty() || userLastName.isEmpty()) {
            profileName?.error = "Campos obligatorios"
            profileSurname?.error = "Campos obligatorios"
            errorMsg?.visibility = View.VISIBLE
            errorMsg?.text = "Todos los campos deben ser completados"

            Handler().postDelayed({
                errorMsg?.visibility = View.INVISIBLE
            }, 3000)

            // si el nombre tiene más de 50 caracteres
        } else if (userName.length > 50) {
            profileName?.error = "El nombre no debe superar los 50 caracteres"
            errorMsg?.visibility = View.VISIBLE
            errorMsg?.text = "El nombre no debe superar los 50 caracteres"
            Handler().postDelayed({
                errorMsg?.visibility = View.INVISIBLE
            }, 3000)

            // si el apellido tiene más de 50 caracteres
        } else if (userLastName.length > 50) {
            profileSurname?.error = "El apellido no debe superar los 50 caracteres"
            errorMsg?.visibility = View.VISIBLE
            errorMsg?.text = "El apellido no debe superar los 50 caracteres"
            Handler().postDelayed({
                errorMsg?.visibility = View.INVISIBLE
            }, 3000)
        } else {
            val userInformation = this.userCreatedInformation?.let {
                val userUpdated = User(name = userName,
                    lastName = userLastName,
                    email = it.email
                )
                it.userId?.let { it1 ->
                    this.userService?.updateUser(it1, userUpdated)?.enqueue(object: Callback<Void> {
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
    }
}