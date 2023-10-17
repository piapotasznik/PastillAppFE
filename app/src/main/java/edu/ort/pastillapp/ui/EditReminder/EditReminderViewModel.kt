package edu.ort.pastillapp.ui.EditReminder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.ort.pastillapp.models.Reminder

class EditReminderViewModel : ViewModel() {
    private var reminder : Reminder?= null


    // Método para recibir y almacenar el Reminder
    fun setReminder(reminder: Reminder) {
        this.reminder = reminder
    }

    fun getReminder(): Reminder? {
        return reminder
    }


}