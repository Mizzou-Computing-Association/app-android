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
    companion object {
        fun newInstance() = MapFragment()
    }

    private var mapView: SubsamplingScaleImageView? = null
    private var selection = 0
        set(value) {
            if (value == field || value < 0 || 3 < value) return
            field = value
            updateSelection()
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mapView = SubsamplingScaleImageView(inflater.context)

        val tabLayout = activity?.findViewById<TabLayout>(R.id.tabLayout) ?: throw Exception("")
        //add button onclick events. Handles button visuals and map changing
        tabLayout.getTabAt(selection)?.select()
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                selection = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        updateSelection()

        return mapView
    }

    private fun updateSelection() {
        val drawableRes = when (selection) {
            0 -> R.drawable.floor1map
            1 -> R.drawable.floor2map
            else -> R.drawable.floor3map
        }
        mapView?.setImage(ImageSource.resource(drawableRes))
    }
}
