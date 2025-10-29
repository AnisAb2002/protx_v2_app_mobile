package fr.devmobile.protx_v2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "utilisateurs")

data class Utilisateur(
    @PrimaryKey var id: String = "",
    var nom: String ="",
    var prenom: String ="",
    var identifiant: String ="",
    var motDePasse: String = "",
    var age: Int =0,
    var taille: Float =0f,
    var poids: Float = 0f
)