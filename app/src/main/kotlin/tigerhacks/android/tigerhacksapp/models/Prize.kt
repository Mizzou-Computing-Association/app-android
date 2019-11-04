package tigerhacks.android.tigerhacksapp.models

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import se.ansman.kotshi.JsonSerializable
import tigerhacks.android.tigerhacksapp.models.Prize.Companion.HEADER_KEY

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
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
) {
    companion object {
        const val HEADER_KEY = "HEADER"

        val diff = object : DiffUtil.ItemCallback<Prize>() {
            override fun areContentsTheSame(oldItem: Prize, newItem: Prize) = oldItem.sponsor == newItem.sponsor && oldItem.title == newItem.title
            override fun areItemsTheSame(oldItem: Prize, newItem: Prize) = oldItem == newItem
        }
    }
}