package tigerhacks.android.tigerhacksapp.prizes

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PrizeList (
    @SerializedName("prizes")
    @Expose
    val prizes: List<Prize>? = null
)