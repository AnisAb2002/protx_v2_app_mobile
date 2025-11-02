package fr.devmobile.protx_v2

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import fr.devmobile.protx_v2.databinding.PanierVideBinding
import fr.devmobile.protx_v2.databinding.ProduitDansPanierBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.text.toInt

class Panier : DialogFragment() {

    private var somme = 0.0
    private lateinit var db: BD
    private lateinit var panierDao: PanierEntityDao

    @SuppressLint("UseGetLayoutInflater", "DetachAndAttachSameFragment", "SetTextI18n",
        "DefaultLocale"
    )
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_panier, container, false)

        val container: LinearLayout = view.findViewById(R.id.containerProduits)
        val inflater = LayoutInflater.from(requireContext())

        db = BD.getDatabase(requireContext())
        panierDao = db.panierDao()


        val boutonRetour = view.findViewById<ImageButton>(R.id.retourBouton)
        boutonRetour.setOnClickListener {
            dismiss()
        }

        val total = view.findViewById<Button>(R.id.total)

        total?.text = getString(R.string.total) + "  " + String.format("%.2f",somme)

        val boutonViderPanier = view.findViewById<Button>(R.id.boutonVider)
        val boutonCommander = view.findViewById<Button>(R.id.boutonCommander)

        boutonCommander.setOnClickListener {
            val sharedPref = requireContext().getSharedPreferences("donnees_utilisateur", MODE_PRIVATE)
            val idUtilisateur = sharedPref.getString("idUtilisateur", null)
            CoroutineScope(Dispatchers.IO).launch {
                val produits = panierDao.getTousLesProduits()
                withContext(Dispatchers.Main) {
                    when{
                        produits.isEmpty()-> Toast.makeText(requireContext(), getString(R.string.votre_panier_est_vide), Toast.LENGTH_SHORT).show()
                        idUtilisateur.isNullOrBlank() -> {
                            Toast.makeText(requireContext(), getString(R.string.ouvrir_compte), Toast.LENGTH_SHORT).show()
                            val intent = Intent(requireContext(), Connexion::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                            dismiss()
                        }
                        else-> {
                            Commander().show(parentFragmentManager, "Commander")
                            dismiss()
                        }
                    }
                }
            }
        }

        boutonViderPanier.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                panierDao.viderPanier()
                withContext(Dispatchers.Main) {
                    rafraichirVue()
                }
            }
        }


        CoroutineScope(Dispatchers.IO).launch {
            val produits = panierDao.getTousLesProduits()
            withContext(Dispatchers.Main) {
                if(produits.isEmpty()){
                    val viewVide = PanierVideBinding.inflate(inflater, container, false)
                    container.addView(viewVide.root)
                }
                else{
                    val fbd= Firebase.firestore

                    for (produitPanier in produits) {
                        fbd.collection("produits")
                            .whereEqualTo("id", produitPanier.idProduit)
                            .get()
                            .addOnSuccessListener { resultat ->
                                if (resultat.isEmpty){
                                    Toast.makeText(requireContext(), getString(R.string.aucunProduitErreur),Toast.LENGTH_SHORT).show()
                                }
                                else{
                                    val produit = resultat.documents.first()
                                    if (produit.exists()){
                                        val container: LinearLayout = requireView().findViewById(R.id.containerProduits)
                                        val p = produit.toObject(Produit::class.java) as Produit
                                        afficherProduits(p, container, produitPanier.qnt)

                                        somme = somme + ( p.prix * produitPanier.qnt )
                                        total?.text =
                                            getString(R.string.total) + "  " + String.format("%.2f",somme)
                                    }
                                    else{
                                        val itemBinding = PanierVideBinding.inflate(inflater, container, false)
                                        container.addView(itemBinding.root)
                                    }
                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(requireContext(), getString(R.string.aucunProduitErreur),Toast.LENGTH_SHORT).show()
                            }

                    }
                }
            }
        }

        return  view
    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }




    @SuppressLint("SetTextI18n", "UseGetLayoutInflater", "DetachAndAttachSameFragment",
        "DefaultLocale"
    )
    private fun afficherProduits(produit: Produit, container : LinearLayout, quantite : Int) {


        val inflater = LayoutInflater.from(requireContext())

        val itemBinding = ProduitDansPanierBinding.inflate(inflater, container, false)

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



        itemBinding.quantiteEditText.setText(quantite.toString())


        itemBinding.ajouterQntButton.setOnClickListener {

            var qnt = itemBinding.quantiteEditText.text.toString().toInt()
            qnt = qnt + 1
            itemBinding.quantiteEditText.setText(qnt.toString())

            db = BD.getDatabase(requireContext())
            panierDao = db.panierDao()

            CoroutineScope(Dispatchers.IO).launch {
                panierDao.mettreAJourQuantite(produit.id, qnt)
            }

            somme = somme + ( produit.prix)

            val total = view?.findViewById<Button>(R.id.total)
            total?.text = getString(R.string.total) + "  " + String.format("%.2f",somme)
        }

        itemBinding.diminuerQntButton.setOnClickListener {
            var qnt = itemBinding.quantiteEditText.text.toString().toInt()
            if (qnt>1){
                qnt = qnt - 1
                itemBinding.quantiteEditText.setText(qnt.toString())

                db = BD.getDatabase(requireContext())
                panierDao = db.panierDao()

                CoroutineScope(Dispatchers.IO).launch {
                    panierDao.mettreAJourQuantite(produit.id, qnt)
                }

                somme = somme - ( produit.prix)

                val total = view?.findViewById<Button>(R.id.total)
                total?.text = getString(R.string.total) + "  " + String.format("%.2f",somme)
            }
        }

        itemBinding.boutonSupprimer.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val existe = panierDao.getProduitParId(produit.id)
                if (existe != null) panierDao.supprimerProduit(existe)

                withContext(Dispatchers.Main) {
                    rafraichirVue()
                }
            }
        }


        container.addView(itemBinding.root)
    }






    @SuppressLint("DefaultLocale", "SetTextI18n")
    fun rafraichirVue() {
        val container: LinearLayout = requireView().findViewById(R.id.containerProduits)
        val total = requireView().findViewById<Button>(R.id.total)

        CoroutineScope(Dispatchers.IO).launch {
            val produits = panierDao.getTousLesProduits()

            withContext(Dispatchers.Main) {

                container.removeAllViews()
                somme = 0.0

                if (produits.isEmpty()) {
                    val viewVide = PanierVideBinding.inflate(layoutInflater, container, false)
                    container.addView(viewVide.root)
                    total.text = getString(R.string.total) + "  0.00"
                } else {
                    val fbd = Firebase.firestore
                    for (produitPanier in produits) {
                        fbd.collection("produits")
                            .whereEqualTo("id", produitPanier.idProduit)
                            .get()
                            .addOnSuccessListener { resultat ->
                                if (!resultat.isEmpty) {
                                    val produit = resultat.documents.first().toObject(Produit::class.java)
                                    if (produit != null) {
                                        afficherProduits(produit, container, produitPanier.qnt)
                                        somme += produit.prix * produitPanier.qnt
                                        total.text = getString(R.string.total) + "  " + String.format("%.2f", somme)
                                    }
                                }
                            }
                    }
                }
            }
        }
    }

}