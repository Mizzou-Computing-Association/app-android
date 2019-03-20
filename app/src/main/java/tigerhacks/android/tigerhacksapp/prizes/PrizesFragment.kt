package tigerhacks.android.tigerhacksapp.prizes

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import tigerhacks.android.tigerhacksapp.HomeScreenActivity
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.sponsors.Sponsor
import tigerhacks.android.tigerhacksapp.sponsors.SponsorList
import java.util.ArrayList

class PrizesFragment : Fragment() {
    companion object {
        fun newInstance() = PrizesFragment()
    }

    private lateinit var layoutView: View
    //TODO ll needs to be changed to RecyclerView and scroll view to be removed
    private lateinit var progressBar: ProgressBar
    private var currentType: PrizeCardView.Type = PrizeCardView.Type.MAIN
    private var tabLayout: TabLayout? = null
    private var cardList: ArrayList<Prize>? = null
    private var sponsorList: ArrayList<Sponsor>? = null
    private var home: HomeScreenActivity? = null

    private var prizeAdapter = object : ListAdapter<Prize, RecyclerView.ViewHolder>(Prize.diff) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = object : RecyclerView.ViewHolder(PrizeCardView(parent.context)) {}

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val card = (holder.itemView as PrizeCardView)
            val prize = getItem(position)
            card.setup(prize, sponsorList)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutView = inflater.inflate(R.layout.fragment_prizes, container, false)
        val prizeRecyclerView = layoutView.findViewById<RecyclerView>(R.id.prizeRecyclerView)
        prizeRecyclerView.adapter = prizeAdapter

        tabLayout = layoutView.findViewById(R.id.typeTabLayout)

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        currentType = PrizeCardView.Type.MAIN
                        addCardsByType(cardList, sponsorList)
                    }
                    1 -> {
                        currentType = PrizeCardView.Type.STARTUP
                        addCardsByType(cardList, sponsorList)
                    }
                    2 -> {
                        currentType = PrizeCardView.Type.BEGINNER
                        addCardsByType(cardList, sponsorList)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })

        progressBar = layoutView.findViewById(R.id.progressBar)

        home = activity as HomeScreenActivity?

        //create cards. This is static, and will be replaced by dynamic creation through the API

        return layoutView
    }

    fun loadData(prizeList: PrizeList?, spList: SponsorList?) {
        if (prizeList == null || spList == null) return
        progressBar.visibility = View.GONE
        cardList = prizeList.prizes as ArrayList<Prize>
        sponsorList = spList.sponsors as ArrayList<Sponsor>
        addCardsByType(cardList, sponsorList)
    }

    override fun onStart() {
        super.onStart()
        home?.onFragmentsReady()
    }

    fun addCardsByType(list: ArrayList<Prize>?, sList: ArrayList<Sponsor>?) {
        if (list == null || sList == null) return

        val prizes = list.filter { it.prizeType?.toPrizeType() == currentType }
        prizeAdapter.submitList(prizes)
    }

    private fun String.toPrizeType() = when (this) {
        "Beginner" -> PrizeCardView.Type.BEGINNER
        "Main" -> PrizeCardView.Type.MAIN
        "StartUp" -> PrizeCardView.Type.STARTUP
        else -> PrizeCardView.Type.BEGINNER
    }
}
