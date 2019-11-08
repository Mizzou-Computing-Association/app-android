package tigerhacks.android.tigerhacksapp.sponsors

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.shared.fragments.RecyclerFragment
import tigerhacks.android.tigerhacksapp.models.Sponsor
import tigerhacks.android.tigerhacksapp.shared.CategoryListAdapter
import tigerhacks.android.tigerhacksapp.shared.views.HeaderView

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
class SponsorsFragment : RecyclerFragment<Sponsor>() {
    override val navId = R.id.navigation_sponsors
    override val titleResId = R.string.title_sponsors

    override fun onViewCreated(layoutView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(layoutView, savedInstanceState)
        liveData = viewModel?.sponsorListLiveData
        statusLiveData = viewModel?.sponsorStatusLiveData
    }

    override val onRefresh
        get() = viewModel!!::refreshSponsors

    override val adapter = SponsorAdapter()
}

class SponsorAdapter: CategoryListAdapter<Sponsor>(Sponsor.diff) {

    override fun getItemCount() = if (super.getItemCount() > 0) super.getItemCount() + 1 else 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = object : RecyclerView.ViewHolder(
        when (viewType) {
            0 -> SponsorCardView(parent.context)
            2 -> AllMentorsCardView(parent.context)
            else -> HeaderView(parent.context)
        }
    ) {}

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val itemView = holder.itemView) {
            is SponsorCardView -> {
                val item = getCatItem(position)
                itemView.setSponsor(item)
            }
            is HeaderView -> {
                val level = getHeaderTitle(position).toInt()
                itemView.setSponsorLevel(level)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == itemCount - 1) return 2
        return super.getItemViewType(position)
    }
}
