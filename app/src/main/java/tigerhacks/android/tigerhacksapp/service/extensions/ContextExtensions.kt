package tigerhacks.android.tigerhacksapp.service.extensions

import android.content.Context
import androidx.annotation.ColorRes
import android.util.TypedValue

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
fun Context.dpToPx(dp: Int) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics).toInt()

fun Context.spToPx(sp: Int) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp.toFloat(), resources.displayMetrics).toInt()

@Suppress("deprecated")
fun Context.getColorRes(@ColorRes res: Int) = resources.getColor(res)