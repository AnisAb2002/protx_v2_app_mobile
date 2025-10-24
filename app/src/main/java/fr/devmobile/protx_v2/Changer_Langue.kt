package fr.devmobile.protx_v2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import android.content.Context
import android.content.res.Configuration
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import java.util.*
import androidx.core.content.edit

class Changer_Langue : DialogFragment() {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_changer__langue, container, false)

        val radioGroupe = view.findViewById<RadioGroup>(R.id.groupeLangue)
        val radioFr = view.findViewById<RadioButton>(R.id.langFr)
        val radioEn = view.findViewById<RadioButton>(R.id.langEn)
        val boutonValider = view.findViewById<Button>(R.id.boutonValiderLangue)
        val boutonRetour = view.findViewById<ImageButton>(R.id.retourBouton)

        val prefs = requireContext().getSharedPreferences("donnees_utilisateur", Context.MODE_PRIVATE)
        val langue = prefs.getString("langue", "fr")

        if(langue == "fr"){
            radioFr.isChecked = true
        }
        else{
            radioEn.isChecked = true
        }

        boutonValider.setOnClickListener {
            val idSelect =radioGroupe.checkedRadioButtonId
            val langueSelectinone = view.findViewById<RadioButton>(idSelect)

            val langue = if (langueSelectinone.text.toString().lowercase().contains("english")) "en" else "fr"

            changerLangue(langue)
            requireActivity().recreate()
            dismiss()  //fermer le fragment

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



    private fun changerLangue(langue: String) {
        val locale = Locale(langue)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)

        requireContext().resources.updateConfiguration(config, requireContext().resources.displayMetrics)


        val prefs = requireContext().getSharedPreferences("donnees_utilisateur", Context.MODE_PRIVATE)
        prefs.edit { putString("langue", langue) }
    }


}