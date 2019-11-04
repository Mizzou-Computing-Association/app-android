package tigerhacks.android.tigerhacksapp.sponsors

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.view_sponsor_card.view.divider
import kotlinx.android.synthetic.main.view_sponsor_card.view.sponsorCardView
import kotlinx.android.synthetic.main.view_sponsor_card.view.sponsorImageView
import kotlinx.android.synthetic.main.view_sponsor_card.view.titleTextView
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.models.Sponsor
import tigerhacks.android.tigerhacksapp.sponsors.details.SponsorDetailActivity

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */

class SponsorCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        LayoutInflater.from(context).inflate(R.layout.view_sponsor_card, this, true)
    }

    fun setSponsor(sponsor: Sponsor, hasHeader: Boolean) {
        Glide.with(sponsorImageView).load(sponsor.image).into(sponsorImageView)
        sponsorCardView.setOnClickListener { context.startActivity(SponsorDetailActivity.newInstance(context, sponsor)) }

        setSponsorLevel(sponsor.level)

        val vis = if (hasHeader) View.VISIBLE else View.GONE
        titleTextView.visibility = vis
        divider.visibility = vis
    }

    private fun setSponsorLevel(sponsorLevel: Int) {
        val textRes = when (sponsorLevel) {
            0 -> R.string.platinum_sponsors
            1 -> R.string.gold_sponsors
            2 -> R.string.silver_sponsors
            else -> R.string.bronze_sponsors
        }

        val colorRes = when (sponsorLevel) {
            0 -> R.color.platinum
            1 -> R.color.gold
            2 -> R.color.silver
            else -> R.color.bronze
        }

        titleTextView.text = context.getText(textRes)
        @Suppress("DEPRECATION")
        titleTextView.setTextColor(context.resources.getColor(colorRes))
    }
}