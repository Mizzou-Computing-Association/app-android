package tigerhacks.android.tigerhacksapp.models

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import se.ansman.kotshi.JsonSerializable

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */

class FavoritablePrize {
    companion object {
        val diff = object : DiffUtil.ItemCallback<FavoritablePrize>() {
            override fun areItemsTheSame(oldItem: FavoritablePrize, newItem: FavoritablePrize) = oldItem.prize.id == newItem.prize.id
            override fun areContentsTheSame(oldItem: FavoritablePrize, newItem: FavoritablePrize) = oldItem.prize == newItem.prize && oldItem.favoritable.isFavorited == newItem.favoritable.isFavorited
        }
    }

    @Embedded lateinit var prize: Prize
    @Embedded lateinit var favoritable: FavoriteItem
}

@JsonSerializable
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
@JsonSerializable
data class Prize(
    @PrimaryKey val id: String = "",
    val sponsor: String = "",
    val title: String = "",
    val reward: String = "",
    val description: String = "",
    val prizeType: String = ""
)