package com.lksnext.lksparking.view.fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;

import com.lksnext.lksparking.R;
import com.lksnext.lksparking.data.DataRepository;
import com.lksnext.lksparking.data.TipoVehiculo;
import com.lksnext.lksparking.databinding.FragmentNewReservationBinding;
import com.lksnext.lksparking.databinding.FragmentVigentesBinding;
import com.lksnext.lksparking.domain.Plaza;
import com.lksnext.lksparking.domain.Reserva;
import com.lksnext.lksparking.view.activity.LoginActivity;
import com.lksnext.lksparking.view.activity.MainActivity;
import com.lksnext.lksparking.viewmodel.RegisterViewModel;
import com.lksnext.lksparking.viewmodel.ReservationViewModel;
import com.lksnext.lksparking.viewmodel.VigentesViewModel;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


        // Configurar el DatePicker
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        // Configurar el OnClickListener del botón
        Button calendarButton = binding.calendarButton;
        calendarButton.setOnClickListener(v -> datePicker.show(getChildFragmentManager(), "datePickerTag"));

        // Manejar la fecha seleccionada y actualizar el ViewModel
        datePicker.addOnPositiveButtonClickListener(reservationViewModel::setSelectedDate);
        // Observar cambios en selectedDate y actualizar la vista
        final TextView dateText = binding.dateText;
        reservationViewModel.getSelectedDate().observe(getViewLifecycleOwner(), selectedDate -> {
            dateText.setText(selectedDate);

            // Obtener reservas para la fecha seleccionada
            reservationViewModel.getReservasDia();
        });

        Button reservarButton = binding.reservarButton;
        reservarButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);

            String selectedDate = reservationViewModel.getSelectedDate().getValue();
            selectedDate = selectedDate.replace("/","-");
            Integer selectedPos = reservationViewModel.getSelectedPos().getValue();
            TipoVehiculo selectedType = reservationViewModel.getSelectedType().getValue();
            if (selectedPos == null) {
                Toast.makeText(requireContext(), "Por favor seleccione una plaza", Toast.LENGTH_SHORT).show();
                return;
            }

            ReservationViewModel.ReservaValidationResult validationResult = reservationViewModel.isReservaAvailable(selectedDate, selectedPos);
            if (validationResult.isValid()) {
                // Crear el Bundle con los datos
                Bundle bundle = new Bundle();
                bundle.putString("selectedDate", selectedDate);
                bundle.putParcelable("plaza", new Plaza(selectedType, selectedPos));

                navController.navigate(R.id.reservationTimeFragment, bundle);
            } else {
                Toast.makeText(requireContext(), validationResult.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Obtener las reservas al cargar la página
        reservationViewModel.getReservasDia();

        reservationViewModel.getReservas().observe(getViewLifecycleOwner(), reservas ->
            // Llamar a la función crearPlano para actualizar los botones según las reservas
            reservationViewModel.crearPlano(binding,reservas,requireContext())
        );

        return binding.getRoot();
    }

}