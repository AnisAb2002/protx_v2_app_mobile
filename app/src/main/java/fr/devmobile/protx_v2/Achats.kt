package fr.devmobile.protx_v2

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import fr.devmobile.protx_v2.databinding.CommandeLigneBinding

class Achats : DialogFragment() {

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val view =  inflater.inflate(R.layout.fragment_achats, container, false)


        val commandeContainer = view.findViewById<LinearLayout>(R.id.commandesContainer)
        val inflater = LayoutInflater.from(requireContext())

        val sharedPref = requireContext().getSharedPreferences("donnees_utilisateur", MODE_PRIVATE)
        val idUtilisateur = sharedPref.getString("idUtilisateur", null)

        val fbd = Firebase.firestore
        fbd.collection("commandes")
            .whereEqualTo("idClient", idUtilisateur)
            .get()
            .addOnSuccessListener { resultat ->
                if (resultat.isEmpty){
                    val aucuneCommandeText = view.findViewById<TextView>(R.id.aucuneCommendeText)
                    aucuneCommandeText.text = getString(R.string.aucunAchat)
                }
                else{
                    for (commande in resultat) {

                        val commandeLigne = CommandeLigneBinding.inflate(inflater, commandeContainer, false)
                        commandeContainer.addView(commandeLigne.root)

                        commandeLigne.boutonCommandeLigne.setOnClickListener {
                            val fragment = CommandeLigneInfo()
                            val bundle = Bundle().apply {
                                putString("idCommande", commande.id)
                            }
                            fragment.arguments = bundle
                            fragment.show(parentFragmentManager, "CommandeLigneInfo")
                        }


                        val commande = commande.toObject(Commande::class.java)
                        commandeLigne.boutonCommandeLigne.text = commande.date
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), R.string.aucunProduitErreur, Toast.LENGTH_SHORT).show()
                dismiss()
            }


        val boutonRetour = view.findViewById<ImageButton>(R.id.retourBouton)
        boutonRetour.setOnClickListener {
            dismiss()
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