package com.lksnext.lksparking.viewmodel;

import android.util.Log;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.lksnext.lksparking.data.DataRepository;
import com.lksnext.lksparking.data.TipoVehiculo;
import com.lksnext.lksparking.domain.Callback;
import com.lksnext.lksparking.domain.Hora;
import com.lksnext.lksparking.domain.Plaza;
import com.lksnext.lksparking.domain.Reserva;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReservationViewModel extends ViewModel {

    private MutableLiveData<String> reservaData = new MutableLiveData<>();
    private MutableLiveData<List<Reserva>> reservas = new MutableLiveData<>();

    private MutableLiveData<String> selectedDate = new MutableLiveData<>();

    public LiveData<String> getSelectedDate() {
        return selectedDate;
    }

    public ReservationViewModel() {
        // Inicializa con la fecha actual
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        selectedDate.setValue(currentDate);
    }

    public void showTimePicker(FragmentManager fragmentManager, TextInputEditText timeInputEditText) {
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Appointment time")
                .build();

        timePicker.addOnPositiveButtonClickListener(dialog -> {
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();
            String formattedTime = String.format("%02d:%02d", hour, minute);
            timeInputEditText.setText(formattedTime);
        });

        timePicker.show(fragmentManager, "timePicker");
    }

    public void setSelectedDate(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String formattedDate = String.format("%02d/%02d/%d", day, month + 1, year);
        selectedDate.setValue(formattedDate);
    }

    //PARA PROBAR
    public Reserva crearReservaEjemplo() {
        // Aquí puedes crear un objeto Reserva con datos de ejemplo
        String fecha = "2024-06-28";
        String usuario = "usuario5";
        Plaza plaza = new Plaza(5, TipoVehiculo.ELECTRICO, 5);
        Hora hora = new Hora(17, 30);

        // Crear objeto Reserva con los datos
        return new Reserva(fecha, usuario, plaza, hora);
    }


    public LiveData<String> getReservaData() {
        return reservaData;
    }
    public void addReserva() {
        Reserva reserva = crearReservaEjemplo();
        String TAG = "MiApp";
        DataRepository.getInstance().addReserva(reserva, new DataRepository.Callback() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "Reserva añadida correctamente");
            }

            @Override
            public void onFailure() {
                reservaData.setValue("ERROR AL AÑADIR RESERVA");
                Log.e(TAG, "Todo mal");
            }
        });
    }
}
