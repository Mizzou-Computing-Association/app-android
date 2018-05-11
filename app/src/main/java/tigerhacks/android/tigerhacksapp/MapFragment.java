package tigerhacks.android.tigerhacksapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.widget.BottomNavigationView;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;


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
    private Button btn1, btn2, btn3, btn4;
    private OnFragmentInteractionListener mListener;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layoutView = inflater.inflate(R.layout.fragment_map, container, false);
        return layoutView;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        //this section fixes the events list layout and prevents it from
        //rendering events under the bottom navigation bar
        final ListView lv = layoutView.findViewById(R.id.listView);
        ViewTreeObserver vto = lv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                lv.setLayoutParams(
                        new LinearLayout.LayoutParams(lv.getLayoutParams().width, lv.getHeight() - getActivity().findViewById(R.id.navigation).getHeight()));
                ViewTreeObserver obs = lv.getViewTreeObserver();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeOnGlobalLayoutListener(this);
                } else {
                    obs.removeGlobalOnLayoutListener(this);
                }
            }

        });


        btn1 = layoutView.findViewById(R.id.Floor1Btn);
        btn2 = layoutView.findViewById(R.id.Floor2Btn);
        btn3 = layoutView.findViewById(R.id.Floor3Btn);
        btn4 = layoutView.findViewById(R.id.Floor4Btn);
        mapView = layoutView.findViewById(R.id.mapView);

        //add button onclick events. Handles button visuals and map changing
        btn1.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                btn1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btn2.setBackgroundColor(Color.GRAY);
                btn3.setBackgroundColor(Color.GRAY);
                btn4.setBackgroundColor(Color.GRAY);
                mapView.setImageDrawable(getResources().getDrawable(R.drawable.im_laf_map_1));
            }
        });
        btn2.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                btn1.setBackgroundColor(Color.GRAY);
                btn2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btn3.setBackgroundColor(Color.GRAY);
                btn4.setBackgroundColor(Color.GRAY);
                mapView.setImageDrawable(getResources().getDrawable(R.drawable.im_laf_map_2));
            }
        });
        btn3.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                btn1.setBackgroundColor(Color.GRAY);
                btn2.setBackgroundColor(Color.GRAY);
                btn3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btn4.setBackgroundColor(Color.GRAY);
                mapView.setImageDrawable(getResources().getDrawable(R.drawable.im_laf_map_3));
            }
        });
        btn4.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                btn1.setBackgroundColor(Color.GRAY);
                btn2.setBackgroundColor(Color.GRAY);
                btn3.setBackgroundColor(Color.GRAY);
                btn4.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mapView.setImageDrawable(getResources().getDrawable(R.drawable.im_laf_map_4));
            }
        });

        //default button setup
        btn1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        btn2.setBackgroundColor(Color.GRAY);
        btn3.setBackgroundColor(Color.GRAY);
        btn4.setBackgroundColor(Color.GRAY);

        //this bit sets up an adapter for the ListView to use to display events
        String[] list = {"test","test2","test3","test4","test5","test6","test7","test8"};
        ArrayAdapter adapter = new ArrayAdapter<String>(this.getContext(), R.layout.text_view, list);
        ListView listView = (ListView) layoutView.findViewById(R.id.listView);
        listView.setAdapter(adapter);
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
