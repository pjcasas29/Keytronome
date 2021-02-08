package com.example.keytronome.ui;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.keytronome.R;
import com.example.keytronome.viewmodels.MainActivityViewModel;

import org.w3c.dom.Text;

import static com.example.keytronome.R.color.design_default_color_primary_dark;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimeSignatureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeSignatureFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ImageView twofour;
    private ImageView fourfour;
    private ImageView sixeight;
    private ImageView threefour;

    private String mParam1;
    private String mParam2;
    private MainActivityViewModel viewModel;

    public TimeSignatureFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimeSignatureFragment.
     */
    public static TimeSignatureFragment newInstance(String param1, String param2) {
        TimeSignatureFragment fragment = new TimeSignatureFragment();
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
        return inflater.inflate(R.layout.fragment_time_signature, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        twofour = getActivity().findViewById(R.id.twoFourButton);
        twofour.setOnClickListener(view1 -> timeSigSelected((ImageView) view1));
        fourfour = getActivity().findViewById(R.id.fourFourButton);
        fourfour.setOnClickListener(view13 -> timeSigSelected((ImageView) view13));
        threefour = getActivity().findViewById(R.id.threeFourButton);
        threefour.setOnClickListener(view12 -> timeSigSelected((ImageView) view12));
        sixeight = getActivity().findViewById(R.id.sixEightButton);
        sixeight.setOnClickListener(view14 -> timeSigSelected((ImageView) view14));

        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        String timeSig = viewModel.getTimeSig().getValue();

        if (timeSig.equals("4/4")) {
            timeSigSelected(fourfour);
        } else if (timeSig.equals("3/4")) {
            timeSigSelected(threefour);
        } else if (timeSig.equals("6/8")) {
            timeSigSelected(sixeight);
        } else if (timeSig.equals("2/4")) {
            timeSigSelected(twofour);
        }
    }

    private void timeSigSelected(ImageView view) {
        String unfocusedColor = "#00ffffff";
        twofour.setBackgroundColor(Color.parseColor(unfocusedColor));
        fourfour.setBackgroundColor(Color.parseColor(unfocusedColor));
        threefour.setBackgroundColor(Color.parseColor(unfocusedColor));
        sixeight.setBackgroundColor(Color.parseColor(unfocusedColor));
        //focused
        view.setBackgroundColor(Color.parseColor("#10ffffff"));

        if(view.getId()==R.id.twoFourButton) {
            viewModel.setTimeSig("2/4");
        }else if(view.getId()==R.id.fourFourButton){
            viewModel.setTimeSig("4/4");
        }else if(view.getId()==R.id.sixEightButton){
            viewModel.setTimeSig("6/8");
        }else if(view.getId()==R.id.threeFourButton){
            viewModel.setTimeSig("3/4");
        }
    }
}