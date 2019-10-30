package tigerhacks.android.tigerhacksapp.resources

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

class ResourcesFragment : androidx.fragment.app.Fragment() {

    companion object {
        fun newInstance() = ResourcesFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_resources, container, false)
        arrayOf(
            IdLink(R.id.tigerHacksWebsiteCardView, "http://tiger-hacks.com/"),
            IdLink(R.id.mcaSlackCardView,"https://join.slack.com/t/tigerhacks2019/shared_invite/enQtNzg3ODQxMjQyNDg2LWExZTIyNWQ1ZThlMGRhMzAwNjQ4MGEwZDhhMmQxNTUwMTcyOGZiNjAxNzFkN2IzZjQxMDhhZGI5ZmFlMzkxMWQ"),
            IdLink(R.id.introFlaskCardView,"https://www.youtube.com/watch?v=V0QmmrTTbY4"),
            IdLink(R.id.introiOSCardView,"https://www.youtube.com/watch?v=kobP_rJAuyI"),
            IdLink(R.id.introWebDevCardView,"https://www.youtube.com/watch?v=KaNfsfwSUu4&t=66s")
        ).forEach { idLink ->
            //For Every IdLink register a click listener to the view id
            view.findViewById<ResourceItemView>(idLink.id).setOnClickListener {
                //When clicked pull Id's link and start a browse intent with that link
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(idLink.link)))
            }
        }

        val selectedThemePos = TigerApplication.getThemeMode().getPosition()
        val spinner = view.findViewById<Spinner>(R.id.appThemeSpinner)

        context?.let {
            val res = if (Build.VERSION.SDK_INT >= 29) R.array.theme_q_data else R.array.theme_data
            spinner.adapter = ArrayAdapter.createFromResource(it, res, android.R.layout.simple_spinner_dropdown_item)
            spinner.setSelection(selectedThemePos)
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val themeMode = if (position >= 2 && Build.VERSION.SDK_INT >= 29) { ThemeMode.FOLLOW_SYSTEM } else {
                        ThemeMode.values()[position]
                    }
                    TigerApplication.setThemeMode(themeMode)
                }
            }
        }

        return view
    }
}
