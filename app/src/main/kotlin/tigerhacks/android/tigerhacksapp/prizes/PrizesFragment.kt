package tigerhacks.android.tigerhacksapp.prizes

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
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

private enum class PrizeCategory {
    DEVELOPER,
    BEGINNER,
    MISC
}

class PrizesFragment : Fragment() {
    companion object {
        fun newInstance() = PrizesFragment()
    }

    private lateinit var viewModel: HomeScreenViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var adapter: ListAdapter<Prize, RecyclerView.ViewHolder>
    private var currentCategory = PrizeCategory.DEVELOPER

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutView = inflater.inflate(R.layout.fragment_prizes, container, false)
        val recyclerView = layoutView.findViewById<RecyclerView>(R.id.recyclerView)
        swipeRefreshLayout = layoutView.findViewById(R.id.swipeRefreshLayout)

        adapter = object : ListAdapter<Prize, RecyclerView.ViewHolder>(Prize.diff) {
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
                    (itemView as? PrizeHeaderView)?.setCategoryLevel(getItem(position).prizeType)
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

        val tabLayout = layoutView.findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> syncCategoryState(PrizeCategory.DEVELOPER)
                    1 -> syncCategoryState(PrizeCategory.BEGINNER)
                    else -> syncCategoryState(PrizeCategory.MISC)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        syncCategoryState(currentCategory, true)
        if (currentCategory.ordinal != tabLayout.selectedTabPosition) {
            tabLayout.getTabAt(currentCategory.ordinal)?.select()
        }

        swipeRefreshLayout.setOnRefreshListener {
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.refreshPrizes()
            }
        }

        return layoutView
    }

    override fun onPause() {
        super.onPause()
        resetObservers()
    }

    private fun syncCategoryState(category: PrizeCategory, bypass: Boolean = false) {
        if (currentCategory == category && !bypass) return
        currentCategory = category
        resetObservers()

        val liveData = when (currentCategory) {
            PrizeCategory.DEVELOPER -> viewModel.developerPrizeListLiveData
            PrizeCategory.BEGINNER -> viewModel.beginnerPrizeListLiveData
            PrizeCategory.MISC -> viewModel.miscPrizeListLiveData
        }

        liveData.observeNotNull(this) {
            swipeRefreshLayout.isRefreshing = false
            adapter.submitList(it)
        }
    }

    private fun resetObservers() {
        viewModel.developerPrizeListLiveData.removeObservers(this)
        viewModel.miscPrizeListLiveData.removeObservers(this)
        viewModel.beginnerPrizeListLiveData.removeObservers(this)
    }
}
