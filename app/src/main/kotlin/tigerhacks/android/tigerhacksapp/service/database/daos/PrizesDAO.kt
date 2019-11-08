package tigerhacks.android.tigerhacksapp.service.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import tigerhacks.android.tigerhacksapp.models.FavoritablePrize
import tigerhacks.android.tigerhacksapp.models.FavoriteItem
import tigerhacks.android.tigerhacksapp.models.Prize

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */

@Dao
interface PrizesDAO : FavoritesDAO {
    @Query("SELECT Prize.*, FavoriteItem.* FROM prize INNER JOIN favoriteitem ON id = itemId")
    fun getAllPrizes(): LiveData<List<FavoritablePrize>>

    @Query("SELECT Prize.*, FavoriteItem.* FROM prize INNER JOIN favoriteitem ON id = itemId WHERE isFavorited=1")
    fun getAllFavoritedPrizes(): LiveData<List<FavoritablePrize>>

    @Query("SELECT * FROM prize")
    fun getAllBasicPrizes(): List<Prize>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(prizes: List<Prize>)

    @Query("DELETE FROM prize")
    fun deleteAll()

    @Transaction
    suspend fun updatePrizes(prizes: List<Prize>) {
        val oldPrizes = getAllBasicPrizes()
        val addedItems = prizes.filter { !oldPrizes.contains(it) }.map { FavoriteItem(it.id) }
        val removedItems = oldPrizes.filter { !prizes.contains(it) }.map { it.id }
        insertFavoriteItems(addedItems)
        deleteFavoriteItems(removedItems)
        deleteAll()
        insertAll(prizes)
    }
}