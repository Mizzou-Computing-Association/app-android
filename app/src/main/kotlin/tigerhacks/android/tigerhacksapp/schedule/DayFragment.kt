package tigerhacks.android.tigerhacksapp.schedule

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.models.Event
import tigerhacks.android.tigerhacksapp.service.RecyclerFragment

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
class DayFragment(val day: Event.Day) : RecyclerFragment<Event>() {
    override val navId = R.id.navigation_schedule
    override val titleResId = R.string.title_schedule

    override val onRefresh: suspend () -> Unit
        get() = viewModel::refreshEvents

    override val adapter = object : ListAdapter<Event, RecyclerView.ViewHolder>(Event.diff) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = object : RecyclerView.ViewHolder(EventView(parent.context)) {}

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val eventCardView = (holder.itemView as? EventView) ?: return
            eventCardView.setup(getItem(position), position == 0, position == itemCount - 1)
        }
    }

    override fun onViewCreated(layoutView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(layoutView, savedInstanceState)
        liveData = when (day) {
            Event.Day.FRIDAY -> viewModel.fridayEventListLiveData
            Event.Day.SATURDAY -> viewModel.saturdayEventListLiveData
            Event.Day.SUNDAY -> viewModel.sundayEventListLiveData
        }

        recyclerView.scrollBarSize = 0
    }
}