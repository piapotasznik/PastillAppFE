package edu.ort.pastillapp.ui.EditReminder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.ort.pastillapp.models.Reminder

class EditReminderViewModel : ViewModel() {
    private val _reminder = MutableLiveData<Reminder>()
    val reminder: LiveData<Reminder>
        get()=_reminder

    // Método para recibir y almacenar el Reminder
    fun setReminder(reminder: Reminder) {
        _reminder.value= reminder
        //_reminderTitle.value = reminder.medicine
        // Agrega aquí más líneas de código para los demás campos
    }

    fun updateMedicine(medicine: String) {
        val currentReminder = _reminder.value ?: return
        val updatedReminder = currentReminder.copy(medicine = medicine)
        _reminder.value = updatedReminder
    }
}