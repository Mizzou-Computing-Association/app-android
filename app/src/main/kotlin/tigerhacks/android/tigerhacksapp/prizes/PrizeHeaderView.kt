package tigerhacks.android.tigerhacksapp.prizes

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.service.extensions.dpToPx

class PrizeHeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {
    init {
        layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT).apply {
            topMargin = context.dpToPx(10)
            bottomMargin = context.dpToPx(10)
        }
        setBackgroundResource(R.color.colorPrimary)
        gravity = Gravity.CENTER
        textSize = 48f
        setTypeface(null, Typeface.BOLD)
        val dp = context.dpToPx(4)
        setPadding(dp, dp, dp, dp)
        setTextAppearance(context, R.style.AppTheme_TitlePrimary)
    }

    fun setCategoryLevel(prizeCategory: String) {
        text = prizeCategory
    }
}