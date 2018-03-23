package tigerhacks.android.tigerhacksapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import java.net.URI;

public class HomeScreenActivity extends AppCompatActivity implements MapFragment.OnFragmentInteractionListener, PrizesFragment.OnFragmentInteractionListener, SponsorsFragment.OnFragmentInteractionListener, ScheduleFragment.OnFragmentInteractionListener, TigerTalksFragment.OnFragmentInteractionListener {

    private TextView mTextMessage;
    private FragmentManager fragmentManager;
    private MapFragment mapFragment;
    private PrizesFragment prizesFragment;
    private ScheduleFragment scheduleFragment;
    private SponsorsFragment sponsorsFragment;
    private TigerTalksFragment tigerTalksFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_map:
                    FragmentTransaction mapFragmentTransaction = fragmentManager.beginTransaction()
                            .replace(R.id.container, mapFragment)
                            .addToBackStack(null);
                    mapFragmentTransaction.commit();
                    return true;
                case R.id.navigation_prizes:
                    FragmentTransaction prizesFragmentTransaction = fragmentManager.beginTransaction()
                            .replace(R.id.container, prizesFragment)
                            .addToBackStack(null);
                    prizesFragmentTransaction.commit();
                    return true;
                case R.id.navigation_schedule:
                    FragmentTransaction scheduleFragmentTransaction = fragmentManager.beginTransaction()
                            .replace(R.id.container, scheduleFragment)
                            .addToBackStack(null);
                    scheduleFragmentTransaction.commit();
                    return true;
                case R.id.navigation_sponsors:
                    FragmentTransaction sponsorsFragmentTransaction = fragmentManager.beginTransaction()
                            .replace(R.id.container, sponsorsFragment)
                            .addToBackStack(null);
                    sponsorsFragmentTransaction.commit();
                    return true;
                case R.id.navigation_tigertalks:
                    FragmentTransaction tigerTalksFragmentTransaction = fragmentManager.beginTransaction()
                            .replace(R.id.container, tigerTalksFragment)
                            .addToBackStack(null);
                    tigerTalksFragmentTransaction.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mapFragment = new MapFragment();
        prizesFragment = new PrizesFragment();
        sponsorsFragment = new SponsorsFragment();
        scheduleFragment = new ScheduleFragment();
        tigerTalksFragment = new TigerTalksFragment();

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction initialFragmentTransaction = fragmentManager.beginTransaction()
                .add(R.id.container, mapFragment)
                .addToBackStack(null);
        initialFragmentTransaction.commit();

    }

    public void onFragmentInteraction(Uri uri)
    {}

}
