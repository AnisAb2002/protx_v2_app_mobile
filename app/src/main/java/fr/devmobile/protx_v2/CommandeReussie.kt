package fr.devmobile.protx_v2

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton

class CommandeReussie : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle? ): View? {
        val view = inflater.inflate(R.layout.fragment_commande_reussie, container, false)

        val buttonFermer = view.findViewById<Button>(R.id.boutonFermer)
        buttonFermer.setOnClickListener {
            dismiss()
        }

        val buttonRetour = view.findViewById< ImageButton>(R.id.retourBouton)
        buttonRetour.setOnClickListener {
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