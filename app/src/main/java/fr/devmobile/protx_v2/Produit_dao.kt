package fr.devmobile.protx_v2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Produit_dao {

    @Query("SELECT * FROM produits")
    suspend fun getProduits(): List<Produit>

    @Query("SELECT * FROM produits WHERE LOWER(nom) LIKE '%' || LOWER(:produitRecherche) || '%' OR LOWER(categorie) LIKE '%' || LOWER(:produitRecherche) || '%'")
    suspend fun rechercheProduits(produitRecherche : String): List<Produit>

    @Insert
    suspend fun insert(produit: Produit)

    @Query("DELETE FROM produits")
    suspend fun deleteAll()
}