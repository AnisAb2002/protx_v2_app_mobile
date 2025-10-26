package fr.devmobile.protx_v2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface Utilisateur_dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserer(utilisateur: Utilisateur)

    @Query("SELECT * FROM utilisateurs WHERE identifiant = :identifiant AND motDePasse = :motDePasse LIMIT 1")
    suspend fun authentifier(identifiant: String, motDePasse: String): Utilisateur?

    @Query("SELECT * FROM utilisateurs WHERE idUtilisateur = :id LIMIT 1")
    suspend fun getConnecter(id: Int): Utilisateur?

    @Update
    suspend fun update(utilisateur: Utilisateur)

    @Query("DELETE FROM utilisateurs")
    suspend fun supprimerTous()
}