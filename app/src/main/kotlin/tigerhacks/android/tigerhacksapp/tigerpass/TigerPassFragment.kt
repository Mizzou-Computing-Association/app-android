package tigerhacks.android.tigerhacksapp.tigerpass

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tigerhacks.android.tigerhacksapp.HomeScreenActivity
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.models.Profile
import tigerhacks.android.tigerhacksapp.service.BaseFragment
import tigerhacks.android.tigerhacksapp.service.extensions.dpToPx
import tigerhacks.android.tigerhacksapp.service.extensions.observeNotNull

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
class TigerPassFragment : BaseFragment() {
    companion object {
        const val RC_SIGN_IN = 9001
    }

    override val navId = R.id.navigation_profile
    override val titleResId = R.string.title_profile

    private lateinit var loginContainer: ConstraintLayout
    private lateinit var loginButton: AppCompatButton
    private lateinit var loginGoogleButton: AppCompatButton
    private lateinit var loginGithubButton: AppCompatButton
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    private lateinit var logOutButton: Button
    private lateinit var registerButton: Button
    private lateinit var qrCodeImageView: ImageView
    private lateinit var progressBarView: ProgressBar

    private lateinit var auth: FirebaseAuth
    private val githubProvider = OAuthProvider.newBuilder("github.com")
        .addCustomParameter("login", "your-email@gmail.com")
        .setScopes(listOf("user:email"))
        .build()
    
    private var observer: Observer<Profile>? = null
    private lateinit var home: HomeScreenActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutView = inflater.inflate(R.layout.fragment_tiger_pass, container, false)

        home = activity as HomeScreenActivity

        /* Login Views */
        loginContainer = layoutView.findViewById(R.id.loginContainer)
        loginButton = layoutView.findViewById(R.id.loginButton)
        loginGoogleButton = layoutView.findViewById(R.id.loginGoogleButton)
        loginGithubButton = layoutView.findViewById(R.id.loginGithubButton)
        emailEditText = layoutView.findViewById(R.id.emailEditText)
        passwordEditText = layoutView.findViewById(R.id.passwordEditText)

        /* Tiger Pass Views */
        logOutButton = layoutView.findViewById(R.id.logOutButton)
        registerButton = layoutView.findViewById(R.id.registerButton)
        qrCodeImageView = layoutView.findViewById(R.id.qrCodeImageView)
        progressBarView = layoutView.findViewById(R.id.progressBarView)

        auth = FirebaseAuth.getInstance()
        
        val twentyFourDp = home.dpToPx(30)
        val img = resources.getDrawable(R.mipmap.ic_launcher_round, home.theme)
        img.setBounds(0, 0, twentyFourDp, twentyFourDp)
        loginButton.setCompoundDrawables(img, null, null, null)

        loginButton.setOnClickListener(::login)
        loginGoogleButton.setOnClickListener(::loginWithGoogle)
        loginGithubButton.setOnClickListener(::loginWithGithub)
        logOutButton.setOnClickListener(::logout)

        registerButton.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://tigerhacks.com"))) }

        update()

        return layoutView
    }

    override fun onStart() {
        super.onStart()
        update()
    }

    override fun onStop() {
        super.onStop()
        observer?.let { home.viewModel.profileLiveData.removeObserver(it) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java) ?: return
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                if (e.statusCode != 12501) { //User cancel status code
                    loginFailure()
                }
            }
        }
    }

    private fun setLoginVisibility(visibility: Int) {
        emailEditText.visibility = visibility
        passwordEditText.visibility = visibility
        loginButton.visibility = visibility
        loginGoogleButton.visibility = visibility
        loginGithubButton.visibility = visibility
        if (visibility == View.GONE) {
            emailEditText.editableText.clear()
            passwordEditText.editableText.clear()
        }
    }

    private fun setProfileVisibility(visibility: Int) {
        progressBarView.visibility = visibility
        qrCodeImageView.visibility = visibility
        logOutButton.visibility = visibility
    }

    private fun update() {
        observer?.let { home.viewModel.profileLiveData.removeObserver(it) }

        home.updateTitle()
        requireActivity()

        if (auth.currentUser == null) {
            setLoginVisibility(View.VISIBLE)
            setProfileVisibility(View.GONE)
            registerButton.visibility = View.GONE
        } else {
            setLoginVisibility(View.GONE)
            setProfileVisibility(View.VISIBLE)

            observer = home.viewModel.profileLiveData.observeNotNull(this) {
                registerButton.visibility = if (!it.registered) View.VISIBLE else View.GONE
                Glide.with(this)
                    .load(it.pass)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            qrCodeImageView.visibility = View.VISIBLE
                            progressBarView.visibility = View.GONE
                            return false
                        }
                    })
                    .into(qrCodeImageView)
            }
        }
    }

    private fun login(v: View) {
        if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()) {
            val email = emailEditText.text.toString()
            val pass = passwordEditText.text.toString()
            auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        loginComplete()
                    } else {
                        val message = it.exception?.message
                        if (message == null) loginFailure() else loginFailure(message)
                    }
                }
        } else {
            loginFailure("Invalid Email or Password")
        }
    }

    private fun loginWithGithub(v: View) {
        auth.startActivityForSignInWithProvider(home, githubProvider)
            .addOnSuccessListener { loginComplete() }
            .addOnFailureListener {
                if (it.message?.contains("An account already") == true) {
                    loginFailure("Email address already registered using a different Sign in method!")
                } else loginFailure()
            }
    }

    private fun loginWithGoogle(v: View) {
        val logInIntent = home.googleSignInClient.signInIntent
        startActivityForResult(logInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(home) { task ->
                if (task.isSuccessful) { //Success
                    loginComplete()
                } else {
                    loginFailure()
                }
            }
    }

    private fun loginComplete() {
        val userId = auth.currentUser?.uid ?: return
        CoroutineScope(Dispatchers.Main).launch {
            home.viewModel.refreshProfile(userId)
        }
        update()
    }

    private fun loginFailure(message: String = "Authentication Failed.") {
        Snackbar.make(loginContainer, message, Snackbar.LENGTH_LONG).show()
    }

    private fun logout(v: View) {
        CoroutineScope(Dispatchers.IO).launch {
            home.viewModel.database.profileDAO().deleteAll()
        }
        auth.signOut()
        home.googleSignInClient.revokeAccess()
        update()
    }
}