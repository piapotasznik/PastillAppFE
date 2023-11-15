package edu.ort.pastillapp.Fragments

import android.app.Dialog
import android.widget.Toast
import androidx.fragment.app.Fragment
import edu.ort.pastillapp.R

open class BaseFragment : Fragment() {

    private lateinit var pb: Dialog

    fun showProgressBar() {
        pb = Dialog(requireContext())
        pb.setContentView(R.layout.progress_bar)
        pb.setCancelable(false)
        pb.show()
    }

    fun hideProgressBar() {
        if (::pb.isInitialized && pb.isShowing) {
            pb.dismiss()
        }
    }

    fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}