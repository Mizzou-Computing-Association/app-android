package tigerhacks.android.tigerhacksapp.sponsors

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_sponsor_detail.appBarLayout
import kotlinx.android.synthetic.main.activity_sponsor_detail.descriptionText
import kotlinx.android.synthetic.main.activity_sponsor_detail.descriptionTitleTextView
import kotlinx.android.synthetic.main.activity_sponsor_detail.informationTitle
import kotlinx.android.synthetic.main.activity_sponsor_detail.informationTitleDivider
import kotlinx.android.synthetic.main.activity_sponsor_detail.internetImageView
import kotlinx.android.synthetic.main.activity_sponsor_detail.linkText
import kotlinx.android.synthetic.main.activity_sponsor_detail.mainImage
import kotlinx.android.synthetic.main.activity_sponsor_detail.mentorDivider
import kotlinx.android.synthetic.main.activity_sponsor_detail.mentorLayout
import kotlinx.android.synthetic.main.activity_sponsor_detail.toolbar
import kotlinx.android.synthetic.main.activity_sponsor_detail.toolbarLayout
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.service.database.TigerHacksDatabase
import tigerhacks.android.tigerhacksapp.service.extensions.darkenColor
import tigerhacks.android.tigerhacksapp.service.extensions.getColorRes
import tigerhacks.android.tigerhacksapp.service.extensions.observeNotNull
import tigerhacks.android.tigerhacksapp.models.Mentor
import tigerhacks.android.tigerhacksapp.models.Sponsor

private const val SPONSOR_KEY = "sponsor_key"

class SponsorDetailActivity : AppCompatActivity() {

    companion object {
        fun newInstance(context: Context, sponsor: Sponsor): Intent = Intent(context, SponsorDetailActivity::class.java).putExtra(SPONSOR_KEY, sponsor)
    }

    private lateinit var db: TigerHacksDatabase
    private lateinit var sponsor: Sponsor
    private var observer: Observer<List<Mentor>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sponsor_detail)

        sponsor = intent.getParcelableExtra(SPONSOR_KEY) ?: Sponsor()

        //Sponsor Info
        toolbar.title = sponsor.name
        if (sponsor.image.isNotEmpty()) Glide.with(mainImage).load(sponsor.image).into(mainImage)

        if (sponsor.website.isNotEmpty()) {
            linkText.text = sponsor.website
            val typedValue = TypedValue()
            theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)
            linkText.setBackgroundResource(typedValue.resourceId)
        } else {
            informationTitleDivider.visibility = View.GONE
            internetImageView.visibility = View.GONE
            informationTitle.visibility = View.GONE
            linkText.visibility = View.GONE
            appBarLayout.setExpanded(false, false)
        }

        if (sponsor.description.isEmpty()) {
            descriptionTitleTextView.visibility = View.GONE
            descriptionText.visibility = View.GONE
            informationTitleDivider.visibility = View.GONE
        } else {
            descriptionText.text = sponsor.description
        }

        linkText.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(sponsor.website))
            startActivity(browserIntent)
        }

        db = TigerHacksDatabase.getDatabase(applicationContext)
        val liveData = if (sponsor.name == Sponsor.ALL_MENTORS_KEY) {
            mentorDivider.visibility = View.GONE
            db.sponsorsDAO().getAllMentors()
        } else {
            db.sponsorsDAO().getMentorsForSponsor(sponsor.name)
        }

        observer = liveData.observeNotNull(this) {
            mentorLayout.mentors = it
        }

        //Toolbar Setup
        val sponsorColor = sponsor.getSponsorLevelColorRes()
        toolbarLayout.setContentScrimColor(getColorRes(sponsorColor))
        toolbarLayout.setBackgroundResource(sponsorColor)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        window.statusBarColor = getColorRes(sponsorColor).darkenColor()
    }

    override fun onStop() {
        super.onStop()
        if (observer != null) db.sponsorsDAO().getMentorsForSponsor(sponsor.name).removeObservers(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
