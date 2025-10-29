package fr.devmobile.protx_v2

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import fr.devmobile.protx_v2.databinding.ActivityRechercheBinding
import fr.devmobile.protx_v2.databinding.AucunProduitTrouveBinding
import fr.devmobile.protx_v2.databinding.ProduitCaseBinding

class Recherche : AppCompatActivity() {
    private lateinit var binding : ActivityRechercheBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRechercheBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.retourBouton.setOnClickListener {
            finish()
        }

        binding.rechercheBouton.setOnClickListener {
            val rechercheEditText = binding.rechercheEditText.text.toString()

            if(rechercheEditText.isEmpty()){
                Toast.makeText(this, getString(R.string.rechercheVide),Toast.LENGTH_SHORT).show()
            }
            else{
                val texteRecherche = rechercheEditText.lowercase().trim()

                val sharedPref = getSharedPreferences("donnees_utilisateur", MODE_PRIVATE)
                val langue = sharedPref.getString("langue","fr")

                val produit_container: LinearLayout = binding.produitsContainer
                produit_container.removeAllViews()

                val inflater = LayoutInflater.from(this@Recherche)

                val db = Firebase.firestore
                db.collection("produits").get()
                .addOnSuccessListener {
                    produits ->
                    if (produits.isEmpty){
                        val aucunProduit = AucunProduitTrouveBinding.inflate(inflater, produit_container, false)
                        produit_container.addView(aucunProduit.root)
                    }
                    else{
                        var listeProduit = mutableListOf<Produit>()
                        for (produit in produits) {
                            val prod = produit.toObject(Produit::class.java)
                            if(prod.categorie.contains(texteRecherche, ignoreCase = true)
                                || prod.nom.contains(texteRecherche, ignoreCase = true)){

                                listeProduit.add(prod)
                            }
                        }
                        if (listeProduit.isEmpty()){
                            val aucunProduit = AucunProduitTrouveBinding.inflate(inflater, produit_container, false)
                            produit_container.addView(aucunProduit.root)
                        }
                        else{
                            for (produit in produits) {
                                val prod = produit.toObject(Produit::class.java)
                                if(prod.categorie.contains(texteRecherche, ignoreCase = true)
                                    || prod.nom.contains(texteRecherche, ignoreCase = true)){

                                    afficherProduits(prod, langue.toString())
                                }
                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    val aucunProduit = AucunProduitTrouveBinding.inflate(inflater, produit_container, false)
                    produit_container.addView(aucunProduit.root)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n", "UseGetLayoutInflater")
    private fun afficherProduits(produit: Produit, langue: String) {


        val container = findViewById<LinearLayout>(R.id.produits_container)
        val inflater = LayoutInflater.from(this)

        val itemBinding = ProduitCaseBinding.inflate(inflater, container, false)

        itemBinding.nomProduit.text = produit.nom
        itemBinding.categorieProduit.text = produit.categorie
        itemBinding.poidsProduit.text = produit.poids
        itemBinding.prixProduit.text = "${produit.prix} €"

        val imageNom = "produit_${produit.id}"
        val imageId = itemBinding.root.context.resources.getIdentifier(
            imageNom,
            "drawable",
            itemBinding.root.context.packageName
        )

        itemBinding.imageProduit.setImageResource(imageId)

        itemBinding.btnApercu.setOnClickListener {
            val fragment = ApercuProduit()
            val bundle = Bundle().apply {
                putString("nom", produit.nom)
                putString("poids", produit.poids)
                putDouble("prix", produit.prix)
                putString("categorie", produit.categorie)
                putInt("image_src", imageId)
                putString("portion",produit.portion)

                if (langue == "fr"){
                    putString("composition",produit.compo_fr)
                    putString("description", produit.desc_fr)
                }
                else{
                    putString("composition",produit.compo_en)
                    putString("description", produit.desc_en)
                }
            }
            fragment.arguments = bundle
            fragment.show(supportFragmentManager, "ApercuProduit")
        }
        container.addView(itemBinding.root)
    }
}