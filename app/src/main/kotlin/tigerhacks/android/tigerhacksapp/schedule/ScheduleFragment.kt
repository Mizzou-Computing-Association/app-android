package tigerhacks.android.tigerhacksapp.schedule

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.fragment_schedule.mainViewPager
import kotlinx.android.synthetic.main.fragment_schedule.tabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tigerhacks.android.tigerhacksapp.HomeScreenActivity
import tigerhacks.android.tigerhacksapp.HomeScreenViewModel
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.service.BaseFragment
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
class ScheduleFragment : BaseFragment(R.layout.fragment_schedule) {
    override val navId = R.id.navigation_schedule
    override val titleResId = R.string.title_schedule

    private var viewModel: HomeScreenViewModel? = null

    override fun onViewCreated(layout: View, savedInstanceState: Bundle?) {
        super.onViewCreated(layout, savedInstanceState)

        tabLayout.setupWithViewPager(mainViewPager)

        val home = activity as HomeScreenActivity
        viewModel = home.viewModel
        val fridayLiveData = home.viewModel.fridayEventListLiveData
        val saturdayLiveData = home.viewModel.saturdayEventListLiveData
        val sundayLiveData = home.viewModel.sundayEventListLiveData

        mainViewPager.adapter = object : PagerAdapter() {
            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val dayView = when (position) {
                    0 -> DayView(home).apply {
                        setDay(this@ScheduleFragment, fridayLiveData)
                    }
                    1 -> DayView(home).apply {
                        setDay(this@ScheduleFragment, saturdayLiveData)
                    }
                    else -> DayView(home).apply {
                        setDay(this@ScheduleFragment, sundayLiveData)
                    }
                }

                dayView.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout).setOnRefreshListener(::refresh)
                container.addView(dayView)
                return dayView
            }

            override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
                container.removeView(obj as View)
            }

            override fun isViewFromObject(view: View, obj: Any): Boolean {
                return view == obj
            }

            override fun getCount() = 3

            override fun getPageTitle(position: Int) = when (position) {
                0 -> getString(R.string.friday)
                1 -> getString(R.string.saturday)
                else -> getString(R.string.sunday)
            }
        }
    }

    private fun refresh() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel?.refreshEvents()
        }
    }
}

