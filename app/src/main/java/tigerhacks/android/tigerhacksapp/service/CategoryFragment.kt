package tigerhacks.android.tigerhacksapp.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import tigerhacks.android.tigerhacksapp.HomeScreenViewModel
import tigerhacks.android.tigerhacksapp.service.database.TigerHacksDatabase
import tigerhacks.android.tigerhacksapp.service.extensions.observeNotNull


/**
 * @author pauldg7@gmail.com (Paul Gillis)
 * Used as the parent class for the sub Prize and Event fragments
 */
abstract class CategoryFragment<T> : Fragment() {
    abstract val position: Lazy<Int>

    private var adapter: ListAdapter<T, RecyclerView.ViewHolder>? = null
    internal lateinit var viewModel: HomeScreenViewModel
    private var observer: Observer<List<T>>? = null
    private var layout: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.run {
            val db = TigerHacksDatabase.getDatabase(this.applicationContext)
            ViewModelProviders.of(this, HomeScreenViewModel.FACTORY(db)).get(HomeScreenViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        val adapter = buildAdapter()
        observer = getLiveData(position.value).observeNotNull(this) {
            adapter.submitList(it)
            layout?.isRefreshing = false
        }
        this.adapter = adapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val recyclerView = RecyclerView(inflater.context)
        val layout = SwipeRefreshLayout(inflater.context)
        layout.addView(recyclerView)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(inflater.context)
            this.adapter = this@CategoryFragment.adapter
        }
        this.layout = layout
        layout.setOnRefreshListener {
            onRefresh()
            adapter?.submitList(null)
        }
        return layout
    }

    override fun onDestroyView() {
        super.onDestroyView()
        layout = null
    }

    override fun onDestroy() {
        super.onDestroy()
        val obs = observer
        if (obs != null) {
            getLiveData(position.value).removeObserver(obs)
        }
    }

    abstract fun buildAdapter(): ListAdapter<T, RecyclerView.ViewHolder>

    abstract fun getLiveData(position: Int): LiveData<List<T>>

    abstract fun onRefresh()
}