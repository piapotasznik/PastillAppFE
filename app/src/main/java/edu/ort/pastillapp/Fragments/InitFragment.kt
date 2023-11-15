package edu.ort.pastillapp.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import edu.ort.pastillapp.R
import edu.ort.pastillapp.databinding.FragmentInitBinding

class InitFragment : BaseFragment() {

    private lateinit var binding: FragmentInitBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInitBinding.inflate(inflater, container, false)

        binding?.btnLogIn?.setOnClickListener {
            findNavController().navigate(R.id.action_initFragment_to_logInFragment)
        }

        binding?.btnSignUp?.setOnClickListener {
            findNavController().navigate(R.id.action_initFragment_to_signUpFragment)
        }

        return binding.root
    }
}