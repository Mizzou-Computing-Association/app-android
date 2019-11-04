package tigerhacks.android.tigerhacksapp.models

import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import se.ansman.kotshi.JsonSerializable
import tigerhacks.android.tigerhacksapp.R

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
@JsonSerializable
data class SponsorList(
    val Gold: List<Sponsor> = emptyList(),
    val Bronze: List<Sponsor> = emptyList(),
    val Platinum: List<Sponsor> = emptyList(),
    val Silver: List<Sponsor> = emptyList()
) {
    fun combine(): List<Sponsor> {
        val totalSponsors = arrayListOf<Sponsor>()
        totalSponsors.addAll(Platinum)
        totalSponsors.addAll(Gold)
        totalSponsors.addAll(Silver)
        totalSponsors.addAll(Bronze)
        return totalSponsors
    }
}

@Entity
@Parcelize
@JsonClass(generateAdapter = true)
data class Sponsor(
    @PrimaryKey val name: String = "",
    val description: String = "",
    val website: String = "",
    val image: String = "",
    val level: Int = 0
) : Parcelable {

    companion object {
        val diff = object : DiffUtil.ItemCallback<Sponsor>() {
            override fun areItemsTheSame(oldItem: Sponsor, newItem: Sponsor) = oldItem.name == newItem.name
            override fun areContentsTheSame(oldItem: Sponsor, newItem: Sponsor) = oldItem == newItem
        }
    }

    @IgnoredOnParcel
    @Ignore
    var mentors: List<Mentor> = emptyList()

    @ColorRes
    fun getSponsorLevelColorRes(): Int {
        return when (level) {
            0 -> R.color.platinum
            1 -> R.color.gold
            2 -> R.color.silver
            3 -> R.color.bronze
            else -> R.color.darkBlue
        }
    }
}
