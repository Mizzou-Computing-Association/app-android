package tigerhacks.android.tigerhacksapp.models

import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.shared.CategoryDiffItemCallback

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
        val diff = object : CategoryDiffItemCallback<Sponsor>() {
            override fun areCategoryItemsTheSame(oldItem: Sponsor, newItem: Sponsor) = oldItem.name == newItem.name
            override fun areCategoryItemContentsTheSame(oldItem: Sponsor, newItem: Sponsor) = oldItem == newItem
            override fun findCategory(item: Sponsor) = item.level.toString()
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
