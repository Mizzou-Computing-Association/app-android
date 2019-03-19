package tigerhacks.android.tigerhacksapp.sponsors

import android.os.Parcelable
import android.support.annotation.ColorRes
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import tigerhacks.android.tigerhacksapp.R

@Parcelize
data class Sponsor(
    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("mentors")
    @Expose
    var mentors: List<Mentor>? = null,

    @SerializedName("description")
    @Expose
    var description: String? = null,

    @SerializedName("website")
    @Expose
    var website: String? = null,

    @SerializedName("location")
    @Expose
    var location: String? = null,

    @SerializedName("image")
    @Expose
    var image: String? = null,

    @SerializedName("level")
    @Expose
    var level: String? = null
) : Parcelable {
    @ColorRes
    fun getSponsorLevelColorRes(): Int {
        return when (level) {
            "Platinum" -> R.color.platinum
            "Gold" -> R.color.gold
            "Silver" -> R.color.silver
            "Bronze" -> R.color.bronze
            else -> R.color.colorPrimary
        }
    }
}