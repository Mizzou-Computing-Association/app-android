package tigerhacks.android.tigerhacksapp.sponsors

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.service.RecyclerFragment
import tigerhacks.android.tigerhacksapp.models.Sponsor

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
class SponsorsFragment : RecyclerFragment<Sponsor>() {
    override val navId = R.id.navigation_sponsors
    override val titleResId = R.string.title_sponsors

    override fun onViewCreated(layoutView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(layoutView, savedInstanceState)
        liveData = viewModel.sponsorListLiveData
        statusLiveData = viewModel.sponsorStatusLiveData
    }

    override val onRefresh
        get() = viewModel::refreshSponsors

    override val adapter = SponsorAdapter()
}

class SponsorAdapter: ListAdapter<Sponsor, RecyclerView.ViewHolder>(Sponsor.diff) {

    override fun getItemCount() = if (super.getItemCount() > 0) super.getItemCount() + 1 else 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = object : RecyclerView.ViewHolder(
        if(viewType == 0) {
            SponsorCardView(parent.context)
        } else {
            AllMentorsCardView(parent.context)
        }
    ) {}

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemView = holder.itemView
        if (itemView is SponsorCardView) {
            val item = getItem(position)
            val hasHeader = position == 0 || getItem(position - 1).level != item.level

            itemView.setSponsor(item, hasHeader)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == itemCount - 1) return 1
        return super.getItemViewType(position)
    }
}
