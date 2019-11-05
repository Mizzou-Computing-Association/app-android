package tigerhacks.android.tigerhacksapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteItem (
    @PrimaryKey val itemId: String,
    val isFavorited: Boolean = false
)