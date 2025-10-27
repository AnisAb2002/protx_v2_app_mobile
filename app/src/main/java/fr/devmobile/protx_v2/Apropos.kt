package fr.devmobile.protx_v2

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

class Apropos : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle? ): View? {
        val view = inflater.inflate(R.layout.fragment_apropos, container, false)

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