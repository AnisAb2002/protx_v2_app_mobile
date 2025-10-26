package fr.devmobile.protx_v2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "utilisateurs")

data class Utilisateur(
    @PrimaryKey(autoGenerate = true) val idUtilisateur: Int = 0,
    val nom: String,
    val prenom: String,
    val identifiant: String,
    val motDePasse: String,
    val age: Int,
    val taille: Float,
    val poids: Float
)