package tigerhacks.android.tigerhacksapp.tigerpass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import tigerhacks.android.tigerhacksapp.HomeScreenActivity
import tigerhacks.android.tigerhacksapp.R

class TigerPassFragment : Fragment() {
    companion object {
        fun newInstance() = TigerPassFragment()
    }

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutView = inflater.inflate(R.layout.fragment_tiger_pass, container, false)
        val logOutButton = layoutView.findViewById<Button>(R.id.logOutButton)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser == null) {
            swapToLogin()
        }

        logOutButton.setOnClickListener {
            logout()
            swapToLogin()
        }

        return layoutView
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser == null) {
            swapToLogin()
        }
    }

    private fun logout() {
        auth.signOut()
        (activity as? HomeScreenActivity)?.googleSignInClient?.revokeAccess()
    }

    private fun swapToLogin() {
        (activity as? HomeScreenActivity)?.swapLogInAndPass(false)
    }
}