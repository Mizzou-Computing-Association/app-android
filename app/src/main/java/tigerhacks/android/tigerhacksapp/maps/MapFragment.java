package tigerhacks.android.tigerhacksapp.maps;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jsibbold.zoomage.ZoomageView;

import org.jetbrains.annotations.NotNull;

import tigerhacks.android.tigerhacksapp.HomeScreenActivity;
import tigerhacks.android.tigerhacksapp.R;

public class MapFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private ZoomageView mapView;
    private HomeScreenActivity home;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layoutView = inflater.inflate(R.layout.fragment_map, container, false);

        TabLayout tabLayout = layoutView.findViewById(R.id.tabLayout);
        mapView = layoutView.findViewById(R.id.mapView);

        //add button onclick events. Handles button visuals and map changing
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()) {
                    case 0:
                        mapView.setImageDrawable(getResources().getDrawable(R.drawable.floor1map));
                        break;
                    case 1:
                        mapView.setImageDrawable(getResources().getDrawable(R.drawable.floor2map));
                        break;
                    case 2:
                        mapView.setImageDrawable(getResources().getDrawable(R.drawable.floor3map));
                        break;
                }
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        home = (HomeScreenActivity)getActivity();

        //initial setup of event list
        //return fragment layout to main activity
        return layoutView;
    }

    @Override
    public void onStart() {
        super.onStart();
        home.onFragmentsReady();
    }
}
