package tigerhacks.android.tigerhacksapp.schedule

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import java.util.regex.Pattern

@Entity
@JsonClass(generateAdapter = true)
data class Event (
    val day: Int = 0,
    val time: String = "",
    val location: String = "",
    val floor: Int = 0,
    @PrimaryKey val title: String = "",
    val description: String = ""
) {
    companion object {
        val diff = object : DiffUtil.ItemCallback<Event>() {
            override fun areItemsTheSame(oldItem: Event, newItem: Event) = oldItem.time == newItem.time && oldItem.location == newItem.location
            override fun areContentsTheSame(oldItem: Event, newItem: Event) = oldItem == newItem
        }
    }

    val easyTime: EasyTime
        get() {
            val searchPattern = Pattern.compile("(\\d)T(\\d{2}):(\\d{2})")
            val matcher = searchPattern.matcher(time)
            return if (matcher.find()) {
                val groupTwo = matcher.group(2)
                groupTwo ?: return EasyTime()
                val militaryTime = Integer.parseInt(groupTwo)

                val hoursTime = when (militaryTime) {
                    0 -> EasyTime(hours = "12", inAm = true)
                    in 1..11 -> EasyTime(hours = Integer.toString(militaryTime), inAm = true)
                    12 -> EasyTime(hours = "12", inAm = false)
                    else -> EasyTime(hours = Integer.toString(militaryTime - 12), inAm = false)
                }

                val day = when (matcher.group(1)) {
                    "2" -> EasyTime.Day.FRIDAY
                    "3" -> EasyTime.Day.SATURDAY
                    "4" -> EasyTime.Day.SUNDAY
                    else -> EasyTime.Day.FRIDAY
                }

                val groupThree = matcher.group(3)
                groupThree ?: return EasyTime()
                hoursTime.copy(minutes = groupThree, day = day)
            } else {
                EasyTime()
            }
        }
}