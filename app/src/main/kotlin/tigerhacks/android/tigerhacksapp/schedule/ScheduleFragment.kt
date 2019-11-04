package tigerhacks.android.tigerhacksapp.schedule

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.models.Event
import tigerhacks.android.tigerhacksapp.service.BaseFragment

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
class ScheduleFragment : BaseFragment(R.layout.fragment_schedule) {
    override val navId = R.id.navigation_schedule
    override val titleResId = R.string.title_schedule

    override fun onViewCreated(layout: View, savedInstanceState: Bundle?) {
        super.onViewCreated(layout, savedInstanceState)
        val viewPager = layout.findViewById<ViewPager>(R.id.mainViewPager)
        val tabLayout = layout.findViewById<TabLayout>(R.id.tabLayout)

        tabLayout.setupWithViewPager(viewPager)

        viewPager.adapter = object : FragmentPagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int) = when (position) {
                0 -> DayFragment(Event.Day.FRIDAY)
                1 -> DayFragment(Event.Day.SATURDAY)
                else -> DayFragment(Event.Day.SUNDAY)
            }

            override fun getCount() = 3

            override fun getPageTitle(position: Int) = when (position) {
                0 -> getString(R.string.friday)
                1 -> getString(R.string.saturday)
                else -> getString(R.string.sunday)
            }
        }
    }
}

