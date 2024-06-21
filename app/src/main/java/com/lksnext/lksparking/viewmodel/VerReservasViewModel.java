package com.lksnext.lksparking.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;

public class VerReservasViewModel extends ViewModel {
    private MutableLiveData<String> mesActual = new MutableLiveData<>();

    public LiveData<String> getMesActual() {
        return mesActual;
    }

    public VerReservasViewModel(){
        //Inicializa con el mes actual
        Calendar calendar = Calendar.getInstance();
        int mesNum = calendar.get(Calendar.MONTH);
        String[] nombresMeses = {"enero", "febrero", "marzo", "abril", "mayo", "junio",
                "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"};
        String mes = nombresMeses[mesNum];
        mesActual.setValue(mes);
    }
}
