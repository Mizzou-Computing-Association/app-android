package tigerhacks.android.tigerhacksapp

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.OAuthProvider
import kotlinx.android.synthetic.main.activity_sign_in.loginButton
import java.lang.Exception

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = FirebaseAuth.getInstance()

        startAuthFlow()

        loginButton.setOnClickListener { startAuthFlow() }
    }

    private fun startAuthFlow() {
        val provider = OAuthProvider.newBuilder("github.com")
        provider.addCustomParameter("login", "example@example.com")

        val pendingResultTask = auth.pendingAuthResult
        if (pendingResultTask != null) {
            pendingResultTask
                .addOnSuccessListener { onAuthSuccess(it) }
                .addOnFailureListener { onAuthFailure(it) }
        } else {
            auth.startActivityForSignInWithProvider(this, provider.build())
                .addOnSuccessListener { onAuthSuccess(it) }
                .addOnFailureListener { onAuthFailure(it) }
        }
    }

    private fun onAuthSuccess(result: AuthResult? = null) {
        startActivity(Intent(this, HomeScreenActivity::class.java))
        finish()
    }

    private fun onAuthFailure(exception: Exception) {
        if ((exception as? FirebaseAuthException)?.errorCode == "ERROR_WEB_CONTEXT_CANCELED") {
            window.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.colorPrimary)))
            loginButton.visibility = View.VISIBLE
        } else throw exception
    }
}