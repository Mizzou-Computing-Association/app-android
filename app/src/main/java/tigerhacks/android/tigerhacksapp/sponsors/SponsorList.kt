package tigerhacks.android.tigerhacksapp.sponsors

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SponsorList (
    @SerializedName("sponsors")
    @Expose
    var sponsors: List<Sponsor>? = null
)