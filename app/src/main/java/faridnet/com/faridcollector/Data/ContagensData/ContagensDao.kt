package faridnet.com.faridcollector.Data.ContagensData

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ContagensDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addContagem(contagens: Contagens)

    // Deleta somente 1 linha
    @Delete
    suspend fun deleteContagens(contagens: Contagens)

    @Query("DELETE FROM contagem_table")
    suspend fun deleteAllContagens()


    @Query("SELECT * FROM contagem_table ORDER BY contagemId ASC")
    fun readAllData(): LiveData<List<Contagens>>



}