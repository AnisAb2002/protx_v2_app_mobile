package fr.devmobile.protx_v2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import fr.devmobile.protx_v2.databinding.ActivityInscriptionBinding
import kotlinx.coroutines.launch

class Inscription : AppCompatActivity() {
    private lateinit var binding: ActivityInscriptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInscriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val utilisateurDao = BD.getDatabase(this).utilisateurDao()

        binding.buttonInscription.setOnClickListener {
            val nom = binding.nomEditText.text.toString().trim()
            val prenom = binding.prenomEditText.text.toString().trim()
            val age = binding.ageEditText.text.toString().trim()
            val taille = binding.tailleEditText.text.toString().trim()
            val poids = binding.poidsEditText.text.toString().trim()
            val identifiant = binding.identifiantEditText.text.toString().trim()
            val mdp = binding.mdpEditText.text.toString().trim()
            val mdpConfirmation = binding.mdpConfirmationEditText.text.toString().trim()

            if (nom.isEmpty() || prenom.isEmpty() || age.isEmpty() || taille.isEmpty() || poids.isEmpty()
                || identifiant.isEmpty() || mdp.isEmpty() || mdpConfirmation.isEmpty()){
                Toast.makeText(this, getString(R.string.remplir), Toast.LENGTH_SHORT).show()
            }
            else if (mdp != mdpConfirmation){
                Toast.makeText(this, getString(R.string.MdpConfirmation), Toast.LENGTH_SHORT).show()
            }
            else {
                val utilisateur = Utilisateur(
                    nom = nom,
                    prenom = prenom,
                    identifiant = identifiant,
                    motDePasse = mdp,
                    age = age.toInt(),
                    taille = taille.toFloat(),
                    poids = poids.toFloat()
                )
                lifecycleScope.launch {
                    val utilisateurExistant = utilisateurDao.authentifier(identifiant,mdp)
                    if(utilisateurExistant == null){
                        utilisateurDao.inserer(utilisateur)
                        Toast.makeText(this@Inscription, getString(R.string.inscriptionReussi), Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this@Inscription, getString(R.string.existant), Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                val intent = Intent(this, Connexion::class.java)
                startActivity(intent)
                finish()
            }
        }
        binding.buttonIgnorer.setOnClickListener {
            val intent = Intent(this, Connexion::class.java)
            startActivity(intent)
            finish()
        }
    }
}