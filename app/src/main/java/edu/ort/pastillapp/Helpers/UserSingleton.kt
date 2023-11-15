package edu.ort.pastillapp.Helpers

import com.google.firebase.auth.FirebaseUser

object UserSingleton {
    var currentUser: FirebaseUser? = null
    var currentUserEmail: String? = null
    var userId: Int? = null
}