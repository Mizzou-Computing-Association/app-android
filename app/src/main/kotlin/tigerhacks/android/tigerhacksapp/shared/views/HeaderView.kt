package tigerhacks.android.tigerhacksapp.shared.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_header.view.titleTextView
import tigerhacks.android.tigerhacksapp.R

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
class HeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        orientation = VERTICAL
        LayoutInflater.from(context).inflate(R.layout.view_header, this, true)
    }

    @SuppressLint("SetTextI18n")
    fun setPrizeCategory(prizeType: String) {
        titleTextView.text = "$prizeType Prizes"
    }

    fun setSponsorLevel(sponsorLevel: Int) {
        val textRes = when (sponsorLevel) {
            0 -> R.string.platinum_sponsors
            1 -> R.string.gold_sponsors
            2 -> R.string.silver_sponsors
            else -> R.string.bronze_sponsors
        }

        val colorRes = when (sponsorLevel) {
            0 -> R.color.platinum
            1 -> R.color.gold
            2 -> R.color.silver
            else -> R.color.bronze
        }

        titleTextView.text = context.getText(textRes)
        @Suppress("DEPRECATION")
        titleTextView.setTextColor(context.resources.getColor(colorRes))
    }
}