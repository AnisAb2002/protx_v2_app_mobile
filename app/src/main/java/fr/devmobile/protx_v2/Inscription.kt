package fr.devmobile.protx_v2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import fr.devmobile.protx_v2.data.BD
import fr.devmobile.protx_v2.data.Utilisateur
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
            val nom = binding.nomEditText.text.toString()
            val prenom = binding.prenomEditText.text.toString()
            val age = binding.ageEditText.text.toString()
            val taille = binding.tailleEditText.text.toString()
            val poids = binding.poidsEditText.text.toString()
            val identifiant = binding.identifiantEditText.text.toString()
            val mdp = binding.mdpEditText.text.toString()
            val mdpConfirmation = binding.mdpConfirmationEditText.text.toString()

            if (nom.isEmpty() || prenom.isEmpty() || age.isEmpty() || taille.isEmpty() || poids.isEmpty()
                || identifiant.isEmpty() || mdp.isEmpty() || mdpConfirmation.isEmpty()){
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            }
            else if (mdp != mdpConfirmation){
                Toast.makeText(this, "La confirmation de mot de passe est incorrecte", Toast.LENGTH_SHORT).show()
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
                    utilisateurDao.inserer(utilisateur)
                    Toast.makeText(this@Inscription, "Inscription réussie", Toast.LENGTH_SHORT).show()
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