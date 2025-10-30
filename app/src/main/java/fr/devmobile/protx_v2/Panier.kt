package fr.devmobile.protx_v2

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
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
import androidx.core.content.edit
import kotlin.text.toInt

class Panier : DialogFragment() {
    var somme = 0.0

    @SuppressLint("UseGetLayoutInflater", "DetachAndAttachSameFragment", "SetTextI18n",
        "DefaultLocale"
    )
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_panier, container, false)

        setStyle(STYLE_NORMAL, 0)

        val container: LinearLayout = view.findViewById(R.id.containerProduits)
        val inflater = LayoutInflater.from(requireContext())


        val sharedPref = requireContext().getSharedPreferences("donnees_utilisateur", MODE_PRIVATE)
        val panierSet = sharedPref.getStringSet("panier", emptySet())


        val boutonRetour = view.findViewById<ImageButton>(R.id.retourBouton)
        boutonRetour.setOnClickListener {
            dismiss()
        }

        val total = view.findViewById<Button>(R.id.total)

        total?.text = getString(R.string.total) + "  " + String.format("%.2f",somme)

        val boutonViderPanier = view.findViewById<Button>(R.id.boutonVider)
        val boutonCommander = view.findViewById<Button>(R.id.boutonCommander)

        boutonCommander.setOnClickListener {
            Toast.makeText(requireContext(), getString(R.string.passer_commande),Toast.LENGTH_SHORT).show()
        }

        boutonViderPanier.setOnClickListener {
            sharedPref.edit { remove("panier") }

            val nouveauPanier = Panier()
            nouveauPanier.show(parentFragmentManager, "Panier")
            dismiss()
        }






        if (panierSet.isNullOrEmpty()) {
            // Panier vide
            val viewVide = PanierVideBinding.inflate(inflater, container, false)
            container.addView(viewVide.root)
        }
        else {
            val ids = panierSet.map { it }

            val db = Firebase.firestore

            for (idProduit in ids) {

                db.collection("produits")
                    .whereEqualTo("id", idProduit)
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
                                afficherProduits(p, container)

                                somme = somme + ( p.prix )
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

        return  view
    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.window?.setWindowAnimations(0)
    }




    @SuppressLint("SetTextI18n", "UseGetLayoutInflater", "DetachAndAttachSameFragment",
        "DefaultLocale"
    )
    private fun afficherProduits(produit: Produit, container : LinearLayout) {


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



        itemBinding.quantiteEditText.setText("1")


        itemBinding.ajouterQntButton.setOnClickListener {
            var qnt = itemBinding.quantiteEditText.text.toString().toInt()
            qnt = qnt + 1
            itemBinding.quantiteEditText.setText(qnt.toString())
            somme = somme + ( produit.prix)

            val total = view?.findViewById<Button>(R.id.total)
            total?.text = getString(R.string.total) + "  " + String.format("%.2f",somme)
        }

        itemBinding.diminuerQntButton.setOnClickListener {
            var qnt = itemBinding.quantiteEditText.text.toString().toInt()
            if (qnt>1){
                qnt = qnt - 1
                itemBinding.quantiteEditText.setText(qnt.toString())

                somme = somme - ( produit.prix)

                val total = view?.findViewById<Button>(R.id.total)
                total?.text = getString(R.string.total) + "  " + String.format("%.2f",somme)
            }
        }

        itemBinding.boutonSupprimer.setOnClickListener {
            val sharedPref = requireContext().getSharedPreferences("donnees_utilisateur", MODE_PRIVATE)
            val panierSet = sharedPref.getStringSet("panier", mutableSetOf())?.toMutableSet() ?: mutableSetOf()

            panierSet.remove(produit.id)
            sharedPref.edit { putStringSet("panier", panierSet) }

            val nouveauPanier = Panier()
            nouveauPanier.show(parentFragmentManager, "Panier")
            dismiss()
        }


        container.addView(itemBinding.root)
    }
}