package tigerhacks.android.tigerhacksapp.sponsors.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
@Entity
@JsonClass(generateAdapter = true)
@Parcelize
data class Mentor(
    val sponsor: String = "",
    @PrimaryKey val name: String = "",
    val skills: String = "",
    val contact: String = ""
) : Parcelable