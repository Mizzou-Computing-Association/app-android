package tigerhacks.android.tigerhacksapp.sponsors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.service.RecyclerFragment
import tigerhacks.android.tigerhacksapp.models.Sponsor
import tigerhacks.android.tigerhacksapp.sponsors.views.SponsorCardView
import tigerhacks.android.tigerhacksapp.sponsors.views.SponsorHeader

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

    override val adapter = object : ListAdapter<Sponsor, RecyclerView.ViewHolder>(Sponsor.diff) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return if (viewType == 0) { //Header
                object : RecyclerView.ViewHolder(SponsorHeader(parent.context)) {}
            } else {
                object : RecyclerView.ViewHolder(SponsorCardView(parent.context)) {}
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val itemView = holder.itemView
            val item = getItem(position)
            if (itemView is SponsorHeader) {
                itemView.setSponsorLevel(item.level)
            } else {
                val card = (itemView as? SponsorCardView)
                card?.setSponsor(item)
                card?.setOnClickListener {
                    val context = context ?: return@setOnClickListener
                    startActivity(SponsorDetailActivity.newInstance(context, item))
                }
            }
        }

        override fun getItemViewType(position: Int): Int {
            return when (getItem(position).name) {
                "${Sponsor.HEADER_KEY}0", "${Sponsor.HEADER_KEY}1", "${Sponsor.HEADER_KEY}2", "${Sponsor.HEADER_KEY}3" -> 0
                else -> 1
            }
        }
    }
}
