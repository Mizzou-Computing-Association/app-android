package tigerhacks.android.tigerhacksapp.sponsors

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import com.google.android.material.card.MaterialCardView
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.service.extensions.dpToPx

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */

class AllMentorsCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {
    init {
        val height = context.dpToPx(120)
        layoutParams = MarginLayoutParams(MarginLayoutParams.MATCH_PARENT, height).apply {
            val marginHor = resources.getDimensionPixelSize(R.dimen.margin_start_large)
            marginStart = marginHor
            marginEnd = marginHor
            val marginVer = context.dpToPx(5)
            topMargin = marginVer
            bottomMargin = marginVer
        }
        LayoutInflater.from(context).inflate(R.layout.view_all_mentors_card, this, true)

        cardElevation = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, resources.displayMetrics)
        radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics)
        @Suppress("DEPRECATION")
        setCardBackgroundColor(resources.getColor(R.color.colorSponsorsBackground))

        setOnClickListener { context.startActivity(AllMentorsActivity.newInstance(context)) }
    }
}