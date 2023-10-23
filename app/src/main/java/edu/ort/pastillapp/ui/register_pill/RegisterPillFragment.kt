package edu.ort.pastillapp.ui.register_pill

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.isVisible
import edu.ort.pastillapp.R
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.ort.pastillapp.databinding.FragmentRegisterPillBinding
import java.time.LocalTime
import edu.ort.pastillapp.helpers.formatter
import edu.ort.pastillapp.helpers.setTime
import java.time.LocalDate


class RegisterPillFragment : Fragment() {

    private var _binding: FragmentRegisterPillBinding? = null
    private var medicineSpinner: Spinner? = null
    private var notifyCheckBox: CheckBox? = null
    private var doseInput: EditText? = null
    private var presentationSpinner: Spinner? = null
    private var dateInput: TextView? = null
    private var timeInput: TextView? = null
    private var quantityFrequencySpinner: Spinner? = null
    private var valueFrequencySpinner: Spinner? = null
    private var valueDurationSpinner: Spinner? = null
    private var quantityDurationSpinner: Spinner? = null
    private var savebtn: Button? = null
    private var errorMsg: TextView? = null
    //private var medicines: [Medicine]? = null // CUANDO SE CREE LA CLASE MEDICINE DESCOMENTAR ESTA VARIABLE

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
        quantityFrequencySpinner = binding.quantityFrequencySpinner
        valueFrequencySpinner = binding.valueFrequencySpinner
        valueDurationSpinner = binding.valueDurationSpinner
        quantityDurationSpinner = binding.quantityDurationSpinner
        errorMsg = binding.errorMsg
        errorMsg?.visibility = View.INVISIBLE
        savebtn = binding.saveReminderBtn
        savebtn?.setOnClickListener {
            saveReminder()
        }

        fillSpinnerValues()
        setUpTime()
        setUpDate()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun fillSpinnerValues() {
        activity?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.quantity,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                this.quantityFrequencySpinner?.adapter = adapter
                this.quantityDurationSpinner?.adapter = adapter
            }

            ArrayAdapter.createFromResource(
                it,
                R.array.frequency_values,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                this.valueFrequencySpinner?.adapter = adapter
            }

            ArrayAdapter.createFromResource(
                it,
                R.array.duration_values,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                this.valueDurationSpinner?.adapter = adapter
            }

            ArrayAdapter.createFromResource(
                it,
                R.array.med_presentation,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                this.presentationSpinner?.adapter = adapter
            }
        }
        this.getMedicines()
        // ACA RELLENAR EL "medicineSpinner" CON LOS VALORES DE LA VARIABLE "medicines"
    }
    private fun setUpTime() {
        timeInput?.text = LocalTime.now().format(formatter)
        timeInput?.setOnClickListener {
            showTimePicker()
        }
    }
    private fun showTimePicker() {
        showDialog { _, hour, minute ->
            val currentTime = LocalTime.of(hour, minute)
            timeInput?.setTime(currentTime)
        }
    }
    private fun showDialog(observer: TimePickerDialog.OnTimeSetListener) {
        TimePickerFragment.newInstance(observer)
            .show(getParentFragmentManager(), "time-picker")
    }
    private fun setUpDate() {
        dateInput?.text = LocalDate.now().toString()
        dateInput?.setOnClickListener {
            showDatePicker()
        }
    }
    private fun showDateDialog(observer: DatePickerDialog.OnDateSetListener) {
        DatePickerFragment.newInstance(observer)
        .show(getParentFragmentManager(), "date-picker")
    }
    private fun showDatePicker() {
        showDateDialog { _, year, month, day ->
            // +1 because January is zero
            val selectedDate = day.toString() + " / " + (month + 1) + " / " + year
            dateInput?.text = selectedDate
        }
    }
    private fun getMedicines() {
        // ACA HACER LA PEGADA DE GET MEDICINES Y CON LA RESPUESTA GUARDAR EN LA VARIABLE "medicines"
    }
    private fun saveReminder() {
        val medicine: String = medicineSpinner?.selectedItem.toString() // ACA EN VEZ DEL NOMBRE IRIA EL MEDICINE_ID
        val notify: Boolean = notifyCheckBox?.isChecked == true
        val dose: String = doseInput?.text.toString()
        val presentation: String = presentationSpinner?.selectedItem.toString()
        val date: String = dateInput?.text.toString()
        val time: String = timeInput?.text.toString()
        val quantityFrequency: String = quantityFrequencySpinner?.selectedItem.toString()
        val valueFrequency: String = valueFrequencySpinner?.selectedItem.toString()
        val quantityDuration: String = quantityDurationSpinner?.selectedItem.toString()
        val valueDuration: String = valueDurationSpinner?.selectedItem.toString()
        if (dose.isEmpty()) {
            doseInput?.setError("Campo obligatorio")
            errorMsg?.visibility = View.VISIBLE
            errorMsg?.text =
                "Todos los campos deben ser completados"
            Handler().postDelayed({
                errorMsg?.visibility = View.INVISIBLE
            }, 3000)
        } else {
            // CREAR EL OBJETO REMINDER Y LA PEGADA DE CREATE REMINDER
        }
    }
}