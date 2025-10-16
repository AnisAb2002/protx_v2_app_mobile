package fr.devmobile.protx_v2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fr.devmobile.protx_v2.databinding.ActivityAccueilBinding

class Accueil : AppCompatActivity() {
    private lateinit var binding : ActivityAccueilBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAccueilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.assistanceBtn.setOnClickListener {
            val intent = Intent(this, Assistance::class.java)
            startActivity(intent)
        }

    }
}