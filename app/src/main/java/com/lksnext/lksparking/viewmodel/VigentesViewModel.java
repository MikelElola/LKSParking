package com.lksnext.lksparking.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lksnext.lksparking.data.DataRepository;
import com.lksnext.lksparking.domain.Reserva;

import java.util.Calendar;
import java.util.List;

public class VigentesViewModel extends ViewModel {
    private final MutableLiveData<String> mesActual = new MutableLiveData<>();
    private final MutableLiveData<Integer> yearActual = new MutableLiveData<>();
    private final MutableLiveData<List<Reserva>> reservas = new MutableLiveData<>();
    private FirebaseAuth firebaseAuth;
    public LiveData<String> getMesActual() {
        return mesActual;
    }
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
                Log.i("MiApp", "Reservas vigentes obtenidas para el usuario " + usuario);
            }

            @Override
            public void onFailure() {
                Log.e("MiApp", "Error al obtener reservas vigentes");
            }
        });
    }

    public String formatHour(long hora) {
        // Asumiendo que el formato es HHMM
        int parteHora = (int) (hora / 60);  // Extraer la parte de la hora
        int parteMinuto = (int) (hora % 60); // Extraer la parte de los minutos

        return String.format("%02d:%02d", parteHora, parteMinuto); // Formato HH:mm
    }


}
