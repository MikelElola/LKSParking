package com.lksnext.lksparking.viewmodel;

import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lksnext.lksparking.data.DataRepository;
import com.lksnext.lksparking.databinding.ActivityLoginBinding;

public class LoginViewModel extends ViewModel {

    // Aquí puedes declarar los LiveData y métodos necesarios para la vista de inicio de sesión
    MutableLiveData<Boolean> logged = new MutableLiveData<>(null);

    public LiveData<Boolean> isLogged(){
        return logged;
    }
    public void loginUser(String email, String password) {
        DataRepository.getInstance().login(email, password, new DataRepository.Callback() {
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
    public void comprobarLogin( ActivityLoginBinding binding, String email, String password){
        if (!(email.isEmpty()||password.isEmpty())) {
            // Verificar además si el formato del email es válido
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                // Iniciar el proceso de recuperación de contraseña
                loginUser(email, password);
            } else {
                // Mostrar un mensaje de error si el formato del email no es válido
                binding.email.setError("Por favor ingresa un email válido");
            }
        } else {
            // Mostrar un mensaje de error si el campo de email o contraseña están vacíos
            binding.email.setError("Por favor proporciona un email");
            binding.password.setError("Por favor proporciona una contraseña");
        }
    }
    public void initiatePasswordReset(String email) {
        // Aquí llamas al repositorio de datos para iniciar el proceso de recuperación de contraseña
        DataRepository.getInstance().initiatePasswordReset(email, new DataRepository.Callback() {
            @Override
            public void onSuccess() {
                // Aquí puedes manejar la lógica después de iniciar la recuperación de contraseña
                // Por ejemplo, mostrar un mensaje de éxito o navegar a una pantalla de confirmación
                Log.i("LoginViewModel", "Se ha iniciado el proceso de recuperación de contraseña");
            }

            @Override
            public void onFailure() {
                // Manejar el fallo en la recuperación de contraseña si es necesario
                Log.e("LoginViewModel", "Error al iniciar el proceso de recuperación de contraseña");
            }
        });
    }
    public boolean comprobarPasswordReset( ActivityLoginBinding binding, String email){
        if (!email.isEmpty()) {
            // Verificar además si el formato del email es válido
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                // Iniciar el proceso de recuperación de contraseña
                initiatePasswordReset(email);
                return true;
            } else {
                // Mostrar un mensaje de error si el formato del email no es válido
                binding.email.setError("Por favor ingresa un email válido");
                return false;
            }
        } else {
            // Mostrar un mensaje de error si el campo de email está vacío
            binding.email.setError("Por favor proporciona un email");
            return false;
        }
    }
}