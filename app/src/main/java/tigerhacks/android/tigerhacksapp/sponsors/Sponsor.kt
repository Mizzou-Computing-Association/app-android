package tigerhacks.android.tigerhacksapp.sponsors

import android.os.Parcelable
import android.support.annotation.ColorRes
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import tigerhacks.android.tigerhacksapp.R

@JsonClass(generateAdapter = true)
@Parcelize
data class Sponsor(
    val name: String = "",
    val description: String = "",
    val website: String = "",
    val location: String = "",
    val image: String = "",
    val level: String = ""
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

    fun getLevel() = when (level) {
        "Platinum" -> 0
        "Gold" -> 1
        "Silver" -> 2
        else -> 3
    }
}