package tigerhacks.android.tigerhacksapp.service

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.vertical_recycler_view.container
import kotlinx.android.synthetic.main.vertical_recycler_view.progressBar
import kotlinx.android.synthetic.main.vertical_recycler_view.recyclerView
import kotlinx.android.synthetic.main.vertical_recycler_view.swipeRefreshLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tigerhacks.android.tigerhacksapp.HomeScreenActivity
import tigerhacks.android.tigerhacksapp.HomeScreenViewModel
import tigerhacks.android.tigerhacksapp.NetworkStatus
import tigerhacks.android.tigerhacksapp.R
import tigerhacks.android.tigerhacksapp.service.extensions.observeNotNull

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 *
 * purpose: Multiple fragments inflate the same view as a base and then load content into it from the viewModel.
 * I made this super class to remove how many times I have to see this same code.
 */
abstract class RecyclerFragment<T> : BaseFragment(R.layout.vertical_recycler_view) {

    internal lateinit var viewModel: HomeScreenViewModel
    private var observer: Observer<List<T>>? = null
    private var statusObserver: Observer<NetworkStatus>? = null

    private var isUserSwipe = false

    abstract val onRefresh: suspend () -> Unit
    abstract val adapter: ListAdapter<T, RecyclerView.ViewHolder>

    internal var liveData: LiveData<List<T>>? = null
        set(value) {
            if (value == field) {
                value?.removeObservers(this)
                return
            }
            resetObserver()
            field = value
            setupObserver()
        }

    internal var statusLiveData: MutableLiveData<NetworkStatus>? = null
        set(value) {
            if (value == field) {
                value?.removeObservers(this)
                return
            }
            resetStatusObserver()
            field = value
            setupStatusObserver()
        }

    override fun onViewCreated(layoutView: View, savedInstanceState: Bundle?) {
        val activity = activity as HomeScreenActivity
        viewModel = activity.viewModel
        recyclerView.adapter = adapter

        swipeRefreshLayout.setOnRefreshListener {
            isUserSwipe = true
            CoroutineScope(Dispatchers.Main).launch {
                onRefresh()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        setupObserver()
        setupStatusObserver()
    }

    override fun onPause() {
        super.onPause()
        resetObserver()
        resetStatusObserver()
    }

    private fun setupObserver() {
        observer = liveData?.observeNotNull(this) {
            swipeRefreshLayout.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            swipeRefreshLayout.isRefreshing = false
            isUserSwipe = false
            adapter.submitList(it)
        }
    }

    internal fun resetObserver() {
        observer?.let { liveData?.removeObserver(it) }
    }

    private fun setupStatusObserver() {
        statusObserver = statusLiveData?.observeNotNull(this) { status ->
            if (isUserSwipe || adapter.itemCount > 0) return@observeNotNull
            when(status) {
                NetworkStatus.LOADING -> {
                    swipeRefreshLayout.visibility = View.INVISIBLE
                    progressBar.visibility = View.VISIBLE
                }
                NetworkStatus.SUCCESS -> {
                    swipeRefreshLayout.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
                else -> {
                    swipeRefreshLayout.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    Snackbar.make(container, "Couldn't connect to tigerhacks.com!", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun resetStatusObserver() {
        statusObserver?.let { statusLiveData?.removeObserver(it) }
    }
}