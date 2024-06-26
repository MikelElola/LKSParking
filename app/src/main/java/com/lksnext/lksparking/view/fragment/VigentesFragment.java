package com.lksnext.lksparking.view.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
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
        Context context = requireContext();

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

        Button editButton = reservaView.findViewById(R.id.editButton);
        editButton.setOnClickListener(v -> {
            // Crear y configurar el cuadro de diálogo para editar la hora
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View dialogView = inflater.inflate(R.layout.dialog_edit_time, null);
            EditText editedTimeEditText = dialogView.findViewById(R.id.editedTimeEditText);
            editedTimeEditText.setText(timeTextView.getText()); // Mostrar el texto actual en el EditText

            builder.setTitle("Editar Hora")
                    .setView(dialogView)
                    .setPositiveButton("Guardar", (dialog, which) -> {
                        // Obtener el nuevo valor del texto de la hora del EditText
                        String editedTime = editedTimeEditText.getText().toString().trim();
                        if (vigentesViewModel.isValidTime(editedTime)) {
                            // Actualizar el texto de la hora en el TextView
                            timeTextView.setText(editedTime);
                            // Aquí puedes guardar el cambio si es necesario
                        } else {
                            // Mostrar un mensaje de error al usuario si el formato no es válido
                            Toast.makeText(context, "Formato de hora no válido.", Toast.LENGTH_SHORT).show();
                            Toast.makeText(context, "Debe ser HH:mm-HH:mm, y la hora final debe ser posterior a la inicial.", Toast.LENGTH_SHORT).show();


                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
        Button cancelButton = reservaView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v-> new MaterialAlertDialogBuilder(context)
                .setTitle("Confirmar cancelación")
                .setMessage("¿Estás seguro que deseas cancelar esta reserva?")
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    // Lógica para cancelar la reserva
                    vigentesViewModel.cancelReserva(reserva.getId());
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    // No se hace nada al cancelar
                })
                .show());


        reservasContainer.addView(reservaView);
    }
}