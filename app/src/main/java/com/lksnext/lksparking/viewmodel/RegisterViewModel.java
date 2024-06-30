package com.lksnext.lksparking.viewmodel;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lksnext.lksparking.data.DataRepository;
import com.lksnext.lksparking.databinding.ActivityRegisterBinding;

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
    public void comprobarRegister(ActivityRegisterBinding binding,String email, String password, String password2){
        if (!email.isEmpty()) {
            // Verificar además si el formato del email es válido
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if(password.equals(password2)){
                    registerUser(email, password);
                } else{
                    binding.password.setError("Las dos contraseñas deben ser iguales");
                    binding.password2.setError("Las dos contraseñas deben ser iguales");
                }
            } else {
                // Mostrar un mensaje de error si el formato del email no es válido
                binding.email.setError("Por favor ingresa un email válido");
            }
        } else {
            // Mostrar un mensaje de error si el campo de email está vacío
            binding.email.setError("Por favor proporciona un email");
        }
    }
}
