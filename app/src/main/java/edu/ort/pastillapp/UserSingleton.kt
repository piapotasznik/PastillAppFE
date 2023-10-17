package edu.ort.pastillapp
import com.google.firebase.auth.FirebaseUser
object UserSingleton {
    var currentUser: FirebaseUser? = null
    var currentUserEmail: String? = null

}