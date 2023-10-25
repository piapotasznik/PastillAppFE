package edu.ort.pastillapp.ViewModels

import androidx.lifecycle.ViewModel
import edu.ort.pastillapp.Models.Reminder

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