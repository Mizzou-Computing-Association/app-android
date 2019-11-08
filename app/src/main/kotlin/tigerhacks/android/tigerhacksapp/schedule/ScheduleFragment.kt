package tigerhacks.android.tigerhacksapp.schedule

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
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
import tigerhacks.android.tigerhacksapp.models.FavoritableEvent
import tigerhacks.android.tigerhacksapp.shared.fragments.BaseFragment

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
class ScheduleFragment : BaseFragment(R.layout.fragment_schedule) {
    override val navId = R.id.navigation_schedule
    override val titleResId = R.string.title_schedule

    private var isFavorFiltering = false
    private var viewModel: HomeScreenViewModel? = null

    private var fridayObserver: Observer<List<FavoritableEvent>>? = null
    private var saturdayObserver: Observer<List<FavoritableEvent>>? = null
    private var sundayObserver: Observer<List<FavoritableEvent>>? = null

    override fun onViewCreated(layout: View, savedInstanceState: Bundle?) {
        super.onViewCreated(layout, savedInstanceState)

        tabLayout.setupWithViewPager(mainViewPager)

        val home = activity as HomeScreenActivity
        viewModel = home.viewModel

        mainViewPager.adapter = object : PagerAdapter() {
            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val dayView = DayView(container.context)
                when(position) {
                    0 -> fridayObserver = dayView.observer
                    1 -> saturdayObserver = dayView.observer
                    2 -> sundayObserver = dayView.observer
                }

                setupDay(position)

                dayView.adapter.onFavorite = { item: FavoritableEvent, state: Boolean ->
                    CoroutineScope(Dispatchers.Main).launch {
                        viewModel?.favoriteEvent(item.event.id, state)
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

    private fun resetDays() {
        setupDay(0)
        setupDay(1)
        setupDay(2)
    }

    private fun setupDay(dayNum: Int) {
        //Reset day observers
        val normalLiveData = when (dayNum) {
            0 -> viewModel?.fridayEventListLiveData
            1 -> viewModel?.saturdayEventListLiveData
            else -> viewModel?.sundayEventListLiveData
        }
        normalLiveData?.removeObservers(this)

        val favoriteLiveData = when (dayNum) {
            0 -> viewModel?.favoriteFridayEventListLiveData
            1 -> viewModel?.favoriteSaturdayEventListLiveData
            else -> viewModel?.favoriteSundayEventListLiveData
        }

        favoriteLiveData?.removeObservers(this)

        // Get correct day observer
        val observer = when (dayNum) {
            0 -> fridayObserver
            1 -> saturdayObserver
            else -> sundayObserver
        }

        // Get live data to observe
        val liveData = if (isFavorFiltering) favoriteLiveData else normalLiveData
        if (observer != null) liveData?.observe(this, observer)
    }

    fun onFavoriteFilter(isChecked: Boolean) {
        isFavorFiltering = isChecked
        resetDays()
    }
}

