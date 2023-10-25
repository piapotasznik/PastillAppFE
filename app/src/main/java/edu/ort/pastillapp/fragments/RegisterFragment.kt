package edu.ort.pastillapp.fragments

import android.content.ContentValues
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.actionCodeSettings
import edu.ort.pastillapp.R
import edu.ort.pastillapp.UserSingleton
import edu.ort.pastillapp.ValidationEmail
import edu.ort.pastillapp.models.User
import edu.ort.pastillapp.models.UserRepository

class RegisterFragment : Fragment() {

    lateinit var v: View
    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_register, container, false)
        return v
    }

    override fun onStart() {
        super.onStart()
        val signUp = v.findViewById<Button>(R.id.signUpBtn)
        val loginTextView = v.findViewById<TextView>(R.id.twFgregisterLogin)
        val errorMsgRegister = v.findViewById<TextView>(R.id.errorMsgRegister)
        val errorMsgRegisterPass = v.findViewById<TextView>(R.id.errorMsgRegisterPass)
        val fillMsg = v.findViewById<TextView>(R.id.errorMsgRegisterFill)
        val emailCollisionMsg = v.findViewById<TextView>(R.id.errorMsgRegisterEmail)

        loginTextView.setOnClickListener {
            // Navega al fragmento de login cuando se hace clic en el TextView "Ingresar"
            val action = RegisterFragmentDirections.actionRegisterFragmentToLogin()
            v.findNavController().navigate(action)
        }


        signUp.setOnClickListener {
            val userName = v.findViewById<EditText>(R.id.signUpName).text.toString();
            val userLastName = v.findViewById<EditText>(R.id.signUpLastName).text.toString();
            // el text es para obtener el valor del campo, y luego lo parseo a String
            val userEmail = v.findViewById<EditText>(R.id.signUpemail).text.toString();
            val passPass = v.findViewById<EditText>(R.id.signUpPassword).text.toString();
            val passPass2 = v.findViewById<EditText>(R.id.signUpPassword2).text.toString();

            // si el campo email o nombre estan vacios
            if (userEmail.isEmpty() || userName.isEmpty() || userLastName.isEmpty()) {
                v.findViewById<EditText>(R.id.signUpName)
                    .setError("Campos obligatorios") // Esto activará el estado de error
                v.findViewById<EditText>(R.id.signUpemail).setError("Campos obligatorios") //
                v.findViewById<EditText>(R.id.signUpLastName).setError("Campos obligatorios")

                fillMsg.visibility = View.VISIBLE
                fillMsg.text =
                    "Todos los campos deben ser completados"

                Handler().postDelayed({
                    fillMsg.visibility = View.INVISIBLE
                }, 3000)

                // si el password tiene menos de 6 caracteres
            } else if (passPass.length < 6) {
                v.findViewById<EditText>(R.id.signUpPassword)
                    .setError("La contraseña debe tener al menos 6 caracteres") // Esto activará el estado de error
                v.findViewById<EditText>(R.id.signUpPassword2)
                    .setError("La contraseña debe tener al menos 6 caracteres") // Esto
                errorMsgRegister.visibility = View.VISIBLE
                errorMsgRegister.text =
                    "La contraseña debe tener al menos 6 caracteres"
                Handler().postDelayed({
                    errorMsgRegister.visibility = View.INVISIBLE
                }, 3000)

            } else if (!ValidationEmail.validate(userEmail)) {
                v.findViewById<EditText>(R.id.signUpemail).setError("Email con formato invalido") //
                errorMsgRegister.visibility = View.VISIBLE
                errorMsgRegister.text =
                    "Email con formato invalido"
                Handler().postDelayed({
                    errorMsgRegister.visibility = View.INVISIBLE
                }, 3000)
            }

            // si los password no coinciden
            else if (passPass != passPass2) {
                v.findViewById<EditText>(R.id.signUpPassword)
                    .setError("Las contraseñas no coinciden") // Esto activará el estado de error
                v.findViewById<EditText>(R.id.signUpPassword2)
                    .setError("Las contraseñas no coinciden") // Esto
                errorMsgRegisterPass.visibility = View.VISIBLE
                errorMsgRegisterPass.text =
                    "Las contraseñas no coinciden"
                Handler().postDelayed({
                    errorMsgRegisterPass.visibility = View.INVISIBLE
                }, 3000)

                // si el nombre tiene más de 50 caracteres
            } else if (userName.length > 50) {
                v.findViewById<EditText>(R.id.signUpName)
                    .setError("El nombre no debe superar los 50 caracteres") // Esto activará el estado de error
                errorMsgRegister.visibility = View.VISIBLE
                errorMsgRegister.text =
                    "El nombre no debe superar los 50 caracteres"
                Handler().postDelayed({
                    errorMsgRegister.visibility = View.INVISIBLE
                }, 3000)

                // si el apellido tiene más de 50 caracteres
            } else if (userLastName.length > 50) {
                v.findViewById<EditText>(R.id.signUpLastName)
                    .setError("El apellido no debe superar los 50 caracteres") // Esto activará el estado de error
                errorMsgRegister.visibility = View.VISIBLE
                errorMsgRegister.text =
                    "El apellido no debe superar los 50 caracteres"
                Handler().postDelayed({
                    errorMsgRegister.visibility = View.INVISIBLE
                }, 3000)

                // si el email tiene más de 50 caracteres
            } else if (userEmail.length > 50) {
                v.findViewById<EditText>(R.id.signUpemail)
                    .setError("El email no debe superar los 50 caracteres") // Esto activará el estado de error
                errorMsgRegister.visibility = View.VISIBLE
                errorMsgRegister.text =
                    "El email no debe superar los 50 caracteres"
                Handler().postDelayed({
                    errorMsgRegister.visibility = View.INVISIBLE
                }, 3000)

            } else {
                auth.createUserWithEmailAndPassword(userEmail, passPass)
                    .addOnCompleteListener() { task ->

                        if (task.isSuccessful) {
                            Log.d(ContentValues.TAG, "createUserWithEmail:success")

                            // Crear una instancia de CreateUserRequest con los datos del usuario
                            val newUser = User(
                                name = userName,
                                lastName = userLastName,
                                email = userEmail,
                            )

                            val userRepository = UserRepository(requireContext())
                            userRepository.createUser(newUser,
                                onSuccess = {
                                // La API ha guardado los datos del usuario con éxito
                                    auth.currentUser?.sendEmailVerification()
                                    Toast.makeText(
                                        requireContext(),
                                        "Email de verificacion enviado!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val actionCodeSettings = actionCodeSettings {
                                        // URL you want to redirect back to. The domain (www.example.com) for this
                                        // URL must be whitelisted in the Firebase Console.
                                        url = "https://www.example.com/finishSignUp?cartId=1234"
                                        // This must be true
                                        handleCodeInApp = true
                                        setIOSBundleId("com.example.ios")
                                        setAndroidPackageName(
                                            "com.example.android",
                                            true, // installIfNotAvailable
                                            "12", // minimumVersion
                                        )
                                    }

                                    val user = auth.currentUser
                                    UserSingleton.currentUser = user

                                    val action =
                                        RegisterFragmentDirections.actionRegisterFragmentToLogin()
                                    v.findNavController().navigate(action)
                                },
                                onError = {
                                // Manejar el error de la llamada a la API adecuadamente
                                // Puedes mostrar un mensaje de error o intentar de nuevo, según sea necesario.
                                }
                            )
                        } else {
                            val exception = task.exception
                            if (exception is FirebaseAuthUserCollisionException) {

                                // El correo electrónico ya está registrado
                                emailCollisionMsg.visibility = View.VISIBLE
                                emailCollisionMsg.text =
                                    "El Email ya figura como registrado"
                                Handler().postDelayed({
                                    errorMsgRegister.visibility = View.INVISIBLE
                                }, 3000)

                            } else {
                                // Otro tipo de error
                                emailCollisionMsg.visibility = View.INVISIBLE
                            }
                        }
                    }

            }

        }
    }
}