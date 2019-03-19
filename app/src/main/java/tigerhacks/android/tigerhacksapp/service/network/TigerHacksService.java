package tigerhacks.android.tigerhacksapp.service.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import tigerhacks.android.tigerhacksapp.prizes.PrizeList;
import tigerhacks.android.tigerhacksapp.schedule.ScheduleItemList;
import tigerhacks.android.tigerhacksapp.sponsors.SponsorList;

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
