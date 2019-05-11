package tigerhacks.android.tigerhacksapp

import android.os.Bundle
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_home_screen.mainViewPager
import kotlinx.android.synthetic.main.activity_home_screen.navigation
import kotlinx.android.synthetic.main.activity_home_screen.toolbar
import tigerhacks.android.tigerhacksapp.maps.MapFragment
import tigerhacks.android.tigerhacksapp.prizes.PrizesFragment
import tigerhacks.android.tigerhacksapp.schedule.ScheduleFragment
import tigerhacks.android.tigerhacksapp.sponsors.SponsorsFragment
import tigerhacks.android.tigerhacksapp.tigertalks.TigerTalksFragment

class HomeScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_home_screen)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        navigation.setup(mainViewPager)
        val mPagerAdapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int) = when (position) {
                0 -> MapFragment.newInstance()
                1 -> PrizesFragment.newInstance()
                2 -> ScheduleFragment.newInstance()
                3 -> SponsorsFragment.newInstance()
                else -> TigerTalksFragment.newInstance()
            }

            override fun getCount() = 5

            override fun getPageTitle(position: Int): String = when (position) {
                0 -> getString(R.string.title_map)
                1 -> getString(R.string.title_prizes)
                2 -> getString(R.string.title_schedule)
                3 -> getString(R.string.title_sponsors)
                else -> getString(R.string.title_tigertalks)
            }
        }

        mainViewPager.adapter = mPagerAdapter
        mainViewPager.offscreenPageLimit = 4

        //set initial page/tab state
        mainViewPager.currentItem = 2
    }

    // Snackbar.make(
    // mPager.rootView,
    // "TigerHacks API call failed. Attempting to reconnect...",
    // Snackbar.LENGTH_SHORT
    // ).show()
}
