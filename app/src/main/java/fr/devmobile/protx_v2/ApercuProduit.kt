package fr.devmobile.protx_v2

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class ApercuProduit : DialogFragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_apercu_produit, container, false)

        val boutonRetour = view.findViewById<ImageButton>(R.id.retourBouton)
        boutonRetour.setOnClickListener {
            dismiss()
        }

        // Récupérer les arguments de Accueil
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

        // Affecte les données
        nomProduit.text = nom
        categorieProduit.text = categorie
        poidsProduit.text = poids
        prixProduit.text = "$prix €"
        descriptionProduit.text = description
        imageViewProduit.setImageResource(imageSrc!!)
        portionPorduit.text = portion
        compositionProduit.text = composition




        val boutonPlus = view.findViewById<Button>(R.id.ajouterQntButton)
        val boutonMoins = view.findViewById<Button>(R.id.diminuerQntButton)
        val qntEditText = view.findViewById<EditText>(R.id.quantiteEditText)
        val ajouterPanierBoutton = view.findViewById<Button>(R.id.ajouterPanierButton)

        qntEditText.setText("0")

        boutonMoins.setOnClickListener {
            var qnt = qntEditText.text.toString().toInt()
            if (qnt>0){
                qnt = qnt - 1
                qntEditText.setText(qnt.toString())
            }
        }
        boutonPlus.setOnClickListener {
            var qnt = qntEditText.text.toString().toInt()
            qnt = qnt + 1
            qntEditText.setText(qnt.toString())
        }
        ajouterPanierBoutton.setOnClickListener {
            val qnt = qntEditText.text.toString().toInt()
            if (qnt==0){
                val message = getString(R.string.choixQnt)
                Toast.makeText(requireContext(),message,  Toast.LENGTH_SHORT).show()
            }
            else{
                //ajout au panier


                val message = getString(R.string.produitAjoutePanier)
                Toast.makeText(requireContext(),message,  Toast.LENGTH_SHORT).show()
            }
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