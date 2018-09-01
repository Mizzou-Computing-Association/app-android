package tigerhacks.android.tigerhacksapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View layoutView;
    private ImageView mapView;
    private TabLayout tabLayout;
    private OnFragmentInteractionListener mListener;

    private ExpandableListAdapter mAdapter;

    private String group[] = {"How To Use StackOverflow" , "Fortran 101", "2007 Runescape LAN Party", "To Catch A Mac User with Chris Hansen"};
    private String[][][] child = {
            {{"Ever wanted to know how a programmer does their job? Join me for a comprehensive" +
                    " look at using the programmer's most important tool.","Ikant Koad"}},
            {{"Take a deep dive into one of today's most innovative languages: Fortran.","Big Richard"}},
            {{"buying gf 2k", "Zezima"}},
            {{"'How do you even use that stupid mouse?'","The Woz"}}
    };
    private String group2[] = {"This Is On Floor 2"};
    private String[][][] child2 = {
            {{"Wow, you have no idea how long this bit took to code.", "Swear to God this took longer than the rest of the app combined."}}
    };
    private String group3[] = {"This Is On Floor 2"};
    private String[][][] child3 = {
            {{"Wow, you have no idea how long this bit took to code.", "Swear to God this took longer than the rest of the app combined."}}
    };
    private String group4[] = {"This Is On Floor 2"};
    private String[][][] child4 = {
            {{"Wow, you have no idea how long this bit took to code.", "Swear to God this took longer than the rest of the app combined."}}
    };


    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layoutView = inflater.inflate(R.layout.fragment_map, container, false);

        tabLayout = layoutView.findViewById(R.id.tabLayout);
        mapView = layoutView.findViewById(R.id.mapView);

        //add button onclick events. Handles button visuals and map changing
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()) {
                    case 0:
                        mapView.setImageDrawable(getResources().getDrawable(R.drawable.im_laf_map_1));
                        populateEventList(group, child);
                        break;
                    case 1:
                        mapView.setImageDrawable(getResources().getDrawable(R.drawable.im_laf_map_2));
                        populateEventList(group2, child2);
                        break;
                    case 2:
                        mapView.setImageDrawable(getResources().getDrawable(R.drawable.im_laf_map_3));
                        populateEventList(group3, child3);
                        break;
                    case 3:
                        mapView.setImageDrawable(getResources().getDrawable(R.drawable.im_laf_map_4));
                        populateEventList(group4, child4);
                        break;
                }
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        //initial setup of event list
        populateEventList(group, child);

        //return fragment layout to main activity
        return layoutView;
    }

    private void populateEventList(String eventList[], String eventDetails[][][])
    {
        //this bit sets up an adapter for the ListView to use to display events
        //first we set up the parameters for the very confusing SimpleExpandableListAdapter class
        List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
        List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
        for (int i = 0; i < eventList.length; i++) {
            Map<String, String> curGroupMap = new HashMap<String, String>();
            groupData.add(curGroupMap);
            curGroupMap.put("GROUP_NAME", eventList[i]);

            List<Map<String, String>> children = new ArrayList<Map<String, String>>();
            for (int j = 0; j < eventDetails[i].length; j++) {
                Map<String, String> curChildMap = new HashMap<String, String>();
                Map<String, String> curChildMap2 = new HashMap<String, String>();
                children.add(curChildMap);
                //children.add(curChildMap2);
                curChildMap.put("CHILD_NAME1", eventDetails[i][j][0]);
                curChildMap.put("CHILD_NAME2", "Speaker: " + eventDetails[i][j][1]);
            }

            childData.add(children);

        }

        // set up expandable event list
        mAdapter = new SimpleExpandableListAdapter(this.getContext(), groupData,
                R.layout.expandable_list_item,
                new String[] { "GROUP_NAME" }, new int[] { R.id.expandable_text_view },
                childData, R.layout.expanded_list_item,
                new String[] { "CHILD_NAME1", "CHILD_NAME2" }, new int[] { R.id.expanded_text_view, R.id.expanded_text_view2});
        ExpandableListView lv = layoutView.findViewById(R.id.listView);
        lv.setChildDivider(getResources().getDrawable(R.color.transparent));
        lv.setAdapter(mAdapter);
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
