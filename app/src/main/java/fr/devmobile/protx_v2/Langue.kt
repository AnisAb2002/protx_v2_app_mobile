package fr.devmobile.protx_v2

import android.content.Context
import android.content.res.Configuration
import androidx.core.content.edit
import java.util.Locale

interface Langue {
    fun setLocale(context: Context, langue: String){
        val locale = Locale(langue)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        val prefs = context.getSharedPreferences("donnees_utilisateur", Context.MODE_PRIVATE)
        prefs.edit { putString("langue", langue) }
    }
}