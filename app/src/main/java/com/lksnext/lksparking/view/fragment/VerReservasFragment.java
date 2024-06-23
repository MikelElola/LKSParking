package com.lksnext.lksparking.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lksnext.lksparking.R;
import com.lksnext.lksparking.databinding.FragmentNewReservationBinding;
import com.lksnext.lksparking.databinding.FragmentVerReservasBinding;
import com.lksnext.lksparking.domain.Plaza;
import com.lksnext.lksparking.domain.Reserva;
import com.lksnext.lksparking.viewmodel.VerReservasViewModel;

import org.w3c.dom.Text;

import java.util.List;

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

        verReservasViewModel.getReservasMes();
        /*TextView textPrueba = binding.textPrueba;

        verReservasViewModel.getReservas().observe(getViewLifecycleOwner(), reservas -> {
            textPrueba.setText(reservas.toString());
        });*/
        // Observar cambios en la lista de reservas
        verReservasViewModel.getReservas().observe(getViewLifecycleOwner(), reservas -> {
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

    public void addItem(FragmentVerReservasBinding binding,LayoutInflater inflater, Reserva reserva){
        LinearLayout reservasContainer = binding.reservasContainer;
        View reservaView = inflater.inflate(R.layout.item_reserva_historial, null);

        //Plaza plaza = reserva.getPlaza();
        //Log.e("MiApp", "Mi reserva: " + plaza.toString());
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

        // Agregar la vista de reserva al contenedor
        reservasContainer.addView(reservaView);
    }
}