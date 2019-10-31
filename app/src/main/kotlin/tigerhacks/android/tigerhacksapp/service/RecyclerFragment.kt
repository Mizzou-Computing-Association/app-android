package tigerhacks.android.tigerhacksapp.service

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
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
abstract class RecyclerFragment<T> : Fragment() {

    private lateinit var layout: ConstraintLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    internal lateinit var recyclerView: RecyclerView
    internal var tabLayout: TabLayout? = null

    internal lateinit var viewModel: HomeScreenViewModel
    private var observer: Observer<List<T>>? = null
    private var statusObserver: Observer<NetworkStatus>? = null

    private var isUserSwipe = false

    abstract val onRefresh: suspend () -> Unit
    abstract val adapter: ListAdapter<T, RecyclerView.ViewHolder>
    open var tabResList: List<Int> = emptyList()

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val toolbarLayoutRes = if (tabResList.isNotEmpty()) R.layout.toolbar_with_tab else R.layout.toolbar_no_tab
        val toolbarContainer = inflater.inflate(toolbarLayoutRes, container, false)
        val containerViewGroup = toolbarContainer.findViewById<ConstraintLayout>(R.id.container)
        val layoutView = inflater.inflate(R.layout.vertical_recycler_view, containerViewGroup as ViewGroup, true)

        val toolbar = toolbarContainer.findViewById<Toolbar>(R.id.toolbar)
        layout = layoutView.findViewById(R.id.container)
        swipeRefreshLayout = layoutView.findViewById(R.id.swipeRefreshLayout)
        recyclerView = layoutView.findViewById(R.id.recyclerView)
        progressBar = layoutView.findViewById(R.id.progressBar)
        val activity = activity as HomeScreenActivity
        tabLayout = toolbarContainer.findViewById(R.id.tabLayout)

        viewModel = activity.viewModel

        recyclerView.adapter = adapter

        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navController = activity.findNavController(R.id.navHostFragment)
        activity.setupActionBarWithNavController(navController, activity.findViewById<DrawerLayout>(R.id.drawerLayout))
        activity.findViewById<NavigationView>(R.id.navigationView).setupWithNavController(navController)

        swipeRefreshLayout.setOnRefreshListener {
            isUserSwipe = true
            CoroutineScope(Dispatchers.Main).launch {
                onRefresh()
            }
        }

        tabLayout?.let { layout ->
            if (layout.tabCount == 0) {
                for (res in tabResList) {
                    val tab = layout.newTab()
                    tab.text = getString(res)
                    layout.addTab(tab)
                }
            }
        }

        initSetup()

        return toolbarContainer
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

    abstract fun initSetup()

    private fun setupObserver() {
        observer = liveData?.observeNotNull(this) {
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
                    Snackbar.make(layout, "Couldn't find slack app or google play store!", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun resetStatusObserver() {
        statusObserver?.let { statusLiveData?.removeObserver(it) }
    }
}