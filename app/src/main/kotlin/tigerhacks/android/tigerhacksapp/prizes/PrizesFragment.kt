package tigerhacks.android.tigerhacksapp.prizes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tigerhacks.android.tigerhacksapp.HomeScreenViewModel
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.service.database.TigerHacksDatabase
import tigerhacks.android.tigerhacksapp.service.extensions.observeNotNull

class PrizesFragment : Fragment() {
    companion object {
        fun newInstance() = PrizesFragment()
    }

    private lateinit var viewModel: HomeScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutView = inflater.inflate(R.layout.vertical_recycler_view, container, false)
        val recyclerView = layoutView.findViewById<RecyclerView>(R.id.recyclerView)
        val swipeRefreshLayout = layoutView.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)

        val adapter = object : ListAdapter<Prize, RecyclerView.ViewHolder>(Prize.diff) {
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

        recyclerView.adapter = adapter

        viewModel = activity?.run {
            val db = TigerHacksDatabase.getDatabase(this.applicationContext)
            ViewModelProviders.of(this, HomeScreenViewModel.FACTORY(db)).get(HomeScreenViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        viewModel.prizeListLiveData.observeNotNull(this) {
            swipeRefreshLayout.isRefreshing = false
            adapter.submitList(it)
        }

        swipeRefreshLayout.setOnRefreshListener {
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.refreshPrizes()
            }
        }

        return layoutView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.prizeListLiveData.removeObservers(this)
    }
}
