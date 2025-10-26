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

        binding.boutonModifierLangue.setOnClickListener {
            val fragmentLangue = Changer_Langue()
            fragmentLangue.show(supportFragmentManager, "Changer_Langue")
        }

        val sharedPref = getSharedPreferences("donnees_utilisateur", MODE_PRIVATE)
        val userId = sharedPref.getInt("donnees_utilisateur", -1)
        val langue = sharedPref.getString("langue","fr")

        if(langue == "fr") {
            binding.langueChoisie.text = "Français"
        }
        else{
            binding.langueChoisie.text = "English"
        }



        if (userId == -1) {
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

            val db = BD.getDatabase(this@Profil)
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
                        val modifierInfos = Modifier_Infos()
                        modifierInfos.show(supportFragmentManager, "Modifier_Infos")
                    }

                    utilisateurInfoContainer.boutonModifierMdp.setOnClickListener {
                        val modifierMotdepasse = ModifierMotdepasse()
                        modifierMotdepasse.show(supportFragmentManager, "ModifierMotdepasse")
                    }

                    utilisateurInfoContainer.boutonSeDeconnecter.setOnClickListener {
                        val sharedPref = getSharedPreferences("donnees_utilisateur", MODE_PRIVATE)
                        sharedPref.edit {
                            clear() // ou bien  editor.remove("id...")
                            apply()
                        }
                        startActivity(Intent(this@Profil, Connexion::class.java))
                        finish()
                    }

                } else {
                    // utilisateur introuvable
                    withContext(Dispatchers.Main) {
                        val message = getString(R.string.introuvable)
                        Toast.makeText(this@Profil,"Utilisateur introuvable, veuillez vous reconnecter",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@Profil, Connexion::class.java))
                        finish()
                    }
                }
            }
        }
    }

}