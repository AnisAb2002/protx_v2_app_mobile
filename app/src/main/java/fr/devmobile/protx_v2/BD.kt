package fr.devmobile.protx_v2.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import fr.devmobile.protx_v2.R
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Produit::class], version = 3)
abstract class BD : RoomDatabase() {
    abstract fun produitDao(): Produit_dao

    companion object {
        @Volatile private var INSTANCE: BD? = null

        fun getDatabase(context: Context): BD {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BD::class.java,
                    "base_donnees_protx"
                )
                .addCallback(DatabaseCallback(context))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
private class DatabaseCallback(
    private val context: Context
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        // On exécute le pré-remplissage dans un thread séparé
        CoroutineScope(Dispatchers.IO).launch {
            prepopulateDatabase(context)
        }
    }

    private suspend fun prepopulateDatabase(context: Context) {
        val produitDao = BD.getDatabase(context).produitDao()

        val produits = listOf(

            //catégorie Gainer / Mass
            Produit(
                id = 1,
                nom = "Solid Mass",
                poids= "1500g",
                categorie = "Gainer / Mass",
                prix = 20.00,
                description = "Solid Mass est un gainer puissant favorisant une prise de masse rapide, riche en glucides et protéines de qualité.",
                composition = "farine d'avoine, fructose, vitamines, créatine monohydrate, concentré de proteine, arômes.",
                portion = "100g",
                image_src = R.drawable.solidmass1500g
            ),
            Produit(
                id = 2,
                nom = "Solid Mass",
                poids= "5000g",
                categorie = "Gainer / Mass",
                prix = 49.99,
                description = "Solid Mass est un gainer puissant favorisant une prise de masse rapide, riche en glucides et protéines de qualité.",
                composition = "farine d'avoine, fructose, vitamines, créatine monohydrate, concentré de proteine, arômes.",
                portion = "100g",
                image_src = R.drawable.solidmass5000g
            ),
            Produit(
                id = 3,
                nom = "Ultra Gainer",
                poids= "1500g",
                categorie = "Gainer / Mass",
                prix = 25.00,
                description = "Ultra Gainer est un complément hypercalorique favorisant la prise de masse, riche en glucides et en protéines.",
                composition = "maltodextrine, fructose, vitamines, créatine monohydrate, isolat de proteine, poudre de cacao.",
                portion = "100g",
                image_src = R.drawable.ultragainer1500g
            ),
            Produit(
                id = 4,
                nom = "Ultra Gainer",
                poids= "5000g",
                categorie = "Gainer / Mass",
                prix = 55.00,
                description = "Ultra Gainer est un complément hypercalorique favorisant la prise de masse, riche en glucides et en protéines.",
                composition = "maltodextrine, fructose, vitamines, créatine monohydrate, isolat de proteine, poudre de cacao.",
                portion = "100g",
                image_src = R.drawable.ultragainer5000g
            ),

            //catégorie Proteines Whey
            Produit(
                id = 5,
                nom = "Casein",
                poids= "1000g",
                categorie = "Protéine Whey",
                prix = 25.00,
                description = "La caséine est idéale pour nourrir les muscles pendant la nuit et favoriser la récupération musculaire prolongée.",
                composition = "caséine micellaire, poudre de cacao, édulcorant : sucralose (SIN955).",
                portion = "30g",
                image_src = R.drawable.casein1000g
            ),
            Produit(
                id = 6,
                nom = "Casein",
                poids= "2000g",
                categorie = "Protéine Whey",
                prix = 45.00,
                description = "La caséine est idéale pour nourrir les muscles pendant la nuit et favoriser la récupération musculaire prolongée.",
                composition = "caséine micellaire, poudre de cacao, édulcorant : sucralose (SIN955).",
                portion = "30g",
                image_src = R.drawable.casein2000g
            ),
            Produit(
                id = 7,
                nom = "Hydrotech",
                poids= "1000g",
                categorie = "Protéine Whey",
                prix = 25.00,
                description = "Hydrotech est une protéine de haute qualité, riche en hydrolysats, aide à la récupération musculaire optimale.",
                composition = "hydrolysat de protéine, L-glutamine, L-arginine HCl, arômes, concentré de jus de betterave.",
                portion = "30g",
                image_src = R.drawable.hydrotech1000g
            ),
            Produit(
                id = 8,
                nom = "Isotech",
                poids= "4000g",
                categorie = "Protéine Whey",
                prix = 95.00,
                description = "Isotech est une protéine de lactosérum pure et rapidement assimilée, idéale pour soutenir la récupération musculaire.",
                composition = "isolat de protéine, poudre de cacao, acide citrique (SIN330), concentré de jus de betterave, vitamine D.",
                portion = "25g",
                image_src = R.drawable.isotech4000g
            ),

            //catégorie Créatine / Force
            Produit(
                id = 9,
                nom = "Créatine",
                poids= "350g",
                categorie = "Créatine / Force",
                prix = 20.00,
                description = "La créatine est un complément naturel utilisé pour améliorer la performance physique. Elle favorise aussi la prise de masse musculaire.",
                composition = "lait, céréales, soja, œufs,cacahuètes et noix",
                portion = "5g",
                image_src = R.drawable.creatine350g
            ),
            Produit(
                id = 10,
                nom = "Amino EAA",
                poids= "400g",
                categorie = "Créatine / Force",
                prix = 20.00,
                description = "Amino EAA est un mélange d’acides aminés essentiels qui améliore l’endurance et favorise la croissance musculaire.",
                composition = "L-leucine, L-isoleucine, L-phénylalanine, L-méthionine, L-thréonine, L-tryptophane, malate de citrulline.",
                portion = "10g",
                image_src = R.drawable.amino_eaa
            ),
            Produit(
                id = 11,
                nom = "L-Glutamine",
                poids= "400g",
                categorie = "Créatine / Force",
                prix = 20.00,
                description = "La L-Glutamine est un acide aminé essentiel à la récupération musculaire, qui aide à réduire la fatigue et soutient le système immunitaire après l’entraînement.",
                composition = "L-Glutamine pure, sans autres ingrédients.",
                portion = "5g",
                image_src = R.drawable.glutamine
            ),
            Produit(
                id = 12,
                nom = "Flex Shield",
                poids= "375g",
                categorie = "Créatine / Force",
                prix = 25.00,
                description = "Flex Shield est conçu pour protéger et renforcer les articulations et soutenir les entraînements intensifs.",
                composition = "Glucosamine, MSM (Méthylsulfonylméthane), Chondroïtine, Boswellia Serrata, acide hyaluronique.",
                portion = "15g",
                image_src = R.drawable.flex_shield
            ),

            //catégorie Énergie
            Produit(
                id = 13,
                nom = "Savage Pre-Workout",
                poids= "20x200ml",
                categorie = "Énergie",
                prix = 10.00,
                description = "Savage Pre-Workout est une boisson énergisante conçue pour booster l'énergie et l'endurance avant l'entraînement.",
                composition = "citrulline malate, caféine anhydre, L-théanine, extrait de thé vert, acide D-aspartique.",
                portion = "200ml",
                image_src = R.drawable.savage
            ),
            Produit(
                id = 14,
                nom = "EAA",
                poids= "12x330ml",
                categorie = "Énergie",
                prix = 8.00,
                description = "La boisson EAA est une formule riche en citrulline pour soutenir la circulation sanguine et pour booster l'énergie.",
                composition = "sucralose (E955), citrulline malate, caféine anhydre, L-théanine, L-leucine, L-valine, L-isoleucine.",
                portion = "330ml",
                image_src = R.drawable.boisson_eaa
            ),
            Produit(
                id = 15,
                nom = "L-Carnitine",
                poids= "12x500ml",
                categorie = "Énergie",
                prix = 8.00,
                description = "La boisson L-Carnitine aide à optimiser la combustion des graisses et à améliorer l'endurance pendant l'exercice.",
                composition = "eau, sucralose, acide citrique, l-carnitine, l-tartrate, sels minéraux, arômes.",
                portion = "500ml",
                image_src = R.drawable.carnitine
            ),
            Produit(
                id = 16,
                nom = "Carbo fuel",
                poids= "1000g",
                categorie = "Énergie",
                prix = 9.50,
                description = "Carbo Fuel est un complément à base de glucides, idéale pour soutenir l'endurance et la performance.",
                composition = "maltodextrine, dextrose, fructose, acide citrique, potassium citrate, arômes.",
                portion = "75g",
                image_src = R.drawable.carbo1000g
            ),
        )

        produitDao.deleteAll()
        produits.forEach { produitDao.insert(it) }
    }
}