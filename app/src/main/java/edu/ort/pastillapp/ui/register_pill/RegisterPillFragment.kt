package edu.ort.pastillapp.ui.register_pill

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import edu.ort.pastillapp.R
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.ort.pastillapp.databinding.FragmentRegisterPillBinding


class RegisterPillFragment : Fragment() {

    private var _binding: FragmentRegisterPillBinding? = null
    private var medicineSpinner: Spinner? = null
    private var notifyCheckBox: CheckBox? = null
    private var doseInput: EditText? = null
    private var presentationSpinner: Spinner? = null
    private var dateInput: EditText? = null
    private var timeInput: EditText? = null
    private var quantityFrequencySpinner: Spinner? = null
    private var valueFrequencySpinner: Spinner? = null
    private var valueDurationSpinner: Spinner? = null
    private var quantityDurationSpinner: Spinner? = null
    private var extraNotesInput: EditText? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(RegisterPillViewModel::class.java)

        _binding = FragmentRegisterPillBinding.inflate(inflater, container, false)
        val root: View = binding.root

        medicineSpinner = binding.medicineSpinner
        notifyCheckBox = binding.notifyCheckBox
        doseInput = binding.doseInput
        presentationSpinner = binding.presentationSpinner
        dateInput = binding.dateInput
        timeInput = binding.timeInput
        this.quantityFrequencySpinner = binding.quantityFrequencySpinner
        valueFrequencySpinner = binding.valueFrequencySpinner
        valueDurationSpinner = binding.valueDurationSpinner
        quantityDurationSpinner = binding.quantityDurationSpinner
        extraNotesInput = binding.extraNotesInput

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fillValues() {
        activity?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.quantity_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                this.quantityFrequencySpinner?.adapter = adapter
                this.quantityDurationSpinner?.adapter = adapter
            }

            ArrayAdapter.createFromResource(
                it,
                R.array.values_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                this.valueFrequencySpinner?.adapter = adapter
                this.valueDurationSpinner?.adapter = adapter
            }
        }
        
    }
}