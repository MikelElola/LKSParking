package com.lksnext.lksparking.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.lksnext.lksparking.databinding.FragmentVigentesBinding;
import com.lksnext.lksparking.domain.Reserva;
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

        // Verificar si se recibió una instancia de Reserva en el Bundle
        if (getArguments() != null && getArguments().containsKey("reserva")) {
            Reserva reserva = getArguments().getParcelable("reserva");
            if (reserva != null) {
                vigentesViewModel.addItem(binding, inflater, reserva, requireContext());
            }
        }

        vigentesViewModel.getReservasVigentes();

        // Observar cambios en la lista de reservas
        vigentesViewModel.getReservas().observe(getViewLifecycleOwner(), reservas -> {
            if (reservas != null) {
                // Limpiar el contenedor antes de agregar nuevas vistas
                binding.reservasContainer.removeAllViews();

                // Iterar sobre cada reserva y agregarla como una vista
                for (Reserva reserva : reservas) {
                    vigentesViewModel.addItem(binding, inflater, reserva, requireContext());
                }
            }
        });

        return binding.getRoot();

    }
}