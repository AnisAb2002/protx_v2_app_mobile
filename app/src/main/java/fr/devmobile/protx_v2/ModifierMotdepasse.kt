package fr.devmobile.protx_v2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.security.MessageDigest

class ModifierMotdepasse : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_modifier_motdepasse, container, false)

        val ancienMdpEditText = view.findViewById<EditText>(R.id.ancienMdpEditText)
        val mdpEditText = view.findViewById<EditText>(R.id.mdpEditText)
        val mdpConfirmationEditText = view.findViewById<EditText>(R.id.mdpConfirmationEditText)
        val confirmerBoutton = view.findViewById<Button>(R.id.boutonConfirmer)
        val boutonRetour = view.findViewById<ImageButton>(R.id.retourBouton)

        val sharedPref = requireContext().getSharedPreferences("donnees_utilisateur", 0)
        val identifiant = sharedPref.getString("identifiant", null)

        val db = Firebase.firestore

        confirmerBoutton.setOnClickListener {
            val ancienMdp = ancienMdpEditText.text.toString()
            val nvMdp = mdpEditText.text.toString()
            val mdpConfirmation = mdpConfirmationEditText.text.toString()

            if (ancienMdp.isEmpty() ||nvMdp.isEmpty() || mdpConfirmation.isEmpty()){
                Toast.makeText(requireContext(), getString(R.string.remplir), Toast.LENGTH_SHORT).show()
            }
            else if(nvMdp != mdpConfirmation){
                Toast.makeText(requireContext(), getString(R.string.MdpConfirmation), Toast.LENGTH_SHORT).show()
            }
            else if (identifiant != null){

                db.collection("utilisateurs")
                    .whereEqualTo("identifiant", identifiant)
                    .whereEqualTo("motDePasse", hashMotDePasse(ancienMdp))
                    .get()
                    .addOnSuccessListener { result ->
                        if (result.isEmpty) {
                            Toast.makeText(requireContext(),getString(R.string.MdpIncorrect), Toast.LENGTH_SHORT).show()
                        }
                        else {
                            val utilisateur = result.documents.first().reference

                            utilisateur.update("motDePasse",hashMotDePasse(nvMdp))
                                .addOnSuccessListener {
                                    Toast.makeText(requireContext(), getString(R.string.miseajour_mot_de_passe), Toast.LENGTH_SHORT).show()
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


    fun hashMotDePasse(mdp: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(mdp.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}