package tigerhacks.android.tigerhacksapp.sponsors.models

import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import tigerhacks.android.tigerhacksapp.R

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */

@JsonClass(generateAdapter = true)
data class SponsorList(
    val Gold: List<Sponsor> = emptyList(),
    val Bronze: List<Sponsor> = emptyList(),
    val Platinum: List<Sponsor> = emptyList(),
    val Silver: List<Sponsor> = emptyList()
) {
    fun combine(): List<Sponsor> {
        val totalSponsors = arrayListOf<Sponsor>()
        totalSponsors.addAll(Gold)
        totalSponsors.addAll(Bronze)
        totalSponsors.addAll(Platinum)
        totalSponsors.addAll(Silver)
        return totalSponsors
    }
}

@Entity
@JsonClass(generateAdapter = true)
@Parcelize
data class Sponsor(
    @PrimaryKey val name: String = "",
    val description: String = "",
    val website: String = "",
    val image: String = "",
    val level: Int = 0
) : Parcelable {
    @ColorRes
    fun getSponsorLevelColorRes(): Int {
        return when (level) {
            0 -> R.color.platinum
            1 -> R.color.gold
            2 -> R.color.silver
            3 -> R.color.bronze
            else -> R.color.colorPrimary
        }
    }
}