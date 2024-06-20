package com.lksnext.lksparking.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lksnext.lksparking.R;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ver_reservas, container, false);
    }
}