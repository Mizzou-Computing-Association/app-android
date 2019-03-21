package tigerhacks.android.tigerhacksapp.service.network

import retrofit2.Call
import retrofit2.http.GET
import tigerhacks.android.tigerhacksapp.prizes.PrizeList
import tigerhacks.android.tigerhacksapp.schedule.EventList
import tigerhacks.android.tigerhacksapp.sponsors.SponsorList

/**
 * Created by Conno on 9/22/2018.
 * @author pauldg7@gmail.com (Paul Gillis)
 */

interface TigerHacksService {
    @GET("tigerhacksPrizes")
    fun listPrizes(): Call<PrizeList>

    @GET("tigerhacksSchedule")
    fun listEvents(): Call<EventList>

    @GET("tigerhacksSponsors")
    fun listSponsors(): Call<SponsorList>
}
