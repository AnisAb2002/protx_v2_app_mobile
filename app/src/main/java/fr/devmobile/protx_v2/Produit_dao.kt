package fr.devmobile.protx_v2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Produit_dao {

    @Query("SELECT * FROM produits")
    suspend fun getProduits(): List<Produit>

    @Query("SELECT * FROM produits WHERE categorie IN ('Gainer / Mass', 'Créatine / Force', 'Énergie')")
    suspend fun getProduitsPrisePoids(): List<Produit>

    @Query("SELECT * FROM produits WHERE categorie IN ('Créatine / Force', 'Protéine Whey') AND nom NOT IN ('Hydrotech', 'Isotech')")
    suspend fun getProduitsPertePoids(): List<Produit>

    @Query("SELECT * FROM produits WHERE categorie IN ('Créatine / Force', 'Protéine Whey', 'Énergie')")
    suspend fun getProduitsPertePoidsLeger(): List<Produit>

    @Query("SELECT * FROM produits WHERE categorie IN ('Créatine / Force', 'Énergie')")
    suspend fun getProduitsPerformance(): List<Produit>

    @Query("SELECT * FROM produits WHERE categorie IN ('Créatine / Force', 'Protéine Whey')")
    suspend fun getProduitsForme(): List<Produit>

    @Query("SELECT * FROM produits WHERE LOWER(nom) LIKE '%' || LOWER(:produitRecherche) || '%' OR LOWER(categorie) LIKE '%' || LOWER(:produitRecherche) || '%'")
    suspend fun rechercheProduits(produitRecherche : String): List<Produit>

    @Insert
    suspend fun insert(produit: Produit)

    @Query("DELETE FROM produits")
    suspend fun deleteAll()
}