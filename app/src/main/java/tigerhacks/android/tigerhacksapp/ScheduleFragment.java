package tigerhacks.android.tigerhacksapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tigerhacks.android.tigerhacksapp.ScheduleCardView.Day;


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

        Retrofit tigerHacksRetrofit = new Retrofit.Builder()
                .baseUrl("https://n61dynih7d.execute-api.us-east-2.amazonaws.com/production/tigerhacksPrizes/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TigerHacksService service = tigerHacksRetrofit.create(TigerHacksService.class);
        Call<ScheduleItemList> prizes = service.listItems("https://n61dynih7d.execute-api.us-east-2.amazonaws.com/production/tigerhacksSchedule");
        prizes.enqueue(new Callback<ScheduleItemList>() {
            @Override
            public void onResponse(Call<ScheduleItemList> call, Response<ScheduleItemList> response) {
                int statusCode = response.code();
                ScheduleItemList scheduleList = response.body();
                //progressBar.setVisibility(View.GONE);
                populateSchedule(scheduleList);
                Log.e("HEYERROR", "Called succeeded");
            }

            @Override
            public void onFailure(Call<ScheduleItemList> call, Throwable t) {
                Log.e("HEYERROR", "Call failed");
                Snackbar.make(layoutView, "TigerHacks API call failed. Make sure you are connected to the internet.", Snackbar.LENGTH_SHORT).show();
            }
        });

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
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void populateSchedule(ScheduleItemList scheduleList)
    {
        if(scheduleList == null)
        {
            return;
        }
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

            if(card.getDay() == currentDay) {
                cardLayoutView.addView(card);
            }
        }
    }
}
