package tigerhacks.android.tigerhacksapp

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tigerhacks.android.tigerhacksapp.maps.MapFragment
import tigerhacks.android.tigerhacksapp.prizes.PrizeList
import tigerhacks.android.tigerhacksapp.prizes.PrizesFragment
import tigerhacks.android.tigerhacksapp.schedule.ScheduleFragment
import tigerhacks.android.tigerhacksapp.schedule.ScheduleItemList
import tigerhacks.android.tigerhacksapp.service.network.TigerHacksService
import tigerhacks.android.tigerhacksapp.sponsors.SponsorList
import tigerhacks.android.tigerhacksapp.sponsors.SponsorsFragment
import tigerhacks.android.tigerhacksapp.tigertalks.TigerTalksFragment
import java.util.Timer
import java.util.TimerTask

class HomeScreenActivity : AppCompatActivity() {

    companion object {
        private var loadedCount = 0
        private var apiCount = 0
    }

    private lateinit var fragmentManager: FragmentManager
    private lateinit var mapFragment: MapFragment
    private lateinit var prizesFragment: PrizesFragment
    private lateinit var scheduleFragment: ScheduleFragment
    private lateinit var sponsorsFragment: SponsorsFragment
    private lateinit var tigerTalksFragment: TigerTalksFragment
    private lateinit var mPager: ViewPager

    var sponsorList: SponsorList? = null
    var prizeList: PrizeList? = null
    var scheduleList: ScheduleItemList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        //action bar can be called differently depending on SDK version, so this checks that
        //and sets up the action bar custom xml layout (layout/action_bar_layout.xml)

        setContentView(R.layout.activity_home_screen)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val navigation = findViewById<NavigationTabLayout>(R.id.navigation)
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //initalize fragments and the fragment manager, then add the map fragment as the default on app start
        mapFragment = MapFragment.newInstance()
        prizesFragment = PrizesFragment.newInstance()
        sponsorsFragment = SponsorsFragment.newInstance()
        scheduleFragment = ScheduleFragment.newInstance()
        tigerTalksFragment = TigerTalksFragment.newInstance()

        fragmentManager = supportFragmentManager

        mPager = findViewById(R.id.pager)
        navigation.setup(mPager)
        val mPagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager)
        mPager.adapter = mPagerAdapter
        mPager.offscreenPageLimit = 4

        //set initial page/tab state
        mPager.currentItem = 0
    }

    //registers the MyGestureListener for handling touch gestures

    //this class handles gesture recognition, mainly for swiping between tabs on the app

    private inner class ScreenSlidePagerAdapter internal constructor(fm: FragmentManager) :
        FragmentPagerAdapter(fm) {

        override fun getItem(position: Int) = when (position) {
            0 -> mapFragment
            1 -> prizesFragment
            2 -> scheduleFragment
            3 -> sponsorsFragment
            4 -> tigerTalksFragment
            else -> scheduleFragment
        }

        override fun getCount() = 5

        override fun getPageTitle(position: Int): String = when (position) {
            0 -> getString(R.string.title_map)
            1 -> getString(R.string.title_prizes)
            2 -> getString(R.string.title_schedule)
            3 -> getString(R.string.title_sponsors)
            4 -> getString(R.string.title_tigertalks)
            else -> getString(R.string.title_schedule)
        }
    }

    fun onFragmentsReady() {
        loadedCount++
        if (loadedCount == 5) {
            sponsorAPI()
            scheduleAPI()
            prizesAPI()
        }
    }

    private fun sponsorAPI() {

        val tigerHacksRetrofit = Retrofit.Builder()
            .baseUrl("https://n61dynih7d.execute-api.us-east-2.amazonaws.com/production/tigerhacksSponsors/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = tigerHacksRetrofit.create(TigerHacksService::class.java)
        val sponsorCall = service.listSponsors("https://n61dynih7d.execute-api.us-east-2.amazonaws.com/production/tigerhacksSponsors")

        sponsorCall.clone().enqueue(object : Callback<SponsorList> {
            override fun onResponse(call: Call<SponsorList>?, response: Response<SponsorList>?) {
                if (response != null) {
                    sponsorList = response.body()
                    apiCount++
                    checkAPIReady()
                }
            }

            override fun onFailure(call: Call<SponsorList>?, t: Throwable?) {
                Snackbar.make(
                    mPager.rootView,
                    "TigerHacks API call failed. Attempting to reconnect...",
                    Snackbar.LENGTH_SHORT
                ).show()
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        sponsorAPI()
                    }
                }, 10000)
            }
        })
    }

    private fun scheduleAPI() {
        val tigerHacksRetrofit = Retrofit.Builder()
            .baseUrl("https://n61dynih7d.execute-api.us-east-2.amazonaws.com/production/tigerhacksPrizes/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = tigerHacksRetrofit.create(TigerHacksService::class.java)
        val schedule =
            service.listItems("https://n61dynih7d.execute-api.us-east-2.amazonaws.com/production/tigerhacksSchedule")

        schedule.clone().enqueue(object : Callback<ScheduleItemList> {
            override fun onResponse(
                call: Call<ScheduleItemList>?,
                response: Response<ScheduleItemList>?
            ) {
                if (response != null) {
                    scheduleList = response.body()
                    //progressBar.setVisibility(View.GONE);
                    apiCount++
                    checkAPIReady()
                }
            }

            override fun onFailure(call: Call<ScheduleItemList>?, t: Throwable?) {
                scheduleFragment.view?.let { scheduleView ->
                    Snackbar.make(
                        scheduleView,
                        "TigerHacks API call failed. Attempting to reconnect...",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        scheduleAPI()
                    }
                }, 10000)
            }
        })
    }

    fun prizesAPI() {
        val tigerHacksRetrofit = Retrofit.Builder()
            .baseUrl("https://n61dynih7d.execute-api.us-east-2.amazonaws.com/production/tigerhacksPrizes/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = tigerHacksRetrofit.create(TigerHacksService::class.java)
        val prizes =
            service.listPrizes("https://n61dynih7d.execute-api.us-east-2.amazonaws.com/production/tigerhacksPrizes")

        prizes.clone().enqueue(object : Callback<PrizeList> {
            override fun onResponse(call: Call<PrizeList>?, response: Response<PrizeList>?) {
                if (response != null) {
                    prizeList = response.body()
                    apiCount++
                    checkAPIReady()
                }
            }

            override fun onFailure(call: Call<PrizeList>?, t: Throwable?) {
                Snackbar.make(
                    mPager.rootView,
                    "TigerHacks API call failed. Attempting to reconnect...",
                    Snackbar.LENGTH_SHORT
                ).show()
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        prizesAPI()
                    }
                }, 10000)
            }
        })
    }

    private fun checkAPIReady() {
        if (apiCount >= 3) {
            sponsorsFragment.loadSponsorData(sponsorList)
            prizesFragment.loadData(prizeList, sponsorList)
            scheduleFragment.loadSchedule(scheduleList)
        }
    }
}
