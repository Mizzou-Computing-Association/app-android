package tigerhacks.android.tigerhacksapp.service

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tigerhacks.android.tigerhacksapp.HomeScreenViewModel
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders.of(this).get(HomeScreenViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        val adapter = buildAdapter()
        observer = getLiveData(position.value).observeNotNull(this) { adapter.submitList(it) }
        this.adapter = adapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val recyclerView = RecyclerView(inflater.context)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(inflater.context)
            this.adapter = this@CategoryFragment.adapter
        }
        return recyclerView
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
}