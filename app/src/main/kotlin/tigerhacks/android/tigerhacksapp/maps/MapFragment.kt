package tigerhacks.android.tigerhacksapp.maps

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.service.BaseFragment

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
class MapFragment : BaseFragment() {
    override val navId = R.id.navigation_map
    override val titleResId = R.string.title_map

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
            0 -> "floor1map.webp"
            1 -> "floor2map.webp"
            else -> "floor3map.webp"
        }
        mapView?.setImage(ImageSource.asset(asset))
    }
}
