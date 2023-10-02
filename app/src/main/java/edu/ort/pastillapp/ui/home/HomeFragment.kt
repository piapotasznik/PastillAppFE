package edu.ort.pastillapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.text.toUpperCase
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.ort.pastillapp.databinding.FragmentHomeBinding
import edu.ort.pastillapp.helpers.DayToday
import edu.ort.pastillapp.ui.symtoms_.SymtomsViewModel
import java.text.DateFormatSymbols
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {

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
        val dateAdapter = DateAdapter(obtenerDiasDelMes())
        val posicionInicial = DayToday().dayToday()+2

        binding.rvDate.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvDate.adapter = dateAdapter
        binding.rvDate.smoothScrollToPosition(posicionInicial)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




    fun obtenerDiasDelMes(): List<Dia> {
        val dias = mutableListOf<Dia>()
        val calendario = Calendar.getInstance()
        val formatoNumeroDia = SimpleDateFormat("d", Locale.getDefault())

        val symbols = DateFormatSymbols(Locale("es", "ES"))
        val nombresDias = symbols.shortWeekdays // Obtiene los nombres cortos de los días en español

        val today = calendario.get(Calendar.DAY_OF_MONTH)
        val ultimoDiaDelMes = calendario.getActualMaximum(Calendar.DAY_OF_MONTH)

        // Obtener los tres días anteriores a hoy
        for (i in today - 3 until today) {
            calendario.set(Calendar.DAY_OF_MONTH, i)
            val diaSemana = nombresDias[calendario.get(Calendar.DAY_OF_WEEK)]
            val primeraLetra = diaSemana[0].toString().uppercase()
            val numeroDia = formatoNumeroDia.format(calendario.time).toInt()
            val dia = Dia(primeraLetra, numeroDia, false)
            dias.add(dia)
        }

        // Obtener el día de hoy
        calendario.set(Calendar.DAY_OF_MONTH, today)
        val diaSemanaHoy = nombresDias[calendario.get(Calendar.DAY_OF_WEEK)]
        val primeraLetraHoy = diaSemanaHoy[0].toString().uppercase()
        val diaHoy = Dia(primeraLetraHoy, today, true)
        dias.add(diaHoy)

        // Obtener los tres días posteriores a hoy
        for (i in today + 1..today + 3) {
            calendario.set(Calendar.DAY_OF_MONTH, i)
            val diaSemana = nombresDias[calendario.get(Calendar.DAY_OF_WEEK)]
            val primeraLetra = diaSemana[0].toString().uppercase()
            val numeroDia = formatoNumeroDia.format(calendario.time).toInt()
            val dia = Dia(primeraLetra, numeroDia, false)
            dias.add(dia)
        }

        return dias
    }
}