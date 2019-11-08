package tigerhacks.android.tigerhacksapp.schedule

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.vertical_recycler_view.view.emptyTextView
import kotlinx.android.synthetic.main.vertical_recycler_view.view.progressBar
import kotlinx.android.synthetic.main.vertical_recycler_view.view.recyclerView
import kotlinx.android.synthetic.main.vertical_recycler_view.view.swipeRefreshLayout
import kotlinx.android.synthetic.main.view_event.view.favoriteButton
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.models.FavoritableEvent

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
class DayView @JvmOverloads constructor (
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {
    class Adapter : ListAdapter<FavoritableEvent, RecyclerView.ViewHolder>(FavoritableEvent.diff) {
        var onFavorite: ((FavoritableEvent, Boolean) -> Unit)? = null
        var prevItemCount = 0

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = object : RecyclerView.ViewHolder(EventView(parent.context)) {}

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val eventCardView = (holder.itemView as? EventView) ?: return
            val item = getItem(position)
            eventCardView.setup(item, position == 0, position == itemCount - 1)
            eventCardView.favoriteButton.onToggle = {
                onFavorite?.invoke(item, it)
            }
        }

        override fun submitList(list: List<FavoritableEvent>?) {
            prevItemCount = itemCount
            super.submitList(list)
        }

        fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            if (prevItemCount == 0) return
            notifyItemChanged(0)
            if (positionStart > 0) notifyItemChanged(positionStart - 1)
        }

        fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            if (prevItemCount == 0) return
            notifyItemChanged(0)
            if (positionStart > 0) notifyItemChanged(positionStart - 1)
            val temp = positionStart + itemCount
            if (temp > 0) notifyItemChanged(temp)
        }
    }

    val adapter = Adapter()
    val observer: Observer<List<FavoritableEvent>>

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        LayoutInflater.from(context).inflate(R.layout.vertical_recycler_view, this, true)
        recyclerView.scrollBarSize = 0
        recyclerView.adapter = adapter

        adapter.registerAdapterDataObserver (
            object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) = adapter.onItemRangeInserted(positionStart, itemCount)
                override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) = adapter.onItemRangeRemoved(positionStart, itemCount)
            }
        )

        observer = Observer {
            if (it == null) return@Observer
            progressBar.visibility = View.GONE
            swipeRefreshLayout.isRefreshing = false
            adapter.submitList(it)
            if (it.isEmpty()) {
                emptyTextView.visibility = View.VISIBLE
                emptyTextView.text = context.getString(R.string.empty_favorite_events)
            } else {
                emptyTextView.visibility = View.GONE
            }
        }
    }
}