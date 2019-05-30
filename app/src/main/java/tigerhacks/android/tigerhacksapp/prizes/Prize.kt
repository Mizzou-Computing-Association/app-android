package tigerhacks.android.tigerhacksapp.prizes

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity
@JsonClass(generateAdapter = true)
data class Prize(
    val sponsor: String = "",
    @PrimaryKey val title: String = "",
    val reward: String = "",
    val description: String = "",
    @Json(name = "prizetype")
    val prizeType: String = ""
) {
    companion object {
        val diff = object : DiffUtil.ItemCallback<Prize>() {
            override fun areContentsTheSame(oldItem: Prize, newItem: Prize) = oldItem.sponsor == newItem.sponsor && oldItem.title == newItem.title
            override fun areItemsTheSame(oldItem: Prize, newItem: Prize) = oldItem == newItem
        }
    }
}