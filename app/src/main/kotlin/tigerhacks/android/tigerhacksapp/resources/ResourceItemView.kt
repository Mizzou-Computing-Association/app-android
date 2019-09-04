package tigerhacks.android.tigerhacksapp.resources

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.view_resource_item.view.iconImageView
import kotlinx.android.synthetic.main.view_resource_item.view.titleTextView
import tigerhacks.android.tigerhacksapp.R

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */

class ResourceItemView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.view_resource_item, this)
        val typedValue = TypedValue()
        getContext().theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)
        setBackgroundResource(typedValue.resourceId)

        val attr = context.obtainStyledAttributes(attrs, R.styleable.ResourceItemView, defStyleAttr, 0)
        val iconRes = attr.getResourceId(R.styleable.ResourceItemView_titleIcon, -1)
        val titleRes = attr.getResourceId(R.styleable.ResourceItemView_titleRes, -1)
        val autoTint = attr.getBoolean(R.styleable.ResourceItemView_autoTint, true)
        attr.recycle()

        if (iconRes != -1) {
            iconImageView.setImageResource(iconRes)
            iconImageView.visibility = View.VISIBLE
            if (!autoTint) iconImageView.imageTintList = null
        }

        if (titleRes != -1) titleTextView.setText(titleRes)
    }
}