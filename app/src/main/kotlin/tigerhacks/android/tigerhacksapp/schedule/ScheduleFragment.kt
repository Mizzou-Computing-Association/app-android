package tigerhacks.android.tigerhacksapp.schedule

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tigerhacks.android.tigerhacksapp.HomeScreenViewModel
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.service.database.TigerHacksDatabase
import tigerhacks.android.tigerhacksapp.service.extensions.observeNotNull

class ScheduleFragment : Fragment() {
    companion object {
        fun newInstance() = ScheduleFragment()
    }

    enum class Day {
        FRIDAY, SATURDAY, SUNDAY
    }

    private lateinit var viewModel: HomeScreenViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var adapter: ListAdapter<Event, RecyclerView.ViewHolder>
    private var currentDay = Day.FRIDAY

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutView = inflater.inflate(R.layout.vertical_recycler_view, container, false)
        val recyclerView = layoutView.findViewById<RecyclerView>(R.id.recyclerView)
        swipeRefreshLayout = layoutView.findViewById(R.id.swipeRefreshLayout)

        adapter = object : ListAdapter<Event, RecyclerView.ViewHolder>(Event.diff) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = object : RecyclerView.ViewHolder(EventView(parent.context)) {}

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val eventCardView = (holder.itemView as? EventView) ?: return
                eventCardView.setup(getItem(position), position == 0, position == itemCount - 1)
            }
        }

        recyclerView.adapter = adapter

        viewModel = activity?.run {
            val db = TigerHacksDatabase.getDatabase(this.applicationContext)
            ViewModelProviders.of(this, HomeScreenViewModel.FACTORY(db)).get(HomeScreenViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        val tabLayout = activity?.findViewById<TabLayout>(R.id.tabLayout) ?: throw Exception("")

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

        syncDayState(currentDay, true)
        if (currentDay.ordinal != tabLayout.selectedTabPosition) {
            tabLayout.getTabAt(currentDay.ordinal)?.select()
        }

        swipeRefreshLayout.setOnRefreshListener {
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.refreshEvents()
            }
        }

        return layoutView
    }

    override fun onPause() {
        super.onPause()
        resetObservers()
    }

    private fun syncDayState(day: Day, bypass: Boolean = false) {
        if (currentDay == day && !bypass) return
        currentDay = day
        resetObservers()

        val liveData = when (currentDay) {
            Day.FRIDAY -> viewModel.fridayEventListLiveData
            Day.SATURDAY -> viewModel.saturdayEventListLiveData
            Day.SUNDAY -> viewModel.sundayEventListLiveData
        }

        liveData.observeNotNull(this) {
            swipeRefreshLayout.isRefreshing = false
            adapter.submitList(it)
        }
    }

    private fun resetObservers() {
        viewModel.fridayEventListLiveData.removeObservers(this)
        viewModel.saturdayEventListLiveData.removeObservers(this)
        viewModel.sundayEventListLiveData.removeObservers(this)
    }
}

