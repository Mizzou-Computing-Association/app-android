package tigerhacks.android.tigerhacksapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PrizesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PrizesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrizesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private PrizeCardView card1;
    private View layoutView;

    private OnFragmentInteractionListener mListener;

    public PrizesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PrizesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PrizesFragment newInstance(String param1, String param2) {
        PrizesFragment fragment = new PrizesFragment();
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

        layoutView = inflater.inflate(R.layout.fragment_prizes, container, false);
        LinearLayout ll = layoutView.findViewById(R.id.linearView);

        //create cards. This is static, and will be replaced by dynamic creation through the API
        PrizeCardView card1 = (PrizeCardView)LayoutInflater.from(ll.getContext()).inflate(R.layout.prize_card_layout, ll, false);
        PrizeCardView card2 = (PrizeCardView)LayoutInflater.from(ll.getContext()).inflate(R.layout.prize_card_layout, ll, false);
        PrizeCardView card3 = (PrizeCardView)LayoutInflater.from(ll.getContext()).inflate(R.layout.prize_card_layout, ll, false);
        PrizeCardView card4 = (PrizeCardView)LayoutInflater.from(ll.getContext()).inflate(R.layout.prize_card_layout, ll, false);

        card1.setTitle("A Great Test");
        card2.setTitle("An Even Greater Test");

        card1.setImage(R.drawable.im_laf_map_1);

        card1.setDescription("A great description.");
        card2.setDescription("An even better description.");
        card1.setCriteria(new String[]{"Here's criteria one","Here's criteria two"});
        card1.setPrizes(new String[]{"Here's prize one", "Here's prize two"});

        ArrayList<PrizeCardView> cardList = new ArrayList<PrizeCardView>();
        cardList.add(card1);
        cardList.add(card2);
        cardList.add(card3);
        cardList.add(card4);

        for(PrizeCardView p : cardList)
        {
            ll.addView(p);
            p.onClickAction(layoutView);
        }



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
}
