package tigerhacks.android.tigerhacksapp.prizes

import android.os.Bundle
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import tigerhacks.android.tigerhacksapp.service.CategoryFragment

private const val POSITION_KEY = "PRIZE_CATEGORY_FRAGMENT_POSITION"

class PrizeCategoryFragment : CategoryFragment<Prize>() {
    companion object {
        fun newInstance(position: Int) = PrizeCategoryFragment().apply {
            arguments = Bundle().apply {
                putInt(POSITION_KEY, position)
            }
        }
    }

    override val position = lazy { arguments?.getInt(POSITION_KEY) ?: 0 }

    override fun buildAdapter() = object : ListAdapter<Prize, RecyclerView.ViewHolder>(Prize.diff) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = object : RecyclerView.ViewHolder(PrizeCardView(parent.context)) {}

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val card = (holder.itemView as PrizeCardView)
            val prize = getItem(position)
            card.setup(prize)
        }
    }

    override fun getLiveData(position: Int) = when (position) {
        0 -> viewModel.developerPrizeListLiveData
        1 -> viewModel.startupPrizeListLiveData
        else -> viewModel.beginnerPrizeListLiveData
    }
}