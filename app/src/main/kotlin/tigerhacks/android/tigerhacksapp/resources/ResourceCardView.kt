package tigerhacks.android.tigerhacksapp.resources

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.view_tiger_card.view.iconImageView
import kotlinx.android.synthetic.main.view_tiger_card.view.titleTextView
import tigerhacks.android.tigerhacksapp.R

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */

class ResourceCardView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.view_tiger_card, this)
        val typedValue = TypedValue()
        getContext().theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)
        setBackgroundResource(typedValue.resourceId)

        val attr = context.obtainStyledAttributes(attrs, R.styleable.ResourceCardView, defStyleAttr, 0)
        val iconRes = attr.getResourceId(R.styleable.ResourceCardView_titleIcon, -1)
        val titleRes = attr.getResourceId(R.styleable.ResourceCardView_titleRes, -1)
        attr.recycle()

        if (iconRes != -1) {
            iconImageView.setImageResource(iconRes)
            iconImageView.visibility = View.VISIBLE
        }

        if (titleRes != -1) titleTextView.setText(titleRes)
    }
}