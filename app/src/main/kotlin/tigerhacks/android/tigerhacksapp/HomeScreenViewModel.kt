package tigerhacks.android.tigerhacksapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import tigerhacks.android.tigerhacksapp.schedule.Event
import tigerhacks.android.tigerhacksapp.service.database.TigerHacksDatabase
import tigerhacks.android.tigerhacksapp.service.network.TigerHacksService
import java.io.IOException
import com.squareup.moshi.Types
import tigerhacks.android.tigerhacksapp.schedule.EventTimeAdapter


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

enum class NetworkStatus {
    LOADING, FAILURE, SUCCESS
}

class HomeScreenViewModel(private val database: TigerHacksDatabase) : ViewModel() {
    companion object {
        val FACTORY = singleArgViewModelFactory(::HomeScreenViewModel)
    }

    private val moshi = Moshi.Builder().add(EventTimeAdapter()).add(KotlinJsonAdapterFactory()).build()

    private val tigerHacksRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://tigerhacks.com/api/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val service = tigerHacksRetrofit.create(TigerHacksService::class.java)

    val sponsorListLiveData = database.sponsorsDAO().getSponsors()
    val sponsorStatusLiveData = MutableLiveData<NetworkStatus>()

    val prizeListLiveData = database.prizeDAO().getAllPrizes()
    val prizeStatusLiveData = MutableLiveData<NetworkStatus>()

    val fridayEventListLiveData = database.scheduleDAO().getFridayEvents()
    val saturdayEventListLiveData = database.scheduleDAO().getSaturdayEvents()
    val sundayEventListLiveData = database.scheduleDAO().getSundayEvents()
    val eventStatusLiveData = MutableLiveData<NetworkStatus>()

    private val uiScope = CoroutineScope(Dispatchers.Main)

    init {
        refresh()
    }

    private fun refresh() {
        uiScope.launch {
            refreshPrizes()
            refreshEvents()
            refreshSponsors()
        }
    }

    suspend fun refreshPrizes() = withContext(Dispatchers.IO) {
        prizeStatusLiveData.postValue(NetworkStatus.LOADING)
        try {
            val response = service.listPrizes().execute()
            if (response.isSuccessful) {
                prizeStatusLiveData.postValue(NetworkStatus.SUCCESS)
                val totalPrizes = response.body()?.combine()
                if (totalPrizes != null) {
                    database.prizeDAO().updatePrizes(totalPrizes)
                }
            } else {
                prizeStatusLiveData.postValue(NetworkStatus.FAILURE)
            }
        } catch (e: IOException) {
            prizeStatusLiveData.postValue(NetworkStatus.FAILURE)
        }
    }

    suspend fun refreshEvents() = withContext(Dispatchers.IO) {
        eventStatusLiveData.postValue(NetworkStatus.LOADING)
        try {
            val response = service.listEvents().execute()
            if (response.isSuccessful) {
                eventStatusLiveData.postValue(NetworkStatus.SUCCESS)
                val totalEvents = response.body()?.string()
                if (totalEvents != null) {
                    try {
                        val jsonBody = JSONObject(totalEvents)
                        val jsonArr = jsonBody.toJSONArray(jsonBody.names())
                        val type = Types.newParameterizedType(List::class.java, Event::class.java)
                        val adapter = moshi.adapter<List<Event>>(type)
                        if (jsonArr != null) {
                            var json = jsonArr.toString()
                                .replace("[", "")
                                .replace("]", "")
                            json = "[${json}]"
                            val result = adapter.fromJson(json)
                            if (result != null) database.scheduleDAO().updateEvents(result)
                        }
                    } catch(io: Exception) {}
                }
            } else {
                eventStatusLiveData.postValue(NetworkStatus.FAILURE)
            }
        } catch (e: IOException) {
            eventStatusLiveData.postValue(NetworkStatus.FAILURE)
        }
    }

    suspend fun refreshSponsors() = withContext(Dispatchers.IO) {
        sponsorStatusLiveData.postValue(NetworkStatus.LOADING)
        try {
            val response = service.listSponsors().execute()
            if (response.isSuccessful) {
                sponsorStatusLiveData.postValue(NetworkStatus.SUCCESS)
                val sponsors = response.body()
                if (sponsors != null) {
                    database.sponsorsDAO().updateSponsors(sponsors.combine())
                }
            } else {
                sponsorStatusLiveData.postValue(NetworkStatus.FAILURE)
            }
        } catch (e: IOException) {
            sponsorStatusLiveData.postValue(NetworkStatus.FAILURE)
        }
    }
}