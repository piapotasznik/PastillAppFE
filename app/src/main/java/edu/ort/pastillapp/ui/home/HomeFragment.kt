package edu.ort.pastillapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.ort.pastillapp.databinding.FragmentHomeBinding
import edu.ort.pastillapp.helpers.Helpers
import edu.ort.pastillapp.listener.OnClickNavigate
import edu.ort.pastillapp.models.Medicine
import edu.ort.pastillapp.models.Reminder
import edu.ort.pastillapp.services.ActivityServiceApiBuilder

import edu.ort.pastillapp.ui.symtoms_.SymtomsViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() , OnClickNavigate {
    var medication: MutableList<Medication> = ArrayList()
    private val reminderList = mutableListOf<Reminder>()
    private val medicineList = mutableListOf<Medicine>()
    private lateinit var adapter: ReminderAdatper

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


        //agrego reminder mockeado hastas que se arregle endpoint
        getMockReminder()
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



        // adapter = ReminderAdatper(reminderList)
        adapter = ReminderAdatper(reminderList, findNavController())
//       adapter.setOnMedicineClickListener(this)
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



    fun getMockReminder(){
        // ojo que calendar toma los meses de 0 a 11  y no de 1 a 12
        val fecha1 = Helpers().crearDateTimeManualmente(2023, 9, 20, 19, 0) // Año, mes (0-11), día, hora, minuto
        val fecha2 = Helpers().crearDateTimeManualmente(2023, 10, 20, 8, 30)
        if(reminderList.isEmpty()){
            reminderList.add(Reminder(1,
                                    2,"20 grm",
                                    "Comprimidos",
                                    fecha1,
                                    "Dias",
                                    8,
                                    false,
                                    "",
                                    "Meses",
                                    1,
                                    "fran",
                                    "Paracetamol"

                ))
            reminderList.add(Reminder(1,
                3,"5 grm",
                "Comprimidos",
                fecha2,
                "Horas",
                12,
                false,
                "",
                "Dias",
                5,
                "fran",
                "Amlodipina"

            ))
            Log.e("lista","Saliendo fel getmocjReminder")
        }
        Log.e("lista",reminderList.toString())
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
                        medicineList.clear()
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

    override fun OnClickNavigate(reminder :Reminder) {
        val action = HomeFragmentDirections.actionNavigationHomeToEditReminderFragment(reminder)
        Log.e("click","estoy clickeando aqui!!!")
        this.findNavController().navigate(action)
    }

}