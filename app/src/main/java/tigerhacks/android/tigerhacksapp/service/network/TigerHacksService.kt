package tigerhacks.android.tigerhacksapp.service.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url
import tigerhacks.android.tigerhacksapp.prizes.PrizeList
import tigerhacks.android.tigerhacksapp.schedule.ScheduleItemList
import tigerhacks.android.tigerhacksapp.sponsors.SponsorList

/**
 * Created by Conno on 9/22/2018.
 */

interface TigerHacksService {
    @GET
    fun listPrizes(@Url url: String): Call<PrizeList>

    @GET
    fun listItems(@Url url: String): Call<ScheduleItemList>

    @GET
    fun listSponsors(@Url url: String): Call<SponsorList>
}
