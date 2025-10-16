package fr.devmobile.protx_v2

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import fr.devmobile.protx_v2.databinding.ActivityAssistanceBinding
import fr.devmobile.protx_v2.databinding.ActivityMainBinding

class Assistance : AppCompatActivity() {

    private lateinit var binding : ActivityAssistanceBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAssistanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.retourBouton.setOnClickListener {
            finish()
        }


    }
}