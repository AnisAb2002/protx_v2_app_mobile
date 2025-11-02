package fr.devmobile.protx_v2

import androidx.room.*

@Dao
interface PanierEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun ajouterProduit(panierEntity: PanierEntity)

    @Query("SELECT * FROM panier")
    suspend fun getTousLesProduits(): List<PanierEntity>

    @Query("SELECT * FROM panier WHERE idProduit = :idProduit LIMIT 1")
    suspend fun getProduitParId(idProduit: String): PanierEntity?

    @Query("DELETE FROM panier")
    suspend fun viderPanier()

    @Query("UPDATE panier SET qnt = :nouvelleQuantite WHERE idProduit = :idProduit")
    suspend fun mettreAJourQuantite(idProduit: String, nouvelleQuantite: Int)

    @Delete
    suspend fun supprimerProduit(panierEntity: PanierEntity)
}
