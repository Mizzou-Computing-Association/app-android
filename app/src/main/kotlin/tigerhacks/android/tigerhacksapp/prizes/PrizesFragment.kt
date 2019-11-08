package tigerhacks.android.tigerhacksapp.prizes

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.vertical_recycler_view.emptyTextView
import kotlinx.android.synthetic.main.view_prize.view.favoriteButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.models.FavoritablePrize
import tigerhacks.android.tigerhacksapp.shared.CategoryListAdapter
import tigerhacks.android.tigerhacksapp.shared.CategoryWrapper
import tigerhacks.android.tigerhacksapp.shared.fragments.RecyclerFragment
import tigerhacks.android.tigerhacksapp.shared.views.HeaderView

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
class PrizesFragment : RecyclerFragment<FavoritablePrize>() {
    override val navId = R.id.navigation_prizes
    override val titleResId = R.string.title_prizes

    override val onRefresh
        get() = viewModel!!::refreshPrizes

    override fun onViewCreated(layoutView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(layoutView, savedInstanceState)
        liveData = viewModel?.prizeListLiveData
        statusLiveData = viewModel?.prizeStatusLiveData
    }

    override val adapter = object : CategoryListAdapter<FavoritablePrize>(FavoritablePrize.diff) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return object : RecyclerView.ViewHolder(
                if (viewType == 1) HeaderView(parent.context) else PrizeView(parent.context)
            ) {}
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (val itemView = holder.itemView) {
                is HeaderView -> itemView.setPrizeCategory(getHeaderTitle(position))
                is PrizeView -> {
                    val item = getCatItem(position)
                    itemView.setup(item)
                    itemView.favoriteButton.onToggle = {
                        CoroutineScope(Dispatchers.Main).launch {
                            viewModel?.favoritePrize(item.prize.id, it)
                        }
                    }
                }
            }
        }
    }

    override fun onSubmit(list: List<FavoritablePrize>) {
        if (list.isNotEmpty()) {
            emptyTextView.visibility = View.GONE
        } else {
            emptyTextView.visibility = View.VISIBLE
            emptyTextView.text = getString(R.string.empty_favorite_prizes)
        }
    }

    fun onFavoriteFilter(isChecked: Boolean) {
        liveData = if (isChecked) viewModel?.favoritePrizeListLiveData else viewModel?.prizeListLiveData
    }
}
