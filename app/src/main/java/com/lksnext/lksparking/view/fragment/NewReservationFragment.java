package com.lksnext.lksparking.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.datepicker.MaterialDatePicker;

import com.lksnext.lksparking.R;
import com.lksnext.lksparking.data.TipoVehiculo;
import com.lksnext.lksparking.databinding.FragmentNewReservationBinding;
import com.lksnext.lksparking.domain.Plaza;
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
        reservationViewModel.getSelectedDate().observe(getViewLifecycleOwner(), dateText::setText);

        Button reservarButton = binding.reservarButton;
        reservarButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);

            String selectedDate = reservationViewModel.getSelectedDate().getValue();
            selectedDate = selectedDate.replace("/","-");
            Plaza plaza = new Plaza(5, TipoVehiculo.NORMAL, 5);

            // Crear el Bundle con los datos
            Bundle bundle = new Bundle();
            bundle.putString("selectedDate", selectedDate);
            bundle.putParcelable("plaza", plaza);

            navController.navigate(R.id.reservationTimeFragment, bundle);
        });

       /* Button dbButton = binding.databasebutton;
        dbButton.setOnClickListener(v -> reservationViewModel.addReserva());

        /**
         *
         * EJEMPLO DE CAMBIAR UN TEXTO CON LA INFORMACION DE LA BD
        TextView dbText= binding.databaseText;
        Button dbButton = binding.databasebutton;
        dbButton.setOnClickListener(v -> reservationViewModel.addReserva());
        reservationViewModel.getReservaData().observe(getViewLifecycleOwner(), reservaData -> {
            // Actualizar la interfaz de usuario con el nuevo valor de reservaData
            // Por ejemplo, mostrarlo en un TextView
            dbText.setText(reservaData);
        });
        */
        return binding.getRoot();
    }
}