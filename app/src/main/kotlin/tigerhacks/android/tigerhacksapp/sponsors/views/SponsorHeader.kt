package tigerhacks.android.tigerhacksapp.sponsors.views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup.MarginLayoutParams
import android.widget.TextView
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.service.extensions.dpToPx
import tigerhacks.android.tigerhacksapp.service.extensions.getColorRes

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
class SponsorHeader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {
    init {
        layoutParams = MarginLayoutParams(MarginLayoutParams.MATCH_PARENT, MarginLayoutParams.WRAP_CONTENT).apply {
            bottomMargin = context.dpToPx(10)
        }
        setTextColor(context.getColorRes(R.color.white))
        textSize = 24f
        setTypeface(null, Typeface.BOLD)
        gravity = Gravity.CENTER
        val dp = context.dpToPx(4)
        setPadding(dp, dp, dp, dp)
        elevation = context.dpToPx(3).toFloat()
    }

    fun setSponsorLevel(sponsorLevel: Int) {
        when (sponsorLevel) {
            0 -> {
                setBackgroundResource(R.color.platinum)
                text = context.getText(R.string.platinum_sponsors)
            }
            1 -> {
                setBackgroundResource(R.color.gold)
                text = context.getText(R.string.gold_sponsors)
            }
            2 -> {
                setBackgroundResource(R.color.silver)
                text = context.getText(R.string.silver_sponsors)
            }
            else -> {
                setBackgroundResource(R.color.bronze)
                text = context.getText(R.string.bronze_sponsors)
            }
        }
    }
}