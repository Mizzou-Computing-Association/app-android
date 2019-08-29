package tigerhacks.android.tigerhacksapp.prizes

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.prize_card_layout.view.descriptionTextView
import kotlinx.android.synthetic.main.prize_card_layout.view.prizeListTextView
import kotlinx.android.synthetic.main.prize_card_layout.view.titleTextView
import tigerhacks.android.tigerhacksapp.R

/**
 * Created by Conno on 9/8/2018.
 */

//PrizeCardView is an extension of the CardView that adds onClick functionality for the prize cards
class PrizeCardView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        LayoutInflater.from(context).inflate(R.layout.prize_card_layout, this, true)
    }

    fun setup(prize: Prize) {
        titleTextView.text = prize.title
        descriptionTextView.text = prize.description
        prizeListTextView.text = prize.reward
    }
}
