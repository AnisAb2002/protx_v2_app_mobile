package fr.devmobile.protx_v2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import fr.devmobile.protx_v2.data.BD
import fr.devmobile.protx_v2.databinding.ActivityProfilBinding
import fr.devmobile.protx_v2.databinding.ConnecterProfilBinding
import fr.devmobile.protx_v2.databinding.InformationProfilBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.content.edit

class Profil : AppCompatActivity() {

    private lateinit var binding: ActivityProfilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.retourBouton.setOnClickListener {
            finish()
        }

        binding.boutonModifierLangue.setOnClickListener {
            Toast.makeText(this@Profil, "Langue", Toast.LENGTH_SHORT).show()
        }

        val sharedPref = getSharedPreferences("donnees_utilisateur", MODE_PRIVATE)
        val userId = sharedPref.getInt("donnees_utilisateur", -1)

        if (userId == -1) {
            Toast.makeText(this@Profil, "Veuillez vous connectez", Toast.LENGTH_SHORT).show()
            val profil_container: LinearLayout = binding.informationProfilContainer
            val inflater = LayoutInflater.from(this)

            val connecter = ConnecterProfilBinding.inflate(inflater, profil_container, false)
            profil_container.addView(connecter.root)

            connecter.seConnecter.setOnClickListener {
                val intent = Intent(this, Connexion::class.java)
                startActivity(intent)
                finish()
            }
        }
        else {
            //connecté déjà
            val profil_container: LinearLayout = binding.informationProfilContainer
            val inflater = LayoutInflater.from(this)

            val utilisateurInfoContainer =
                InformationProfilBinding.inflate(inflater, profil_container, false)
            profil_container.addView(utilisateurInfoContainer.root)

            val db = BD.getDatabase(this)
            val utilisateurDao = db.utilisateurDao()

            lifecycleScope.launch {
                val utilisateur = utilisateurDao.getConnecter(userId)
                if (utilisateur != null) {
                    utilisateurInfoContainer.nomText.text = utilisateur.nom
                    utilisateurInfoContainer.prenomText.text = utilisateur.prenom
                    utilisateurInfoContainer.ageText.text = utilisateur.age.toString()
                    utilisateurInfoContainer.tailleText.text = utilisateur.taille.toString()
                    utilisateurInfoContainer.poidsText.text = utilisateur.poids.toString()

                    utilisateurInfoContainer.boutonModifierInfo.setOnClickListener {
                        Toast.makeText(this@Profil, "Modifier info", Toast.LENGTH_SHORT).show()
                    }
                    utilisateurInfoContainer.boutonSeDeconnecter.setOnClickListener {
                        val sharedPref = getSharedPreferences("donnees_utilisateur", MODE_PRIVATE)
                        sharedPref.edit {
                            clear() // ou editor.remove("user_id")
                            apply()
                        }

                        Toast.makeText(this@Profil, "Déconnexion réussie", Toast.LENGTH_SHORT).show()

                        startActivity(Intent(this@Profil, Connexion::class.java))
                        finish()
                    }

                } else {
                    // utilisateur introuvable → forcer reconnexion
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@Profil,
                            "Utilisateur introuvable, veuillez vous reconnecter",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@Profil, Connexion::class.java))
                        finish()
                    }
                }
            }
        }
    }
}