package tigerhacks.android.tigerhacksapp.help

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tigerhacks.android.tigerhacksapp.R
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import tigerhacks.android.tigerhacksapp.HomeScreenActivity

//Helper data class to link View Id's to the Url that should open that that view Id is clicked
private data class IdLink(val id: Int, val link: String)

class HelpFragment : androidx.fragment.app.Fragment() {

    companion object {
        fun newInstance() = HelpFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val toolbarView = inflater.inflate(R.layout.toolbar_no_tab, container, false)
        val toolbarContainer = toolbarView.findViewById<ConstraintLayout>(R.id.container)
        val view = inflater.inflate(R.layout.fragment_help, toolbarContainer, true)
        arrayOf(
            IdLink(R.id.tigerHacksWebsiteCardView, "http://tiger-hacks.com/"),
            IdLink(R.id.mcaSlackCardView,"https://join.slack.com/t/tigerhacks2019/shared_invite/enQtNzg3ODQxMjQyNDg2LWExZTIyNWQ1ZThlMGRhMzAwNjQ4MGEwZDhhMmQxNTUwMTcyOGZiNjAxNzFkN2IzZjQxMDhhZGI5ZmFlMzkxMWQ"),
            IdLink(R.id.introFlaskCardView,"https://www.youtube.com/watch?v=V0QmmrTTbY4"),
            IdLink(R.id.introiOSCardView,"https://www.youtube.com/watch?v=kobP_rJAuyI"),
            IdLink(R.id.introWebDevCardView,"https://www.youtube.com/watch?v=KaNfsfwSUu4&t=66s")
        ).forEach { idLink ->
            //For Every IdLink register a click listener to the view id
            view.findViewById<HelpItemView>(idLink.id).setOnClickListener {
                //When clicked pull Id's link and start a browse intent with that link
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(idLink.link)))
            }
        }

        val activity = activity as HomeScreenActivity
        val toolbar = toolbarView.findViewById<Toolbar>(R.id.toolbar)

        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navController = activity.findNavController(R.id.navHostFragment)
        activity.setupActionBarWithNavController(navController, activity.findViewById<DrawerLayout>(R.id.drawerLayout))
        activity.findViewById<NavigationView>(R.id.navigationView).setupWithNavController(navController)

        return toolbarView
    }
}
