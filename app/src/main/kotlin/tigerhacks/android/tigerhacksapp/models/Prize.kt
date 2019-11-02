package tigerhacks.android.tigerhacksapp.models

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import tigerhacks.android.tigerhacksapp.models.Prize.Companion.HEADER_KEY

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
        total.add(Prize(id = "${HEADER_KEY}0", prizeType = "Developer"))
        total.addAll(Developer)
        total.add(Prize(id = "${HEADER_KEY}1", prizeType = "Beginner"))
        total.addAll(Beginner)
        total.add(Prize(id = "${HEADER_KEY}2", prizeType = "Sponsored"))
        total.addAll(Sponsored)
        total.add(Prize(id = "${HEADER_KEY}3", prizeType = "StartUp"))
        total.addAll(StartUp)
        total.add(Prize(id = "${HEADER_KEY}4", prizeType = "Visuals"))
        total.addAll(Visuals)
        total.add(Prize(id = "${HEADER_KEY}5", prizeType = "Audio"))
        total.addAll(Audio)
        total.add(Prize(id = "${HEADER_KEY}6", prizeType = "Hardware"))
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
) {
    companion object {
        const val HEADER_KEY = "HEADER"

        val diff = object : DiffUtil.ItemCallback<Prize>() {
            override fun areContentsTheSame(oldItem: Prize, newItem: Prize) = oldItem.sponsor == newItem.sponsor && oldItem.title == newItem.title
            override fun areItemsTheSame(oldItem: Prize, newItem: Prize) = oldItem == newItem
        }
    }
}