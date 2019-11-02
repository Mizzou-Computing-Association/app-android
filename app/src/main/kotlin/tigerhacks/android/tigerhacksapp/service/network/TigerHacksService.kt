package tigerhacks.android.tigerhacksapp.service.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import tigerhacks.android.tigerhacksapp.models.PrizeList
import tigerhacks.android.tigerhacksapp.models.SponsorList
import tigerhacks.android.tigerhacksapp.models.Profile

/**
 * Created by Conno on 9/22/2018.
 * @author pauldg7@gmail.com (Paul Gillis)
 */
interface TigerHacksService {
    @GET("prizes")
    fun listPrizes(): Call<PrizeList>

    @GET("schedule")
    fun listEvents(): Call<ResponseBody>

    @GET("sponsors")
    fun listSponsors(): Call<SponsorList>

    @GET("profile")
    fun getProfile(@Query("userid") userId: String): Call<Profile>
}
