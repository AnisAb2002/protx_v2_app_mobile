package fr.devmobile.protx_v2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "produits")
data class Produit(
    @PrimaryKey val id: Int,
    val nom: String,
    val poids : String,
    val categorie: String,
    val prix: Double,
    val description: String,
    val composition: String,
    val portion: String,
    val image_src: Int,
)