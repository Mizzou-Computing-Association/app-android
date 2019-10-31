package tigerhacks.android.tigerhacksapp.help

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.TigerApplication
import android.widget.ArrayAdapter
import tigerhacks.android.tigerhacksapp.ThemeMode

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
        val view = inflater.inflate(R.layout.fragment_help, container, false)
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

        return view
    }
}
