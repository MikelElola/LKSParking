package com.lksnext.lksparking.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.lksnext.lksparking.domain.Callback;

public class RegisterViewModel extends ViewModel {
    // Aquí puedes declarar los LiveData y métodos necesarios para la vista de registro
    // Por ejemplo, un LiveData para el email, contraseña y usuario
    MutableLiveData<Boolean> logged = new MutableLiveData<>(null);

    public LiveData<Boolean> isLogged(){
        return logged;
    }

    public void registerUser(String email, String password){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                email,password
        ).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Si el registro es exitoso, actualizar el estado de logged a true
                logged.setValue(Boolean.TRUE);
            } else {
                // Si el registro falla, actualizar el estado de logged a false
                logged.setValue(Boolean.FALSE);
            }
        });
    }
}
