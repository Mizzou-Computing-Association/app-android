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
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.models.Sponsor.Companion.ALL_MENTORS_KEY
import tigerhacks.android.tigerhacksapp.models.Sponsor.Companion.HEADER_KEY

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
        totalSponsors.add(Sponsor(name = "${HEADER_KEY}0", level = 0))
        totalSponsors.addAll(Platinum)
        totalSponsors.add(Sponsor(name = "${HEADER_KEY}1", level = 1))
        totalSponsors.addAll(Gold)
        totalSponsors.add(Sponsor(name = "${HEADER_KEY}2", level = 2))
        totalSponsors.addAll(Silver)
        totalSponsors.add(Sponsor(name = "${HEADER_KEY}3", level = 3))
        totalSponsors.addAll(Bronze)
        totalSponsors.add(Sponsor(name = ALL_MENTORS_KEY, level = 4))
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

    companion object {
        const val HEADER_KEY = "HEADER"
        const val ALL_MENTORS_KEY = "All Mentors"

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
