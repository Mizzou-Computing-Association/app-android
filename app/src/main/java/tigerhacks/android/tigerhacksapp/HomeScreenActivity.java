package tigerhacks.android.tigerhacksapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeScreenActivity extends AppCompatActivity implements MapFragment.OnFragmentInteractionListener, PrizesFragment.OnFragmentInteractionListener, SponsorsFragment.OnFragmentInteractionListener, ScheduleFragment.OnFragmentInteractionListener, TigerTalksFragment.OnFragmentInteractionListener {

    FragmentManager fragmentManager;
    private MapFragment mapFragment;
    private PrizesFragment prizesFragment;
    private ScheduleFragment scheduleFragment;
    private SponsorsFragment sponsorsFragment;
    private TigerTalksFragment tigerTalksFragment;
    private ViewPager mPager;

    private static int loadedCount = 0;
    private static int apiCount = 0;

    public SponsorList sponsorList = null;
    public PrizeList prizeList = null;
    public ScheduleItemList scheduleList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        //action bar can be called differently depending on SDK version, so this checks that
        //and sets up the action bar custom xml layout (layout/action_bar_layout.xml)

        setContentView(R.layout.activity_home_screen);

        NavigationTabLayout navigation = findViewById(R.id.navigation);
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //initalize fragments and the fragment manager, then add the map fragment as the default on app start
        mapFragment = new MapFragment();
        prizesFragment = new PrizesFragment();
        sponsorsFragment = new SponsorsFragment();
        scheduleFragment = new ScheduleFragment();
        tigerTalksFragment = new TigerTalksFragment();

        fragmentManager = getSupportFragmentManager();

        mPager = findViewById(R.id.pager);
        navigation.setup(mPager);
        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(4);

        //set initial page/tab state
        mPager.setCurrentItem(0);

    }

    //registers the MyGestureListener for handling touch gestures

    //this class handles gesture recognition, mainly for swiping between tabs on the app


    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position)
            {
                case 0:
                    return mapFragment;
                case 1:
                    return prizesFragment;
                case 2:
                    return scheduleFragment;
                case 3:
                    return sponsorsFragment;
                case 4:
                    return tigerTalksFragment;
                default:
                    return scheduleFragment;
            }
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.title_map);
                case 1:
                    return getString(R.string.title_prizes);
                case 2:
                    return getString(R.string.title_schedule);
                case 3:
                    return getString(R.string.title_sponsors);
                case 4:
                    return getString(R.string.title_tigertalks);
                default:
                    return getString(R.string.title_schedule);
            }
        }
    }

    //this is here to allow the use of the Fragment interfaces
    public void onFragmentInteraction(Uri uri)
    {}

    public void onFragmentsReady()
    {
        loadedCount++;
        if(loadedCount == 5)
        {
            sponsorAPI();
            scheduleAPI();
            prizesAPI();
        }
    }

    private void sponsorAPI()
    {

        Retrofit tigerHacksRetrofit = new Retrofit.Builder()
                .baseUrl("https://n61dynih7d.execute-api.us-east-2.amazonaws.com/production/tigerhacksSponsors/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TigerHacksService service = tigerHacksRetrofit.create(TigerHacksService.class);
        Call<SponsorList> sponsorCall = service.listSponsors("https://n61dynih7d.execute-api.us-east-2.amazonaws.com/production/tigerhacksSponsors");

        sponsorCall.clone().enqueue(new Callback<SponsorList>() {
            @Override
            public void onResponse(@Nullable Call<SponsorList> call, @Nullable Response<SponsorList> response) {
                if (response != null) {
                    sponsorList = response.body();
                    apiCount++;
                    checkAPIReady();
                }
            }

            @Override
            public void onFailure(@Nullable Call<SponsorList> call, @Nullable Throwable t) {
                Snackbar.make(mPager.getRootView(), "TigerHacks API call failed. Attempting to reconnect...", Snackbar.LENGTH_SHORT).show();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        sponsorAPI();
                    }
                }, 10000);
            }
        });
    }
    private void scheduleAPI()
    {
        Retrofit tigerHacksRetrofit = new Retrofit.Builder()
                .baseUrl("https://n61dynih7d.execute-api.us-east-2.amazonaws.com/production/tigerhacksPrizes/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TigerHacksService service = tigerHacksRetrofit.create(TigerHacksService.class);
        Call<ScheduleItemList> schedule = service.listItems("https://n61dynih7d.execute-api.us-east-2.amazonaws.com/production/tigerhacksSchedule");

        schedule.clone().enqueue(new Callback<ScheduleItemList>() {
            @Override
            public void onResponse(@Nullable Call<ScheduleItemList> call, @Nullable Response<ScheduleItemList> response) {
                if (response != null) {
                    scheduleList = response.body();
                    //progressBar.setVisibility(View.GONE);
                    apiCount++;
                    checkAPIReady();
                }
            }

            @Override
            public void onFailure(@Nullable Call<ScheduleItemList> call, @Nullable Throwable t) {
                if (scheduleFragment != null && scheduleFragment.getView() != null) {
                    Snackbar.make(scheduleFragment.getView(), "TigerHacks API call failed. Attempting to reconnect...", Snackbar.LENGTH_SHORT).show();
                }
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        scheduleAPI();
                    }
                }, 10000);
            }
        });
    }

    public void prizesAPI()
    {
        Retrofit tigerHacksRetrofit = new Retrofit.Builder()
                .baseUrl("https://n61dynih7d.execute-api.us-east-2.amazonaws.com/production/tigerhacksPrizes/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TigerHacksService service = tigerHacksRetrofit.create(TigerHacksService.class);
        Call<PrizeList> prizes = service.listPrizes("https://n61dynih7d.execute-api.us-east-2.amazonaws.com/production/tigerhacksPrizes");

        prizes.clone().enqueue(new Callback<PrizeList>() {
            @Override
            public void onResponse(@Nullable Call<PrizeList> call, @Nullable Response<PrizeList> response) {
                if (response != null) {
                    prizeList = response.body();
                    apiCount++;
                    checkAPIReady();
                }
            }

            @Override
            public void onFailure(@Nullable Call<PrizeList> call, @Nullable Throwable t) {
                Snackbar.make(mPager.getRootView(), "TigerHacks API call failed. Attempting to reconnect...", Snackbar.LENGTH_SHORT).show();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        prizesAPI();
                    }
                }, 10000);
            }
        });

    }

    private void checkAPIReady()
    {
        if(apiCount >= 3) {
            sponsorsFragment.loadSponsorData(sponsorList);
            prizesFragment.loadData(prizeList, sponsorList);
            scheduleFragment.loadSchedule(scheduleList);
            mapFragment.loadSchedule(scheduleList);
        }
    }

    public void linkWebDev(View v)
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=KaNfsfwSUu4&t=66s"));
        startActivity(browserIntent);
    }

    public void linkIOS(View v)
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=kobP_rJAuyI"));
        startActivity(browserIntent);
    }

    public void linkFlask(View v)
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=V0QmmrTTbY4"));
        startActivity(browserIntent);
    }

    public void linkSlack(View v)
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://mizzoumca.slack.com/"));
        startActivity(browserIntent);
    }

    public void linkWebsite(View v)
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://tiger-hacks.com/"));
        startActivity(browserIntent);
    }
}
