package tigerhacks.android.tigerhacksapp.prizes

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.models.Prize
import tigerhacks.android.tigerhacksapp.service.RecyclerFragment

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
class PrizesFragment : RecyclerFragment<Prize>() {
    override val navId = R.id.navigation_prizes
    override val titleResId = R.string.title_prizes

    override val onRefresh
        get() = viewModel::refreshPrizes

    override fun initSetup() {
        liveData = viewModel.prizeListLiveData
        statusLiveData = viewModel.prizeStatusLiveData
    }

    override val adapter = object : ListAdapter<Prize, RecyclerView.ViewHolder>(Prize.diff) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val itemView = if (viewType == 0) {
                PrizeHeaderView(parent.context)
            } else {
                PrizeView(parent.context)
            }
            return object : RecyclerView.ViewHolder(itemView) {}
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val itemView = holder.itemView
            if (itemView is PrizeView) {
                itemView.setup(getItem(position))
            } else {
                (itemView as? PrizeHeaderView)?.setPrizeCategory(getItem(position).prizeType)
            }
        }

        override fun getItemViewType(position: Int): Int {
            val item = getItem(position) ?: return 1
            return if (item.id.contains(Prize.HEADER_KEY)) 0 else 1
        }
    }
}
