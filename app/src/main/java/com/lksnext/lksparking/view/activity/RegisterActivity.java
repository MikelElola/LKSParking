package com.lksnext.lksparking.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.lksnext.lksparking.databinding.ActivityRegisterBinding;
import com.lksnext.lksparking.viewmodel.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Asignamos la vista/interfaz de registro
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Asignamos el viewModel de register
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        //Quitar el estado de error al cambiar el texto
        binding.emailText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.email.setError(null); // Quitar el mensaje de error
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.password.setError(null); // Quitar el mensaje de error
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.passwordText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.password.setError(null); // Quitar el mensaje de error
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.registerButton.setOnClickListener(v -> {
            String email = binding.emailText.getText().toString();
            String password = binding.passwordText.getText().toString();
            String password2 = binding.passwordText2.getText().toString();
            if (!email.isEmpty()) {
                // Verificar además si el formato del email es válido
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if(password.equals(password2)){
                        registerViewModel.registerUser(email, password);
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
        });

        //Observamos la variable logged, la cual nos informara cuando el usuario intente hacer login y se
        //cambia de pantalla en caso de login correcto
        registerViewModel.isLogged().observe(this, logged -> {
            if (logged != null) {
                if (logged) {
                    //Login Correcto
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    //Login incorrecto
                    binding.email.setError("Email o contraseña incorrectos");
                    binding.password.setError("Email o contraseña incorrectos");
                    binding.password2.setError("Email o contraseña incorrectos");
                }
            }
        });
    }
}