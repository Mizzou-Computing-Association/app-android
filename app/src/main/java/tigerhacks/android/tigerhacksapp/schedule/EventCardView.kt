package tigerhacks.android.tigerhacksapp.schedule

import android.content.Context
import androidx.cardview.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.schedule_card_layout.view.descriptionTextView
import kotlinx.android.synthetic.main.schedule_card_layout.view.locationTextView
import kotlinx.android.synthetic.main.schedule_card_layout.view.timeTextView
import kotlinx.android.synthetic.main.schedule_card_layout.view.titleTextView
import tigerhacks.android.tigerhacksapp.R

/**
 * Created by Conno on 9/15/2018.
 */

class EventCardView @JvmOverloads constructor (context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : CardView(context, attrs, defStyle) {

    private var expanded = false
        set(value) {
            if (field == value) return
            field = value
            descriptionTextView.visibility = if (value) View.VISIBLE else View.GONE
        }

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        LayoutInflater.from(context).inflate(R.layout.schedule_card_layout, this)
        setOnClickListener { expanded = !expanded }
    }

    fun setup(event: Event) {
        titleTextView.text = event.title
        locationTextView.text = event.location
        descriptionTextView.text = event.description
        timeTextView.text = event.easyTime.format()
    }
}