package tigerhacks.android.tigerhacksapp.models

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
@Entity
@Parcelize
@JsonClass(generateAdapter = true)
data class Mentor(
    val sponsor: String = "",
    @PrimaryKey val name: String = "",
    val skills: String = "",
    val contact: String = ""
) : Parcelable {
    companion object {
        val diff = object : DiffUtil.ItemCallback<Mentor>() {
            override fun areItemsTheSame(oldItem: Mentor, newItem: Mentor) = oldItem.name == newItem.name
            override fun areContentsTheSame(oldItem: Mentor, newItem: Mentor) = oldItem == newItem
        }
    }
}