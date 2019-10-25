package tigerhacks.android.tigerhacksapp.tigerpass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class TigerPassFragment : Fragment() {
    var currentUser: FirebaseUser? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        currentUser = FirebaseAuth.getInstance().currentUser

        return if (currentUser == null) {
            LoginView(inflater.context)
        } else {
            PassView(inflater.context)
        }
    }
}
