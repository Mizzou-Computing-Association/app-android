package tigerhacks.android.tigerhacksapp.tigerpass

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_tiger_pass.logOutButton
import tigerhacks.android.tigerhacksapp.R

class TigerPassActivity : AppCompatActivity() {
    companion object {
        fun newInstance(context: Context) = Intent(context, TigerPassActivity::class.java)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tiger_pass)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser == null) {
            swapToLogin()
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.firebase_web_host))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        logOutButton.setOnClickListener {
            logout()
            swapToLogin()
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser == null) {
            swapToLogin()
        }
    }

    private fun logout() {
        auth.signOut()
        googleSignInClient.revokeAccess()
    }

    private fun swapToLogin() {
        startActivity(LoginActivity.newInstance(this))
        finish()
    }
}