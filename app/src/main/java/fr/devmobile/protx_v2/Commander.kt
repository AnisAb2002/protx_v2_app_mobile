package fr.devmobile.protx_v2

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class Commander : DialogFragment() {

    var total = 0.0

    @SuppressLint("DefaultLocale")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_commander, container, false)

        val boutonRetour = view.findViewById<ImageButton>(R.id.retourBouton)
        boutonRetour.setOnClickListener {
            dismiss()
        }


        val boutonAnnuler = view.findViewById<Button>(R.id.boutonAnnuler)
        boutonAnnuler.setOnClickListener {
            dismiss()
        }


        val nomEditText = view.findViewById<EditText>(R.id.nomEditText)
        val prenomEditText = view.findViewById<EditText>(R.id.prenomEditText)
        val adresseEditText = view.findViewById<EditText>(R.id.adresseEditText)
        val phoneEditText = view.findViewById<EditText>(R.id.phoneEditText)
        val boutonConfirmer = view.findViewById<Button>(R.id.boutonConfirmer)



        val sharedPref = requireContext().getSharedPreferences("donnees_utilisateur", MODE_PRIVATE)
        val idUtilisateur = sharedPref.getString("idUtilisateur", null)
        if (idUtilisateur != null) {
            //Conecté
            //remplissage des EditText avec les informations qu'on a déja sur l'utilisateur
            val db = Firebase.firestore
            db.collection("utilisateurs")
                .document(idUtilisateur)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val utilisateur = document.toObject(Utilisateur::class.java)

                        nomEditText.setText(utilisateur!!.nom)
                        prenomEditText.setText(utilisateur.prenom)
                        adresseEditText.setText(utilisateur.adresse)
                        phoneEditText.setText(utilisateur.tel)
                    }
                }

        }

        boutonConfirmer.setOnClickListener {
            val nom = nomEditText.text.toString().trim()
            val prenom = prenomEditText.text.toString().trim()
            val adresse = adresseEditText.text.toString().trim()
            val numero = phoneEditText.text.toString().trim()

            if (nom.isEmpty() || prenom.isEmpty() || adresse.isEmpty()){
                Toast.makeText(requireContext(), getString(R.string.remplir), Toast.LENGTH_SHORT).show()
            }
            else if (!numero.matches(Regex("^\\+?[0-9]{10,15}$"))) {
                Toast.makeText(requireContext(), getString(R.string.numero_invalide), Toast.LENGTH_SHORT).show()
            }
            else{
                val fragment = Paiement()
                val bundle = Bundle().apply {
                    putString("idClient", idUtilisateur)
                    putString("nom", nom)
                    putString("prenom", prenom)
                    putString("adresse", adresse)
                    putString("numero", numero)
                }
                fragment.arguments = bundle
                fragment.show(parentFragmentManager, "Paiement")
                dismiss()
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