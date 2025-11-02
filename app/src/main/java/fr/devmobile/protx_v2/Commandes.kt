package fr.devmobile.protx_v2


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Locale

@Entity(tableName = "Commandes")
data class Commande (
    @PrimaryKey var idClient: String = "",
    var nomClient: String ="",
    var prenomClient: String ="",
    var adresse: String ="",
    var tel: String ="",
    var panier: Map<String, String> = emptyMap(),
    var total: Double =0.0,
    var date: String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(java.util.Date())
)