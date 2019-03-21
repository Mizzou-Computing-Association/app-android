package tigerhacks.android.tigerhacksapp.sponsors.views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.service.extensions.dpToPx
import tigerhacks.android.tigerhacksapp.service.extensions.getColorRes

/*
 * @author pauldg7@gmail.com (Paul Gillis)
 */

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class Header @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {
    init {
        setTextColor(context.getColorRes(R.color.white))
        textSize = 24f
        setTypeface(null, Typeface.BOLD)
        gravity = Gravity.CENTER
        val dp = context.dpToPx(4)
        setPadding(dp, dp, dp, dp)
    }

    @ModelProp
    fun setSponsorLevel(sponsorLevel: Int) {
        when (sponsorLevel) {
            0 -> {
                setBackgroundResource(R.color.platinum)
                text = "Platinum Sponsors"
            }
            1 -> {
                setBackgroundResource(R.color.gold)
                text = "Gold Sponsors"
            }
            2 -> {
                setBackgroundResource(R.color.silver)
                text = "Silver Sponsors"
            }
            else -> {
                setBackgroundResource(R.color.bronze)
                text = "Bronze Sponsors"
            }
        }
    }
}