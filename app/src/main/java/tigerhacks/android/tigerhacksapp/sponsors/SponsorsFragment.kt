package tigerhacks.android.tigerhacksapp.sponsors

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.epoxy.EpoxyRecyclerView
import tigerhacks.android.tigerhacksapp.HomeScreenViewModel
import tigerhacks.android.tigerhacksapp.service.extensions.observeNotNull
import tigerhacks.android.tigerhacksapp.service.extensions.withModels
import tigerhacks.android.tigerhacksapp.sponsors.views.SponsorImage
import tigerhacks.android.tigerhacksapp.sponsors.views.header
import tigerhacks.android.tigerhacksapp.sponsors.views.sponsorImage

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
                    sponsorImage {
                        id(it.name)
                        sponsor(it)
                        listener { epoxyModel ->
                            val model = epoxyModel as? SponsorImage
                            model?.let { startActivity(SponsorDetailActivity.newInstance(activity!!, it.sponsorData)) }
                        }
                    }
                }
            }
        }

        viewModel?.sponsorListLiveData?.observeNotNull(this) {
            sponsorList = it
            recyclerView.requestModelBuild()
        }

        return recyclerView
    }
}
