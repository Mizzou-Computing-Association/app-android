package tigerhacks.android.tigerhacksapp;

import android.app.ActionBar;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class HomeScreenActivity extends AppCompatActivity implements MapFragment.OnFragmentInteractionListener, PrizesFragment.OnFragmentInteractionListener, SponsorsFragment.OnFragmentInteractionListener, ScheduleFragment.OnFragmentInteractionListener, TigerTalksFragment.OnFragmentInteractionListener {

    private FragmentManager fragmentManager;
    private MapFragment mapFragment;
    private PrizesFragment prizesFragment;
    private ScheduleFragment scheduleFragment;
    private SponsorsFragment sponsorsFragment;
    private TigerTalksFragment tigerTalksFragment;
    private int currentTab = 1;
    private BottomNavigationView navigation;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

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
                    return new MapFragment();
                case 1:
                    return new PrizesFragment();
                case 2:
                    return new ScheduleFragment();
                case 3:
                    return new SponsorsFragment();
                case 4:
                    return new TigerTalksFragment();
                default:
                    return new ScheduleFragment();
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

}
