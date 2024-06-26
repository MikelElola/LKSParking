package com.lksnext.lksparking.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.lksnext.lksparking.R;
import com.lksnext.lksparking.data.TipoVehiculo;
import com.lksnext.lksparking.domain.Hora;
import com.lksnext.lksparking.domain.Plaza;
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
       TextInputEditText startTimeText = view.findViewById(R.id.start_time_input_edit_text);
       TextInputEditText endTimeText = view.findViewById(R.id.end_time_input_edit_text);

        // Obtener los argumentos
        String selectedDate = getArguments().getString("selectedDate");
        Plaza plaza = getArguments().getParcelable("plaza");

        // Usar los datos como sea necesario
        Log.i("ReservationTimeFragment", "Selected Date: " + selectedDate);
        if (plaza != null) {
            Log.i("ReservationTimeFragment", "Plaza Tipo: " + plaza.getTipo());
            Log.i("ReservationTimeFragment", "Plaza Pos: " + plaza.getPos());
        }


       ReservationViewModel reservationViewModel = new ViewModelProvider(this).get(ReservationViewModel.class);
        startTimeText.setOnClickListener(v -> reservationViewModel.showTimePicker(getChildFragmentManager(), startTimeText, true));
        endTimeText.setOnClickListener(v -> reservationViewModel.showTimePicker(getChildFragmentManager(), endTimeText, false));

        Button reservarButton = view.findViewById(R.id.reservarButton);
        reservarButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);

            // Obtener las horas de inicio y fin seleccionadas
            long startHour = reservationViewModel.getSelectedStartTime().getValue();
            long endHour = reservationViewModel.getSelectedEndTime().getValue();

            Hora hora = new Hora(startHour, endHour);
            Log.i("MiApp","Hora de inicio: "+ startHour);
            Log.i("MiApp","Hora final: "+ endHour);

            // Llamar al método addReserva del ViewModel
            reservationViewModel.addReserva(selectedDate, plaza, hora);
            navController.navigate(R.id.newReservationFragment);
        });
       return view;
    }
}