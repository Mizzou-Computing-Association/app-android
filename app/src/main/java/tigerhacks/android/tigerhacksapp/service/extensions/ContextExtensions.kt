package tigerhacks.android.tigerhacksapp.service.extensions

import android.content.Context
import android.support.annotation.ColorRes
import android.util.TypedValue

fun Context.dpToPx(dp: Int) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics).toInt()

fun Context.spToPx(sp: Int) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp.toFloat(), resources.displayMetrics).toInt()

@Suppress("deprecated")
fun Context.getColorRes(@ColorRes res: Int) = resources.getColor(res)