package tigerhacks.android.tigerhacksapp.models

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson
import java.util.regex.Pattern
import com.squareup.moshi.JsonQualifier


@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class EventTime

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
) {
    companion object {
        val diff = object : DiffUtil.ItemCallback<Event>() {
            override fun areItemsTheSame(oldItem: Event, newItem: Event) = oldItem.time == newItem.time && oldItem.location == newItem.location
            override fun areContentsTheSame(oldItem: Event, newItem: Event) = oldItem == newItem
        }
    }
}