package tigerhacks.android.tigerhacksapp.sponsors;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.Objects;

import tigerhacks.android.tigerhacksapp.HomeScreenActivity;
import tigerhacks.android.tigerhacksapp.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SponsorsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SponsorsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SponsorsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View layout;
    private HomeScreenActivity home;

    private OnFragmentInteractionListener mListener;

    public SponsorsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SponsorsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SponsorsFragment newInstance(String param1, String param2) {
        SponsorsFragment fragment = new SponsorsFragment();
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
        layout = inflater.inflate(R.layout.fragment_sponsors, container, false);
        home = (HomeScreenActivity) getActivity();
        Log.e("TEST", "view loaded");
        return layout;
    }

    public void onStart()
    {
        super.onStart();
        home.onFragmentsReady();
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
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void loadSponsorData(final SponsorList list) {
        final LinearLayout pLayout = layout.findViewById(R.id.platinumLayout);
        LinearLayout gLayout = layout.findViewById(R.id.goldLayout);
        LinearLayout sLayout = layout.findViewById(R.id.silverLayout);
        LinearLayout bLayout = layout.findViewById(R.id.bronzeLayout);

        for (final Sponsor sponsor : list.getSponsors())
        {
            ImageView image = new ImageView(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
            layoutParams.height = dpToPx(100);
            layoutParams.setMargins(0, 32, 0, 32);
            image.setLayoutParams(layoutParams);
            image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Glide.with(image).load(sponsor.getImage()).into(image);
            switch(sponsor.getLevel())
            {
                case "Platinum":
                    pLayout.addView(image);
                    break;
                case "Gold":
                    gLayout.addView(image);
                    break;
                case "Silver":
                    sLayout.addView(image);
                    break;
                case "Bronze":
                    bLayout.addView(image);
                    break;
            }

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("TEST", "clicked image");
                    startActivity(SponsorDetailActivity.newInstance(getContext(), sponsor));
//                    SponsorDetailDialog dialog = new SponsorDetailDialog(sponsor);
//                    dialog.show(Objects.requireNonNull(getActivity()).getFragmentManager(),"tag");

                    //dialog.getDialog().getWindow().setAttributes();
                }
            });

        }
    }

    private int dpToPx(int dp) {
        float density = Objects.requireNonNull(getContext()).getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }
}
