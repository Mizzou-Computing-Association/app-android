package tigerhacks.android.tigerhacksapp.maps

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import tigerhacks.android.tigerhacksapp.R

class MapFragment : Fragment() {
    private var mapView: SubsamplingScaleImageView? = null
    private var selection = 0
        set(value) {
            if (value == field || value < 0 || 3 < value) return
            field = value
            updateSelection()
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutView = inflater.inflate(R.layout.fragment_map, container, false)
        // Inflate the layout for this fragment
        mapView = layoutView.findViewById(R.id.mapView)

        val tabLayout = layoutView.findViewById<TabLayout>(R.id.tabLayout)

        tabLayout.getTabAt(selection)?.select()
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                selection = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        updateSelection()

        return layoutView
    }

    private fun updateSelection() {
        val asset = when (selection) {
            0 -> "floor1map.png"
            1 -> "floor2map.png"
            else -> "floor3map.png"
        }
        mapView?.setImage(ImageSource.asset(asset))
    }
}
