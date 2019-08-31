package tigerhacks.android.tigerhacksapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import tigerhacks.android.tigerhacksapp.service.database.TigerHacksDatabase
import tigerhacks.android.tigerhacksapp.service.network.TigerHacksService
import java.io.IOException

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */

fun <T : ViewModel, A> singleArgViewModelFactory(constructor: (A) -> T):
    (A) -> ViewModelProvider.NewInstanceFactory {
    return { arg: A ->
        object : ViewModelProvider.NewInstanceFactory() {
            @Suppress("UNCHECKED_CAST")
            override fun <V : ViewModel> create(modelClass: Class<V>): V {
                return constructor(arg) as V
            }
        }
    }
}

class HomeScreenViewModel(private val database: TigerHacksDatabase) : ViewModel() {
    companion object {
        val FACTORY = singleArgViewModelFactory(::HomeScreenViewModel)
    }

    private val tigerHacksRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://n61dynih7d.execute-api.us-east-2.amazonaws.com/production/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val service = tigerHacksRetrofit.create(TigerHacksService::class.java)

    enum class NetworkStatus {
        LOADING, FAILURE, SUCCESS
    }

    val statusLiveData = MutableLiveData<NetworkStatus>()

    val sponsorListLiveData = database.sponsorsDAO().getSponsors()

    val developerPrizeListLiveData = database.prizeDAO().getAllDevPrizes()
    val beginnerPrizeListLiveData = database.prizeDAO().getAllBeginnerPrizes()
    val startupPrizeListLiveData = database.prizeDAO().getAllStartUpPrizes()

    val fridayEventListLiveData = database.scheduleDAO().getFridayEvents()
    val saturdayEventListLiveData = database.scheduleDAO().getSaturdayEvents()
    val sundayEventListLiveData = database.scheduleDAO().getSundayEvents()

    private val uiScope = CoroutineScope(Dispatchers.Main)

    init {
        refresh()
    }

    fun refresh() {
        uiScope.launch {
            refreshPrizes()
            refreshEvents()
            refreshSponsors()
            refreshMentors()
        }
    }

    suspend fun refreshPrizes() = withContext(Dispatchers.IO) {
        statusLiveData.postValue(NetworkStatus.LOADING)
        try {
            val response = service.listPrizes().execute()
            if (response.isSuccessful) {
                statusLiveData.postValue(NetworkStatus.SUCCESS)
                val totalPrizes = response.body()
                if (totalPrizes != null) {
                    database.prizeDAO().updatePrizes(totalPrizes)
                }
            } else {
                statusLiveData.postValue(NetworkStatus.FAILURE)
            }
        } catch (e: IOException) {
            statusLiveData.postValue(NetworkStatus.FAILURE)
        }
    }

    suspend fun refreshEvents() = withContext(Dispatchers.IO) {
        statusLiveData.postValue(NetworkStatus.LOADING)
        try {
            val response = service.listEvents().execute()
            if (response.isSuccessful) {
                statusLiveData.postValue(NetworkStatus.SUCCESS)
                val totalEvents = response.body()
                if (totalEvents != null) {
                    database.scheduleDAO().updateEvents(totalEvents)
                }
            } else {
                statusLiveData.postValue(NetworkStatus.FAILURE)
            }
        } catch (e: IOException) {
            statusLiveData.postValue(NetworkStatus.FAILURE)
        }
    }

    suspend fun refreshSponsors() = withContext(Dispatchers.IO) {
        statusLiveData.postValue(NetworkStatus.LOADING)
        try {
            val response = service.listSponsors().execute()
            if (response.isSuccessful) {
                statusLiveData.postValue(NetworkStatus.SUCCESS)
                val sponsors = response.body()
                if (sponsors != null) {
                    database.sponsorsDAO().updateSponsors(sponsors)
                }
            } else {
                statusLiveData.postValue(NetworkStatus.FAILURE)
            }
        } catch (e: IOException) {
            statusLiveData.postValue(NetworkStatus.FAILURE)
        }
    }

    suspend fun refreshMentors() = withContext(Dispatchers.IO) {
        statusLiveData.postValue(NetworkStatus.LOADING)
        try {
            val response = service.listMentors().execute()
            if (response.isSuccessful) {
                statusLiveData.postValue(NetworkStatus.SUCCESS)
                val mentors = response.body()
                if (mentors != null) {
                    database.sponsorsDAO().updateMentors(mentors)
                }
            } else {
                statusLiveData.postValue(NetworkStatus.FAILURE)
            }
        } catch (e: IOException) {
            statusLiveData.postValue(NetworkStatus.FAILURE)
        }
    }
}