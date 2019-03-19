package tigerhacks.android.tigerhacksapp.schedule;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;

import tigerhacks.android.tigerhacksapp.HomeScreenActivity;
import tigerhacks.android.tigerhacksapp.R;
import tigerhacks.android.tigerhacksapp.schedule.ScheduleCardView.Day;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScheduleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LinearLayout cardLayoutView;
    private View layoutView;
    private TabLayout tabLayout;
    private Day currentDay = Day.FRIDAY;
    private ArrayList<ScheduleItem> cardList;
    private ProgressBar progressBar;
    public HomeScreenActivity home;

    private OnFragmentInteractionListener mListener;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layoutView = inflater.inflate(R.layout.fragment_schedule, container, false);
        tabLayout = layoutView.findViewById(R.id.tabLayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e("TEST","Selected");
                if(tab.getPosition() == 0)
                {
                    currentDay = Day.FRIDAY;
                    addDayEvents(cardList);
                }
                else if(tab.getPosition() == 1)
                {
                    currentDay = Day.SATURDAY;
                    addDayEvents(cardList);
                }
                else if(tab.getPosition() == 2)
                {
                    currentDay = Day.SUNDAY;
                    addDayEvents(cardList);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        cardLayoutView = layoutView.findViewById(R.id.cardLinearLayout);
        progressBar = layoutView.findViewById(R.id.progressBar2);
        home = (HomeScreenActivity)getActivity();
        return layoutView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
     * See the Android Training lesson <a href
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void onStart()
    {
        super.onStart();
        home.onFragmentsReady();
    }

    public void loadSchedule(ScheduleItemList scheduleList)
    {
        if(scheduleList == null)
        {
            return;
        }
        progressBar.setVisibility(View.GONE);
        cardList = (ArrayList<ScheduleItem>)scheduleList.getSchedule();
        addDayEvents(cardList);
    }
    public void addDayEvents(ArrayList<ScheduleItem> list)
    {
        cardLayoutView.removeAllViews();
        for(ScheduleItem item : list)
        {
            ScheduleCardView card = (ScheduleCardView)LayoutInflater.from(cardLayoutView.getContext()).inflate(R.layout.schedule_card_layout, cardLayoutView, false);
            card.setTitle(item.getTitle());
            card.setLocation(item.getLocation());
            card.setTime(item.getTime());
            card.setDescription(item.getDescription());
            card.onClickAction(layoutView);
            if(card.getDay() == currentDay) {
                cardLayoutView.addView(card);
            }
        }
    }
}
