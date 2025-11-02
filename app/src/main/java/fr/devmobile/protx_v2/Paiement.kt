package fr.devmobile.protx_v2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import fr.devmobile.protx_v2.databinding.ProduitCommandeLigneBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar


class Paiement : DialogFragment() {

    var total = 0.0

    @SuppressLint("DefaultLocale", "SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_paiement, container, false)

        val boutonRetour = view.findViewById<ImageButton>(R.id.retourBouton)
        boutonRetour.setOnClickListener {
            dismiss()
        }

        val boutonAnnuler = view.findViewById<Button>(R.id.boutonAnnuler)
        boutonAnnuler.setOnClickListener {
            dismiss()
        }

        val totalText = view.findViewById<TextView>(R.id.totalText)



        val db = BD.getDatabase(requireContext())
        val panierDao = db.panierDao()
        var produits :  List<PanierEntity>
        var listeProduitsDict = mutableMapOf<String, String>()

        CoroutineScope(Dispatchers.IO).launch {
            produits = panierDao.getTousLesProduits()
            withContext(Dispatchers.Main) {
                if(produits.isEmpty()){
                    Toast.makeText(requireContext(), getString(R.string.aucunProduitErreur), Toast.LENGTH_SHORT).show()
                    dismiss()
                }
                else{
                    for (produitPanier in produits) {
                        val container: LinearLayout = requireView().findViewById(R.id.commandeContainer)
                        val produitLigne = ProduitCommandeLigneBinding.inflate(inflater, container, false)

                        produitLigne.nomProduitText.text = produitPanier.nomProduit
                        produitLigne.qntProduitText.text = produitPanier.qnt.toString()

                        val sousTotal = produitPanier.qnt * produitPanier.prix
                        produitLigne.sousTotal.text = String.format("%.2f",sousTotal)

                        total = total + sousTotal
                        totalText.text = String.format("%.2f",total) + " €"



                        listeProduitsDict[produitPanier.idProduit] = produitPanier.qnt.toString()

                        container.addView(produitLigne.root)
                    }
                }
            }
        }



        val cardnumberEditText = view.findViewById<EditText>(R.id.cardnumberEditText)
        val dateEditText = view.findViewById<EditText>(R.id.dateEditText)
        val cvcEditText = view.findViewById<EditText>(R.id.cvcEditText)
        val nomEditText = view.findViewById<EditText>(R.id.nomEditText)


        val boutonConfirmer = view.findViewById<Button>(R.id.boutonConfirmer)
        boutonConfirmer.setOnClickListener {

            val numeroCarte = cardnumberEditText.text.toString().trim()
            val date = dateEditText.text.toString().trim()
            val cvc = cvcEditText.text.toString().trim()
            val nomCarte = nomEditText.text.toString().trim()

            when {
                numeroCarte.length < 16 -> Toast.makeText(requireContext(),
                    getString(R.string.carte_invalide), Toast.LENGTH_SHORT).show()

                cvc.length < 3 -> Toast.makeText(requireContext(),
                getString(R.string.cvc_invalide), Toast.LENGTH_SHORT).show()

                date.isEmpty() || nomCarte.isEmpty() -> Toast.makeText(requireContext(),
                    getString(R.string.remplir), Toast.LENGTH_SHORT).show()

                dateValide(date) -> Toast.makeText(requireContext(),
                    getString(R.string.erreurDate), Toast.LENGTH_SHORT).show()

                else ->{
                    val idClient = arguments?.getString("idClient").toString()
                    val nom = arguments?.getString("nom").toString()
                    val prenom = arguments?.getString("prenom").toString()
                    val adresse = arguments?.getString("adresse").toString()
                    val numeroTel = arguments?.getString("numero").toString()

                    val commande = Commande(
                        idClient = idClient,
                        nomClient = nom,
                        prenomClient = prenom,
                        adresse = adresse,
                        tel = numeroTel,
                        panier = listeProduitsDict,
                        total = total
                    )

                    val fdb = Firebase.firestore
                    fdb.collection("commandes")
                        .add(commande)
                        .addOnSuccessListener {
                            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                                panierDao.viderPanier()

                                withContext(Dispatchers.Main) {
                                    CommandeReussie().show(parentFragmentManager, "CommandeReussie")
                                    dismiss()
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(requireContext(), "Erreur : ${e.message}", Toast.LENGTH_LONG).show()
                        }
                }
            }
        }




        return view
    }

    fun dateValide(date: String): Boolean {

        if (!date.matches(Regex("^(0[1-9]|1[0-2])/[0-9]{2}$"))) {
            return true // format invalide
        }
        val (mois, annee) = date.split("/").map { it.toInt() }

        val cetteAnnee = Calendar.getInstance().get(Calendar.YEAR) % 100
        val ceMois = Calendar.getInstance().get(Calendar.MONTH)+1

        return (       (cetteAnnee > annee)        ||        ( (annee == cetteAnnee) && (mois < ceMois) )   )
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