package com.lksnext.lksparking.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.lksnext.lksparking.R;
import com.lksnext.lksparking.viewmodel.ReservationViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReservationTimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservationTimeFragment extends Fragment {

    public ReservationTimeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     * @return A new instance of fragment ReservationTimeFragment.
     */
    public static ReservationTimeFragment newInstance() {
        return new ReservationTimeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_reservation_time, container, false);
       TextInputEditText timeInputEditText = view.findViewById(R.id.start_time_input_edit_text);

       ReservationViewModel reservationViewModel = new ViewModelProvider(this).get(ReservationViewModel.class);
       timeInputEditText.setOnClickListener(v -> reservationViewModel.showTimePicker(getChildFragmentManager(),timeInputEditText));

       return view;
    }
}