package tigerhacks.android.tigerhacksapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Conno on 9/22/2018.
 */

public interface TigerHacksService {
    @GET
    Call<PrizeList> listPrizes(@Url String url);

    @GET
    Call<ScheduleItemList> listItems(@Url String url);

    @GET
    Call<SponsorList> listSponsors(@Url String url);
}
