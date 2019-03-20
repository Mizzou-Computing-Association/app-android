package tigerhacks.android.tigerhacksapp.prizes

import android.support.v7.util.DiffUtil
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Prize(
    @SerializedName("sponsor")
    @Expose
    var sponsor: Int = 0,
    @SerializedName("title")
    @Expose
    var title: String = "",
    @SerializedName("reward")
    @Expose
    var reward: String = "",
    @SerializedName("description")
    @Expose
    var description: String = "",
    @SerializedName("prizetype")
    @Expose
    var prizeType: String? = null
) {
    companion object {
        val diff = object : DiffUtil.ItemCallback<Prize>() {
            override fun areContentsTheSame(oldItem: Prize?, newItem: Prize?) = oldItem?.sponsor == newItem?.sponsor && oldItem?.title == newItem?.title
            override fun areItemsTheSame(oldItem: Prize?, newItem: Prize?) = oldItem == newItem
        }
    }
}