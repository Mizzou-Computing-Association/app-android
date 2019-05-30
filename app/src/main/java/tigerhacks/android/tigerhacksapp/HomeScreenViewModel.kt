package tigerhacks.android.tigerhacksapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import tigerhacks.android.tigerhacksapp.prizes.Prize
import tigerhacks.android.tigerhacksapp.schedule.Event
import tigerhacks.android.tigerhacksapp.service.network.TigerHacksService
import tigerhacks.android.tigerhacksapp.sponsors.Mentor
import tigerhacks.android.tigerhacksapp.sponsors.Sponsor
import java.util.Timer
import java.util.TimerTask

/**
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
    val mentorsListLiveData = MutableLiveData<List<Mentor>>()

    val developerPrizeListLiveData = MutableLiveData<List<Prize>>()
    val beginnerPrizeListLiveData = MutableLiveData<List<Prize>>()
    val startupPrizeListLiveData = MutableLiveData<List<Prize>>()

    val fridayEventListLiveData = MutableLiveData<List<Event>>()
    val saturdayEventListLiveData = MutableLiveData<List<Event>>()
    val sundayEventListLiveData = MutableLiveData<List<Event>>()


    init {
        refresh()
    }

    fun refresh() {
        statusLiveData.postValue(NetworkStatus.LOADING)

        service.listSponsors().enqueue(object : Callback<List<Sponsor>> {
            override fun onResponse(call: Call<List<Sponsor>>, response: Response<List<Sponsor>>) {
                if (response.isSuccessful) {
                    statusLiveData.postValue(NetworkStatus.SUCCESS)
                    sponsorListLiveData.postValue(response.body())
                }
            }
            override fun onFailure(call: Call<List<Sponsor>>, t: Throwable) { onFailure() }
        })

        service.listMentors().enqueue(object : Callback<List<Mentor>> {
            override fun onResponse(call: Call<List<Mentor>>, response: Response<List<Mentor>>) {
                if (response.isSuccessful) {
                    statusLiveData.postValue(NetworkStatus.SUCCESS)
                    mentorsListLiveData.postValue(response.body())
                }
            }
            override fun onFailure(call: Call<List<Mentor>>, t: Throwable) { onFailure() }
        })

        service.listPrizes().enqueue(object : Callback<List<Prize>> {
            override fun onResponse(call: Call<List<Prize>>, response: Response<List<Prize>>) {
                if (response.isSuccessful) {
                    statusLiveData.postValue(NetworkStatus.SUCCESS)
                    val totalPrizes = response.body() ?: return
                    developerPrizeListLiveData.postValue(totalPrizes.filter { it.prizeType == "Main" })
                    beginnerPrizeListLiveData.postValue(totalPrizes.filter { it.prizeType == "Beginner" })
                    startupPrizeListLiveData.postValue(totalPrizes.filter { it.prizeType == "StartUp" })
                }
            }
            override fun onFailure(call: Call<List<Prize>>, t: Throwable) { onFailure() }
        })

        service.listEvents().enqueue(object : Callback<List<Event>> {
            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                if (response.isSuccessful) {
                    statusLiveData.postValue(NetworkStatus.SUCCESS)
                    val totalEvents = response.body() ?: return
                    fridayEventListLiveData.postValue(totalEvents.filter { it.day == 0 })
                    saturdayEventListLiveData.postValue(totalEvents.filter { it.day == 1 })
                    sundayEventListLiveData.postValue(totalEvents.filter { it.day == 2 })
                }
            }
            override fun onFailure(call: Call<List<Event>>, t: Throwable) { onFailure() }
        })
    }

    @Volatile
    private var refreshInProcess = false

    private fun onFailure() {
        statusLiveData.postValue(NetworkStatus.FAILURE)
        if (refreshInProcess) return
        refreshInProcess = true
        Timer().schedule(object : TimerTask() {
            override fun run() {
                refresh()
                refreshInProcess = false
            }
        }, 10000)
    }
}