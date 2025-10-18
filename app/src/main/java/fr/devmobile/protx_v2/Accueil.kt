package fr.devmobile.protx_v2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import fr.devmobile.protx_v2.data.BD
import fr.devmobile.protx_v2.data.Produit
import fr.devmobile.protx_v2.databinding.ActivityAccueilBinding
import kotlinx.coroutines.launch

class Accueil : AppCompatActivity() {
    private lateinit var binding : ActivityAccueilBinding

    private lateinit var produits: List<Produit>


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





        val db = BD.getDatabase(this)
        val produitDao = db.produitDao()

        lifecycleScope.launch {
            produits = produitDao.getAll() // récupérer les produits
            if (produits.isEmpty()) {
                binding.produitsDescText.text = "Aucun produit trouvé"
            } else {
                binding.produitsDescText.text = "Nombre de produits : ${produits.size}"

            }
        }
    }
}