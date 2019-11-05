package tigerhacks.android.tigerhacksapp.prizes

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_prize.view.favoriteButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.models.FavoritablePrize
import tigerhacks.android.tigerhacksapp.service.RecyclerFragment

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
class PrizesFragment : RecyclerFragment<FavoritablePrize>() {
    override val navId = R.id.navigation_prizes
    override val titleResId = R.string.title_prizes

    override val onRefresh
        get() = viewModel::refreshPrizes

    override fun onViewCreated(layoutView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(layoutView, savedInstanceState)
        liveData = viewModel.prizeListLiveData
        statusLiveData = viewModel.prizeStatusLiveData
    }

    override val adapter = object : ListAdapter<FavoritablePrize, RecyclerView.ViewHolder>(FavoritablePrize.diff) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = object : RecyclerView.ViewHolder(PrizeView(parent.context)) {}

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val item = getItem(position)
            val hasHeader = position == 0 || getItem(position - 1).prize.prizeType != item.prize.prizeType
            val itemView = (holder.itemView as PrizeView)
            itemView.setup(item, hasHeader)
            itemView.favoriteButton.onToggle = {
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.favoritePrize(item.prize.id, it)
                }
            }
        }
    }
}
