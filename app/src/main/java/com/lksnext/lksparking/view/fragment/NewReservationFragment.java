package com.lksnext.lksparking.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.datepicker.MaterialDatePicker;

import com.lksnext.lksparking.R;
import com.lksnext.lksparking.databinding.FragmentNewReservationBinding;
import com.lksnext.lksparking.view.activity.LoginActivity;
import com.lksnext.lksparking.view.activity.MainActivity;
import com.lksnext.lksparking.viewmodel.RegisterViewModel;
import com.lksnext.lksparking.viewmodel.ReservationViewModel;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewReservationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewReservationFragment extends Fragment {

    public NewReservationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     * @return A new instance of fragment ReservationsFragment.
     */
    public static NewReservationFragment newInstance() {
        return new NewReservationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Usar Data Binding para inflar el layout
        FragmentNewReservationBinding binding = FragmentNewReservationBinding.inflate(inflater, container, false);
        ReservationViewModel reservationViewModel = new ViewModelProvider(this).get(ReservationViewModel.class);
        binding.setReservationViewModel(reservationViewModel);
        binding.setLifecycleOwner(this);

        MaterialDatePicker<Long> datePicker ;

        // Configurar el DatePicker
        datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        // Configurar el OnClickListener del botón
        Button calendarButton = binding.calendarButton;
        calendarButton.setOnClickListener(v -> datePicker.show(getChildFragmentManager(), "datePickerTag"));

        // Manejar la fecha seleccionada y actualizar el ViewModel
        datePicker.addOnPositiveButtonClickListener(reservationViewModel::setSelectedDate);

        return binding.getRoot();
    }
}