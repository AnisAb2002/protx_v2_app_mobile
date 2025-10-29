package fr.devmobile.protx_v2

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import fr.devmobile.protx_v2.databinding.ProduitCaseBinding

class AssistanceProduits : DialogFragment() {

    @SuppressLint("UseGetLayoutInflater", "SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_assistance_produits, container, false)

        val retourBouton = view.findViewById<ImageButton>(R.id.retourBouton)
        retourBouton.setOnClickListener {
            dismiss()
        }

        val cas = arguments?.getString("cas")
        val langue = arguments?.getString("langue","fr")
        val imc = arguments?.getString("imc")

        val descriptionText = view.findViewById<TextView>(R.id.textAssistanceDescription)

        val db = Firebase.firestore

        when(cas){
            "1"->{//moins de 18 ans
                descriptionText.setText(R.string.cas1)
            }
            "2"->{
                descriptionText.text = getString(R.string.SelonIMC) +" "+ imc + "\n"+getString(R.string.cas2)

                db.collection("produits").get()
                .addOnSuccessListener {
                    produits ->
                    if (produits.isEmpty){
                        descriptionText.text = getString(R.string.aucunProduitErreur)
                    }
                    for (produit in produits) {
                        val prod = produit.toObject(Produit::class.java)
                        if (prod.categorie in listOf("Energy", "Gainer / Mass", "Creatine / Force")) {
                            afficherProduits(prod, langue.toString())
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    descriptionText.text = getString(R.string.aucunProduitErreur)
                }
            }
            "3"->{
                descriptionText.text = getString(R.string.SelonIMC) +" "+ imc + "\n"+getString(R.string.cas3)

                db.collection("produits").get()
                .addOnSuccessListener {
                    produits ->
                    if (produits.isEmpty){
                        descriptionText.text = getString(R.string.aucunProduitErreur)
                    }
                    for (produit in produits) {
                        val prod = produit.toObject(Produit::class.java)
                        if ((prod.categorie in listOf("Proteine Whey", "Creatine / Force")) and (prod.nom != "Casein")) {
                            afficherProduits(prod, langue.toString())
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    descriptionText.text = getString(R.string.aucunProduitErreur)
                }
            }
            "4"->{
                descriptionText.text = getString(R.string.SelonIMC) +" "+ imc + "\n"+getString(R.string.cas4)

                db.collection("produits").get()
                .addOnSuccessListener {
                    produits ->
                    if (produits.isEmpty){
                        descriptionText.text = getString(R.string.aucunProduitErreur)
                    }
                    for (produit in produits) {
                        val prod = produit.toObject(Produit::class.java)

                        if (prod.categorie in listOf("Creatine / Force", "Proteine Whey", "Energy")) {
                            afficherProduits(prod, langue.toString())
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    descriptionText.text = getString(R.string.aucunProduitErreur)
                }
            }
            "5"->{
                descriptionText.text = getString(R.string.SelonIMC) +" "+ imc + "\n"+getString(R.string.cas5)

                db.collection("produits").get()
                .addOnSuccessListener {
                    produits ->
                    if (produits.isEmpty){
                        descriptionText.text = getString(R.string.aucunProduitErreur)
                    }
                    for (produit in produits) {
                        val prod = produit.toObject(Produit::class.java)
                        if (prod.categorie in listOf("Energy", "Gainer / Mass", "Creatine / Force")) {
                            afficherProduits(prod, langue.toString())
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    descriptionText.text = getString(R.string.aucunProduitErreur)
                }
            }
            "6"->{
                descriptionText.text = getString(R.string.SelonIMC) +" "+ imc + "\n"+getString(R.string.cas6)

                db.collection("produits").get()
                .addOnSuccessListener {
                    produits ->
                    if (produits.isEmpty){
                        descriptionText.text = getString(R.string.aucunProduitErreur)
                    }
                    for (produit in produits) {
                        val prod = produit.toObject(Produit::class.java)
                        if (prod.categorie in listOf("Energy", "Creatine / Force")) {
                            afficherProduits(prod, langue.toString())
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    descriptionText.text = getString(R.string.aucunProduitErreur)
                }
            }
            "7"->{
                descriptionText.text = getString(R.string.SelonIMC) +" "+ imc + "\n"+getString(R.string.cas7)

                db.collection("produits").get()
                .addOnSuccessListener {
                    produits ->
                    if (produits.isEmpty){
                        descriptionText.text = getString(R.string.aucunProduitErreur)
                    }
                    for (produit in produits) {
                        val prod = produit.toObject(Produit::class.java)
                        if (prod.categorie in listOf("Proteine Whey", "Creatine / Force")) {
                            afficherProduits(prod, langue.toString())
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    descriptionText.text = getString(R.string.aucunProduitErreur)
                }
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

    @SuppressLint("SetTextI18n", "UseGetLayoutInflater")
    private fun afficherProduits(produit: Produit, langue: String) {


        val container = requireView().findViewById<LinearLayout>(R.id.assistanceContainer)
        val inflater = LayoutInflater.from(requireContext())


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
            fragment.show(parentFragmentManager, "ApercuProduit")
        }
        container.addView(itemBinding.root)
    }
}