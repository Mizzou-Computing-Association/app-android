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
    @Query("SELECT * FROM prize WHERE prizeType='Main'")
    fun getAllDevPrizes(): LiveData<List<Prize>>

    @Query("SELECT * FROM prize WHERE prizeType='Beginner'")
    fun getAllBeginnerPrizes(): LiveData<List<Prize>>

    @Query("SELECT * FROM prize WHERE prizeType='StartUp'")
    fun getAllStartUpPrizes(): LiveData<List<Prize>>

    @Insert
    fun insertAll(prizes: List<Prize>)

    @Query("DELETE FROM prize")
    fun deleteAll()

    @Transaction
    open suspend fun updatePrizes(prizes: List<Prize>) {
        deleteAll()
        insertAll(prizes)
    }
}