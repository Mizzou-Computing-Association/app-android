package tigerhacks.android.tigerhacksapp.service.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import tigerhacks.android.tigerhacksapp.models.Event
import tigerhacks.android.tigerhacksapp.models.FavoritableEvent
import tigerhacks.android.tigerhacksapp.models.FavoriteItem

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */

@Dao
interface ScheduleDAO : FavoritesDAO{
    @Query("SELECT * FROM event")
    fun getAllBasicEvents(): List<Event>

    @Query("SELECT Event.*, FavoriteItem.* FROM Event INNER JOIN favoriteitem ON id = itemId WHERE day=1")
    fun getFridayEvents(): LiveData<List<FavoritableEvent>>

    @Query("SELECT Event.*, FavoriteItem.* FROM Event INNER JOIN favoriteitem ON id = itemId WHERE day=2")
    fun getSaturdayEvents(): LiveData<List<FavoritableEvent>>

    @Query("SELECT Event.*, FavoriteItem.* FROM Event INNER JOIN favoriteitem ON id = itemId WHERE day=3")
    fun getSundayEvents(): LiveData<List<FavoritableEvent>>

    @Query("SELECT Event.*, FavoriteItem.* FROM Event INNER JOIN favoriteitem ON id = itemId WHERE day=1 AND isFavorited=1")
    fun getFavoriteFridayEvents(): LiveData<List<FavoritableEvent>>

    @Query("SELECT Event.*, FavoriteItem.* FROM Event INNER JOIN favoriteitem ON id = itemId WHERE day=2 AND isFavorited=1")
    fun getFavoriteSaturdayEvents(): LiveData<List<FavoritableEvent>>

    @Query("SELECT Event.*, FavoriteItem.* FROM Event INNER JOIN favoriteitem ON id = itemId WHERE day=3 AND isFavorited=1")
    fun getFavoriteSundayEvents(): LiveData<List<FavoritableEvent>>

    @Query("DELETE FROM event")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(events: List<Event>)

    @Transaction
    suspend fun updateEvents(events: List<Event>) {
        val oldEvents = getAllBasicEvents()
        val addedItems = events.filter { !oldEvents.contains(it) }.map { FavoriteItem(it.id) }
        val removedItems = oldEvents.filter { !events.contains(it) }.map { it.id }
        insertFavoriteItems(addedItems)
        deleteFavoriteItems(removedItems)
        deleteAll()
        insertAll(events)
    }
}