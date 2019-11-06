package tigerhacks.android.tigerhacksapp.models

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson
import java.util.regex.Pattern
import com.squareup.moshi.JsonQualifier

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class EventTime

class FavoritableEvent {
    companion object {
        val diff = object : DiffUtil.ItemCallback<FavoritableEvent>() {
            override fun areItemsTheSame(oldItem: FavoritableEvent, newItem: FavoritableEvent) = oldItem.event.id == newItem.event.id
            override fun areContentsTheSame(oldItem: FavoritableEvent, newItem: FavoritableEvent) = oldItem.event == newItem.event && oldItem.favoritable.isFavorited == newItem.favoritable.isFavorited
        }
    }

    @Embedded lateinit var event: Event
    @Embedded lateinit var favoritable: FavoriteItem
}

class EventTimeAdapter {
    @ToJson fun toJson(@EventTime time: String) = ""

    @FromJson
    @EventTime
    fun fromJson(json: String): String {
        val searchPattern = Pattern.compile("(.*)T(\\d{2}):(\\d{2})")
        val matcher = searchPattern.matcher(json)
        if (!matcher.find()) return ""
        val groupTwo = matcher.group(2) ?: "0"
        var militaryTime = Integer.parseInt(groupTwo) + 6

        val isAm = militaryTime in 12..24

        if (militaryTime > 12) {
            militaryTime -= 12
            if (militaryTime > 12) militaryTime -= 12
        } else if (militaryTime == 0) {
            militaryTime = 12
        }

        val groupThree = matcher.group(3) ?: ""
        return "$militaryTime:$groupThree ${if (isAm) "AM" else "PM"}"
    }
}

@Entity
@JsonClass(generateAdapter = true)
data class Event (
    @PrimaryKey val id: String = "",
    @EventTime val time: String = "",
    val location: String = "",
    val floor: Int = 0,
    val title: String = "",
    val description: String = "",
    val day: Int = 0
)