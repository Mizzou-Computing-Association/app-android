package tigerhacks.android.tigerhacksapp.tigertalks

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.view_tiger_card.view.iconImageView
import kotlinx.android.synthetic.main.view_tiger_card.view.titleTextView
import tigerhacks.android.tigerhacksapp.R

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */

class TigerCardView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.view_tiger_card, this)
        val rippleAttrs = intArrayOf(android.R.attr.selectableItemBackground)
        val typedArray = context.obtainStyledAttributes(rippleAttrs)
        val backgroundResource = typedArray.getResourceId(0, 0)
        setBackgroundResource(backgroundResource)
        typedArray.recycle()

        val attr = context.obtainStyledAttributes(attrs, R.styleable.TigerCardView, defStyleAttr, 0)
        val iconRes = attr.getResourceId(R.styleable.TigerCardView_titleIcon, -1)
        val titleRes = attr.getResourceId(R.styleable.TigerCardView_titleRes, -1)
        attr.recycle()

        if (iconRes != -1) {
            iconImageView.setImageResource(iconRes)
            iconImageView.visibility = View.VISIBLE
        }

        if (titleRes != -1) titleTextView.setText(titleRes)
    }
}