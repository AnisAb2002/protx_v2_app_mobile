package fr.devmobile.protx_v2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "produits")
data class Produit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nom: String,
    val poids : String,
    val categorie: String,
    val prix: Double,
    val description: String,
    val composition: String,
    val portion: String,
    val image_src: String
)