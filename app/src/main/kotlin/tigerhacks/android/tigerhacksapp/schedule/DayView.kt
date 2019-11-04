package tigerhacks.android.tigerhacksapp.schedule

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.vertical_recycler_view.view.progressBar
import kotlinx.android.synthetic.main.vertical_recycler_view.view.recyclerView
import kotlinx.android.synthetic.main.vertical_recycler_view.view.swipeRefreshLayout
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.models.Event
import tigerhacks.android.tigerhacksapp.service.extensions.observeNotNull

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
class DayView @JvmOverloads constructor (
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {
    private val adapter = object : ListAdapter<Event, RecyclerView.ViewHolder>(Event.diff) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = object : RecyclerView.ViewHolder(EventView(parent.context)) {}

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val eventCardView = (holder.itemView as? EventView) ?: return
            eventCardView.setup(getItem(position), position == 0, position == itemCount - 1)
        }
    }

    private var liveData: LiveData<List<Event>>? = null

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        LayoutInflater.from(context).inflate(R.layout.vertical_recycler_view, this, true)
        recyclerView.scrollBarSize = 0
        recyclerView.adapter = adapter
    }

    fun setDay(fragment: Fragment, liveData: LiveData<List<Event>>) {
        this.liveData?.removeObservers(fragment)
        liveData.observeNotNull(fragment) {
            progressBar.visibility = View.GONE
            swipeRefreshLayout.isRefreshing = false
            adapter.submitList(it)
        }
        this.liveData = liveData
    }
}