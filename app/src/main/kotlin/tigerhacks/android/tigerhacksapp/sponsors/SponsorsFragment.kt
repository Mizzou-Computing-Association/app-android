package tigerhacks.android.tigerhacksapp.sponsors

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tigerhacks.android.tigerhacksapp.HomeScreenViewModel
import tigerhacks.android.tigerhacksapp.service.database.TigerHacksDatabase
import tigerhacks.android.tigerhacksapp.service.extensions.observeNotNull
import tigerhacks.android.tigerhacksapp.sponsors.models.Sponsor
import tigerhacks.android.tigerhacksapp.sponsors.views.SponsorCardView
import tigerhacks.android.tigerhacksapp.sponsors.views.SponsorHeader

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
class SponsorsFragment : Fragment() {

    companion object {
        fun newInstance() = SponsorsFragment()
    }

    private var viewModel: HomeScreenViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            val db = TigerHacksDatabase.getDatabase(applicationContext)
            ViewModelProviders.of(this, HomeScreenViewModel.FACTORY(db)).get(HomeScreenViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val context = context ?: return null
        val layout = SwipeRefreshLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        val recyclerView = RecyclerView(context).apply {
            layoutManager = LinearLayoutManager(context)
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }

        layout.addView(recyclerView)

        val adapter = object : ListAdapter<Sponsor, RecyclerView.ViewHolder>(Sponsor.diff) {
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

        recyclerView.adapter = adapter

        layout.setOnRefreshListener {
            CoroutineScope(Dispatchers.Main).launch {
                viewModel?.refreshSponsors()
            }
        }

        viewModel?.sponsorListLiveData?.observeNotNull(this) {
            adapter.submitList(it)
            layout.isRefreshing = false
        }

        return layout
    }
}
