package tigerhacks.android.tigerhacksapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import se.ansman.kotshi.JsonSerializable

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
@Entity
@JsonSerializable
data class Profile (
    @PrimaryKey val id: Int = 0, //Should never be more than one profile in db
    val pass: String = "",
    val registered: Boolean = false
)