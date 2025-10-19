package fr.devmobile.protx_v2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import fr.devmobile.protx_v2.data.BD
import fr.devmobile.protx_v2.databinding.ActivityConnexionBinding
import kotlinx.coroutines.launch
import androidx.core.content.edit

class Connexion : AppCompatActivity() {
    private lateinit var binding : ActivityConnexionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConnexionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val utilisateurDao = BD.getDatabase(this).utilisateurDao()

        binding.buttonSeConnecter.setOnClickListener {
            val identifiant = binding.identifiant.text.toString()
            val motDePasse = binding.mdp.text.toString()

            lifecycleScope.launch {

                val utilisateur = utilisateurDao.authentifier(identifiant, motDePasse)
                if (utilisateur != null) {

                    Toast.makeText(this@Connexion,"Bienvenue ${utilisateur.prenom}",Toast.LENGTH_SHORT).show()

                    val sharedPref = getSharedPreferences("donnees_utilisateur", MODE_PRIVATE)
                    sharedPref.edit {
                        putInt("donnees_utilisateur", utilisateur.idUtilisateur)
                    }

                    val intent = Intent(this@Connexion, Accueil::class.java)

                    startActivity(intent)
                    finish()

                } else {
                    Toast.makeText(this@Connexion, "Identifiants incorrects", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.buttonInscription.setOnClickListener {
            val intent = Intent(this, Inscription::class.java)
            startActivity(intent)
            finish()
        }

        binding.buttonIgnorer.setOnClickListener {
            val intent = Intent(this, Accueil::class.java)
            startActivity(intent)
            finish()
        }
    }
}