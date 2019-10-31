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
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home_screen.drawerLayout

class HomeScreenActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var navController: NavController

    lateinit var viewModel: HomeScreenViewModel
    lateinit var googleSignInClient: GoogleSignInClient

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

        navController = findNavController(R.id.navHostFragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)

        updateNavgraphLabel()

        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navHostFragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun updateNavgraphLabel() {
        val title = if (FirebaseAuth.getInstance().currentUser != null) getString(R.string.title_profile) else getString(R.string.title_sign_in)
        navController.graph.findNode(R.id.navigation_profile)?.label = title

        supportActionBar?.title = title
        navigationView.menu.findItem(R.id.navigation_profile).title = title
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
