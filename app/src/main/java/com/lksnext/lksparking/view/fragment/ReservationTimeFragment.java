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
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.lksnext.lksparking.R;
import com.lksnext.lksparking.domain.Hora;
import com.lksnext.lksparking.domain.Plaza;
import com.lksnext.lksparking.domain.Reserva;
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
        assert getArguments() != null;
        String selectedDate = getArguments().getString("selectedDate");
        Plaza plaza = getArguments().getParcelable("plaza");

        String tag = "ReservationTimeFragment";
        // Usar los datos como sea necesario
        Log.i(tag, "Selected Date: " + selectedDate);
        if (plaza != null) {
            Log.i(tag, "Plaza Tipo: " + plaza.getTipo());
            Log.i(tag, "Plaza Pos: " + plaza.getPos());
        }


       ReservationViewModel reservationViewModel = new ViewModelProvider(this).get(ReservationViewModel.class);
        startTimeText.setOnClickListener(v -> reservationViewModel.showTimePicker(getChildFragmentManager(), startTimeText, true));
        endTimeText.setOnClickListener(v -> reservationViewModel.showTimePicker(getChildFragmentManager(), endTimeText, false));

        Button reservarButton = view.findViewById(R.id.reservarButton);
        reservarButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);

            // Obtener las horas de inicio y fin seleccionadas
            assert reservationViewModel.getSelectedStartTime().getValue() != null;
            long startHour = reservationViewModel.getSelectedStartTime().getValue();
            assert reservationViewModel.getSelectedEndTime().getValue() != null;
            long endHour = reservationViewModel.getSelectedEndTime().getValue();

            // Validar las horas seleccionadas
            ReservationViewModel.ReservaValidationResult validationResult = reservationViewModel.isTimeAvailable(startHour, endHour);
            if (!validationResult.isValid()) {
                // Mostrar mensaje de error según la validación
                Toast.makeText(requireContext(), validationResult.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Hora hora = new Hora(startHour, endHour);
                Log.i("MiApp","Hora de inicio: "+ startHour);
                Log.i("MiApp","Hora final: "+ endHour);

                // Llamar al método addReserva del ViewModel
                Reserva nuevaReserva = reservationViewModel.addReserva(selectedDate, plaza, hora, requireContext());
                //Crear el bundle
                Bundle bundle = new Bundle();
                bundle.putParcelable("reserva", nuevaReserva);
                navController.navigate(R.id.vigentesFragment, bundle);

            }
        });
       return view;
    }
}