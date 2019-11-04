package tigerhacks.android.tigerhacksapp

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_home_screen.navigationView
import tigerhacks.android.tigerhacksapp.service.database.TigerHacksDatabase
import android.os.Build
import android.view.Menu
import androidx.core.view.GravityCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home_screen.drawerLayout
import kotlinx.android.synthetic.main.activity_home_screen.toolbar
import tigerhacks.android.tigerhacksapp.help.HelpFragment
import tigerhacks.android.tigerhacksapp.maps.MapFragment
import tigerhacks.android.tigerhacksapp.prizes.PrizesFragment
import tigerhacks.android.tigerhacksapp.schedule.ScheduleFragment
import tigerhacks.android.tigerhacksapp.service.BaseFragment
import tigerhacks.android.tigerhacksapp.sponsors.SponsorsFragment
import tigerhacks.android.tigerhacksapp.tigerpass.TigerPassFragment
import java.util.Stack

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
class HomeScreenActivity : AppCompatActivity() {

    companion object {
        private const val FRAGMENT_ID_KEY = "FRAGMENT_TAG_KEY"
    }

    private lateinit var currentFragment: BaseFragment
    private lateinit var profileFragment: TigerPassFragment
    private lateinit var scheduleFragment: ScheduleFragment
    private lateinit var prizesFragment: PrizesFragment
    private lateinit var mapsFragment: MapFragment
    private lateinit var sponsorsFragment: SponsorsFragment
    private lateinit var helpFragment: HelpFragment

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    lateinit var viewModel: HomeScreenViewModel
    lateinit var googleSignInClient: GoogleSignInClient

    private var simpleBackStack: Stack<Int> = Stack()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!BuildConfig.DEBUG) FirebaseAnalytics.getInstance(this)
        setContentView(R.layout.activity_home_screen)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.firebase_web_host))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val db = TigerHacksDatabase.getDatabase(applicationContext)
        viewModel = ViewModelProviders.of(this, HomeScreenViewModel.FACTORY(db)).get(HomeScreenViewModel::class.java)

        profileFragment = TigerPassFragment()
        scheduleFragment = ScheduleFragment()
        prizesFragment = PrizesFragment()
        mapsFragment = MapFragment()
        sponsorsFragment = SponsorsFragment()
        helpFragment = HelpFragment()

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        navigationView.setNavigationItemSelectedListener(::navigateTo)

        val title = if (FirebaseAuth.getInstance().currentUser != null) getString(R.string.title_profile) else getString(R.string.title_sign_in)
        navigationView.menu.findItem(R.id.navigation_profile).title = title

        if (savedInstanceState == null) navigateTo(R.id.navigation_schedule) else navigateTo(savedInstanceState.getInt(FRAGMENT_ID_KEY, R.id.navigation_schedule))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(FRAGMENT_ID_KEY, currentFragment.navId)
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

    override fun onBackPressed() {
        if (simpleBackStack.size > 0) {
            if (simpleBackStack.size > 10) simpleBackStack.removeAt(0)
            val fragmentSize = supportFragmentManager.fragments.size
            if (fragmentSize > 0) {
                val navId = simpleBackStack.pop()
                navigateTo(navId, false)
            }
        }
    }

    private var prevId = -1

    private fun addToSimpleBackStack(navId: Int) {
        if (prevId != -1) simpleBackStack.push(prevId)
        prevId = navId
    }

    private fun navigateTo(menuItemId: Int, addToStack: Boolean = true) {
        val menuItem = navigationView.menu.findItem(menuItemId) ?: return
        navigateTo(menuItem)
        if (!addToStack) simpleBackStack.pop()
    }

    private fun navigateTo(menuItem: MenuItem): Boolean {
        val fragment: BaseFragment = when (menuItem.itemId) {
            R.id.navigation_profile -> profileFragment
            R.id.navigation_schedule -> scheduleFragment
            R.id.navigation_prizes -> prizesFragment
            R.id.navigation_map -> mapsFragment
            R.id.navigation_sponsors -> sponsorsFragment
            else -> helpFragment
        }

        menuItem.isChecked = true
        currentFragment = fragment
        updateTitle()

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
            actionBarDrawerToggle.syncState()
        }

        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            .replace(R.id.navFrameLayout, fragment, getString(fragment.titleResId))
            .commit()

        addToSimpleBackStack(menuItem.itemId)

        return true
    }

    private fun updateNavgraphLabel() {
        val title = if (FirebaseAuth.getInstance().currentUser != null) getString(R.string.title_profile) else getString(R.string.title_sign_in)
        supportActionBar?.title = title
        navigationView.menu.findItem(R.id.navigation_profile).title = title
    }

    fun updateTitle() {
        if (currentFragment.navId == R.id.navigation_profile) {
            updateNavgraphLabel()
        } else supportActionBar?.title = getString(currentFragment.titleResId)
    }
}
