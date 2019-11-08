package tigerhacks.android.tigerhacksapp.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import tigerhacks.android.tigerhacksapp.shared.CategoryDiffItemCallback

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */

class FavoritablePrize {
    companion object {
        val diff = object : CategoryDiffItemCallback<FavoritablePrize>() {
            override fun findCategory(item: FavoritablePrize) = item.prize.prizeType
            override fun areCategoryItemsTheSame(oldItem: FavoritablePrize, newItem: FavoritablePrize) = oldItem.prize.id == newItem.prize.id
            override fun areCategoryItemContentsTheSame(oldItem: FavoritablePrize, newItem: FavoritablePrize) = oldItem.prize == newItem.prize && oldItem.favoritable.isFavorited == newItem.favoritable.isFavorited
        }
    }

    @Embedded lateinit var prize: Prize
    @Embedded lateinit var favoritable: FavoriteItem
}

@JsonClass(generateAdapter = true)
data class PrizeList(
    val Developer: List<Prize> = emptyList(),
    val Sponsored: List<Prize> = emptyList(),
    val StartUp: List<Prize> = emptyList(),
    val Visuals: List<Prize> = emptyList(),
    val Audio: List<Prize> = emptyList(),
    val Beginner: List<Prize> = emptyList(),
    val Hardware: List<Prize> = emptyList()
) {
    fun combine(): List<Prize> {
        val total = arrayListOf<Prize>()
        total.addAll(Developer)
        total.addAll(Beginner)
        total.addAll(Sponsored)
        total.addAll(StartUp)
        total.addAll(Visuals)
        total.addAll(Audio)
        total.addAll(Hardware)
        return total
    }
}

@Entity
@JsonClass(generateAdapter = true)
data class Prize(
    @PrimaryKey val id: String = "",
    val sponsor: String = "",
    val title: String = "",
    val reward: String = "",
    val description: String = "",
    val prizeType: String = ""
)