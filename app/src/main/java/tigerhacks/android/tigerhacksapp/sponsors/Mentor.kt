package tigerhacks.android.tigerhacksapp.sponsors

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Mentor(
    val sponsor: String = "",
    val name: String = "",
    val skills: String = "",
    var contact: String = ""
) : Parcelable