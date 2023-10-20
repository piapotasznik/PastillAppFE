package edu.ort.pastillapp.fragments
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import edu.ort.pastillapp.R


class   homePageUser : Fragment() {
    lateinit var v: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home_page, container, false)
        return v
    }

    override fun onStart() {
        super.onStart()
        val singUp = v.findViewById<Button>(R.id.btnSingUp)
        val login = v.findViewById<Button>(R.id.btnLogIn)


        login.setOnClickListener {
            val action = homePageUserDirections.actionHomePageUserToLogin()
            v.findNavController().navigate(action)
        }

        singUp.setOnClickListener {
            val action = homePageUserDirections.actionHomePageUserToRegisterFragment()
            v.findNavController().navigate(action)
        }

    }

}