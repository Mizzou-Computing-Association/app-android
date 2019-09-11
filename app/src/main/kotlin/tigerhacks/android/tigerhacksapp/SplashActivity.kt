package tigerhacks.android.tigerhacksapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.OAuthProvider
import java.lang.Exception

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        val nextAcitvity = if (auth.currentUser == null) {
            // User needs to Sign-in
            SignInActivity::class.java
        } else {
            // User is already signed in
            HomeScreenActivity::class.java
        }

        startActivity(Intent(this, nextAcitvity))
        finish()
    }
}