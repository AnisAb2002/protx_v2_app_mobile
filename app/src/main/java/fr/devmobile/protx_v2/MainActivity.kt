package fr.devmobile.protx_v2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fr.devmobile.protx_v2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonIgnorer.setOnClickListener {
            val intent = Intent(this, Accueil::class.java)
            startActivity(intent)
            finish()
        }
        binding.buttonSeConnecter.setOnClickListener {
            val intent = Intent(this, Connexion::class.java)
            startActivity(intent)
            finish()
        }

        val sharedPref = getSharedPreferences("donnees_utilisateur", MODE_PRIVATE)
        val userId = sharedPref.getInt("donnees_utilisateur", -1)

        if (userId != -1) {
            val intent = Intent(this, Accueil::class.java)
            startActivity(intent)
            finish()
        }


    }


}