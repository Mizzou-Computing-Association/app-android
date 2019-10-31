package tigerhacks.android.tigerhacksapp.maps

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.google.android.material.navigation.NavigationView
import tigerhacks.android.tigerhacksapp.HomeScreenActivity
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
        val toolbarContainer = inflater.inflate(R.layout.toolbar_with_tab, container, false)
        // Inflate the layout for this fragment
        mapView = SubsamplingScaleImageView(inflater.context).apply {
            layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
        }
        val layout = toolbarContainer.findViewById<ConstraintLayout>(R.id.container)
        layout.addView(mapView)

        val tabLayout = toolbarContainer.findViewById<TabLayout>(R.id.tabLayout) ?: throw Exception("")
        //add button onclick events. Handles button visuals and map changing


        if (tabLayout.tabCount == 0) {
            val list = listOf(R.string.floor_1, R.string.floor_2, R.string.floor_3)
            for (res in list) {
                val tab = tabLayout.newTab()
                tab.text = getString(res)
                tabLayout.addTab(tab)
            }
        }

        tabLayout.getTabAt(selection)?.select()
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                selection = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        val activity = activity as HomeScreenActivity
        val toolbar = toolbarContainer.findViewById<Toolbar>(R.id.toolbar)

        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navController = activity.findNavController(R.id.navHostFragment)
        activity.setupActionBarWithNavController(navController, activity.findViewById<DrawerLayout>(R.id.drawerLayout))
        activity.findViewById<NavigationView>(R.id.navigationView).setupWithNavController(navController)

        updateSelection()

        return toolbarContainer
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
