package fr.devmobile.protx_v2

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [PanierEntity::class], version = 1)
abstract class BD : RoomDatabase() {

    abstract fun panierDao(): PanierEntityDao

    companion object {
        @Volatile private var INSTANCE: BD? = null

        fun getDatabase(context: Context): BD {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BD::class.java,
                    "protx_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
