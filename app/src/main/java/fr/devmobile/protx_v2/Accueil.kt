package fr.devmobile.protx_v2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import fr.devmobile.protx_v2.databinding.ActivityAccueilBinding
import fr.devmobile.protx_v2.databinding.ProduitCaseBinding

class Accueil : AppCompatActivity() {
    private lateinit var binding : ActivityAccueilBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAccueilBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.rechercherBtn.setOnClickListener {
            val intent = Intent(this, Recherche::class.java)
            startActivity(intent)
        }

        binding.assistanceBtn.setOnClickListener {
            val intent = Intent(this, Assistance::class.java)
            startActivity(intent)
        }
        binding.profilBtn.setOnClickListener {
            val intent = Intent(this, Profil::class.java)
            startActivity(intent)
            finish()
        }

        val sharedPref = getSharedPreferences("donnees_utilisateur", MODE_PRIVATE)
        val langue = sharedPref.getString("langue","fr")

        val db = Firebase.firestore
        db.collection("produits").get()
        .addOnSuccessListener {
            produits ->
            if (produits.isEmpty){
                binding.produitsDescText.text = getString(R.string.aucunProduitErreur)
            }
            else{
                for (produit in produits) {
                    val prod = produit.toObject(Produit::class.java)
                    afficherProduits(prod, langue.toString())
                }
            }
        }
        .addOnFailureListener { exception ->
            binding.produitsDescText.text = getString(R.string.aucunProduitErreur)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun afficherProduits(produit: Produit, langue : String) {

        val container: LinearLayout = binding.containerProduits
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