package tigerhacks.android.tigerhacksapp.service.extensions

/*
 * @author pauldg7@gmail.com (Paul Gillis)
 */
fun Int.darkenColor(): Int {
    val ratio = 1.0f - 0.2f
    val a = this shr 24 and 0xFF
    val r = ((this shr 16 and 0xFF) * ratio).toInt()
    val g = ((this shr 8 and 0xFF) * ratio).toInt()
    val b = ((this and 0xFF) * ratio).toInt()

    return a shl 24 or (r shl 16) or (g shl 8) or b
}