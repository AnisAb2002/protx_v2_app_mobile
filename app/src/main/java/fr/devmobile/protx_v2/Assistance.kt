package fr.devmobile.protx_v2

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import fr.devmobile.protx_v2.databinding.ActivityAssistanceBinding

class Assistance : AppCompatActivity() {

    private lateinit var binding : ActivityAssistanceBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAssistanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.retourBouton.setOnClickListener {
            finish()
        }

        val sharedPref = getSharedPreferences("donnees_utilisateur", MODE_PRIVATE)
        val langue = sharedPref.getString("langue","fr")
        val idUtilisateur = sharedPref.getString("idUtilisateur", null)

        if (idUtilisateur != null) {
            //Conecté
            //remplissage des EditText avec les informations qu'on a déja sur l'utilisateur

            val db = Firebase.firestore

            db.collection("utilisateurs")
                .document(idUtilisateur)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val utilisateur = document.toObject(Utilisateur::class.java)

                        binding.ageEditText.setText(utilisateur!!.age.toString())
                        binding.tailleEditText.setText(utilisateur.taille.toString())
                        binding.poidsEditText.setText(utilisateur.poids.toString())
                    }
                }

        }

        binding.envoyerButton.setOnClickListener {
            chercerProduit(langue.toString())
        }

    }

    @SuppressLint("DefaultLocale")
    private fun chercerProduit(langue : String) {

        val ageTexte = binding.ageEditText.text.toString()
        val tailleTexte = binding.tailleEditText.text.toString()
        val poidsTexte = binding.poidsEditText.text.toString()
        val objectifRadio = binding.objectifRadio.checkedRadioButtonId


        if (ageTexte.isEmpty() || tailleTexte.isEmpty() || poidsTexte.isEmpty() || (objectifRadio == -1)) {
            val message = getString(R.string.remplir)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            return
        }

        val age = ageTexte.toInt()
        val taille = tailleTexte.toFloat() /100 //en m pas en cm
        val  poids = poidsTexte.toFloat()
        val objectif = when{
            binding.pertePoidsRadio.isChecked -> binding.pertePoidsRadio.text.toString()
            binding.prisePoidsRadio.isChecked -> binding.prisePoidsRadio.text.toString()
            binding.performanceRadio.isChecked -> binding.performanceRadio.text.toString()
            binding.maintienRadio.isChecked -> binding.maintienRadio.text.toString()
            else -> binding.pertePoidsRadio.text.toString()
        }

        val imc = poids/(taille*taille)

        val bundle = Bundle().apply {
            putString("imc", String.format("%.2f",imc))
            putString("langue",langue)
        }

        when {
            age <18 -> bundle.putString("cas", "1")
            imc < 18.5 -> bundle.putString("cas", "2")
            imc>30 -> bundle.putString("cas", "3")
            objectif == getString(R.string.perte_poids) -> bundle.putString("cas", "4")
            objectif == getString(R.string.prise_poids) -> bundle.putString("cas", "5")
            objectif == getString(R.string.performance) -> bundle.putString("cas", "6")
            objectif == getString(R.string.maintien) -> bundle.putString("cas", "7")
        }

        val fragment = AssistanceProduits().apply { arguments = bundle }

        fragment.show(supportFragmentManager, "AssistanceProduits")
    }
}