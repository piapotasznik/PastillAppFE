package edu.ort.pastillapp.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import edu.ort.pastillapp.Helpers.SharedPref
import edu.ort.pastillapp.Helpers.UserSingleton
import edu.ort.pastillapp.Models.DailyStatus
import edu.ort.pastillapp.Models.UpdateDailyStatusDTO
import edu.ort.pastillapp.Services.ActivityServiceApiBuilder
import edu.ort.pastillapp.Services.DailyStatusService
import edu.ort.pastillapp.databinding.FragmentSymptomsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class SymptomsFragment : Fragment() {

    private var _binding: FragmentSymptomsBinding? = null
    private val binding get() = _binding!!
    private val checkBoxList by lazy {
        listOf(
            binding.fatigueCheckBox,
            binding.headacheCheckbox,
            binding.feverCheckbox,
            binding.soreThroatCheckbox,
            binding.stuffyNoseCheckbox,
            binding.coughCheckbox,
            binding.stomachAcheCheckbox,
            binding.musclePainCheckbox
        )
    }
    private var id = SharedPref.read(SharedPref.ID, UserSingleton.userId!!)
    private lateinit var service: DailyStatusService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSymptomsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        service = ActivityServiceApiBuilder.createDailyStatus()

        val btnSave = binding.saveBtn
        val btnCancel = binding.cxlBtn
        val editText1 = binding.editNotes3
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)

        // Obtener y mostrar el estado diario al iniciar el fragmento
        getDailyStatus(id, formattedDate)

        btnSave.setOnClickListener {
            val selectedSymptoms = mutableListOf<String>()

            checkBoxList.forEach { checkBox ->
                if (checkBox.isChecked) {
                    val index = checkBoxList.indexOf(checkBox)
                    selectedSymptoms.add(getSymptomByIndex(index))
                }
            }

            val updateStatusDTO = UpdateDailyStatusDTO(selectedSymptoms.toString(), editText1.text.toString())

            service.updateStatus(id, updateStatusDTO).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        // Actualizar y mostrar el estado diario después de la actualización
                        getDailyStatus(id, formattedDate)
                        Log.d("SymptomsFragment", "Estado diario actualizado con éxito")
                        Toast.makeText(requireContext(), "Estado diario creado", Toast.LENGTH_SHORT).show()
                    } else {
                        val responseCode = response.code()
                        Log.e("SymptomsFragment", "Response code (not successful): $responseCode")
                        Toast.makeText(requireContext(), "Error al crear estado", Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("SymptomsFragment", "Error en la llamada: ${t.message}")
                    // Puedes mostrar un mensaje de error al usuario si es necesario
                }
            })
        }

        btnCancel.setOnClickListener {
            editText1.text.clear()
            checkBoxList.forEach { checkBox ->
                checkBox.isChecked = false
            }
        }
    }

    private fun getDailyStatus(userId: Int, dateString: String) {
        service.getLogsFrom(userId, dateString).enqueue(object : Callback<DailyStatus> {
            override fun onResponse(call: Call<DailyStatus>, response: Response<DailyStatus>) {
                if (response.isSuccessful) {
                    val dailyStatus = response.body()
                    // Actualizar la interfaz de usuario con el estado diario obtenido
                    updateUIWithDailyStatus(dailyStatus)
                } else {
                    val responseCode = response.code()
                    Log.e("SymptomsFragment", "Response code (not successful): $responseCode")
                    // Puedes mostrar un mensaje de error al usuario si es necesario
                }
            }

            override fun onFailure(call: Call<DailyStatus>, t: Throwable) {
                Log.e("SymptomsFragment", "Error en la llamada: ${t.message}")
                // Puedes mostrar un mensaje de error al usuario si es necesario
            }
        })
    }

    private fun updateUIWithDailyStatus(dailyStatus: DailyStatus?) {
        if (dailyStatus != null) {
            updateCheckboxesWithSymptoms(dailyStatus.symptoms)
            binding.editNotes3.setText(dailyStatus.observation)
        }
    }

    private fun updateCheckboxesWithSymptoms(symptoms: String) {
        val symptomList = symptoms.removeSurrounding("[","]").split(", ").map { it.trim() }
        checkBoxList.forEachIndexed { index, checkBox ->
            checkBox.isChecked = symptomList.contains(getSymptomByIndex(index))
        }
    }

    private fun getSymptomByIndex(index: Int): String {
        return when (index) {
            0 -> "Fatiga"
            1 -> "Dolor de cabeza"
            2 -> "Fiebre"
            3 -> "Dolor de garganta"
            4 -> "Congestión nasal"
            5 -> "Tos"
            6 -> "Dolor de estómago"
            7 -> "Dolor muscular"
            else -> ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}