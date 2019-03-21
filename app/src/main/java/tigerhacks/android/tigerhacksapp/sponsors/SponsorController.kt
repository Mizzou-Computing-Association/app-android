package tigerhacks.android.tigerhacksapp.sponsors

import android.content.Context
import android.graphics.Typeface
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TypedEpoxyController
import com.bumptech.glide.Glide
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.service.extensions.dpToPx
import tigerhacks.android.tigerhacksapp.service.extensions.getColorRes

/*
 * @author pauldg7@gmail.com (Paul Gillis)
 */
 
class SponsorController : TypedEpoxyController<List<Sponsor>>() {
    var clickListener: View.OnClickListener? = null

    override fun buildModels(list: List<Sponsor>) {
        header {
            id("platnumTitle")
            sponsorLevel(0)
        }
        list.filter { it.getLevel() == 0 }.forEach {
            sponsorImage {
                id(it.name)
                sponsor(it)
                listener(clickListener)
            }
        }
        header {
            id("goldTitle")
            sponsorLevel(1)
        }
        list.filter { it.getLevel() == 1 }.forEach {
            sponsorImage {
                id(it.name)
                sponsor(it)
                listener(clickListener)
            }
        }
        header {
            id("silverTitle")
            sponsorLevel(2)
        }
        list.filter { it.getLevel() == 2 }.forEach {
            sponsorImage {
                id(it.name)
                sponsor(it)
                listener(clickListener)
            }
        }
        header {
            id("bronzeTitle")
            sponsorLevel(3)
        }
        list.filter { it.getLevel() == 3 }.forEach {
            sponsorImage {
                id(it.name)
                sponsor(it)
                listener(clickListener)
            }
        }
    }
}

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class Header @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {
    init {
        setTextColor(context.getColorRes(R.color.white))
        textSize = 24f
        setTypeface(null, Typeface.BOLD)
        gravity = Gravity.CENTER
        val dp = context.dpToPx(4)
        setPadding(dp, dp, dp, dp)
    }

    @ModelProp
    fun setSponsorLevel(sponsorLevel: Int) {
        when (sponsorLevel) {
            0 -> {
                setBackgroundResource(R.color.platinum)
                text = "Platinum Sponsors"
            }
            1 -> {
                setBackgroundResource(R.color.gold)
                text = "Gold Sponsors"
            }
            2 -> {
                setBackgroundResource(R.color.silver)
                text = "Silver Sponsors"
            }
            else -> {
                setBackgroundResource(R.color.bronze)
                text = "Bronze Sponsors"
            }
        }
    }
}

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class SponsorImage @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val sponsorImageView: ImageView
    lateinit var sponsorData: Sponsor

    var listener: View.OnClickListener? = null
        @CallbackProp set

    init {
        LayoutInflater.from(context).inflate(R.layout.fragment_sponsors, this, true)
        sponsorImageView = findViewById(R.id.sponsorImageView)
    }

    @ModelProp
    fun setSponsor(sponsor: Sponsor) {
        sponsorData = sponsor
        Glide.with(sponsorImageView).load(sponsor.image).into(sponsorImageView)
    }

    @AfterPropsSet
    fun useProps() {
        setOnClickListener(listener)
    }
}