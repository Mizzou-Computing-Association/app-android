package tigerhacks.android.tigerhacksapp.prizes

import android.support.v7.util.DiffUtil
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Prize(
    val sponsor: String = "",
    val title: String = "",
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