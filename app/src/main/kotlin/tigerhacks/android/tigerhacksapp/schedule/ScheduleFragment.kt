package tigerhacks.android.tigerhacksapp.schedule

import com.google.android.material.tabs.TabLayout
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tigerhacks.android.tigerhacksapp.service.RecyclerFragment

class ScheduleFragment : RecyclerFragment<Event>() {
    companion object {
        fun newInstance() = ScheduleFragment()
    }

    enum class Day {
        FRIDAY, SATURDAY, SUNDAY
    }

    private var currentDay = Day.FRIDAY

    override val onRefresh
        get() = viewModel::refreshEvents

    override fun initSetup() {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> syncDayState(Day.FRIDAY)
                    1 -> syncDayState(Day.SATURDAY)
                    else -> syncDayState(Day.SUNDAY)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        recyclerView.scrollBarSize = 0

        syncDayState(currentDay, true)
        statusLiveData = viewModel.eventStatusLiveData
        if (currentDay.ordinal != tabLayout.selectedTabPosition) {
            tabLayout.getTabAt(currentDay.ordinal)?.select()
        }
    }

    override val adapter = object : ListAdapter<Event, RecyclerView.ViewHolder>(Event.diff) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = object : RecyclerView.ViewHolder(EventView(parent.context)) {}

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val eventCardView = (holder.itemView as? EventView) ?: return
            eventCardView.setup(getItem(position), position == 0, position == itemCount - 1)
        }
    }

    private fun syncDayState(day: Day, bypass: Boolean = false) {
        if (currentDay == day && !bypass) return
        currentDay = day
        resetObserver()

        liveData = when (currentDay) {
            Day.FRIDAY -> viewModel.fridayEventListLiveData
            Day.SATURDAY -> viewModel.saturdayEventListLiveData
            Day.SUNDAY -> viewModel.sundayEventListLiveData
        }
    }
}

