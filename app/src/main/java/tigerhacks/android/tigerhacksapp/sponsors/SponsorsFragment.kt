package tigerhacks.android.tigerhacksapp.sponsors

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tigerhacks.android.tigerhacksapp.HomeScreenViewModel
import tigerhacks.android.tigerhacksapp.service.extensions.observeNotNull

class SponsorsFragment : Fragment() {

    companion object {
        fun newInstance() = SponsorsFragment()
    }

    private var recyclerView: RecyclerView? = null
    private var viewModel: HomeScreenViewModel? = null

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
        // Inflate the layout for this fragment
        // layout = inflater.inflate(R.layout.fragment_sponsors, container, false)

        val controller = SponsorController()
        recyclerView = RecyclerView(context!!).apply {
            adapter = controller.adapter
            layoutManager = LinearLayoutManager(context)
        }

        controller.clickListener = View.OnClickListener { epoxyModel ->
            val model = epoxyModel as? SponsorImage
            model?.let { startActivity(SponsorDetailActivity.newInstance(activity!!, it.sponsorData)) }
        }

        viewModel?.sponsorListLiveData?.observeNotNull(this) {
            controller.setData(it)
        }

        return recyclerView
    }
}
