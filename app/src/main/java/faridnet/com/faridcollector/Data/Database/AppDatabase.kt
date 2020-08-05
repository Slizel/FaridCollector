package faridnet.com.faridcollector.Data.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import faridnet.com.faridcollector.Data.ContagensData.Contagens
import faridnet.com.faridcollector.Data.ContagensData.ContagensDao
import faridnet.com.faridcollector.Data.ProdutosData.Produtos
import faridnet.com.faridcollector.Data.ProdutosData.ProdutosDao

@Database(entities = [Contagens::class, Produtos::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun contagensDao(): ContagensDao
    abstract fun produtosDao(): ProdutosDao

    companion object{
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }

            //Evita concorrência das threads
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "contagem_database"
                ).build()
                INSTANCE = instance
                return instance

            }
        }

    }

}