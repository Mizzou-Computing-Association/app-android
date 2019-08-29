package tigerhacks.android.tigerhacksapp.schedule

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.schedule_card_layout.view.descriptionTextView
import kotlinx.android.synthetic.main.schedule_card_layout.view.locationIconImageView
import kotlinx.android.synthetic.main.schedule_card_layout.view.locationTextView
import kotlinx.android.synthetic.main.schedule_card_layout.view.timeLineSide
import kotlinx.android.synthetic.main.schedule_card_layout.view.timeLineSide2
import kotlinx.android.synthetic.main.schedule_card_layout.view.timeTextView
import kotlinx.android.synthetic.main.schedule_card_layout.view.titleTextView
import tigerhacks.android.tigerhacksapp.R

/**
 * Created by Conno on 9/15/2018.
 */

class EventCardView @JvmOverloads constructor (context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : ConstraintLayout(context, attrs, defStyle) {
    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        LayoutInflater.from(context).inflate(R.layout.schedule_card_layout, this)
    }

    fun setup(event: Event) {
        titleTextView.text = event.title
        locationTextView.text = event.location
        descriptionTextView.text = event.description
        timeTextView.text = event.easyTime.format()

        if (event.location.isEmpty()) {
            locationTextView.visibility = View.GONE
            locationIconImageView.visibility = View.GONE
        }
    }

    fun hideTopLine() {
        timeLineSide2.visibility = View.GONE
    }

    fun hideBottomLine() {
        timeLineSide.visibility = View.INVISIBLE
    }
}