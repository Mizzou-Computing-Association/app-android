package tigerhacks.android.tigerhacksapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
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
    private int currentTab = 1;
    private BottomNavigationView navigation;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    static boolean sponsorsLoaded = false;
    static boolean scheduleLoaded = false;
    static boolean prizesLoaded = false;
    private static int loadedCount = 0;

    public SponsorList sponsorList = null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            //this switch will show the proper fragment depending on which navigation item is clicked
            switch (item.getItemId()) {
                case R.id.navigation_map:
                    mPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_prizes:
                    mPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_schedule:
                    mPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_sponsors:
                    mPager.setCurrentItem(3);
                    return true;
                case R.id.navigation_tigertalks:
                    mPager.setCurrentItem(4);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        //action bar can be called differently depending on SDK version, so this checks that
        //and sets up the action bar custom xml layout (layout/action_bar_layout.xml)
//        if(getSupportActionBar() != null) {
//            getSupportActionBar().setCustomView(R.layout.action_bar_layout);
//            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
//        }
//        else if(getActionBar() != null)
//        {
//            getActionBar().setCustomView(R.layout.action_bar_layout);
//            getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
//        }

        setContentView(R.layout.activity_home_screen);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //initalize fragments and the fragment manager, then add the map fragment as the default on app start
        mapFragment = new MapFragment();
        prizesFragment = new PrizesFragment();
        sponsorsFragment = new SponsorsFragment();
        scheduleFragment = new ScheduleFragment();
        tigerTalksFragment = new TigerTalksFragment();

        fragmentManager = getSupportFragmentManager();
        /*FragmentTransaction initialFragmentTransaction = fragmentManager.beginTransaction()
                .add(R.id.container, mapFragment)
                .addToBackStack(null);
        initialFragmentTransaction.commit();*/

        mPager = findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(4);
        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            public void onPageSelected(int position) {
                switch(position)
                {
                    case 0:
                        navigation.setSelectedItemId(R.id.navigation_map);
                        break;
                    case 1:
                        navigation.setSelectedItemId(R.id.navigation_prizes);
                        break;
                    case 2:
                        navigation.setSelectedItemId(R.id.navigation_schedule);
                        break;
                    case 3:
                        navigation.setSelectedItemId(R.id.navigation_sponsors);
                        break;
                    case 4:
                        navigation.setSelectedItemId(R.id.navigation_tigertalks);
                        break;
                }
            }
        });

        //set initial page/tab state
        mPager.setCurrentItem(0);
        navigation.setSelectedItemId(R.id.navigation_map);

    }

    //registers the MyGestureListener for handling touch gestures

    //this class handles gesture recognition, mainly for swiping between tabs on the app


    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        ScreenSlidePagerAdapter(FragmentManager fm) { super(fm); }

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
        /*
        @Override
        public void finishUpdate(ViewGroup container)
        {
            Log.d("TEST", "UPDATE");
        }*/
    }

    //this is here to allow the use of the Fragment interfaces
    public void onFragmentInteraction(Uri uri)
    {}

    public void onSponsorPageReady()
    {
        if(sponsorsLoaded)
        {
            return;
        }
        sponsorAPI();
    }

    public void onSchedulePageReady()
    {
        if(scheduleLoaded)
        {
            return;
        }
        scheduleAPI();
    }

    public void onMapReady()
    {
        if(scheduleLoaded)
        {
            return;
        }
        scheduleAPI();
    }

    public void onFragmentsReady()
    {
        loadedCount++;
        if(loadedCount == 5)
        {
            sponsorAPI();
            scheduleAPI();
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
            public void onResponse(Call<SponsorList> call, Response<SponsorList> response) {
                int statusCode = response.code();
                sponsorList = response.body();
                sponsorsFragment.loadSponsorData(sponsorList);
            }

            @Override
            public void onFailure(Call<SponsorList> call, Throwable t) {
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
            public void onResponse(Call<ScheduleItemList> call, Response<ScheduleItemList> response) {
                int statusCode = response.code();
                ScheduleItemList scheduleList = response.body();
                //progressBar.setVisibility(View.GONE);
                scheduleFragment.loadSchedule(scheduleList);
                mapFragment.loadSchedule(scheduleList);
            }

            @Override
            public void onFailure(Call<ScheduleItemList> call, Throwable t) {
                Snackbar.make(scheduleFragment.getView(), "TigerHacks API call failed. Attempting to reconnect...", Snackbar.LENGTH_SHORT).show();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        scheduleAPI();
                    }
                }, 10000);
            }
        });
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
