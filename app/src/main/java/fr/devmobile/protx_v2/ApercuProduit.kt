package fr.devmobile.protx_v2

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.content.Context.MODE_PRIVATE
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.DialogFragment

class ApercuProduit : DialogFragment() {

    @SuppressLint("SetTextI18n", "MutatingSharedPrefs")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_apercu_produit, container, false)

        val boutonRetour = view.findViewById<ImageButton>(R.id.retourBouton)
        boutonRetour.setOnClickListener {
            dismiss()
        }

        // Récupérer les arguments
        val idProduit = arguments?.getString("idProduit")
        val nom = arguments?.getString("nom")
        val categorie = arguments?.getString("categorie")
        val poids = arguments?.getString("poids")
        val prix = arguments?.getDouble("prix")
        val description = arguments?.getString("description")
        val imageSrc = arguments?.getInt("image_src")
        val portion = arguments?.getString("portion")
        val composition = arguments?.getString("composition")

        val imageViewProduit = view.findViewById<ImageView>(R.id.imageProduitApercu)
        val nomProduit = view.findViewById<TextView>(R.id.nomProduitApercu)
        val categorieProduit = view.findViewById<TextView>(R.id.categorieProduitApercu)
        val poidsProduit = view.findViewById<TextView>(R.id.poidsProduitApercu)
        val prixProduit = view.findViewById<TextView>(R.id.prixProduitApercu)
        val descriptionProduit = view.findViewById<TextView>(R.id.descriptionProduitApercu)
        val portionPorduit = view.findViewById<TextView>(R.id.portionProduitApercu)
        val compositionProduit = view.findViewById<TextView>(R.id.compositionProduitApercu)

        // Affecter les données
        nomProduit.text = nom
        categorieProduit.text = categorie
        poidsProduit.text = poids
        prixProduit.text = "$prix €"
        descriptionProduit.text = description
        imageViewProduit.setImageResource(imageSrc!!)
        portionPorduit.text = portion
        compositionProduit.text = composition



        val ajouterPanierBoutton = view.findViewById<Button>(R.id.ajouterPanierButton)



        ajouterPanierBoutton.setOnClickListener {
            val sharedPref = requireContext().getSharedPreferences("donnees_utilisateur", MODE_PRIVATE)
            val panierSet = sharedPref.getStringSet("panier", mutableSetOf()) ?: mutableSetOf()

            panierSet.add(idProduit)

            sharedPref.edit { putStringSet("panier", panierSet) }

            Toast.makeText(requireContext(),getString(R.string.produitAjoutePanier),  Toast.LENGTH_SHORT).show()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}