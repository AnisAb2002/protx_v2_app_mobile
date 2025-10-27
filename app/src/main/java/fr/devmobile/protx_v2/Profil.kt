package fr.devmobile.protx_v2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import fr.devmobile.protx_v2.databinding.ActivityProfilBinding
import fr.devmobile.protx_v2.databinding.ConnecterProfilBinding
import fr.devmobile.protx_v2.databinding.InformationProfilBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.content.edit

class Profil : AppCompatActivity() {

    private lateinit var binding: ActivityProfilBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.retourBouton.setOnClickListener {
            val intent = Intent(this, Accueil::class.java)
            startActivity(intent)
            finish()
        }

        binding.boutonPropos.setOnClickListener {
            Apropos().show(supportFragmentManager, "Apropos")
        }

        binding.boutonModifierLangue.setOnClickListener {
            Changer_Langue().show(supportFragmentManager, "Changer_Langue")
        }

        val sharedPref = getSharedPreferences("donnees_utilisateur", MODE_PRIVATE)
        val idUtilisateur = sharedPref.getInt("idUtilisateur", -1)
        val langue = sharedPref.getString("langue","fr")

        if(langue == "fr") {
            binding.langueChoisie.text = "Français"
        }
        else{
            binding.langueChoisie.text = "English"
        }



        if (idUtilisateur == -1) {
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

            val utilisateurInfoContainer = InformationProfilBinding.inflate(inflater, profil_container, false)
            profil_container.addView(utilisateurInfoContainer.root)

            val db = BD.getDatabase(this)
            val utilisateurDao = db.utilisateurDao()

            lifecycleScope.launch {
                val utilisateur = utilisateurDao.getConnecter(idUtilisateur)
                if (utilisateur != null) {
                    utilisateurInfoContainer.nomText.text = utilisateur.nom
                    utilisateurInfoContainer.prenomText.text = utilisateur.prenom
                    utilisateurInfoContainer.ageText.text = utilisateur.age.toString()
                    utilisateurInfoContainer.tailleText.text = utilisateur.taille.toString()
                    utilisateurInfoContainer.poidsText.text = utilisateur.poids.toString()

                    utilisateurInfoContainer.boutonModifierInfo.setOnClickListener {
                        Modifier_Infos().show(supportFragmentManager, "Modifier_Infos")
                    }

                    utilisateurInfoContainer.boutonModifierMdp.setOnClickListener {
                        ModifierMotdepasse().show(supportFragmentManager, "ModifierMotdepasse")
                    }

                    utilisateurInfoContainer.boutonSeDeconnecter.setOnClickListener {
                        sharedPref.edit {
                            clear() // ou bien  editor.remove("id...")
                            apply()
                        }
                        startActivity(Intent(this@Profil, Connexion::class.java))
                        finish()
                    }

                    utilisateurInfoContainer.boutonSupprimer.setOnClickListener {
                        SupprimerProfil().show(supportFragmentManager, "SupprimerProfil")
                    }

                } else {
                    // utilisateur introuvable
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@Profil,getString(R.string.introuvable),Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@Profil, Connexion::class.java))
                        finish()
                    }
                }
            }
        }
    }
}