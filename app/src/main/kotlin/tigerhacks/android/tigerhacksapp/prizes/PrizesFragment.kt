package tigerhacks.android.tigerhacksapp.prizes

import android.os.Bundle
import android.view.View
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

    override fun onViewCreated(layoutView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(layoutView, savedInstanceState)
        liveData = viewModel.prizeListLiveData
        statusLiveData = viewModel.prizeStatusLiveData
    }

    override val adapter = object : ListAdapter<Prize, RecyclerView.ViewHolder>(Prize.diff) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = object : RecyclerView.ViewHolder(PrizeView(parent.context)) {}

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val item = getItem(position)
            val hasHeader = position == 0 || getItem(position - 1).prizeType != item.prizeType
            (holder.itemView as PrizeView).setup(item, hasHeader)
        }
    }
}
