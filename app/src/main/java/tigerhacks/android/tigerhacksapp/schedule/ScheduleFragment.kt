package tigerhacks.android.tigerhacksapp.schedule

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tigerhacks.android.tigerhacksapp.R

class ScheduleFragment : Fragment() {
    companion object {
        fun newInstance() = ScheduleFragment()
    }

    private var tabLayout: TabLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutView = inflater.inflate(R.layout.fragment_schedule, container, false)
        val scheduleViewPager = layoutView.findViewById<ViewPager>(R.id.scheduleViewPager)

        val fridayFragment = EventCategoryFragment.newInstance(0)
        val saturdayFragment = EventCategoryFragment.newInstance(1)
        val sundayFragment = EventCategoryFragment.newInstance(2)

        scheduleViewPager?.apply {
            adapter = object : FragmentPagerAdapter(childFragmentManager) {
                override fun getItem(position: Int): Fragment {
                    return when (position) {
                        0 -> fridayFragment
                        1 -> saturdayFragment
                        else -> sundayFragment
                    }
                }

                override fun getCount() = 3

                override fun getPageTitle(position: Int): CharSequence? {
                    return when (position) {
                        0 -> "Friday"
                        1 -> "Saturday"
                        else -> "Sunday"
                    }
                }
            }
            offscreenPageLimit = 2
        }

        tabLayout = layoutView.findViewById(R.id.tabLayout)
        if (scheduleViewPager != null) {
            tabLayout?.setupWithViewPager(scheduleViewPager)
        }

        return layoutView
    }
}

