package edu.ort.pastillapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.text.toUpperCase
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.ort.pastillapp.databinding.FragmentHomeBinding
import edu.ort.pastillapp.databinding.FragmentSymtomsBinding
import edu.ort.pastillapp.helpers.Helpers
import edu.ort.pastillapp.listener.OnClickNavigate
import edu.ort.pastillapp.models.Medicine
import edu.ort.pastillapp.models.Reminder
import edu.ort.pastillapp.services.ActivityServiceApiBuilder
import edu.ort.pastillapp.ui.symtoms_.MedAdapter
import edu.ort.pastillapp.ui.symtoms_.Medication
import edu.ort.pastillapp.ui.symtoms_.SymtomsFragmentDirections
import edu.ort.pastillapp.ui.symtoms_.SymtomsViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormatSymbols
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() , OnClickNavigate {
    var medication: MutableList<Medication> = ArrayList()
    private val reminderList = mutableListOf<Reminder>()
    private val medicineList = mutableListOf<Medicine>()
    private lateinit var adapter: MedAdapter

    private var _binding: FragmentHomeBinding? = null



    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(SymtomsViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val rvDate: RecyclerView = binding.rvDate
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            rvDate.text = it
//        }

        //  getReminder()
//            MedAdapter(medication)

        getMedName()
        return root
    }

    override fun onStart() {
        super.onStart()
        intiRecylcerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun intiRecylcerView(){
        val dateAdapter = DateAdapter(Helpers().getDayOfMoth())
        val posicionInicial = Helpers().dayToday()+2

        binding.rvDate.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvDate.adapter = dateAdapter
        binding.rvDate.smoothScrollToPosition(posicionInicial)


        // invento medicaction para cargar, luego pedir por Retrofit
        medication.add(Medication(2,"Pill","Paracetamol"))
        medication.add(Medication(10,"Pill","Rosubastatina"))

        // adapter = ReminderAdatper(reminderList)
        adapter = MedAdapter(medicineList)
        adapter.setOnMedicineClickListener(this)
        binding.medList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.medList.adapter = adapter
    }
    fun getReminder() {


        val service = ActivityServiceApiBuilder.createReminder()

        service.getReminders().enqueue(object : Callback<List<Reminder>> {
            override fun onResponse(
                call: Call<List<Reminder>>,
                response: Response<List<Reminder>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val responseReminder = response.body()

                    if (responseReminder != null) {
                        reminderList.addAll(responseReminder)


                    }

                    // Actualizar el RecyclerView
                    adapter.notifyDataSetChanged()


                } else {
                    Log.e("Respuesta no exitosa", " esta es la respuesta $response")
                }

            }

            override fun onFailure(call: Call<List<Reminder>>, t: Throwable) {
                Log.e("Example", t.stackTraceToString())
            }
        })
    }




    fun getMedName() {


        val service = ActivityServiceApiBuilder.createMedicine()

        service.getAll().enqueue(object : Callback<List<Medicine>> {
            override fun onResponse(
                call: Call<List<Medicine>>,
                response: Response<List<Medicine>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val responseMedicine = response.body()

                    if (responseMedicine != null) {
                        medicineList.addAll(responseMedicine)


                    }

                    // Actualizar el RecyclerView
                    adapter.notifyDataSetChanged()


                } else {
                    Log.e("Respuesta no exitosa", " esta es la respuesta $response")
                }

            }

            override fun onFailure(call: Call<List<Medicine>>, t: Throwable) {
                Log.e("Example", t.stackTraceToString())
            }
        })
    }

    override fun OnClickNavigate() {
        val action = SymtomsFragmentDirections.actionNavigationSymptomsToEditReminderFragment()
        Log.e("click","estoy clickeando aqui!!!")
        this.findNavController().navigate(action)
    }

}