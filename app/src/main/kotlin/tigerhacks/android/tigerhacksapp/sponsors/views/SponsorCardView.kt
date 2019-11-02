package tigerhacks.android.tigerhacksapp.sponsors.views

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintSet
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.view_sponsor_card.view.allMentorsTextView
import kotlinx.android.synthetic.main.view_sponsor_card.view.sponsorContainer
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.service.extensions.dpToPx
import tigerhacks.android.tigerhacksapp.models.Sponsor

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */

class SponsorCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val sponsorImageView: ImageView
    lateinit var sponsorData: Sponsor

    init {
        val height = context.dpToPx(130)
        layoutParams = MarginLayoutParams(MarginLayoutParams.MATCH_PARENT, height)
        LayoutInflater.from(context).inflate(R.layout.view_sponsor_card, this, true)
        sponsorImageView = findViewById(R.id.sponsorImageView)
        val tenDp = context.dpToPx(10)
        setPadding(tenDp, 0, tenDp, tenDp)
        clipToPadding = false
        clipChildren = false
    }

    fun setSponsor(sponsor: Sponsor) {
        sponsorData = sponsor
        if (sponsor.name == Sponsor.ALL_MENTORS_KEY) {
            allMentorsTextView.visibility = View.VISIBLE
            val constraintSet = ConstraintSet()
            constraintSet.clone(sponsorContainer)
            constraintSet.setHorizontalBias(R.id.sponsorImageView, 0f)

            val width = sponsorImageView.height - (resources.getDimension(R.dimen.margin_start_large))
            sponsorImageView.minimumWidth = width.toInt()
            sponsorImageView.maxWidth = width.toInt()
            sponsorImageView.setImageResource(R.mipmap.ic_launcher_round)
        } else {
            val constraintSet = ConstraintSet()
            constraintSet.clone(sponsorContainer)
            constraintSet.setHorizontalBias(R.id.sponsorImageView, 0.5f)

            allMentorsTextView.visibility = View.GONE
            Glide.with(sponsorImageView).load(sponsor.image).into(sponsorImageView)
        }
    }
}