package tigerhacks.android.tigerhacksapp.tigerpass

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity
@JsonClass(generateAdapter = true)
data class Profile (
    @PrimaryKey val id: Int = 0, //Should never be more than one profile in db
    val pass: String = "",
    val registered: Boolean = false
)