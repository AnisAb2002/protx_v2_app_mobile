package fr.devmobile.protx_v2

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.edit
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class SupprimerProfil : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_supprimer_profil, container, false)

        val mdpEditText = view.findViewById<EditText>(R.id.mdpEditText)

        val boutonRetour = view.findViewById<ImageButton>(R.id.retourBouton)
        boutonRetour.setOnClickListener {
            dismiss()
        }
        val boutonAnnuler = view.findViewById<Button>(R.id.boutonAnnuler)
        boutonAnnuler.setOnClickListener {
            dismiss()
        }

        val boutonConfirmer = view.findViewById<Button>(R.id.boutonConfirmer)
        boutonConfirmer.setOnClickListener {
            val mdp = mdpEditText.text.toString()

            if (mdp.isEmpty()){
                Toast.makeText(requireContext(), getString(R.string.remplir), Toast.LENGTH_SHORT).show()
            }
            else{

                val sharedPrefs = requireContext().getSharedPreferences("donnees_utilisateur", MODE_PRIVATE)
                val idUtilisateur = sharedPrefs.getString("idUtilisateur", null)

                if (idUtilisateur == null) {
                    Toast.makeText(requireContext(), R.string.introuvable, Toast.LENGTH_SHORT).show()
                    dismiss()
                }

                val db = Firebase.firestore

                db.collection("utilisateurs")
                    .document(idUtilisateur.toString())
                    .delete()
                    .addOnSuccessListener {

                        sharedPrefs.edit {
                            clear()
                            apply()
                        }

                        val intent = Intent(requireContext(), Connexion::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)

                        dismiss()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(requireContext(), R.string.mdpIncorrect, Toast.LENGTH_SHORT).show()
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