package tigerhacks.android.tigerhacksapp

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_home_screen.container
import kotlinx.android.synthetic.main.activity_home_screen.navigationView
import kotlinx.android.synthetic.main.activity_home_screen.tabLayout
import kotlinx.android.synthetic.main.activity_home_screen.toolbar
import tigerhacks.android.tigerhacksapp.prizes.PrizesFragment
import tigerhacks.android.tigerhacksapp.help.HelpFragment
import tigerhacks.android.tigerhacksapp.schedule.ScheduleFragment
import tigerhacks.android.tigerhacksapp.sponsors.SponsorsFragment
import tigerhacks.android.tigerhacksapp.maps.MapFragment
import tigerhacks.android.tigerhacksapp.service.database.TigerHacksDatabase

class HomeScreenActivity : AppCompatActivity() {
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    private lateinit var mapFragment: MapFragment
    private lateinit var prizesFragment: PrizesFragment
    private lateinit var scheduleFragment: ScheduleFragment
    private lateinit var sponsorsFragment: SponsorsFragment
    private lateinit var helpFragment: HelpFragment

    lateinit var viewModel: HomeScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        FirebaseAnalytics.getInstance(this)
        setContentView(R.layout.activity_home_screen)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val db = TigerHacksDatabase.getDatabase(applicationContext)
        viewModel = ViewModelProviders.of(this, HomeScreenViewModel.FACTORY(db)).get(HomeScreenViewModel::class.java)

        mapFragment = MapFragment.newInstance()
        prizesFragment = PrizesFragment.newInstance()
        scheduleFragment = ScheduleFragment.newInstance()
        sponsorsFragment = SponsorsFragment.newInstance()
        helpFragment = HelpFragment.newInstance()

        val fragmentCount = supportFragmentManager.fragments.size
        if (fragmentCount == 0) {
            supportActionBar?.title = getString(R.string.title_schedule)
            supportFragmentManager
                .beginTransaction()
                .add(R.id.contentFrameLayout, scheduleFragment, "${R.id.navigation_schedule}~${getString(R.string.title_schedule)}")
                .commit()
            updateTabs(R.id.navigation_schedule)
        } else {
            val fragTag = supportFragmentManager.fragments[0].tag?.split("~")
            if (fragTag != null && fragTag.size > 1) {
                updateTabs(fragTag[0].toInt())
                supportActionBar?.title = fragTag[1]
            }
        }

        actionBarDrawerToggle = ActionBarDrawerToggle(this, container, R.string.open, R.string.close)
        container.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener { menuItem ->
            updateTabs(menuItem.itemId)
            val fragment = when (menuItem.itemId) {
                R.id.navigation_map -> mapFragment
                R.id.navigation_prizes -> prizesFragment
                R.id.navigation_schedule -> scheduleFragment
                R.id.navigation_sponsors -> sponsorsFragment
                else -> helpFragment
            }
            supportActionBar?.title = menuItem.title
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.contentFrameLayout, fragment, "${menuItem.itemId}~${menuItem.title}")
                .commit()
            true
        }
    }

    private fun updateTabs(fragId: Int) {
        val list = when (fragId) {
            R.id.navigation_map -> listOf(R.string.floor_1, R.string.floor_2, R.string.floor_3)
            R.id.navigation_schedule -> listOf(R.string.friday, R.string.saturday, R.string.sunday)
            else -> emptyList()
        }
        tabLayout.removeAllTabs()
        for (res in list) {
            val tab = tabLayout.newTab()
            tab.text = getString(res)
            tabLayout.addTab(tab)
        }
        tabLayout.visibility = if (list.isNotEmpty()) View.VISIBLE else View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }
}
