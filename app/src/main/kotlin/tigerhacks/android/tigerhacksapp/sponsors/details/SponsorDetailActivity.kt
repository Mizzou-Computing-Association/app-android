package tigerhacks.android.tigerhacksapp.sponsors.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_sponsor_detail.mainImage
import kotlinx.android.synthetic.main.activity_sponsor_detail.sponsorRecyclerView
import kotlinx.android.synthetic.main.activity_sponsor_detail.toolbar
import kotlinx.android.synthetic.main.activity_sponsor_detail.toolbarLayout
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.service.database.TigerHacksDatabase
import tigerhacks.android.tigerhacksapp.service.extensions.darkenColor
import tigerhacks.android.tigerhacksapp.service.extensions.getColorRes
import tigerhacks.android.tigerhacksapp.models.Mentor
import tigerhacks.android.tigerhacksapp.models.Sponsor

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
class SponsorDetailActivity : AppCompatActivity() {

    companion object {
        private const val SPONSOR_KEY = "sponsor_key"

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

        db = TigerHacksDatabase.getDatabase(applicationContext)
        val liveData = db.sponsorsDAO().getMentorsForSponsor(sponsor.name)

        val adapter = SponsorAdapter(sponsor)
        sponsorRecyclerView.adapter = adapter

        val observer: Observer<List<Mentor>> = Observer { adapter.submitList(it) }
        liveData.observe(this, observer)
        this.observer = observer

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
