package tigerhacks.android.tigerhacksapp.schedule

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tigerhacks.android.tigerhacksapp.HomeScreenViewModel
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.service.extensions.observeNotNull

class ScheduleFragment : Fragment() {

    companion object {
        fun newInstance() = ScheduleFragment()
    }

    private lateinit var viewModel: HomeScreenViewModel
    private var observer: Observer<List<Event>>? = null

    private val eventsAdapter = object : ListAdapter<Event, RecyclerView.ViewHolder>(Event.diff) {
        private var totalList: List<Event>? = null
        var currentDay = EasyTime.Day.FRIDAY
            set(value) {
                if (field == value) return
                field = value
                submitList(totalList)
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = object : RecyclerView.ViewHolder(ScheduleCardView(parent.context)) {}

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val card = (holder.itemView as ScheduleCardView)
            val item = getItem(position)
            card.setup(item)
        }

        override fun submitList(list: List<Event>?) {
            totalList = list
            val todayList = list?.filter { it.easyTime.day == currentDay }
            super.submitList(todayList)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders.of(this).get(HomeScreenViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val layoutView = inflater.inflate(R.layout.fragment_schedule, container, false)
        val tabLayout = layoutView.findViewById<TabLayout>(R.id.tabLayout)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> eventsAdapter.currentDay = EasyTime.Day.FRIDAY
                    1 -> eventsAdapter.currentDay = EasyTime.Day.SATURDAY
                    2 -> eventsAdapter.currentDay = EasyTime.Day.SUNDAY
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        val recyclerView = layoutView.findViewById<RecyclerView>(R.id.scheduleRecyclerView)
        recyclerView.adapter = eventsAdapter

        observer = viewModel.eventListLiveData.observeNotNull(this, eventsAdapter::submitList)

        return layoutView
    }

    override fun onDestroyView() {
        observer?.let(viewModel.eventListLiveData::removeObserver)
        super.onDestroyView()
    }
}
