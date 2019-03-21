package tigerhacks.android.tigerhacksapp

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import tigerhacks.android.tigerhacksapp.prizes.Prize
import tigerhacks.android.tigerhacksapp.prizes.PrizeList
import tigerhacks.android.tigerhacksapp.schedule.Event
import tigerhacks.android.tigerhacksapp.schedule.EventList
import tigerhacks.android.tigerhacksapp.service.network.TigerHacksService
import tigerhacks.android.tigerhacksapp.sponsors.Sponsor
import tigerhacks.android.tigerhacksapp.sponsors.SponsorList
import java.util.Timer
import java.util.TimerTask

/*
 * @author pauldg7@gmail.com (Paul Gillis)
 */

class HomeScreenViewModel : ViewModel() {
    private val tigerHacksRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://n61dynih7d.execute-api.us-east-2.amazonaws.com/production/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val service = tigerHacksRetrofit.create(TigerHacksService::class.java)

    enum class NetworkStatus {
        LOADING, FAILURE, SUCCESS
    }

    val statusLiveData = MutableLiveData<NetworkStatus>()
    val sponsorListLiveData = MutableLiveData<List<Sponsor>>()
    val prizeListLiveData = MutableLiveData<List<Prize>>()
    val eventListLiveData = MutableLiveData<List<Event>>()

    init {
        refresh()
    }

    fun refresh() {
        statusLiveData.postValue(NetworkStatus.LOADING)

        service.listSponsors().enqueue(object : Callback<SponsorList> {
            override fun onResponse(call: Call<SponsorList>, response: Response<SponsorList>) {
                if (response.isSuccessful) {
                    statusLiveData.postValue(NetworkStatus.SUCCESS)
                    sponsorListLiveData.postValue(response.body()?.sponsors)
                }
            }
            override fun onFailure(call: Call<SponsorList>, t: Throwable) { onFailure() }
        })

        service.listPrizes().enqueue(object : Callback<PrizeList> {
            override fun onResponse(call: Call<PrizeList>, response: Response<PrizeList>) {
                if (response.isSuccessful) {
                    statusLiveData.postValue(NetworkStatus.SUCCESS)
                    prizeListLiveData.postValue(response.body()?.prizes)
                }
            }
            override fun onFailure(call: Call<PrizeList>, t: Throwable) { onFailure() }
        })

        service.listEvents().enqueue(object : Callback<EventList> {
            override fun onResponse(call: Call<EventList>, response: Response<EventList>) {
                if (response.isSuccessful) {
                    statusLiveData.postValue(NetworkStatus.SUCCESS)
                    eventListLiveData.postValue(response.body()?.schedule)
                }
            }
            override fun onFailure(call: Call<EventList>, t: Throwable) { onFailure() }
        })
    }

    private fun onFailure() {
        statusLiveData.postValue(NetworkStatus.FAILURE)
        Timer().schedule(object : TimerTask() {
            override fun run() {
                refresh()
            }
        }, 10000)
    }
}