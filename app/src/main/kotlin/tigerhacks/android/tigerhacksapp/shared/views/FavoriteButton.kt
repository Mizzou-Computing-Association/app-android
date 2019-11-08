package tigerhacks.android.tigerhacksapp.shared.views

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.widget.ImageView
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.service.extensions.dpToPx

class FavoriteButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): ImageView(context, attrs, defStyleAttr) {
    var isChecked: Boolean = false
        set(value) {
            if (field == value) return
            field = value
            val iconRes = if (isChecked) R.drawable.filled_star_icon else R.drawable.star_icon
            setImageResource(iconRes)
        }
    var onToggle: ((Boolean) -> Unit)? = null

    init {
        setImageResource(R.drawable.star_icon)
        imageTintList = ColorStateList.valueOf(resources.getColor(R.color.orange))
        val tenDp = context.dpToPx(20)
        setPadding(tenDp, tenDp, tenDp, tenDp)

        setOnClickListener { toggle(true) }
    }

    fun toggle(isUserToggle: Boolean = false) {
        isChecked = !isChecked
        if (isUserToggle) onToggle?.invoke(isChecked)
    }
}