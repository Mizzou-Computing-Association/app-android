package tigerhacks.android.tigerhacksapp.sponsors

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
@Entity
@JsonClass(generateAdapter = true)
@Parcelize
data class Sponsor(
    @PrimaryKey val name: String = "",
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

    fun getLevelNum() = when (level) {
        "Platinum" -> 0
        "Gold" -> 1
        "Silver" -> 2
        else -> 3
    }
}