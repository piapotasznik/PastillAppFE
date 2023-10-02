package edu.ort.pastillapp.fragments
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import edu.ort.pastillapp.HomeScreenActivity
import edu.ort.pastillapp.R
import edu.ort.pastillapp.UserSingleton


class login : Fragment() {
    lateinit var v: View
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_login, container, false)

        return v
    }

    override fun onStart() {
        super.onStart()

        auth = FirebaseAuth.getInstance()

        val emailEditText = v.findViewById<EditText>(R.id.textFgLoginEmail)
        val passwordEditText = v.findViewById<EditText>(R.id.textFgLoginPass)
        val logInButton = v.findViewById<Button>(R.id.btnFgLoginLogin)
        val registerTextView = v.findViewById<TextView>(R.id.twFgloginRegistrer)

        // Agregar OnClickListener para "Olvidé mi contraseña"
        val forgotPasswordTextView =
            v.findViewById<TextView>(edu.ort.pastillapp.R.id.textFgLoginOlvideCont)
        forgotPasswordTextView.setOnClickListener {
            val action = loginDirections.actionLoginToForgotPassword()
            v.findNavController().navigate(action)
        }
        registerTextView.setOnClickListener {
            // Navega al fragmento de registro cuando se hace clic en el TextView "Regístrese"
            val action = loginDirections.actionLoginToRegisterFragment()
            v.findNavController().navigate(action)
        }
        logInButton.setOnClickListener() {
            if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()) {

                val errorMessageTextView = v.findViewById<TextView>(R.id.errorMessageTextView)
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        // Navega a la siguiente pantalla

                        Log.d(ContentValues.TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        UserSingleton.currentUser = user

//                        val action = loginDirections.actionLoginToProfileUserFragment()
//                        v.findNavController().navigate(action)

//                        val intent = Intent(activity, HomeScreenActivity::class.java)
//                        startActivity(intent)

                        // En la primera actividad, al crear el Intent

                        // En la primera actividad, al crear el Intent
                        val intent = Intent(context, HomeScreenActivity::class.java)
                        intent.putExtra(
                            "user",
                            user
                        )

                        startActivity(intent)


                    } else {
                        // Autenticación fallida. Mensaje que indica error,
                        // se muestra text view que estaba oculto
                        errorMessageTextView.visibility = View.VISIBLE
                        errorMessageTextView.text =
                            "El usuario y/o contrasena ingresados son incorrectos"

                    }
                }

            } else {
                emailEditText.setError("Este campo es requerido") // Esto activará el estado de error
                passwordEditText.setError("Este campo es requerido") // Esto
            }
        }
    }
}