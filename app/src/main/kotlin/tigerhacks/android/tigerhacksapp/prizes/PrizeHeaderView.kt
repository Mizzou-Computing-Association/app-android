package tigerhacks.android.tigerhacksapp.prizes

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.view_prize_header.view.titleTextView
import tigerhacks.android.tigerhacksapp.R

class PrizeHeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        LayoutInflater.from(context).inflate(R.layout.view_prize_header, this, true)
    }

    @SuppressLint("SetTextI18n")
    fun setPrizeCategory(prizeType: String) {
        titleTextView.text = "$prizeType Prizes"
    }
}