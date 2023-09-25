package edu.ort.pastillapp.fragments

import android.content.ContentValues
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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

        singUp.setOnClickListener{
            val userName = v.findViewById<EditText>(R.id.singUpName).text.toString();
            // el text es para obtener el valor del campo, y luego lo parseo a String
            val userEmail = v.findViewById<EditText>(R.id.singUpemail).text.toString();
            val passPass = v.findViewById<EditText>(R.id.singUpPassword).text.toString();
            val passPass2 = v.findViewById<EditText>(R.id.singUpPassword2).text.toString();


            if (passPass != passPass2 ){
                val text = "Password are not the same!"
                val duration = Toast.LENGTH_SHORT

                //snackbar
                val snackbar = Snackbar.make(v,text,duration)
                snackbar.setTextColor(Color.RED)
                // snackbar.setBackgroundTint(getResources().getColor(R.color.black))

                snackbar.show()
            } else{
                auth.createUserWithEmailAndPassword(userEmail, passPass).addOnCompleteListener(){
                    if (it.isSuccessful){
                        Log.d(ContentValues.TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        UserSingleton.currentUser = user

                        val action = RegisterFragmentDirections.actionRegisterFragmentToProfileUserFragment()
                        v.findNavController().navigate(action)
                    }
                }
            }

            }

    }
}