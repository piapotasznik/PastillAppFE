package edu.ort.pastillapp.fragments

import android.content.ContentValues
import android.graphics.Color
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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import edu.ort.pastillapp.R
import edu.ort.pastillapp.UserSingleton

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
        val singUp = v.findViewById<Button>(R.id.singUpBtn)
        val loginTextView = v.findViewById<TextView>(R.id.twFgregisterLogin)
        val errorMsgRegister = v.findViewById<TextView>(R.id.errorMsgRegister)
        val errorMsgRegisterPass = v.findViewById<TextView>(R.id.errorMsgRegisterPass)
        val fillMsg = v.findViewById<TextView>(R.id.errorMsgRegisterFill)

        loginTextView.setOnClickListener {
            // Navega al fragmento de login cuando se hace clic en el TextView "Ingresar"
            val action = RegisterFragmentDirections.actionRegisterFragmentToLogin()
            v.findNavController().navigate(action)
        }


        singUp.setOnClickListener{
            val userName = v.findViewById<EditText>(R.id.singUpName).text.toString();
            // el text es para obtener el valor del campo, y luego lo parseo a String
            val userEmail = v.findViewById<EditText>(R.id.singUpemail).text.toString();
            val passPass = v.findViewById<EditText>(R.id.singUpPassword).text.toString();
            val passPass2 = v.findViewById<EditText>(R.id.singUpPassword2).text.toString();


            if (userEmail.isEmpty() || userName.isEmpty()){
                fillMsg.visibility = View.VISIBLE
                fillMsg.text =
                    "Todos los campos deben ser completados"

                Handler().postDelayed({
                    fillMsg.visibility = View.INVISIBLE
                }, 3000)
            } else if (passPass != passPass2 ){
                errorMsgRegisterPass.visibility = View.VISIBLE
                errorMsgRegisterPass.text =
                    "Las contraseñas no coinciden"
                Handler().postDelayed({
                    errorMsgRegisterPass.visibility = View.INVISIBLE
                }, 3000)

            } else{
                auth.createUserWithEmailAndPassword(userEmail, passPass).addOnCompleteListener(){
                    if (it.isSuccessful){
                        Log.d(ContentValues.TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        UserSingleton.currentUser = user

                        val action = RegisterFragmentDirections.actionRegisterFragmentToProfileUserFragment()
                        v.findNavController().navigate(action)
                    } else {
                        errorMsgRegister.visibility = View.VISIBLE
                        errorMsgRegister.text =
                            "La contraseña debe tener al menos 6 caracteres"
                        Handler().postDelayed({
                            errorMsgRegister.visibility = View.INVISIBLE
                        }, 3000)
                    }
                }
            }

            }

    }
}