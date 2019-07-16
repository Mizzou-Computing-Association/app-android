package tigerhacks.android.tigerhacksapp.prizes

import android.content.Context
import androidx.cardview.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.prize_card_layout.view.descriptionTextView
import kotlinx.android.synthetic.main.prize_card_layout.view.prizeInfoLinearLayout
import kotlinx.android.synthetic.main.prize_card_layout.view.prizeListTextView
import kotlinx.android.synthetic.main.prize_card_layout.view.titleTextView
import tigerhacks.android.tigerhacksapp.R
import java.util.Arrays

/**
 * Created by Conno on 9/8/2018.
 */

//PrizeCardView is an extension of the CardView that adds onClick functionality for the prize cards
class PrizeCardView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): CardView(context, attrs, defStyleAttr) {
    private var expanded = false
        set(value) {
            if (field == value) return
            field = value
            prizeInfoLinearLayout.visibility = if (value) View.VISIBLE else View.GONE
        }

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        LayoutInflater.from(context).inflate(R.layout.prize_card_layout, this, true)
        setOnClickListener { expanded = !expanded }
    }

    fun setup(prize: Prize) {
        titleTextView.text = prize.title
        descriptionTextView.text = prize.description
        val list = listOf(Arrays.asList(prize.reward))
        var newString = ""
        for (rewards in list) {
            newString += "\u2022 $rewards\n"
        }
        newString = newString.substring(0, newString.length - 1)
        prizeListTextView.text = newString
    }
}
