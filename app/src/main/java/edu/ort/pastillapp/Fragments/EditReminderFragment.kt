package edu.ort.pastillapp.Fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import edu.ort.pastillapp.Helpers.Helpers
import edu.ort.pastillapp.Helpers.SharedPref
import edu.ort.pastillapp.Helpers.UserSingleton
import edu.ort.pastillapp.Models.ApiContactEmergencyServerResponse
import edu.ort.pastillapp.Models.ApiUserResponse
import edu.ort.pastillapp.Models.Reminder
import edu.ort.pastillapp.Models.ReminderUpdate
import edu.ort.pastillapp.R
import edu.ort.pastillapp.Services.ActivityServiceApiBuilder
import edu.ort.pastillapp.Services.UserService
import edu.ort.pastillapp.ViewModels.EditReminderViewModel
import edu.ort.pastillapp.databinding.FragmentEditReminderBinding
import edu.ort.pastillapp.ViewModels.SharedViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.util.Locale


class EditReminderFragment : Fragment() {
    private lateinit var viewModel: EditReminderViewModel
    private lateinit var binding: FragmentEditReminderBinding // Referencia al ViewBinding
    private var reminder: Reminder? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val reminderId = EditReminderFragmentArgs.fromBundle(requireArguments()).reminderId
        getReminderByID(reminderId)
        binding = FragmentEditReminderBinding.inflate(inflater, container, false)
        binding.editDosis.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // Aquí puedes realizar la validación
                val dosis = charSequence.toString()

                if (dosis.isNotEmpty()) {
                    // Si el texto no está vacío, verifica si es "0" y evita la entrada
                    if (dosis == "0") {
                        binding.editDosis.setText("")
                    }

                    // Limita la entrada a un máximo de tres dígitos
                    if (dosis.length > 3) {
                        binding.editDosis.setText(dosis.substring(0, 3))
                        binding.editDosis.setSelection(3) // Coloca el cursor al final
                    }
                }
            }

            override fun afterTextChanged(editable: Editable?) {}
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditReminderViewModel::class.java)
        val btnCxl = binding.cxlBtn
        btnCxl.setOnClickListener {
            findNavController().popBackStack()
        }


        val btnSave = binding.saveBtn
        btnSave.setOnClickListener {
            val editedReminder = getReminderValues()
            Log.e("edited", "este es el editado ${editedReminder.toString()}")
            if (editedReminder != null) {
                update(editedReminder)
                sharedViewModel.refreshData()
                findNavController().popBackStack()
            }

        }

        val editNotes = binding.editNotes // Asegúrate de que este sea el ID correcto en tu diseño XML
        val maxLength = 50 // El máximo de caracteres permitidos

        val inputFilter = InputFilter { source, start, end, dest, dstart, dend ->
            val newLength = dest.length - (dend - dstart) + end - start
            if (newLength <= maxLength) {
                null // No se supera el límite, se permite la entrada.
            } else {
                "" // Se supera el límite, se bloquea la entrada.
            }
        }

        binding.emergencyCheckBox.setOnClickListener(){
            val userService = ActivityServiceApiBuilder.create()
            val userEmail = SharedPref.read(SharedPref.EMAIL, UserSingleton.currentUserEmail.toString())

            hasEmergencyContact(userService, userEmail) { hasEmergencyContact ->
                if (hasEmergencyContact) {
                    binding.emergencyCheckBox.isEnabled = true
                    Toast.makeText(requireContext(), "Notificación habilitada.", Toast.LENGTH_LONG).show()
                } else {
                    // No hay contacto de emergencia, mensaje de error
                    Toast.makeText(requireContext(), "No tienes un contacto de emergencia. Debes agregar uno primero.", Toast.LENGTH_LONG).show()
                    binding.emergencyCheckBox.isEnabled = false
                    binding.emergencyCheckBox.isChecked = false
                }
            }
        }

        editNotes.filters = arrayOf(inputFilter)


    }

    override fun onStart() {
        super.onStart()
        reminder?.let {
            viewModel.setReminder(it)
            setReminderValues(it)

        }
        val dateText = binding.editDateIntake
        dateText.setOnClickListener {
            showDateTimePicker()
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
                        binding.editDateIntake.text = formattedDate
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

    // coloca en los inptus los valores que ya contiene el reminder
    fun setReminderValues(reminder: Reminder) {
        val presentationValues = resources.getStringArray(R.array.med_presentation)
        val positionToSelectPresentation = presentationValues.indexOf(reminder.presentation)
        Log.e("position", "position to select ${positionToSelectPresentation}")
        binding.presentationSpinner.setSelection(positionToSelectPresentation)
        val frecuencyIntervalValues = resources.getStringArray(R.array.frecuency_intake_type)
        val frquencyInEn = reminder.frequencyText.toString()
        val positionToSelectFrInterval =
            frecuencyIntervalValues.indexOf(Helpers().translateFrequency(frquencyInEn))
        val durationIntakeValues = resources.getStringArray(R.array.duration_intake)
        val durationInEn = reminder.intakeTimeText.toString()
        val positionToSelectDuration =
            durationIntakeValues.indexOf(Helpers().translateFrequency(durationInEn))
        binding.editNameMed.setText(reminder.medicineName.toString()) /// ver de corregir cuando tenga el nombre
        binding.editDosis.setText(reminder.quantity.toString())
        binding.emergencyCheckBox.isChecked = reminder.emergencyAlert
        binding.presentationSpinner.setSelection(positionToSelectPresentation)
        binding.editFrecuencyInterval.setSelection(positionToSelectFrInterval)
        binding.editFrecuencyInt.setSelection(reminder.frequencyNumber - 1)
        binding.editDurationIntake.setSelection(positionToSelectDuration)
        binding.editDurationNum.setSelection(reminder.intakeTimeNumber - 1)
        binding.editNotes.setText(reminder.observation)
        val dateFormat = reminder.dateTimeStart?.let { Helpers().convertDate(it) }
        binding.editDateIntake.setText(dateFormat)
    }

 fun hasEmergencyContact(userService: UserService, userEmail: String, callback: (Boolean) -> Unit) {
     Log.d("checkEmergencyContactAndEnableCheckBox", "Función checkEmergencyContactAndEnableCheckBox se está ejecutando.")
        val call = userService.getUserEmail(userEmail)
        val userService = ActivityServiceApiBuilder.create()
        val userEmail = SharedPref.read(SharedPref.EMAIL, UserSingleton.currentUserEmail.toString())
     call.enqueue(object : Callback<ApiUserResponse> {
         override fun onResponse(call: Call<ApiUserResponse>, response: Response<ApiUserResponse>) {
             if (response.isSuccessful) {
                 val user = response.body()
                 val hasEmergencyContact = user?.emergencyUserId != null
                 Log.d("hasEmergencyContact", "Has Emergency Contact: $hasEmergencyContact")
                 callback(hasEmergencyContact)
             } else {
                 // En caso de que la llamada no sea exitosa, asumimos que no hay contacto de emergencia
                 Log.d("hasEmergencyContact", "Request was not successful. No Emergency Contact.")
                 callback(false)
             }
         }

         
    fun getReminderValues(): ReminderUpdate? {
        val reminderid = reminder?.reminderId
        val frecuencyIntakeSp = binding.editFrecuencyInterval.selectedItem.toString()
        Log.d("traduccion", "freq intake sp = ${frecuencyIntakeSp}")
        val frequencyEnglish =
            Helpers().translateFrequencyEn(frecuencyIntakeSp)
        val durationIntakeSp = binding.editDurationIntake.selectedItem.toString()
        Log.d("traduccion", "freq intake sp = ${durationIntakeSp}")
        val durationEnglish = Helpers().translateFrequencyEn(durationIntakeSp)

        Log.e("put33", binding.editDateIntake.text.toString())
        val dateFromat = Helpers().convertInvertDate(binding.editDateIntake.text.toString())
        //validar si el campo de dosis está vacío
        val dosisText = binding.editDosis.text.toString()
        if (dosisText.isEmpty()) {
            // El campo "dosis" está vacío, mostrar un Toast
            Toast.makeText(requireContext(), "El campo de dosis es obligatorio", Toast.LENGTH_SHORT).show()
            return null
        }

        var updateReminder = reminderid?.let {
            ReminderUpdate(
                it,
                binding.editDosis.text.toString().toInt(),
                binding.presentationSpinner.selectedItem.toString(),
                dateFromat,
                binding.editFrecuencyInt.selectedItem.toString().toInt(),
                frequencyEnglish,
                binding.editDurationNum.selectedItem.toString().toInt(),
                durationEnglish,
                binding.emergencyCheckBox.isChecked,
                binding.KeepsLogsCheckBox.isChecked,
                binding.editNotes.text.toString()
            )
        }
        return updateReminder
    }

         override fun onFailure(call: Call<ApiUserResponse>, t: Throwable) {
             // En caso de que ocurra un error en la solicitud, asumimos que no hay contacto de emergencia
             callback(false)
         }
     })
 }
 fun getReminderValues(): ReminderUpdate? {
     val reminderid = reminder?.reminderId
     val frequencyEnglish =
         Helpers().translateFrequencyEn(binding.editFrecuencyInterval.selectedItem.toString())
     val durationEnglish = Helpers().translateFrequencyEn(
         binding.editDurationIntake.selectedItem.toString()
     )
     Log.e("put33", binding.editDateIntake.text.toString())
     val dateFromat = Helpers().convertInvertDate(binding.editDateIntake.text.toString())
     var updateReminder = reminderid?.let {
         ReminderUpdate(
             it,
             binding.editDosis.text.toString().toInt(),
             binding.presentationSpinner.selectedItem.toString(),
             dateFromat,
             binding.editFrecuencyInt.selectedItem.toString().toInt(),
             frequencyEnglish,
             binding.editDurationNum.selectedItem.toString().toInt(),
             durationEnglish,
             binding.emergencyCheckBox.isChecked,
             binding.KeepsLogsCheckBox.isChecked,
             binding.editNotes.text.toString(),
         )
     }
     return updateReminder
 }

 override fun onDestroyView() {
     super.onDestroyView()
     // viewModel.reminder.removeObservers(viewLifecycleOwner)
 }

 fun update(updateReminder: ReminderUpdate) {
     val service = ActivityServiceApiBuilder.createReminder()
     val reminderId = updateReminder.reminderId
     val call = service.putReminderId(reminderId, updateReminder)
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
                 Log.e("put33", updateReminder.toString())
             }
         }

         override fun onFailure(call: Call<ApiContactEmergencyServerResponse>, t: Throwable) {
             // Manejar el error de la solicitud
             Log.e("put33", "respuesta fallo en el failure")
         }
     })
 }

 fun getReminderByID(reminderId: Int) {
     Log.e("llamando al reminder", "llamado exitoso id :${reminderId}")
     val service = ActivityServiceApiBuilder.createReminder()

     service.getReminderId(reminderId).enqueue(object : Callback<Reminder> {
         override fun onResponse(
             call: Call<Reminder>,
             response: Response<Reminder>
         ) {
             if (response.isSuccessful && response.body() != null) {
                 val responseReminder = response.body()
                 Log.e("llamando", "responde del reminder${responseReminder.toString()}")
                 if (responseReminder != null) {
                     reminder = responseReminder
                     Log.e("llamando", "responde no es null ${reminder.toString()}")
                 }
                 reminder?.let {
                     setReminderValues(it)
                 }
             } else {
                 Log.e("llamando", "respuesta no exitosa ${response}")
             }
         }

         override fun onFailure(call: Call<Reminder>, t: Throwable) {
             Log.e("Example", t.stackTraceToString())
         }
     })
 }
}