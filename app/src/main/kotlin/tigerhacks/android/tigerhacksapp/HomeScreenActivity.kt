package tigerhacks.android.tigerhacksapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_home_screen.navigation
import tigerhacks.android.tigerhacksapp.maps.MapFragment
import tigerhacks.android.tigerhacksapp.prizes.PrizesFragment
import tigerhacks.android.tigerhacksapp.resources.ResourcesFragment
import tigerhacks.android.tigerhacksapp.schedule.ScheduleFragment
import tigerhacks.android.tigerhacksapp.sponsors.SponsorsFragment
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class HomeScreenActivity : AppCompatActivity() {

    private lateinit var mapFragment: MapFragment
    private lateinit var prizesFragment: PrizesFragment
    private lateinit var scheduleFragment: ScheduleFragment
    private lateinit var sponsorsFragment: SponsorsFragment
    private lateinit var resourcesFragment: ResourcesFragment

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    companion object {
        private const val RC_SIGN_IN = 9001
    }

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
                .beginTransaction()
                .add(R.id.contentFrameLayout, scheduleFragment)
                .commit()
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
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.contentFrameLayout, fragment)
                .commit()
            true
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        //TODO updateUI(currentUser)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                //TODO
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) { //Success
                    val user = auth.currentUser
                    //TODO updateUI(user)
                } else {
                    //TODO Snackbar.make(main_layout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                    //updateUI(null)
                }

                // ...
            }
    }
}
