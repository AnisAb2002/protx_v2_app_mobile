package fr.devmobile.protx_v2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "panier")
data class PanierEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val idProduit: String,
    val nomProduit: String,
    val prix: Double,
    val qnt: Int
)
