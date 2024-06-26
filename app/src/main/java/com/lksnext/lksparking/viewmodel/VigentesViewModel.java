package com.lksnext.lksparking.viewmodel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lksnext.lksparking.R;
import com.lksnext.lksparking.data.DataRepository;
import com.lksnext.lksparking.databinding.FragmentVigentesBinding;
import com.lksnext.lksparking.domain.Reserva;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VigentesViewModel extends ViewModel {
    public static final String MI_APP = "MiApp";
    private static final Logger LOGGER = Logger.getLogger(VigentesViewModel.class.getName());
    private final MutableLiveData<String> mesActual = new MutableLiveData<>();
    private final MutableLiveData<Integer> yearActual = new MutableLiveData<>();
    private final MutableLiveData<List<Reserva>> reservas = new MutableLiveData<>();
    private final FirebaseAuth firebaseAuth;
    public LiveData<List<Reserva>> getReservas() {return reservas;}

    public VigentesViewModel() {
        //Inicializa con el mes actual
        Calendar calendar = Calendar.getInstance();
        int mesNum = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        String[] nombresMeses = {"enero", "febrero", "marzo", "abril", "mayo", "junio",
                "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"};
        String mes = nombresMeses[mesNum];
        mesActual.setValue(mes);
        yearActual.setValue(year);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void getReservasVigentes(){
        Calendar calendar = Calendar.getInstance();
        long tiempoActual = calendar.getTimeInMillis();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String usuario = (currentUser != null) ? currentUser.getEmail() : "usuarioDesconocido";
        DataRepository.getInstance().getReservasVigentes(tiempoActual, usuario, new DataRepository.ReservasCallback<List<Reserva>>() {
            @Override
            public void onSuccess(List<Reserva> reservasObtenidas) {
                reservas.setValue(reservasObtenidas);
                Log.i(MI_APP, "Reservas vigentes obtenidas para el usuario " + usuario);
            }

            @Override
            public void onFailure() {
                Log.e(MI_APP, "Error al obtener reservas vigentes");
            }
        });
    }

    @SuppressLint("DefaultLocale")
    public String formatHour(long hora) {
        // Asumiendo que el formato es HHMM
        int parteHora = (int) (hora / 60);  // Extraer la parte de la hora
        int parteMinuto = (int) (hora % 60); // Extraer la parte de los minutos

        return String.format("%02d:%02d", parteHora, parteMinuto); // Formato HH:mm
    }

    public void editReserva(Reserva reserva){
        DataRepository.getInstance().updateReserva(reserva, new DataRepository.Callback() {
            @Override
            public void onSuccess() {
                Log.i(MI_APP, "Reserva actualizada correctamente");
            }

            @Override
            public void onFailure() {
                Log.e(MI_APP, "Error al actualizar la hora de la reserva");
            }
        });
    }

    public void cancelReserva(String id){
        DataRepository.getInstance().deleteReserva(id, new DataRepository.Callback() {
            @Override
            public void onSuccess() {Log.i(MI_APP, "Reserva con el id: "+id+" eliminada correctamente");
            }

            @Override
            public void onFailure() {Log.e(MI_APP, "Error al cancelar la reserva");
            }
        });
    }
    public boolean isValidTime(String time){
        // El formato debe ser "HH:mm-HH:mm"
        String regex = "\\d{2}:\\d{2}-\\d{2}:\\d{2}";
        if (time.matches(regex)){
            String[] parts = time.split("-");
            String startTime = parts[0].trim();
            String endTime = parts[1].trim();
            try {
                // Convertir las horas a objetos Calendar para compararlas
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                Calendar calStart = Calendar.getInstance();
                calStart.setTime(Objects.requireNonNull(sdf.parse(startTime)));
                Calendar calEnd = Calendar.getInstance();
                calEnd.setTime(Objects.requireNonNull(sdf.parse(endTime)));

                // Comparar las horas
                return calStart.before(calEnd);
            } catch (ParseException e) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LOGGER.log(Level.SEVERE, e, () -> "Error parsing time: " + time);
                }
                return false;
            }
        } else{
            return false;
        }
    }

    public void addItem(FragmentVigentesBinding binding, LayoutInflater inflater, Reserva reserva, Context context) {
        LinearLayout reservasContainer = binding.reservasContainer;
        View reservaView = inflater.inflate(R.layout.item_reserva_vigente, null);

        ImageView vehicleIcon = reservaView.findViewById(R.id.vehicle_icon);
        switch (reserva.getPlaza().getTipo()) {
            case "Minusválido":
                vehicleIcon.setImageResource(R.drawable.baseline_accessible_40);
                break;
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
                break;
        }

        TextView dateTextView = reservaView.findViewById(R.id.textDate);
        dateTextView.setText(reserva.getFecha());
        TextView timeTextView = reservaView.findViewById(R.id.textTime);
        String hora = formatHour((int) reserva.getHora().getHoraInicio()) + "-" + formatHour((int) reserva.getHora().getHoraFin());
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
                        if (isValidTime(editedTime)) {
                            // Actualizar el texto de la hora en el TextView
                            timeTextView.setText(editedTime);
                            String[] partes = editedTime.split("-");
                            String horaInicio = partes[0];
                            String horaFin = partes[1];

                            long horaInicioMinutos = convertirHoraAMinutos(horaInicio);
                            long horaFinMinutos = convertirHoraAMinutos(horaFin);

                            // Asignamos los valores a la reserva
                            reserva.getHora().setHoraInicio(horaInicioMinutos);
                            reserva.getHora().setHoraFin(horaFinMinutos);
                            editReserva(reserva);
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
        cancelButton.setOnClickListener(v -> new MaterialAlertDialogBuilder(context)
                .setTitle("Confirmar cancelación")
                .setMessage("¿Estás seguro que deseas cancelar esta reserva?")
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    // Lógica para cancelar la reserva
                    cancelReserva(reserva.getId());
                    reservasContainer.removeView(reservaView);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    // No se hace nada al cancelar
                })
                .show());

        reservasContainer.addView(reservaView);
    }

    private long convertirHoraAMinutos(String hora) {
        String[] partesHora = hora.split(":");
        int horas = Integer.parseInt(partesHora[0]);
        int minutos = Integer.parseInt(partesHora[1]);
        return horas * 60L + minutos;
    }
}
