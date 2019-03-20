package tigerhacks.android.tigerhacksapp.sponsors

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import tigerhacks.android.tigerhacksapp.HomeScreenActivity
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.service.extensions.dpToPx

class SponsorsFragment : Fragment() {

    companion object {
        fun newInstance() = SponsorsFragment()
    }

    private var layout: View? = null
    private var home: HomeScreenActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_sponsors, container, false)
        home = activity as HomeScreenActivity?
        return layout
    }

    override fun onStart() {
        super.onStart()
        home!!.onFragmentsReady()
    }

    fun loadSponsorData(list: SponsorList?) {
        layout?.let { immutableLayout ->
            val pLayout = immutableLayout.findViewById<LinearLayout>(R.id.platinumLayout)
            val gLayout = immutableLayout.findViewById<LinearLayout>(R.id.goldLayout)
            val sLayout = immutableLayout.findViewById<LinearLayout>(R.id.silverLayout)
            val bLayout = immutableLayout.findViewById<LinearLayout>(R.id.bronzeLayout)

            list?.sponsors?.forEach { sponsor ->
                val image = ImageView(context)
                val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100)
                layoutParams.height = context!!.dpToPx(100)
                layoutParams.setMargins(0, 32, 0, 32)
                image.layoutParams = layoutParams
                image.scaleType = ImageView.ScaleType.FIT_CENTER
                Glide.with(image).load(sponsor.image).into(image)
                when (sponsor.level) {
                    "Platinum" -> pLayout.addView(image)
                    "Gold" -> gLayout.addView(image)
                    "Silver" -> sLayout.addView(image)
                    "Bronze" -> bLayout.addView(image)
                }

                image.setOnClickListener {
                    startActivity(
                        SponsorDetailActivity.newInstance(
                            context!!,
                            sponsor
                        )
                    )
                }
            }
        }

    }
}
