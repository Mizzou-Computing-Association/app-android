package tigerhacks.android.tigerhacksapp.sponsors.views

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import com.bumptech.glide.Glide
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.service.extensions.dpToPx
import tigerhacks.android.tigerhacksapp.sponsors.models.Sponsor

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
        layoutParams = MarginLayoutParams(MarginLayoutParams.MATCH_PARENT, MarginLayoutParams.WRAP_CONTENT)
        LayoutInflater.from(context).inflate(R.layout.fragment_sponsors, this, true)
        sponsorImageView = findViewById(R.id.sponsorImageView)
        val tenDp = context.dpToPx(10)
        setPadding(tenDp, 0, tenDp, tenDp)
        clipToPadding = false
        clipChildren = false
    }

    fun setSponsor(sponsor: Sponsor) {
        sponsorData = sponsor
        Glide.with(sponsorImageView).load(sponsor.image).into(sponsorImageView)
    }
}