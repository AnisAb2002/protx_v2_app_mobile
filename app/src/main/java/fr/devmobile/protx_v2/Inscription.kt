package fr.devmobile.protx_v2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import fr.devmobile.protx_v2.databinding.ActivityInscriptionBinding
import java.security.MessageDigest

class Inscription : AppCompatActivity() {
    private lateinit var binding: ActivityInscriptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInscriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)



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
                val db = Firebase.firestore

                val utilisateur = Utilisateur(
                    nom = nom,
                    prenom = prenom,
                    identifiant = identifiant,
                    motDePasse = hashMotDePasse(mdp),
                    age = age.toInt(),
                    taille = taille.toFloat(),
                    poids = poids.toFloat()
                )

                db.collection("utilisateurs")
                    .whereEqualTo("identifiant", identifiant)
                    .whereEqualTo("motDePasse", hashMotDePasse(mdp))
                    .get()
                    .addOnSuccessListener { result ->
                        if (!result.isEmpty) {
                            // existant
                            Toast.makeText(this@Inscription, getString(R.string.existant), Toast.LENGTH_SHORT).show()
                        }
                        else {
                            val idUtilisateur = db.collection("utilisateurs").document().id // génère un ID unique

                            db.collection("utilisateurs").document(idUtilisateur)
                                .set(utilisateur)
                                .addOnSuccessListener {
                                    Toast.makeText(this@Inscription, getString(R.string.inscriptionReussi), Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, Connexion::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Erreur : ${e.message}", Toast.LENGTH_LONG).show()
                                }
                        }
                    }
            }
        }
        binding.buttonIgnorer.setOnClickListener {
            val intent = Intent(this, Connexion::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun hashMotDePasse(mdp: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(mdp.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}