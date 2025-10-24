package fr.devmobile.protx_v2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import fr.devmobile.protx_v2.data.BD
import fr.devmobile.protx_v2.databinding.ActivityRechercheBinding
import fr.devmobile.protx_v2.databinding.AucunProduitTrouveBinding
import fr.devmobile.protx_v2.databinding.ConnecterProfilBinding
import fr.devmobile.protx_v2.databinding.ProduitCaseBinding
import fr.devmobile.protx_v2.databinding.ProduitsTrouvesBinding
import kotlinx.coroutines.launch

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
            val rechercheEditText = binding.rechercheEditText.text

            if(rechercheEditText.isEmpty()){
                val message = getString(R.string.rechercheVide)
                Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
            }
            else{
                val texteRecherche = rechercheEditText.toString().lowercase().trim()

                val bd = BD.getDatabase(this)
                val produitDao = bd.produitDao()

                lifecycleScope.launch {
                    val listeProduitsTrouves = produitDao.rechercheProduits(texteRecherche)
                    val produit_container: LinearLayout = binding.produitsContainer

                    produit_container.removeAllViews()
                    val inflater = LayoutInflater.from(this@Recherche)

                    if (listeProduitsTrouves.isEmpty()){
                        val aucunProduit = AucunProduitTrouveBinding.inflate(inflater, produit_container, false)
                        produit_container.addView(aucunProduit.root)
                    }
                    else{
                        val produitTrouves = ProduitsTrouvesBinding.inflate(inflater, produit_container, false)
                        produit_container.addView(produitTrouves.root)

                        for (produit in listeProduitsTrouves) {

                            val itemBinding = ProduitCaseBinding.inflate(inflater, produit_container, false)

                            itemBinding.nomProduit.text = produit.nom
                            itemBinding.categorieProduit.text = produit.categorie
                            itemBinding.poidsProduit.text = produit.poids
                            itemBinding.prixProduit.text = "${produit.prix} €"
                            itemBinding.imageProduit.setImageResource(produit.image_src)

                            itemBinding.btnApercu.setOnClickListener {
                                // Ex: ouvrir une page de détails
                            }

                            produit_container.addView(itemBinding.root)
                        }
                    }
                }

            }
        }
    }
}