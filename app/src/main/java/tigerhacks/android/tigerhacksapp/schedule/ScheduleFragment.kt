package tigerhacks.android.tigerhacksapp.schedule

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_schedule.progressBar
import tigerhacks.android.tigerhacksapp.HomeScreenActivity
import tigerhacks.android.tigerhacksapp.R
import java.util.ArrayList

class ScheduleFragment : Fragment() {

    companion object {
        fun newInstance() = ScheduleFragment()
    }

    private var currentDay = EasyTime.Day.FRIDAY
        set(value) {
            if (field == value) return
            field = value
            addDayEvents(cardList)
        }

    private var cardList: ArrayList<ScheduleItem>? = null
    private lateinit var home: HomeScreenActivity

    private val eventsAdapter = object : ListAdapter<ScheduleItem, RecyclerView.ViewHolder>(ScheduleItem.diff) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = object : RecyclerView.ViewHolder(ScheduleCardView(parent.context)) {}

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val card = (holder.itemView as ScheduleCardView)
            val item = getItem(position)
            card.setup(item)
        }
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
                    0 -> currentDay = EasyTime.Day.FRIDAY
                    1 -> currentDay = EasyTime.Day.SATURDAY
                    2 -> currentDay = EasyTime.Day.SUNDAY
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        home = activity as HomeScreenActivity

        val recyclerView = layoutView.findViewById<RecyclerView>(R.id.scheduleRecyclerView)
        recyclerView.adapter = eventsAdapter

        return layoutView
    }

    override fun onStart() {
        super.onStart()
        home.onFragmentsReady()
    }

    fun loadSchedule(scheduleList: ScheduleItemList?) {
        if (scheduleList == null) {
            return
        }
        progressBar.visibility = View.GONE
        cardList = scheduleList.schedule as ArrayList<ScheduleItem>
        addDayEvents(cardList)
    }

    private fun addDayEvents(list: ArrayList<ScheduleItem>?) {
        if (list == null) return
        val todayList = list.filter { it.easyTime.day == currentDay }
        eventsAdapter.submitList(todayList)
    }
}
