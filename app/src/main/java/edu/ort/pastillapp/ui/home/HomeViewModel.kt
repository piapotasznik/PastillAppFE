package edu.ort.pastillapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.ort.pastillapp.models.Reminder

class HomeViewModel : ViewModel() {


    val reminderListLiveData  = MutableLiveData<List<Reminder>>()
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}