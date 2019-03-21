package tigerhacks.android.tigerhacksapp.maps

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsibbold.zoomage.ZoomageView
import tigerhacks.android.tigerhacksapp.HomeScreenActivity
import tigerhacks.android.tigerhacksapp.R

class MapFragment : Fragment() {
    companion object {
        fun newInstance() = MapFragment()
    }

    private var home: HomeScreenActivity? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val layoutView = inflater.inflate(R.layout.fragment_map, container, false)

        val tabLayout = layoutView.findViewById<TabLayout>(R.id.tabLayout)
        val mapView = layoutView.findViewById<ZoomageView>(R.id.mapView)

        //add button onclick events. Handles button visuals and map changing
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val drawableRes = when (tab.position) {
                    0 -> R.drawable.floor1map
                    1 -> R.drawable.floor2map
                    2 -> R.drawable.floor3map
                    else -> null
                }
                drawableRes?.let {
                    mapView.setImageDrawable(context!!.getDrawable(it))
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        home = activity as HomeScreenActivity?

        //initial setup of event list
        //return fragment layout to main activity
        return layoutView
    }
}
