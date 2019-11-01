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
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home_screen.drawerLayout
import kotlinx.android.synthetic.main.activity_home_screen.navFrameLayout
import kotlinx.android.synthetic.main.activity_home_screen.toolbar
import tigerhacks.android.tigerhacksapp.help.HelpFragment
import tigerhacks.android.tigerhacksapp.maps.MapFragment
import tigerhacks.android.tigerhacksapp.prizes.PrizesFragment
import tigerhacks.android.tigerhacksapp.schedule.ScheduleFragment
import tigerhacks.android.tigerhacksapp.sponsors.SponsorsFragment
import tigerhacks.android.tigerhacksapp.tigerpass.TigerPassFragment
import java.util.Stack

data class FragmentTag(val fragment: Fragment, val tag: String)

class HomeScreenActivity : AppCompatActivity() {

    companion object {
        private const val FRAGMENT_ID_KEY = "FRAGMENT_TAG_KEY"
    }

    private lateinit var currentFragmentTag: FragmentTag
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
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        FirebaseAnalytics.getInstance(this)
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

        if (savedInstanceState == null) navigateTo(R.id.navigation_schedule) else navigateTo(savedInstanceState.getInt(FRAGMENT_ID_KEY, R.id.navigation_schedule))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val fragments = supportFragmentManager.fragments
        val tag = fragments[fragments.size - 1].tag
        val menuId = when (tag) {
            getString(R.string.title_profile) -> R.id.navigation_profile
            getString(R.string.title_schedule) -> R.id.navigation_schedule
            getString(R.string.title_prizes) -> R.id.navigation_prizes
            getString(R.string.title_map) -> R.id.navigation_map
            getString(R.string.title_sponsors) -> R.id.navigation_sponsors
            getString(R.string.title_help) -> R.id.navigation_help
            else -> null
        }
        if (menuId != null) outState.putInt(FRAGMENT_ID_KEY, menuId)
    }

    override fun onStop() {
        super.onStop()
        simpleBackStack.clear()
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
        if (simpleBackStack.size > 1) {
            simpleBackStack.pop()
            val navId = simpleBackStack.pop()
            navigateTo(navId)
        } else {
            Snackbar.make(navFrameLayout, "Nothing else to go back to!", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun addToSimpleBackStack(navId: Int) {
        simpleBackStack.push(navId)
    }

    private fun navigateTo(menuItemId: Int) {
        val menuItem = navigationView.menu.findItem(menuItemId) ?: return
        navigateTo(menuItem)
    }

    private fun navigateTo(menuItem: MenuItem): Boolean {
        val fragmentTag = when (menuItem.itemId) {
            R.id.navigation_profile -> FragmentTag(profileFragment, getString(R.string.title_profile))
            R.id.navigation_schedule -> FragmentTag(scheduleFragment, getString(R.string.title_schedule))
            R.id.navigation_prizes -> FragmentTag(prizesFragment, getString(R.string.title_prizes))
            R.id.navigation_map -> FragmentTag(mapsFragment, getString(R.string.title_map))
            R.id.navigation_sponsors -> FragmentTag(sponsorsFragment, getString(R.string.title_sponsors))
            else -> FragmentTag(helpFragment, getString(R.string.title_help))
        }

        menuItem.isChecked = true
        currentFragmentTag = fragmentTag
        updateTitle()

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
            actionBarDrawerToggle.syncState()
        }

        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            .replace(R.id.navFrameLayout, fragmentTag.fragment, fragmentTag.tag)
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
        if (currentFragmentTag.tag == getString(R.string.title_profile)) {
            updateNavgraphLabel()
        } else supportActionBar?.title = currentFragmentTag.tag
    }
}
