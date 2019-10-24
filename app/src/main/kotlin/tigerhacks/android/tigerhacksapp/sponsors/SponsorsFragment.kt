package tigerhacks.android.tigerhacksapp.sponsors

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.epoxy.EpoxyRecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tigerhacks.android.tigerhacksapp.HomeScreenViewModel
import tigerhacks.android.tigerhacksapp.service.database.TigerHacksDatabase
import tigerhacks.android.tigerhacksapp.service.extensions.observeNotNull
import tigerhacks.android.tigerhacksapp.sponsors.models.Sponsor
import tigerhacks.android.tigerhacksapp.sponsors.views.SponsorCardView
import tigerhacks.android.tigerhacksapp.sponsors.views.header
import tigerhacks.android.tigerhacksapp.sponsors.views.sponsorCardView

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
class SponsorsFragment : Fragment() {

    companion object {
        fun newInstance() = SponsorsFragment()
    }

    private var viewModel: HomeScreenViewModel? = null
    private var sponsorList: List<Sponsor>? = null

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
        val recyclerView = EpoxyRecyclerView(context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }

        layout.addView(recyclerView)

        recyclerView.withModels {
            for (i in 0..3) {
                header {
                    id("level${i}Title")
                    sponsorLevel(i)
                }
                sponsorList?.filter { it.level == i }?.forEach {
                    sponsorCardView {
                        id(it.name)
                        sponsor(it)
                        listener { epoxyModel ->
                            val model = epoxyModel as? SponsorCardView ?: return@listener
                            activity?.let { startActivity(SponsorDetailActivity.newInstance(it, model.sponsorData)) }
                        }
                    }
                }
            }
        }

        layout.setOnRefreshListener {
            sponsorList = null
            recyclerView.requestModelBuild()
            CoroutineScope(Dispatchers.Main).launch {
                viewModel?.refreshSponsors()
                //viewModel?.refreshMentors()
            }
        }

        viewModel?.sponsorListLiveData?.observeNotNull(this) {
            sponsorList = it
            recyclerView.requestModelBuild()
            layout.isRefreshing = false
        }

        return layout
    }
}
