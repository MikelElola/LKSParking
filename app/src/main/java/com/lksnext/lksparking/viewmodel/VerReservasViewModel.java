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

public class VerReservasViewModel extends ViewModel {
    private final MutableLiveData<String> mesActual = new MutableLiveData<>();
    private final MutableLiveData<Integer> yearActual = new MutableLiveData<>();
    private final MutableLiveData<List<Reserva>> reservas = new MutableLiveData<>();
    private final FirebaseAuth firebaseAuth;

    public LiveData<String> getMesActual() {
        return mesActual;
    }
    public LiveData<List<Reserva>> getReservas() {return reservas;}

    public VerReservasViewModel(){
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

    public void getReservasMes(){
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH)+1;
        assert yearActual.getValue() != null;
        int year = yearActual.getValue();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String usuario = (currentUser != null) ? currentUser.getEmail() : "usuarioDesconocido";
        DataRepository.getInstance().getReservasPorMes(month,year, usuario, new DataRepository.ReservasCallback<List<Reserva>>() {
            @Override
            public void onSuccess(List<Reserva> reservasObtenidas) {
                reservas.setValue(reservasObtenidas);
                Log.i("MiApp", "Reservas obtenidas para el mes " + mesActual.getValue() + " del año " + yearActual.getValue());
            }

            @Override
            public void onFailure() {
                Log.e("MiApp", "Error al obtener reservas del mes");
            }
        });
    }

}
