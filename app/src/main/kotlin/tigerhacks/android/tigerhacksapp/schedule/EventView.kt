package tigerhacks.android.tigerhacksapp.schedule

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.view_event.view.*
import kotlinx.android.synthetic.main.view_event.view.descriptionTextView
import kotlinx.android.synthetic.main.view_event.view.titleTextView
import tigerhacks.android.tigerhacksapp.R

/**
 * Created by Conno on 9/15/2018.
 */

class EventView @JvmOverloads constructor (context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : ConstraintLayout(context, attrs, defStyle) {
    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        LayoutInflater.from(context).inflate(R.layout.view_event, this)
    }

    fun setup(event: Event, isFirstItem: Boolean, isLastItem: Boolean) {
        titleTextView.text = event.title
        locationTextView.text = event.location
        descriptionTextView.text = event.description
        timeTextView.text = event.easyTime.format()

        if (event.location.isEmpty()) {
            locationTextView.visibility = View.GONE
            locationIconImageView.visibility = View.GONE
        }
        if ( event.description.isEmpty()) {
            descriptionTextView.visibility = View.GONE
            infoImageView.visibility = View.GONE
        }
        else{
            descriptionTextView.visibility = View.VISIBLE
            infoImageView.visibility = View.VISIBLE
        }
        
        timeLineSide.visibility = if (isLastItem) View.INVISIBLE else View.VISIBLE
        timeLineSide2.visibility = if (isFirstItem) View.GONE else View.VISIBLE
        divider.visibility = if(isLastItem) View.INVISIBLE else View.VISIBLE

    }
}