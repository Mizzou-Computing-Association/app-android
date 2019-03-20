package tigerhacks.android.tigerhacksapp.schedule

import android.support.v7.util.DiffUtil
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.regex.Pattern

data class ScheduleItem (
    @SerializedName("time")
    @Expose
    var time: String = "",
    @SerializedName("location")
    @Expose
    var location: String = "",
    @SerializedName("floor")
    @Expose
    var floor: Int = 0,
    @SerializedName("title")
    @Expose
    var title: String = "",
    @SerializedName("description")
    @Expose
    var description: String = ""
) {
    companion object {
        val diff = object : DiffUtil.ItemCallback<ScheduleItem>() {
            override fun areItemsTheSame(oldItem: ScheduleItem?, newItem: ScheduleItem?) = oldItem?.time == newItem?.time && oldItem?.location == newItem?.location
            override fun areContentsTheSame(oldItem: ScheduleItem?, newItem: ScheduleItem?) = oldItem == newItem
        }
    }

    val easyTime: EasyTime
        get() {
            val searchPattern = Pattern.compile("(\\d)T(\\d{2}):(\\d{2})")
            val matcher = searchPattern.matcher(time)
            return if (matcher.find()) {
                val militaryTime = Integer.parseInt(matcher.group(2))

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

                hoursTime.copy(minutes = matcher.group(3), day = day)
            } else {
                EasyTime()
            }
        }
}