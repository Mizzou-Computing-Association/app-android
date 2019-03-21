package tigerhacks.android.tigerhacksapp.prizes

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tigerhacks.android.tigerhacksapp.HomeScreenViewModel
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.service.extensions.observeNotNull
import tigerhacks.android.tigerhacksapp.sponsors.Sponsor

class PrizesFragment : Fragment() {
    companion object {
        fun newInstance() = PrizesFragment()
    }

    private var tabLayout: TabLayout? = null
    private var sponsorList: List<Sponsor>? = null

    private var prizeAdapter = object : ListAdapter<Prize, RecyclerView.ViewHolder>(Prize.diff) {
        private var totalList: List<Prize>? = null
        var currentType: PrizeCardView.Type = PrizeCardView.Type.MAIN
            set(value) {
                field = value
                submitList(totalList)
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = object : RecyclerView.ViewHolder(PrizeCardView(parent.context)) {}

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val card = (holder.itemView as PrizeCardView)
            val prize = getItem(position)
            card.setup(prize, sponsorList)
        }

        override fun submitList(list: List<Prize>?) {
            totalList = list
            val filteredList = list?.filter { it.prizeType.toPrizeType() == currentType }
            super.submitList(filteredList)
        }
    }

    private lateinit var viewModel: HomeScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(HomeScreenViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        viewModel.prizeListLiveData.observeNotNull(this) { list ->
            prizeAdapter.submitList(list)
        }

        viewModel.sponsorListLiveData.observeNotNull(this) { sponsorList = it }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutView = inflater.inflate(R.layout.fragment_prizes, container, false)
        val prizeRecyclerView = layoutView.findViewById<RecyclerView>(R.id.prizeRecyclerView)
        prizeRecyclerView.adapter = prizeAdapter

        tabLayout = layoutView.findViewById(R.id.typeTabLayout)

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> prizeAdapter.currentType = PrizeCardView.Type.MAIN
                    1 -> prizeAdapter.currentType = PrizeCardView.Type.STARTUP
                    2 -> prizeAdapter.currentType = PrizeCardView.Type.BEGINNER
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        return layoutView
    }

    private fun String.toPrizeType() = when (this) {
        "Beginner" -> PrizeCardView.Type.BEGINNER
        "Main" -> PrizeCardView.Type.MAIN
        "StartUp" -> PrizeCardView.Type.STARTUP
        else -> PrizeCardView.Type.BEGINNER
    }
}
