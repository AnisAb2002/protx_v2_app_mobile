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
import fr.devmobile.protx_v2.databinding.ProduitCaseBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AssistanceProduits : DialogFragment() {

    @SuppressLint("UseGetLayoutInflater", "SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_assistance_produits, container, false)

        val retourBouton = view.findViewById<ImageButton>(R.id.retourBouton)
        retourBouton.setOnClickListener {
            dismiss()
        }

        val cas = arguments?.getString("cas")

        val descriptionText = view.findViewById<TextView>(R.id.textAssistanceDescription)

        val db = BD.getDatabase(requireContext())
        val produitDao = db.produitDao()
        val imc = arguments?.getString("imc")

        when(cas){
            "1"->{//moins de 18 ans
                descriptionText.setText(R.string.cas1)
            }
            "2"->{

                descriptionText.text = getString(R.string.SelonIMC) +" "+ imc + "\n"+getString(R.string.cas2)

                CoroutineScope(Dispatchers.IO).launch {
                    val produits = produitDao.getProduitsPrisePoids()

                    withContext(Dispatchers.Main) {
                        if (produits.isEmpty()) {
                            descriptionText.text = getString(R.string.aucunProduitTrouve)
                        } else {
                            afficherProduits(produits)
                        }
                    }
                }
            }
            "3"->{
                descriptionText.text = getString(R.string.SelonIMC) +" "+ imc + "\n"+getString(R.string.cas3)

                CoroutineScope(Dispatchers.IO).launch {
                    val produits = produitDao.getProduitsPertePoids()

                    withContext(Dispatchers.Main) {
                        if (produits.isEmpty()) {
                            descriptionText.text = getString(R.string.aucunProduitTrouve)
                        } else {
                            afficherProduits(produits)
                        }
                    }
                }
            }
            "4"->{
                descriptionText.text = getString(R.string.SelonIMC) +" "+ imc + "\n"+getString(R.string.cas4)

                CoroutineScope(Dispatchers.IO).launch {
                    val produits = produitDao.getProduitsPertePoidsLeger()

                    withContext(Dispatchers.Main) {
                        if (produits.isEmpty()) {
                            descriptionText.text = getString(R.string.aucunProduitTrouve)
                        } else {
                            afficherProduits(produits)
                        }
                    }
                }
            }
            "5"->{
                descriptionText.text = getString(R.string.SelonIMC) +" "+ imc + "\n"+getString(R.string.cas5)

                CoroutineScope(Dispatchers.IO).launch {
                    val produits = produitDao.getProduitsPrisePoids()

                    withContext(Dispatchers.Main) {
                        if (produits.isEmpty()) {
                            descriptionText.text = getString(R.string.aucunProduitTrouve)
                        } else {
                            afficherProduits(produits)
                        }
                    }
                }
            }
            "6"->{
                descriptionText.text = getString(R.string.SelonIMC) +" "+ imc + "\n"+getString(R.string.cas6)

                CoroutineScope(Dispatchers.IO).launch {
                    val produits = produitDao.getProduitsPerformance()

                    withContext(Dispatchers.Main) {
                        if (produits.isEmpty()) {
                            descriptionText.text = getString(R.string.aucunProduitTrouve)
                        } else {
                            afficherProduits(produits)
                        }
                    }
                }
            }
            "7"->{
                descriptionText.text = getString(R.string.SelonIMC) +" "+ imc + "\n"+getString(R.string.cas7)

                CoroutineScope(Dispatchers.IO).launch {
                    val produits = produitDao.getProduitsForme()

                    withContext(Dispatchers.Main) {
                        if (produits.isEmpty()) {
                            descriptionText.text = getString(R.string.aucunProduitTrouve)
                        } else {
                            afficherProduits(produits)
                        }
                    }
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
    private fun afficherProduits(produits: List<Produit>) {


        val container = requireView().findViewById<LinearLayout>(R.id.assistanceContainer)
        val inflater = LayoutInflater.from(requireContext())

        for (produit in produits) {

            val itemBinding = ProduitCaseBinding.inflate(inflater, container, false)

            itemBinding.nomProduit.text = produit.nom
            itemBinding.categorieProduit.text = produit.categorie
            itemBinding.poidsProduit.text = produit.poids
            itemBinding.prixProduit.text = "${produit.prix} €"
            itemBinding.imageProduit.setImageResource(produit.image_src)

            itemBinding.btnApercu.setOnClickListener {
                val fragment = ApercuProduit()

                val bundle = Bundle().apply {
                    putString("nom", produit.nom)
                    putString("categorie", produit.categorie)
                    putString("poids", produit.poids)
                    putDouble("prix", produit.prix)
                    putString("description", produit.description)
                    putInt("image_src", produit.image_src)
                    putString("composition",produit.composition)
                    putString("portion",produit.portion)
                }
                fragment.arguments = bundle
                fragment.show(parentFragmentManager, "ApercuProduit")
            }

            container?.addView(itemBinding.root)
        }

    }
}