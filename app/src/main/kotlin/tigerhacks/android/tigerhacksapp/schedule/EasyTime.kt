package tigerhacks.android.tigerhacksapp.schedule

/*
 * @author pauldg7@gmail.com (Paul Gillis)
 */

data class EasyTime(
    val hours: String = "00",
    val minutes: String = "00",
    val inAm: Boolean = true,
    val day: EasyTime.Day = EasyTime.Day.SUNDAY
) {
    enum class Day {
        FRIDAY, SATURDAY, SUNDAY
    }

    fun format() = "$hours:$minutes ${if (inAm) "AM" else "PM"}"
}