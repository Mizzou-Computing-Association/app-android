package tigerhacks.android.tigerhacksapp.sponsors

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.epoxy.EpoxyRecyclerView
import tigerhacks.android.tigerhacksapp.HomeScreenViewModel
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.service.extensions.observeNotNull
import tigerhacks.android.tigerhacksapp.service.extensions.withModels
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
            ViewModelProviders.of(this).get(HomeScreenViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val recyclerView = EpoxyRecyclerView(context!!)

        recyclerView.withModels {
            for (i in 0..3) {
                header {
                    id("level${i}Title")
                    sponsorLevel(i)
                }
                sponsorList?.filter { it.getLevel() == i }?.forEach {
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

        viewModel?.sponsorListLiveData?.observeNotNull(this) {
            sponsorList = it
            recyclerView.requestModelBuild()
        }

        recyclerView.setBackgroundResource(R.color.colorPrimaryBackground)

        return recyclerView
    }
}
