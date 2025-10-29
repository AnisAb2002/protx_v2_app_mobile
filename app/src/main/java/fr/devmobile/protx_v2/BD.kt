package fr.devmobile.protx_v2

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Utilisateur::class], version = 4, exportSchema = false)
abstract class BD : RoomDatabase() {
    abstract fun utilisateurDao(): Utilisateur_dao

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

    }
}