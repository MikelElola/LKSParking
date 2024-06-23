package com.lksnext.lksparking.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lksnext.lksparking.data.DataRepository;
import com.lksnext.lksparking.domain.Callback;

public class RegisterViewModel extends ViewModel {
    // Aquí puedes declarar los LiveData y métodos necesarios para la vista de registro
    MutableLiveData<Boolean> logged = new MutableLiveData<>(null);

    public LiveData<Boolean> isLogged(){
        return logged;
    }

    public void registerUser(String email, String password) {
        DataRepository.getInstance().register(email, password, new DataRepository.Callback() {
            @Override
            public void onSuccess() {
                logged.setValue(Boolean.TRUE);
            }

            @Override
            public void onFailure() {
                logged.setValue(Boolean.FALSE);
            }
        });
    }
}
