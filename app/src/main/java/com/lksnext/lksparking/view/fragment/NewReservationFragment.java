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
            int selectedPos = reservationViewModel.getSelectedPos().getValue();
            TipoVehiculo selectedType = reservationViewModel.getSelectedType().getValue();
            Plaza plaza = new Plaza(selectedPos, selectedType, selectedPos);

            // Crear el Bundle con los datos
            Bundle bundle = new Bundle();
            bundle.putString("selectedDate", selectedDate);
            bundle.putParcelable("plaza", plaza);

            navController.navigate(R.id.reservationTimeFragment, bundle);
        });

        reservationViewModel.getReservas().observe(getViewLifecycleOwner(), reservas -> {
            // Llamar a la función crearPlano para actualizar los botones según las reservas
            crearPlano(binding, reservas);
        });
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
    public void crearPlano(FragmentNewReservationBinding binding, List<Reserva> reservas) {
        ReservationViewModel reservationViewModel = new ViewModelProvider(this).get(ReservationViewModel.class);
        Map<Integer, Button> botones = new HashMap<>();
        Map<Integer, ImageButton> imageButtons = new HashMap<>();

        Button[] botonesArray = {
                binding.pos1, binding.pos2, binding.pos3, binding.pos4, binding.pos5,
                binding.pos6, binding.pos7, binding.pos8, binding.pos9, binding.pos10,
                binding.pos11
        };
        for (int i = 0; i < botonesArray.length; i++) {
            botones.put(i + 1, botonesArray[i]);// Coloca en el HashMap, la posición comienza desde 1
            Button buttonInicial = botones.get(i+1);
            buttonInicial.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.free_parkingslot)));
            int finalI = i+1;
            buttonInicial.setOnClickListener(v -> {
                    reservationViewModel.setSelectedPos(finalI);
                    reservationViewModel.setSelectedType(TipoVehiculo.NORMAL);
            });
        }
        ImageButton[] imageButtonsArray = {
                binding.pos12, binding.pos13, binding.pos14, binding.pos15, binding.pos16,
                binding.pos17, binding.pos18, binding.pos19
        };
        for (int i = 0; i < imageButtonsArray.length; i++) {
            imageButtons.put(12 + i , imageButtonsArray[i]);  // Coloca en el HashMap, la posición comienza desde 12
            ImageButton iButtonInicial = imageButtons.get(12+i);
            iButtonInicial.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.free_parkingslot)));
            int finalI = i + 12;
            if (finalI<=14){
                //Si se ha elegido una plaza para minusválidos
                iButtonInicial.setOnClickListener(v -> {
                    reservationViewModel.setSelectedPos(finalI);
                    reservationViewModel.setSelectedType(TipoVehiculo.MINUSVALIDO);
                });
            } else if (finalI<=16){
                //Si se ha elegido una plaza para motos
                iButtonInicial.setOnClickListener(v -> {
                    reservationViewModel.setSelectedPos(finalI);
                    reservationViewModel.setSelectedType(TipoVehiculo.MOTO);
                });
            } else{
                //Si se ha elegido una plaza para coches eléctricos
                iButtonInicial.setOnClickListener(v -> {
                    reservationViewModel.setSelectedPos(finalI);
                    reservationViewModel.setSelectedType(TipoVehiculo.ELECTRICO);
                });
            }

        }

        // Iterar sobre las reservas y actualizar los colores de fondo
        for (Reserva reserva : reservas) {
            int pos = reserva.getPlaza().getPos(); // Obtener la posición de la reserva
            // Verificar si la posición está en el HashMap de botones
            if (botones.containsKey(pos)) {
                Log.i("MiApp", "Reserva añadida en la plaza normal " + pos);
                Button button = botones.get(pos);
                button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.occupied_parkingslot)));
            }
            // Verificar si la posición está en el HashMap de imageButtons
            if (imageButtons.containsKey(pos)) {
                Log.i("MiApp", "Reserva añadida en la plaza imagen " + pos);
                ImageButton imageButton = imageButtons.get(pos);
                imageButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.occupied_parkingslot)));
            }
        }
    }
}