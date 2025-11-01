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
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import fr.devmobile.protx_v2.databinding.ProduitCommandeLigneBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CommandeLigneInfo : DialogFragment() {

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_commande_ligne_info, container, false)

        val boutonRetour = view.findViewById<ImageButton>(R.id.retourBouton)
        boutonRetour.setOnClickListener { dismiss() }

        val idCommande = arguments?.getString("idCommande") ?: return view

        val fdb = Firebase.firestore
        val commandeContainer = view.findViewById<LinearLayout>(R.id.commandeContainer)

        // Utilisation de coroutines pour simplifier et éviter les callbacks imbriqués
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val docSnapshot = fdb.collection("commandes")
                    .document(idCommande)
                    .get()
                    .await()

                if (!docSnapshot.exists()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), getString(R.string.aucunProduitErreur), Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
                    return@launch
                }

                val commande = docSnapshot.toObject(Commande::class.java) ?: return@launch

                // Remplissage des infos principales
                view.findViewById<TextView>(R.id.titreText).text = commande.date
                view.findViewById<TextView>(R.id.prenomText).text = commande.prenomClient
                view.findViewById<TextView>(R.id.nomText).text = commande.nomClient
                view.findViewById<TextView>(R.id.adresseText).text = commande.adresse
                view.findViewById<TextView>(R.id.telText).text = commande.tel
                view.findViewById<TextView>(R.id.totalText).text = String.format("%.2f €", commande.total)

                // Chargement des produits
                for ((idProduit, qnt) in commande.panier) {
                    try {
                        val produitSnapshot = fdb.collection("produits")
                            .document(idProduit)
                            .get()
                            .await()

                        if (produitSnapshot.exists()) {
                            val produit = produitSnapshot.toObject(Produit::class.java)
                            val produitLigne = ProduitCommandeLigneBinding.inflate(inflater, commandeContainer, false)

                            produitLigne.nomProduitText.text = produit?.nom ?: "Inconnu"
                            produitLigne.qntProduitText.text = qnt

                            val sousTotal = qnt.toInt() * (produit?.prix ?: 0.0)
                            produitLigne.sousTotal.text = String.format("%.2f", sousTotal)

                            commandeContainer.addView(produitLigne.root)
                        }
                    } catch (e: Exception) {
                        if (isAdded) {
                            Toast.makeText(requireContext(), getString(R.string.aucunProduitErreur), Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            } catch (e: Exception) {
                if (isAdded) {
                    Toast.makeText(requireContext(), "Erreur : ${e.message}", Toast.LENGTH_SHORT).show()
                    dismiss()
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
}
