package tigerhacks.android.tigerhacksapp.sponsors

import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_sponsor_detail.descriptionText
import kotlinx.android.synthetic.main.activity_sponsor_detail.linkText
import kotlinx.android.synthetic.main.activity_sponsor_detail.mainImage
import kotlinx.android.synthetic.main.activity_sponsor_detail.mentorLayout
import kotlinx.android.synthetic.main.activity_sponsor_detail.secondText
import kotlinx.android.synthetic.main.activity_sponsor_detail.toolbar
import kotlinx.android.synthetic.main.activity_sponsor_detail.toolbarLayout
import tigerhacks.android.tigerhacksapp.HomeScreenViewModel
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.service.extensions.darkenColor
import tigerhacks.android.tigerhacksapp.service.extensions.getColorRes
import tigerhacks.android.tigerhacksapp.service.extensions.observeNotNull

private const val SPONSOR_KEY = "sponsor_key"

class SponsorDetailActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        fun newInstance(context: Context, sponsor: Sponsor): Intent = Intent(context, SponsorDetailActivity::class.java).putExtra(SPONSOR_KEY, sponsor)
    }

    private lateinit var sponsor: Sponsor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sponsor_detail)

        sponsor = intent.getParcelableExtra(SPONSOR_KEY)

        //Sponsor Info
        toolbar.title = sponsor.name
        Glide.with(mainImage).load(sponsor.image).into(mainImage)
        if (sponsor.location != "tbd") {
            secondText.text = sponsor.location
        } else {
            secondText.text = getString(R.string.na)
        }

        linkText.text = sponsor.website
        descriptionText.text = sponsor.description

        linkText.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(sponsor.website))
            startActivity(browserIntent)
        }

        val viewModel = ViewModelProviders.of(this).get(HomeScreenViewModel::class.java)
        viewModel.mentorsListLiveData.observeNotNull(this) {
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
