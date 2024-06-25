package com.lksnext.lksparking.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lksnext.lksparking.R;
import com.lksnext.lksparking.databinding.FragmentNewReservationBinding;
import com.lksnext.lksparking.databinding.FragmentVerReservasBinding;
import com.lksnext.lksparking.databinding.FragmentVigentesBinding;
import com.lksnext.lksparking.domain.Reserva;
import com.lksnext.lksparking.viewmodel.ReservationViewModel;
import com.lksnext.lksparking.viewmodel.VigentesViewModel;

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
        FragmentVigentesBinding binding = FragmentVigentesBinding.inflate(inflater, container, false);
        VigentesViewModel vigentesViewModel = new ViewModelProvider(this).get(VigentesViewModel.class);

        vigentesViewModel.getReservasVigentes();

        // Observar cambios en la lista de reservas
        vigentesViewModel.getReservas().observe(getViewLifecycleOwner(), reservas -> {
            if (reservas != null) {
                // Limpiar el contenedor antes de agregar nuevas vistas
                binding.reservasContainer.removeAllViews();

                // Iterar sobre cada reserva y agregarla como una vista
                for (Reserva reserva : reservas) {
                    addItem(binding, inflater, reserva);
                }
            }
        });

        return binding.getRoot();

    }

    public void addItem(FragmentVigentesBinding binding, LayoutInflater inflater, Reserva reserva){
        LinearLayout reservasContainer = binding.reservasContainer;
        View reservaView = inflater.inflate(R.layout.item_reserva_vigente, null);
        VigentesViewModel vigentesViewModel = new ViewModelProvider(this).get(VigentesViewModel.class);

        ImageView vehicleIcon = reservaView.findViewById(R.id.vehicle_icon);
        vehicleIcon.setImageResource(R.drawable.car_24);
        switch (reserva.getPlaza().getTipo()) {
            case "Normal":
                vehicleIcon.setImageResource(R.drawable.car_24);
                break;
            case "Eléctrico":
                vehicleIcon.setImageResource(R.drawable.baseline_electric_40);
                break;
            case "Moto":
                vehicleIcon.setImageResource(R.drawable.baseline_bike_40);
                break;
            default:
                // Puedes manejar un caso por defecto si es necesario
                break;
        }
        TextView dateTextView = reservaView.findViewById(R.id.textDate);
        dateTextView.setText(reserva.getFecha());
        TextView timeTextView = reservaView.findViewById(R.id.textTime);
        String hora = vigentesViewModel.formatHour((int) reserva.getHora().getHoraInicio())
                + "-" + vigentesViewModel.formatHour((int) reserva.getHora().getHoraFin());
        timeTextView.setText(hora);

        reservasContainer.addView(reservaView);
    }
}