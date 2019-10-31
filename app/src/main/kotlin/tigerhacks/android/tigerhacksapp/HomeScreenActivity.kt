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
import android.os.Build
import android.view.Menu
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import tigerhacks.android.tigerhacksapp.tigerpass.LoginFragment
import tigerhacks.android.tigerhacksapp.tigerpass.TigerPassFragment

class HomeScreenActivity : AppCompatActivity() {
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    private lateinit var mapFragment: MapFragment
    private lateinit var prizesFragment: PrizesFragment
    private lateinit var scheduleFragment: ScheduleFragment
    private lateinit var sponsorsFragment: SponsorsFragment
    private lateinit var helpFragment: HelpFragment

    lateinit var viewModel: HomeScreenViewModel

    lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        FirebaseAnalytics.getInstance(this)
        setContentView(R.layout.activity_home_screen)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.firebase_web_host))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

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
            val pos = supportFragmentManager.fragments.size - 1
            val fragTag = supportFragmentManager.fragments[pos].tag?.split("~")
            if (fragTag != null && fragTag.size > 1) {
                updateTabs(fragTag[0].toInt())
                supportActionBar?.title = fragTag[1]
            }
        }

        actionBarDrawerToggle = ActionBarDrawerToggle(this, container, R.string.open, R.string.close)
        container.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        val isLoggedIn = FirebaseAuth.getInstance().currentUser != null
        val profileTitle = if (isLoggedIn) getString(R.string.title_profile) else getString(R.string.title_sign_in)
        navigationView.menu.findItem(R.id.navigation_profile).title = profileTitle

        navigationView.setNavigationItemSelectedListener(::navigate)
    }

    fun swapLogInAndPass(swapToPass: Boolean) {
        val navItem = navigationView.menu.findItem(R.id.navigation_profile)
        val titleRes = if (swapToPass) R.string.title_profile else R.string.title_sign_in
        navItem.title = getString(titleRes)
        navigate(navItem)
    }

    private fun navigate(menuItem: MenuItem): Boolean {
        val isUserLoggedIn = FirebaseAuth.getInstance().currentUser != null
        updateTabs(menuItem.itemId)
        val fragment = when (menuItem.itemId) {
            R.id.navigation_map -> mapFragment
            R.id.navigation_prizes -> prizesFragment
            R.id.navigation_schedule -> scheduleFragment
            R.id.navigation_sponsors -> sponsorsFragment
            R.id.navigation_profile -> if (!isUserLoggedIn) LoginFragment.newInstance() else TigerPassFragment.newInstance()
            else -> helpFragment
        }
        supportActionBar?.title = menuItem.title
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            .replace(R.id.contentFrameLayout, fragment, "${menuItem.itemId}~${menuItem.title}")
            .commit()
        return true
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.options, menu)

        val kept = if (Build.VERSION.SDK_INT < 29) R.id.options_battery else R.id.options_system
        val removed = if (Build.VERSION.SDK_INT >= 29) R.id.options_battery else R.id.options_system
        menu.removeItem(removed)

        when (TigerApplication.getThemeMode().getPosition()) {
            0 -> menu.findItem(R.id.options_day).isChecked = true
            1 -> menu.findItem(R.id.options_night).isChecked = true
            2 -> menu.findItem(kept).isChecked = true
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) return true
        val themeMode = when (item.itemId) {
            R.id.options_day -> ThemeMode.LIGHT
            R.id.options_night -> ThemeMode.DARK
            R.id.options_system -> ThemeMode.FOLLOW_SYSTEM
            R.id.options_battery -> ThemeMode.AUTO_BATTERY
            else -> null
        }
        if (themeMode != null) {
            item.isChecked = true
            TigerApplication.setThemeMode(themeMode)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
