package fr.devmobile.protx_v2.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Produit_dao {

    @Query("SELECT * FROM produits")
    suspend fun getProduits(): List<Produit>

    @Insert
    suspend fun insert(produit: Produit)

    @Query("DELETE FROM produits")
    suspend fun deleteAll()
}