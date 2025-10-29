package fr.devmobile.protx_v2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fr.devmobile.protx_v2.databinding.ActivityConnexionBinding
import androidx.core.content.edit
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.security.MessageDigest

class Connexion : AppCompatActivity() {
    private lateinit var binding : ActivityConnexionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConnexionBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.buttonSeConnecter.setOnClickListener {
            val identifiant = binding.identifiant.text.toString()
            val motDePasse = binding.mdp.text.toString()

            val db = Firebase.firestore

            db.collection("utilisateurs")
                .whereEqualTo("identifiant", identifiant)
                .whereEqualTo("motDePasse", hashMotDePasse(motDePasse))
                .get()
                .addOnSuccessListener { result ->
                    if (result.isEmpty) {
                        Toast.makeText(this@Connexion, getString(R.string.identifiantIncorecte), Toast.LENGTH_SHORT).show()

                    }
                    else {
                        val doc = result.documents.first()
                        val utilisateur = doc.toObject(Utilisateur::class.java)

                        Toast.makeText(this@Connexion, getString(R.string.bienvenue) + " ${utilisateur?.prenom}",Toast.LENGTH_SHORT).show()

                        val sharedPref = getSharedPreferences("donnees_utilisateur", MODE_PRIVATE)
                        sharedPref.edit {
                            putString("idUtilisateur", doc.id)
                            putString("identifiant", utilisateur!!.identifiant)
                        }

                        val intent = Intent(this@Connexion, Accueil::class.java)

                        startActivity(intent)
                        finish()
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


    fun hashMotDePasse(mdp: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(mdp.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}