package tigerhacks.android.tigerhacksapp.tigertalks

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tigerhacks.android.tigerhacksapp.HomeScreenActivity
import tigerhacks.android.tigerhacksapp.R

class TigerTalksFragment : Fragment() {

    companion object {
        fun newInstance() = TigerTalksFragment()
    }

    private var home: HomeScreenActivity? = null

    private val clickListener = View.OnClickListener { clickedView ->
        val intent = when (clickedView.id) {
            R.id.tigerHacksWebsiteCardView -> Intent(Intent.ACTION_VIEW, Uri.parse("http://tiger-hacks.com/"))
            R.id.mcaSlackCardView -> Intent(Intent.ACTION_VIEW, Uri.parse("https://mizzoumca.slack.com/"))
            R.id.introFlaskCardView -> Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=V0QmmrTTbY4"))
            R.id.introiOSCardView -> Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=kobP_rJAuyI"))
            R.id.introWebDevCardView -> Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=KaNfsfwSUu4&t=66s"))
            else -> null
        }
        intent?.let { nonNullIntent -> startActivity(nonNullIntent) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        home = activity as HomeScreenActivity
        val view = inflater.inflate(R.layout.fragment_tigertalks, container, false)
        view.findViewById<CardView>(R.id.tigerHacksWebsiteCardView).setOnClickListener(clickListener)
        view.findViewById<CardView>(R.id.mcaSlackCardView).setOnClickListener(clickListener)
        view.findViewById<CardView>(R.id.introFlaskCardView).setOnClickListener(clickListener)
        view.findViewById<CardView>(R.id.introiOSCardView).setOnClickListener(clickListener)
        view.findViewById<CardView>(R.id.introWebDevCardView).setOnClickListener(clickListener)
        return view
    }

    override fun onStart() {
        super.onStart()
        home?.onFragmentsReady()
    }
}
