package tigerhacks.android.tigerhacksapp.sponsors.details

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.view_sponsor_info.view.descriptionText
import kotlinx.android.synthetic.main.view_sponsor_info.view.internetImageView
import kotlinx.android.synthetic.main.view_sponsor_info.view.linkText
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.models.Sponsor

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
class InformationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        LayoutInflater.from(context).inflate(R.layout.view_sponsor_info, this, true)

        val typedValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)
        linkText.setBackgroundResource(typedValue.resourceId)
    }

    fun setSponsor(sponsor: Sponsor) {
        linkText.text = sponsor.website
        descriptionText.text = sponsor.description

        //Visibility
        descriptionText.visibility = if (sponsor.description.isNotEmpty()) View.VISIBLE else View.GONE

        internetImageView.visibility = if (sponsor.website.isNotEmpty()) View.VISIBLE else View.GONE
        linkText.visibility = if (sponsor.website.isNotEmpty()) {
            linkText.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(sponsor.website))
                context.startActivity(browserIntent)
            }

            View.VISIBLE
        } else View.GONE
    }
}