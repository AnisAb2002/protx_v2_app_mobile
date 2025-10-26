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
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


class Modifier_Infos : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_modifier__infos, container, false)

        val prenomEditText = view.findViewById<EditText>(R.id.prenomEditText)
        val nomEditText = view.findViewById<EditText>(R.id.nomEditText)
        val ageEditText = view.findViewById<EditText>(R.id.ageEditText)
        val poidsEditText = view.findViewById<EditText>(R.id.poidsEditText)
        val tailleEditText = view.findViewById<EditText>(R.id.tailleEditText)
        val boutonRetour = view.findViewById<ImageButton>(R.id.retourBouton)
        val boutonConfirmer = view.findViewById<Button>(R.id.boutonConfirmer)

        val sharedPref = requireContext().getSharedPreferences("donnees_utilisateur", 0)
        val idUtilisateur = sharedPref.getInt("donnees_utilisateur", -1)

        val utilisateurDao = BD.getDatabase(requireContext()).utilisateurDao()

        if (idUtilisateur != -1){
            viewLifecycleOwner.lifecycleScope.launch {
                val utilisateur = utilisateurDao.getConnecter(idUtilisateur)
                if (utilisateur != null) {
                    prenomEditText.setText(utilisateur.prenom)
                    nomEditText.setText(utilisateur.nom)
                    ageEditText.setText(utilisateur.age.toString())
                    poidsEditText.setText(utilisateur.poids.toString())
                    tailleEditText.setText(utilisateur.taille.toString())


                    boutonConfirmer.setOnClickListener {
                        val nvNom = nomEditText.text.toString()
                        val nvPrenom = prenomEditText.text.toString()
                        val nvAge = ageEditText.text.toString()
                        val nvTaille = tailleEditText.text.toString()
                        val nvPoids = poidsEditText.text.toString()

                        if (nvNom.isEmpty() || nvPrenom.isEmpty() || nvAge.isEmpty() || nvTaille.isEmpty() || nvPoids.isEmpty()){
                            val message = getString(R.string.remplir)
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        }
                        else {
                            viewLifecycleOwner.lifecycleScope.launch {
                                val nvUtilisateur = utilisateur.copy(
                                    nom = nvNom,
                                    prenom = nvPrenom,
                                    age = nvAge.toInt(),
                                    taille = nvTaille.toFloat(),
                                    poids = nvPoids.toFloat()
                                )

                                utilisateurDao.update(nvUtilisateur)
                            }

                            val message = getString(R.string.misejour)
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                            requireActivity().recreate()
                            dismiss()
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