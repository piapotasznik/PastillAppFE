package edu.ort.pastillapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import edu.ort.pastillapp.databinding.ActivityHomeScreenBinding

class HomeScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeScreenBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val extras = intent.extras
        if (extras != null) {
            val valorRecibido = extras.getString("user") // "clave" debe ser la misma que usaste para enviar el dato
        }


        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_home_screen)


        getSupportActionBar()?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar()?.setCustomView(R.layout.toolbar_layout);

        // Después de haber inflado tu Toolbar personalizada, grabamos el nombre
        val customToolbar = supportActionBar?.customView
        val textViewTitulo = customToolbar?.findViewById<TextView>(R.id.tvToolbar)

     // Verificar si el TextView fue encontrado
        if (textViewTitulo != null) {
            textViewTitulo.text = UserSingleton.currentUser?.email
        }

        // Configura el BottomNavigationView con el NavController
           navView.setupWithNavController(navController)
    }
}