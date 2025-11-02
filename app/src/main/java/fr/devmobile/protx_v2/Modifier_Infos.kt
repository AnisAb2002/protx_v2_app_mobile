package fr.devmobile.protx_v2


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class Modifier_Infos : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_modifier__infos, container, false)

        val prenomEditText = view.findViewById<EditText>(R.id.prenomEditText)
        val nomEditText = view.findViewById<EditText>(R.id.nomEditText)
        val adresseEditText = view.findViewById<EditText>(R.id.adresseEditText)
        val numEditText = view.findViewById<EditText>(R.id.numEditText)
        val ageEditText = view.findViewById<EditText>(R.id.ageEditText)
        val poidsEditText = view.findViewById<EditText>(R.id.poidsEditText)
        val tailleEditText = view.findViewById<EditText>(R.id.tailleEditText)
        val boutonRetour = view.findViewById<ImageButton>(R.id.retourBouton)
        val boutonConfirmer = view.findViewById<Button>(R.id.boutonConfirmer)

        val sharedPref = requireContext().getSharedPreferences("donnees_utilisateur", 0)
        val idUtilisateur = sharedPref.getString("idUtilisateur", null)



        if (idUtilisateur != null){
            val db = Firebase.firestore

            db.collection("utilisateurs")
                .document(idUtilisateur)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val utilisateur = document.toObject(Utilisateur::class.java)

                        prenomEditText.setText(utilisateur!!.prenom)
                        nomEditText.setText(utilisateur.nom)
                        adresseEditText.setText(utilisateur.adresse)
                        numEditText.setText(utilisateur.tel)
                        ageEditText.setText(utilisateur.age.toString())
                        poidsEditText.setText(utilisateur.poids.toString())
                        tailleEditText.setText(utilisateur.taille.toString())

                        boutonConfirmer.setOnClickListener {
                            val nvNom = nomEditText.text.toString()
                            val nvPrenom = prenomEditText.text.toString()
                            val nvAdresse = adresseEditText.text.toString()
                            val nvNum = numEditText.text.toString()
                            val nvAge = ageEditText.text.toString()
                            val nvTaille = tailleEditText.text.toString()
                            val nvPoids = poidsEditText.text.toString()

                            if (nvNom.isEmpty() || nvPrenom.isEmpty() || nvAdresse.isEmpty() || nvAge.isEmpty() ||
                                nvTaille.isEmpty() || nvPoids.isEmpty()){
                                Toast.makeText(requireContext(), getString(R.string.remplir), Toast.LENGTH_SHORT).show()
                            }
                            else if (!nvNum.matches(Regex("^\\+?[0-9]{10,15}$"))) {
                                Toast.makeText(requireContext(), getString(R.string.numero_invalide), Toast.LENGTH_SHORT).show()
                            }
                            else {
                                val nvUtilisateur = utilisateur.copy(
                                    nom = nvNom,
                                    prenom = nvPrenom,
                                    adresse = nvAdresse,
                                    tel = nvNum,
                                    age = nvAge.toInt(),
                                    taille = nvTaille.toFloat(),
                                    poids = nvPoids.toFloat()
                                )

                                //mettre à jour les infos
                                db.collection("utilisateurs")
                                    .document(idUtilisateur)
                                    .set(nvUtilisateur)
                                    .addOnSuccessListener {
                                        Toast.makeText(requireContext(), getString(R.string.misejour), Toast.LENGTH_SHORT).show()
                                        requireActivity().recreate()
                                        dismiss()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(requireContext(), "Erreur : ${e.message}", Toast.LENGTH_SHORT).show()
                                    }

                            }
                        }

                    }

                }
        }

        boutonRetour.setOnClickListener {
            dismiss()
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
}