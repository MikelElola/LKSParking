package com.lksnext.lksparking.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lksnext.lksparking.R;
import com.lksnext.lksparking.databinding.FragmentNewReservationBinding;
import com.lksnext.lksparking.databinding.FragmentVerReservasBinding;
import com.lksnext.lksparking.viewmodel.VerReservasViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VerReservasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VerReservasFragment extends Fragment {


    public VerReservasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     * @return A new instance of fragment VerReservasFragment.
     */
    public static VerReservasFragment newInstance() {
        return new VerReservasFragment();
    }

    private VerReservasViewModel verReservasViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentVerReservasBinding binding = FragmentVerReservasBinding.inflate(inflater, container, false);
        verReservasViewModel = new ViewModelProvider(this).get(VerReservasViewModel.class);
        binding.setVerReservasViewModel(verReservasViewModel);
        return binding.getRoot();
    }
}