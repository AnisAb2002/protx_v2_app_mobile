package fr.devmobile.protx_v2

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

        binding.envoyerButton.setOnClickListener {
            chercerProduit()
        }

    }

    private fun chercerProduit() {

        val ageTexte = binding.ageEditText.text.toString()
        val tailleTexte = binding.tailleEditText.text.toString()
        val poidsTexte = binding.poidsEditText.text.toString()

        if (ageTexte.isEmpty() || tailleTexte.isEmpty() || poidsTexte.isEmpty()) {
            val message = getString(R.string.remplir)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            return
        }
        else{
            val age = ageTexte.toFloat()
            val taille = tailleTexte.toFloat() /100 //en m pas en cm
            val  poids = poidsTexte.toFloat()
            val  objectif = binding.objectifSpinner.selectedItem.toString()

            val imc = poids/(taille*taille)

            if(age <18){
                //pas trop agé
                Toast.makeText(this, "Conseil : éviter ces produits", Toast.LENGTH_SHORT).show()
            }
            else{
                if (imc<18.5){
                    //prise de masse forcée
                    Toast.makeText(this, "$imc produits de prise de mass", Toast.LENGTH_SHORT).show()
                }
                else if(imc>30){
                    //perte de poids forcée
                    //proteine
                    Toast.makeText(this, "produits de perte de poids ", Toast.LENGTH_SHORT).show()
                }
                else{
                    when(objectif){
                        "Perte de poids" -> Toast.makeText(this, "Créatine", Toast.LENGTH_SHORT).show()
                        "Prise de masse" -> Toast.makeText(this, "Proteines Whey, Gainer Mass et energisant", Toast.LENGTH_SHORT).show()
                        "Amélioration des performances" -> Toast.makeText(this, "Energisant et créatine", Toast.LENGTH_SHORT).show()
                        "Maintien de la forme" -> Toast.makeText(this, "Energisant et proteines ", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }


    }
}