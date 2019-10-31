package tigerhacks.android.tigerhacksapp.service.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import tigerhacks.android.tigerhacksapp.prizes.Prize

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */

@Dao
interface PrizesDAO {
    @Query("SELECT * FROM prize")
    fun getAllPrizes(): LiveData<List<Prize>>

    @Insert
    fun insertAll(prizes: List<Prize>)

    @Query("DELETE FROM prize")
    fun deleteAll()

    @Transaction
    suspend fun updatePrizes(prizes: List<Prize>) {
        deleteAll()
        insertAll(prizes)
    }
}