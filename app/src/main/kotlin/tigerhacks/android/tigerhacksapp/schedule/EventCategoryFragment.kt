package tigerhacks.android.tigerhacksapp.schedule

import android.os.Bundle
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tigerhacks.android.tigerhacksapp.service.CategoryFragment

private const val POSITION_KEY = "EVENT_CATEGORY_FRAGMENT_POSITION"

class EventCategoryFragment : CategoryFragment<Event>() {
    companion object {
        fun newInstance(position: Int) = EventCategoryFragment().apply {
            arguments = Bundle().apply {
                putInt(POSITION_KEY, position)
            }
        }
    }

    override val position = lazy { arguments?.getInt(POSITION_KEY) ?: 0 }

    override fun buildAdapter() = object : ListAdapter<Event, RecyclerView.ViewHolder>(Event.diff) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = object : RecyclerView.ViewHolder(EventCardView(parent.context)) {}

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val card = (holder.itemView as EventCardView)
            val event = getItem(position)
            card.setup(event)
            if (position == 0) card.hideTopLine()
            if (position == itemCount - 1) card.hideBottomLine()
        }
    }

    override fun getLiveData(position: Int) = when (position) {
        0 -> viewModel.fridayEventListLiveData
        1 -> viewModel.saturdayEventListLiveData
        else -> viewModel.sundayEventListLiveData
    }

    override fun onRefresh() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.refreshEvents()
        }
    }
}