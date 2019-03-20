package tigerhacks.android.tigerhacksapp.schedule

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ScheduleItemList (
    @SerializedName("schedule")
    @Expose
    var schedule: List<ScheduleItem>? = null
)