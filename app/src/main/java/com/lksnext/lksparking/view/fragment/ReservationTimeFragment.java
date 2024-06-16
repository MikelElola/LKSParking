package com.lksnext.lksparking.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.lksnext.lksparking.R;
import com.lksnext.lksparking.viewmodel.ReservationTimeViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReservationTimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservationTimeFragment extends Fragment {

    private ReservationTimeViewModel reservationTimeViewModel;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReservationTimeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReservationTimeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReservationTimeFragment newInstance(String param1, String param2) {
        ReservationTimeFragment fragment = new ReservationTimeFragment();
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
       View view= inflater.inflate(R.layout.fragment_reservation_time, container, false);
       TextInputEditText timeInputEditText = view.findViewById(R.id.start_time_input_edit_text);

       reservationTimeViewModel = new ViewModelProvider(this).get(ReservationTimeViewModel.class);
       timeInputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservationTimeViewModel.showTimePicker(getChildFragmentManager(),timeInputEditText);
            }
       });

       return view;
    }
}