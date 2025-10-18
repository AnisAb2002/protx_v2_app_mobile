package fr.devmobile.protx_v2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import fr.devmobile.protx_v2.databinding.ActivityParametresBinding

class Parametres : AppCompatActivity() {

    private lateinit var binding: ActivityParametresBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityParametresBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}