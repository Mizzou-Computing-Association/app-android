package tigerhacks.android.tigerhacksapp.service.database.daos

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tigerhacks.android.tigerhacksapp.models.FavoriteItem

interface FavoritesDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteItems(favorites: List<FavoriteItem>)

    @Query("UPDATE FavoriteItem SET isFavorited = :state WHERE itemId = :itemId")
    fun updateItem(itemId: String, state: Boolean)

    @Query("DELETE FROM FavoriteItem WHERE itemId in (:list)")
    fun deleteFavoriteItems(list: List<String>)
}