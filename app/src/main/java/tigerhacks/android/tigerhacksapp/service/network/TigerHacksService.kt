package tigerhacks.android.tigerhacksapp.service.network

import retrofit2.Call
import retrofit2.http.GET
import tigerhacks.android.tigerhacksapp.prizes.Prize
import tigerhacks.android.tigerhacksapp.schedule.Event
import tigerhacks.android.tigerhacksapp.sponsors.Mentor
import tigerhacks.android.tigerhacksapp.sponsors.Sponsor

/**
 * Created by Conno on 9/22/2018.
 * @author pauldg7@gmail.com (Paul Gillis)
 */

interface TigerHacksService {
    @GET("tigerhacksNewPrizes")
    fun listPrizes(): Call<List<Prize>>

    @GET("tigerhacksNewSchedule")
    fun listEvents(): Call<List<Event>>

    @GET("tigerhacksNewSponsors")
    fun listSponsors(): Call<List<Sponsor>>

    @GET("tigerhacksMentors")
    fun listMentors(): Call<List<Mentor>>
}
