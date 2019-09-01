package tigerhacks.android.tigerhacksapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_home_screen.navigation
import tigerhacks.android.tigerhacksapp.maps.MapFragment
import tigerhacks.android.tigerhacksapp.prizes.PrizesFragment
import tigerhacks.android.tigerhacksapp.resources.ResourcesFragment
import tigerhacks.android.tigerhacksapp.schedule.ScheduleFragment
import tigerhacks.android.tigerhacksapp.sponsors.SponsorsFragment

class HomeScreenActivity : AppCompatActivity() {

    private lateinit var mapFragment: MapFragment
    private lateinit var prizesFragment: PrizesFragment
    private lateinit var scheduleFragment: ScheduleFragment
    private lateinit var sponsorsFragment: SponsorsFragment
    private lateinit var resourcesFragment: ResourcesFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        FirebaseAnalytics.getInstance(this)
        setContentView(R.layout.activity_home_screen)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        mapFragment = MapFragment.newInstance()
        prizesFragment = PrizesFragment.newInstance()
        scheduleFragment = ScheduleFragment.newInstance()
        sponsorsFragment = SponsorsFragment.newInstance()
        resourcesFragment = ResourcesFragment.newInstance()

        val fragmentCount = supportFragmentManager.fragments.size
        if (fragmentCount == 0) {
            supportFragmentManager
                ?.beginTransaction()
                ?.add(R.id.contentFrameLayout, scheduleFragment)
                ?.commit()
        }

        navigation.setOnNavigationItemSelectedListener { menuItem ->
            val fragment = when(menuItem.itemId) {
                R.id.navigation_map -> mapFragment
                R.id.navigation_prizes -> prizesFragment
                R.id.navigation_schedule -> scheduleFragment
                R.id.navigation_sponsors -> sponsorsFragment
                else -> resourcesFragment
            }
            supportFragmentManager
                ?.beginTransaction()
                ?.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                ?.replace(R.id.contentFrameLayout, fragment)
                ?.commit()
            true
        }
    }
}
