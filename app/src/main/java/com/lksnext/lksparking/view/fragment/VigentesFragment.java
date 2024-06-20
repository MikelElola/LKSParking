package com.lksnext.lksparking.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lksnext.lksparking.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VigentesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VigentesFragment extends Fragment {

    public VigentesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     * @return A new instance of fragment VigentesFragment.
     */
    public static VigentesFragment newInstance() {
        return new VigentesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vigentes, container, false);
    }
}