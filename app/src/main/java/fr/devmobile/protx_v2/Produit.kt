package fr.devmobile.protx_v2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "produits")
data class Produit(
    @PrimaryKey var id: String = "",
    var nom: String ="",
    var poids : String ="",
    var categorie: String ="",
    var prix: Double = 0.0,
    var desc_fr: String ="",
    var desc_en: String ="",
    var compo_en: String ="",
    var compo_fr: String ="",
    var portion: String ="",
    var image_src: Int = 0,
)