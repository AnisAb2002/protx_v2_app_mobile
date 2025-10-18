package fr.devmobile.protx_v2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import fr.devmobile.protx_v2.data.BD
import fr.devmobile.protx_v2.data.Produit
import fr.devmobile.protx_v2.databinding.ActivityAccueilBinding
import fr.devmobile.protx_v2.databinding.ProduitCaseBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Accueil : AppCompatActivity() {
    private lateinit var binding : ActivityAccueilBinding


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAccueilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.assistanceBtn.setOnClickListener {
            val intent = Intent(this, Assistance::class.java)
            startActivity(intent)
        }
        binding.paramBtn.setOnClickListener {
            val intent = Intent(this, Parametres::class.java)
            startActivity(intent)
        }

        binding.produitsBtn.setOnClickListener {
            val db = BD.getDatabase(this)
            val produitDao = db.produitDao()
            CoroutineScope(Dispatchers.IO).launch {
                val produits = produitDao.getProduits()

                if (produits.isEmpty()) {
                    binding.produitsDescText.text = "Aucun produit trouvé"
                } else {
                    withContext(Dispatchers.Main) {
                        afficherProduits(produits)
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun afficherProduits(produits: List<Produit>) {
        val container: LinearLayout = binding.containerProduits
        val inflater = LayoutInflater.from(this)

        for (produit in produits) {

            val itemBinding = ProduitCaseBinding.inflate(inflater, container, false)

            itemBinding.nomProduit.text = produit.nom
            itemBinding.categorieProduit.text = produit.categorie
            itemBinding.poidsProduit.text = produit.poids
            itemBinding.prixProduit.text = "${produit.prix} €"
            itemBinding.imageProduit.setImageResource(produit.image_src)

            itemBinding.btnApercu.setOnClickListener {
                // Ex: ouvrir une page de détails
            }

            container.addView(itemBinding.root)
        }

    }
}