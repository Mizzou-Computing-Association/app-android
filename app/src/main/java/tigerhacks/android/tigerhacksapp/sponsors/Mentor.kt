package tigerhacks.android.tigerhacksapp.sponsors

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Mentor(
    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("skills")
    @Expose
    var skills: String? = null,

    @SerializedName("contact")
    @Expose
    var contact: String? = null
) : Parcelable