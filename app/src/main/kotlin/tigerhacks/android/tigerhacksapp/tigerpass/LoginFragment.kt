package tigerhacks.android.tigerhacksapp.tigerpass

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import tigerhacks.android.tigerhacksapp.HomeScreenActivity
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.service.extensions.dpToPx

class LoginFragment : Fragment() {
    companion object {
        fun newInstance() = LoginFragment()

        const val RC_SIGN_IN = 9001
    }

    private lateinit var loginContainer: ConstraintLayout

    private lateinit var auth: FirebaseAuth
    private val provider = OAuthProvider.newBuilder("github.com")
        .addCustomParameter("login", "your-email@gmail.com")
        .setScopes(listOf("user:email"))
        .build()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutView = inflater.inflate(R.layout.fragment_tiger_login, container, false)

        loginContainer = layoutView.findViewById(R.id.loginContainer)
        val loginButton = layoutView.findViewById<AppCompatButton>(R.id.loginButton)
        val loginGoogleButton = layoutView.findViewById<AppCompatButton>(R.id.loginGoogleButton)
        val loginGithubButton = layoutView.findViewById<AppCompatButton>(R.id.loginGithubButton)
        val usernameEditText = layoutView.findViewById<EditText>(R.id.usernameEditText)
        val passwordEditText = layoutView.findViewById<EditText>(R.id.passwordEditText)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            swapToPass()
        }

        (activity as? HomeScreenActivity)?.let {

            val twentyFourDp = it.dpToPx(30)
            val img = resources.getDrawable(R.mipmap.ic_launcher_round, it.theme)
            img.setBounds(0, 0, twentyFourDp, twentyFourDp)
            loginButton.setCompoundDrawables(img, null, null, null)
        }

        loginButton.setOnClickListener {
            if (usernameEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()) {
                login(usernameEditText.text.toString(), passwordEditText.text.toString())
            } else {
                loginFailure("Invalid Username or Password")
            }
        }

        loginGoogleButton.setOnClickListener {
            loginWithGoogle()
        }

        loginGithubButton.setOnClickListener {
            loginWithGithub()
        }
        return layoutView
    }

    private var isActive = true

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            swapToPass()
        } else isActive = true
    }

    override fun onStop() {
        super.onStop()
        isActive = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java) ?: return
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                loginFailure()
            }
        }
    }

    private fun login(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    swapToPass()
                } else {
                    val message = it.exception?.message
                    if (message == null) loginFailure() else loginFailure(message)
                }
            }
    }

    private fun loginWithGithub() {
        (activity as? HomeScreenActivity)?.let {
            auth.startActivityForSignInWithProvider(it, provider)
                .addOnSuccessListener { swapToPass() }
                .addOnFailureListener {
                    loginFailure()
                }
        }
    }

    private fun loginWithGoogle() {
        (activity as? HomeScreenActivity)?.let {
            val logInIntent = it.googleSignInClient.signInIntent
            startActivityForResult(logInIntent, RC_SIGN_IN)
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        (activity as? HomeScreenActivity)?.let {
            val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) { //Success
                        swapToPass()
                    } else {
                        loginFailure()
                    }
                }
        }
    }

    private fun swapToPass() {
        (activity as? HomeScreenActivity)?.swapLogInAndPass(true)
    }

    private fun loginFailure(message: String = "Authentication Failed.") {
        if (!isActive) return
        Snackbar.make(loginContainer, message, Snackbar.LENGTH_SHORT).show()
    }
}
