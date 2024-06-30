package com.lksnext.lksparking.viewmodel;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lksnext.lksparking.R;
import com.lksnext.lksparking.data.DataRepository;
import com.lksnext.lksparking.data.TipoVehiculo;
import com.lksnext.lksparking.databinding.FragmentNewReservationBinding;
import com.lksnext.lksparking.domain.Hora;
import com.lksnext.lksparking.domain.Plaza;
import com.lksnext.lksparking.domain.Reserva;
import com.lksnext.lksparking.notification.NotificationReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ReservationViewModel extends ViewModel {


    public static final String MI_APP = "MiApp";
    private final MutableLiveData<List<Reserva>> reservas = new MutableLiveData<>();
    private final MutableLiveData<String> selectedDate = new MutableLiveData<>();
    private final MutableLiveData<Integer> selectedPos = new MutableLiveData<>();
    private final MutableLiveData<TipoVehiculo> selectedType = new MutableLiveData<>();
    private final MutableLiveData<Long> selectedStartTime = new MutableLiveData<>();
    private final MutableLiveData<Long> selectedEndTime = new MutableLiveData<>();
    private final FirebaseAuth firebaseAuth;

    public LiveData<List<Reserva>> getReservas() { return reservas; }
    public LiveData<String> getSelectedDate() {
        return selectedDate;
    }
    public MutableLiveData<Integer> getSelectedPos() {return selectedPos;}
    public void setSelectedPos(int pos) {
        this.selectedPos.setValue(pos);
        Log.i(MI_APP, "Se ha seleccionado la plaza " + pos);
    }
    public MutableLiveData<TipoVehiculo> getSelectedType() {return selectedType;}
    public void setSelectedType(TipoVehiculo selectedType) {
        this.selectedType.setValue(selectedType);
    }
    public MutableLiveData<Long> getSelectedStartTime() {return selectedStartTime;}
    public MutableLiveData<Long> getSelectedEndTime() {return selectedEndTime;}

    public ReservationViewModel() {
        // Inicializa con la fecha actual
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        selectedDate.setValue(currentDate);
        firebaseAuth = FirebaseAuth.getInstance();
        getReservasDia();
    }

    public void showTimePicker(FragmentManager fragmentManager, TextInputEditText timeInputEditText, boolean esInicio) {
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Appointment time")
                .build();

        timePicker.addOnPositiveButtonClickListener(dialog -> {
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();
            @SuppressLint("DefaultLocale") String formattedTime = String.format("%02d:%02d", hour, minute);
            timeInputEditText.setText(formattedTime);
            long selectedTime = hour * 60L + minute;
            if (esInicio) {
                selectedStartTime.setValue(selectedTime);
            } else {
                selectedEndTime.setValue(selectedTime);
            }
        });

        timePicker.show(fragmentManager, "timePicker");
    }

    public void setSelectedDate(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        @SuppressLint("DefaultLocale") String formattedDate = String.format("%02d/%02d/%d", day, month + 1, year);
        selectedDate.setValue(formattedDate);
    }

    public void getReservasDia() {
        String fecha = selectedDate.getValue();
        Calendar calendar = Calendar.getInstance();
        long tiempoActual = calendar.getTimeInMillis();
        if (fecha != null) {
            DataRepository.getInstance().getReservasPorFecha(fecha, tiempoActual, new DataRepository.ReservasCallback<List<Reserva>>() {
                @Override
                public void onSuccess(List<Reserva> reservasList) {
                    reservas.setValue(reservasList);
                }

                @Override
                public void onFailure() {
                    // Manejo de error, puedes añadir un mensaje de log o alguna acción adicional
                    Log.e(MI_APP, "Error al obtener las reservas");
                }
            });
        }
    }

    public Reserva addReserva(String selectedDate, Plaza plaza, Hora hora, Context context) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String usuario = (currentUser != null) ? currentUser.getEmail() : "usuarioDesconocido";

        Reserva reserva = new Reserva(selectedDate, usuario, plaza, hora);
        DataRepository.getInstance().addReserva(reserva, new DataRepository.Callback() {
            @Override
            public void onSuccess() {
                Log.i(MI_APP, "Reserva añadida correctamente");
                // Calcular el tiempo de inicio y fin en milisegundos
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    Date date = dateFormat.parse(selectedDate);

                    if (date != null) {
                        Calendar calendarInicio = Calendar.getInstance();
                        calendarInicio.setTime(date);
                        calendarInicio.set(Calendar.HOUR_OF_DAY, (int) hora.getHoraInicio() / 60);
                        calendarInicio.set(Calendar.MINUTE, (int) hora.getHoraInicio() % 60);
                        calendarInicio.set(Calendar.SECOND, 0);

                        Calendar calendarFin = Calendar.getInstance();
                        calendarFin.setTime(date);
                        calendarFin.set(Calendar.HOUR_OF_DAY, (int) hora.getHoraFin() / 60);
                        calendarFin.set(Calendar.MINUTE, (int) hora.getHoraFin() % 60);
                        calendarFin.set(Calendar.SECOND, 0);

                        long startHourInMillis = calendarInicio.getTimeInMillis();
                        long endHourInMillis = calendarFin.getTimeInMillis();

                        // Programar la notificación 30 minutos antes de la hora de inicio
                        long triggerTimeInicio = startHourInMillis - 30 * 60 * 1000;
                        scheduleReservationNotification(context, reserva.getId() + "_inicio", triggerTimeInicio, "Quedan 30 minutos para que comience tu reserva");

                        // Programar la notificación 15 minutos antes de la hora de fin
                        long triggerTimeFin = endHourInMillis - 15 * 60 * 1000;
                        scheduleReservationNotification(context, reserva.getId() + "_fin", triggerTimeFin, "Quedan 15 minutos para que finalice tu reserva");
                    }
                } catch (ParseException e) {
                    Log.e(MI_APP, "Error al analizar la fecha seleccionada", e);
                }
            }

            @Override
            public void onFailure() {
                Log.e(MI_APP, "Error al añadir la reserva");
            }
        });
        return reserva;
    }
    public void scheduleReservationNotification(Context context, String reservationID, long triggerTime, String message) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("reservation_id", reservationID);
        intent.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        Log.i(MI_APP, "Notificación programada con éxito");

    }

    public void crearPlano(FragmentNewReservationBinding binding, List<Reserva> reservas, Context context) {
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
            assert buttonInicial != null;
            buttonInicial.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.free_parkingslot)));
            int finalI = i+1;
            buttonInicial.setOnClickListener(v -> {
                setSelectedPos(finalI);
                setSelectedType(TipoVehiculo.NORMAL);
            });
        }
        ImageButton[] imageButtonsArray = {
                binding.pos12, binding.pos13, binding.pos14, binding.pos15, binding.pos16,
                binding.pos17, binding.pos18, binding.pos19
        };
        for (int i = 0; i < imageButtonsArray.length; i++) {
            imageButtons.put(12 + i , imageButtonsArray[i]);  // Coloca en el HashMap, la posición comienza desde 12
            ImageButton iButtonInicial = imageButtons.get(12+i);
            assert iButtonInicial != null;
            iButtonInicial.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.free_parkingslot)));
            int finalI = i + 12;
            if (finalI<=14){
                //Si se ha elegido una plaza para minusválidos
                iButtonInicial.setOnClickListener(v -> {
                    setSelectedPos(finalI);
                    setSelectedType(TipoVehiculo.MINUSVALIDO);
                });
            } else if (finalI<=16){
                //Si se ha elegido una plaza para motos
                iButtonInicial.setOnClickListener(v -> {
                    setSelectedPos(finalI);
                    setSelectedType(TipoVehiculo.MOTO);
                });
            } else{
                //Si se ha elegido una plaza para coches eléctricos
                iButtonInicial.setOnClickListener(v -> {
                    setSelectedPos(finalI);
                    setSelectedType(TipoVehiculo.ELECTRICO);
                });
            }
        }
        // Iterar sobre las reservas y actualizar los colores de fondo
        for (Reserva reserva : reservas) {
            int pos = reserva.getPlaza().getPos(); // Obtener la posición de la reserva
            // Verificar si la posición está en el HashMap de botones
            if (botones.containsKey(pos)) {
                Log.i(MI_APP, "Reserva añadida en la plaza normal " + pos);
                Button button = botones.get(pos);
                assert button != null;
                button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.occupied_parkingslot)));
            }
            // Verificar si la posición está en el HashMap de imageButtons
            if (imageButtons.containsKey(pos)) {
                Log.i(MI_APP, "Reserva añadida en la plaza imagen " + pos);
                ImageButton imageButton = imageButtons.get(pos);
                assert imageButton != null;
                imageButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.occupied_parkingslot)));
            }
        }
    }

    public static class ReservaValidationResult {
        private final boolean isValid;
        private final String message;

        public ReservaValidationResult(boolean isValid, String message) {
            this.isValid = isValid;
            this.message = message;
        }

        public boolean isValid() {
            return isValid;
        }

        public String getMessage() {
            return message;
        }
    }
    public ReservaValidationResult isReservaAvailable(String date, int pos) {
        // Parsear la fecha seleccionada
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        try {
            Date formatedDate = dateFormat.parse(date);

            // Obtener la fecha actual y calcular la fecha límite (hoy + 7 días)
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0); // Ajustar la hora a 00:00:00

            Date today = calendar.getTime();

            calendar.add(Calendar.DAY_OF_YEAR, 7);
            Date maxDate = calendar.getTime();

            // Verificar si la fecha seleccionada está dentro del rango permitido
            if (formatedDate.before(today) || formatedDate.after(maxDate)) {
                return new ReservaValidationResult(false, "La fecha seleccionada debe estar dentro de los próximos 7 días.");
            }

            // Verificar si la plaza ya está reservada para la fecha seleccionada
            List<Reserva> reservasList = reservas.getValue();
            if (reservasList != null) {
                for (Reserva reserva : reservasList) {
                    if (reserva.getFecha().equals(date) && reserva.getPlaza().getPos() == pos) {
                        return new ReservaValidationResult(false, "La plaza seleccionada ya está reservada.");
                    }
                }
            }

        } catch (ParseException e) {
            return new ReservaValidationResult(false, "Error al analizar la fecha seleccionada.");
        }

        return new ReservaValidationResult(true, "Reserva disponible.");
    }

    public ReservaValidationResult isTimeAvailable(long startHour, long endHour) {
        if (endHour <= startHour) {
            return new ReservaValidationResult(false, "La hora final debe ser posterior a la hora de inicio.");
        }

        // Calcular la diferencia en minutos
        long differenceInMinutes = (endHour - startHour) % (24 * 60);
        // Verificar si la diferencia es mayor a 9 horas (540 minutos)
        if (differenceInMinutes > 540) {
            return new ReservaValidationResult(false, "El tiempo máximo permitido es de 9 horas.");
        }

        return new ReservaValidationResult(true, "Horario disponible.");
    }
}
