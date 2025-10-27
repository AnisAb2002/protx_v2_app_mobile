package fr.devmobile.protx_v2

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fr.devmobile.protx_v2.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        val prefs = getSharedPreferences("donnees_utilisateur", MODE_PRIVATE)
        val langue = prefs.getString("langue", "fr")
        setLocale(this, langue!!)


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
        val idUtilisateur = sharedPref.getInt("idUtilisateur", -1)

        if (idUtilisateur != -1) {
            val intent = Intent(this, Accueil::class.java)
            startActivity(intent)
            finish()
        }


    }

    fun setLocale(context: Context, langue: String){
        val locale = Locale(langue)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}