package edu.ort.pastillapp.Fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.toLowerCase
import edu.ort.pastillapp.R
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import edu.ort.pastillapp.Helpers.Helpers
import edu.ort.pastillapp.Helpers.SharedPref
import edu.ort.pastillapp.Helpers.UserSingleton
import edu.ort.pastillapp.Helpers.formatter
import edu.ort.pastillapp.Helpers.setTime
import edu.ort.pastillapp.Models.ApiContactEmergencyServerResponse
import edu.ort.pastillapp.Models.Medicine
import edu.ort.pastillapp.Models.Reminder
import edu.ort.pastillapp.Models.ReminderCreation
import edu.ort.pastillapp.Services.ActivityServiceApiBuilder
import edu.ort.pastillapp.Services.ActivityServiceApiBuilder.createReminder
import edu.ort.pastillapp.Services.MedicineService
import edu.ort.pastillapp.databinding.FragmentRegisterPillBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import edu.ort.pastillapp.ViewModels.RegisterPillViewModel
import java.time.LocalTime
import java.util.Calendar
import java.util.Locale
import kotlin.math.log


class RegisterPillFragment : Fragment() {

    private var _binding: FragmentRegisterPillBinding? = null
    private var medicineSpinner: Spinner? = null
    private var notifyCheckBox: CheckBox? = null
    private var medicineService: MedicineService? = null
    private var doseInput: EditText? = null
    private var presentationSpinner: Spinner? = null
    private var dateTimeInput: TextView? = null
    private var quantityFrequencySpinner: Spinner? = null
    private var valueFrequencySpinner: Spinner? = null
    private var valueDurationSpinner: Spinner? = null
    private var quantityDurationSpinner: Spinner? = null
    private var savebtn: Button? = null
    private var errorMsg: TextView? = null
    private var medicines: List<Medicine>? = null
    private var observation: EditText? = null

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
        dateTimeInput = binding.dateTimeInput
        dateTimeInput!!.setOnClickListener { showDateTimePicker() }
        quantityFrequencySpinner = binding.quantityFrequencySpinner
        valueFrequencySpinner = binding.valueFrequencySpinner
        valueDurationSpinner = binding.valueDurationSpinner
        quantityDurationSpinner = binding.quantityDurationSpinner
        observation = binding.editNotes3
        errorMsg = binding.errorMsg
        errorMsg?.visibility = View.INVISIBLE
        savebtn = binding.saveReminderBtn
        savebtn?.setOnClickListener {
            saveReminder()
            OnClickNavigate()
        }


        fillSpinnerValues()
        /*
        setUpTime()
        setUpDate()
         */
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun OnClickNavigate() {
        val action = RegisterPillFragmentDirections.actionNavigationRegisterPillToNavigationHome()
        this.findNavController().navigate(action)
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
                R.array.medication_presentation,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                this.presentationSpinner?.adapter = adapter
            }
        }
        this.getMedicines()
        // ACA RELLENAR EL "medicineSpinner" CON LOS VALORES DE LA VARIABLE "medicines"
    }

    private fun getMedicines() {
        medicines?.let {
            this.medicineService?.getAllMedicines()?.enqueue(object: Callback<List<Medicine>> {
                override fun onResponse(
                    call: Call<List<Medicine>>,
                    response: Response<List<Medicine>>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            medicines = response.body();
                            print(medicines);
                        }
                    }
                }
                override fun onFailure(call: Call<List<Medicine>>, t: Throwable) {
                    // Manejar errores de red o solicitud
                    Toast.makeText(requireContext(), "Error de red", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun showDateTimePicker() {
        val currentLocale = Locale("es")
        Locale.setDefault(currentLocale)
        val config = Configuration()
        config.setLocale(currentLocale)
        resources.updateConfiguration(config, resources.displayMetrics)
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.TimePicker,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val timePickerDialog = TimePickerDialog(
                    requireContext(),
                    R.style.TimePicker,
                    TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                        val formattedDate = String.format(
                            "%02d-%02d-%04d %02d:%02d",
                            dayOfMonth,
                            month + 1,
                            year,
                            hourOfDay,
                            minute
                        )
                        binding.dateTimeInput.text = formattedDate
                    },
                    hour,
                    minute,
                    true
                )
                timePickerDialog.show()
            },
            year,
            month,
            day
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }
    private fun showDialog(observer: TimePickerDialog.OnTimeSetListener) {
        TimePickerFragment.newInstance(observer)
            .show(getParentFragmentManager(), "time-picker")
    }

    private fun saveReminder() {
        //val medicine: String = medicineSpinner?.selectedItem.toString() // ACA EN VEZ DEL NOMBRE IRIA EL MEDICINE_ID
        val medicine: Int = 1 //ESTO ESTÁ HARDCODEADO. PONER EL ID DE MEDICINA
        val userId: Int? = UserSingleton.userId?.let { SharedPref.read(SharedPref.ID, it) }
        val emergencyAlert: Boolean = notifyCheckBox?.isChecked == true
        val dose: String = doseInput?.text.toString()
        val presentation: String = presentationSpinner?.selectedItem.toString()
        val dateTime: String = Helpers().convertirFechaInversa(dateTimeInput?.text.toString())
        val quantityFrequency: Int = binding.quantityFrequencySpinner.selectedItem.toString().toInt()
        val valueFrequency: String = Helpers().translateFrequencyEn(binding.valueFrequencySpinner.selectedItem.toString().lowercase().capitalize())
        val quantityDuration: String = Helpers().translateFrequencyEn(binding.valueDurationSpinner.selectedItem.toString().lowercase().capitalize())
        val valueDuration: Int = binding.quantityDurationSpinner.selectedItem.toString().toInt()
        val observation: String? = binding.editNotes3.text.toString()

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
            val newReminderCreation = ReminderCreation(
                userId ?: 0, // Asigna 0 si userId es nulo
                medicineId = medicine,
                quantity = dose.toInt(),
                presentation = presentation,
                dateTimeStart = dateTime,
                frequencyType =  valueFrequency,
                frequencyValue =  quantityFrequency,
                durationType =  quantityDuration,
                durationValue = valueDuration.toInt(), // Asegúrate de convertir a Int
                emergencyAlert = emergencyAlert,
                observation = observation
            )
            create(newReminderCreation)
            Toast.makeText(requireContext(), "Recordatorio registrado!", Toast.LENGTH_SHORT).show()
        }
    }

    fun create(reminderCreation: ReminderCreation){
        val service = ActivityServiceApiBuilder.createReminder()
        val call = service.createReminder(reminderCreation)

        call.enqueue(object : Callback<ApiContactEmergencyServerResponse> {
            override fun onResponse(
                call: Call<ApiContactEmergencyServerResponse>,
                response: Response<ApiContactEmergencyServerResponse>
            ) {
                if (response.isSuccessful) {
                    val respuesta = response.body()
                    Log.e("put33", respuesta.toString())
                    // Procesar la respuesta
                } else {
                    // Manejar la respuesta de error
                    val errorBody = response.errorBody()?.string()
                    Log.e("put33", "Respuesta fallo en el else: $errorBody")
                    Log.e("put33", createReminder().toString())
                }
            }

            override fun onFailure(call: Call<ApiContactEmergencyServerResponse>, t: Throwable) {
                // Manejar el error de la solicitud
                Log.e("put33", "respuesta fallo en el failure")
            }
        })
    }
}